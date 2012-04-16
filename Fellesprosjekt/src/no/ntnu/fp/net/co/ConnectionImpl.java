/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebj�rn Birkeland and Stein Jakob Nordb�
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

    /** Keeps track of the used ports for each server port. */
    private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());
    private final static int RETRY=16;

    /**
     * Initialise initial sequence number and setup state machine.
     * 
     * @param myPort
     *            - the local port to associate with this connection
     */
    public ConnectionImpl(int myPort) {
        super();
        this.myPort=myPort;
        this.myAddress=getIPv4Address();
        usedPorts.put(myPort, true);
    }

    private String getIPv4Address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * Establish a connection to a remote location.
     * 
     * @param remoteAddress
     *            - the remote IP-address to connect to
     * @param remotePort
     *            - the remote portnumber to connect to
     * @throws IOException
     *             If there's an I/O error.
     * @throws java.net.SocketTimeoutException
     *             If timeout expires before connection is completed.
     * @see Connection#connect(InetAddress, int)
     */
    public void connect(InetAddress remoteAddress, int remotePort) throws IOException, SocketTimeoutException {
    	if (state!=State.CLOSED) {
    		throw new SocketTimeoutException("Socket is not closed");
    	}
    	try {
    		//Set the address and port we want to connect to
	    	this.remoteAddress=remoteAddress.getHostAddress();
	    	this.remotePort=remotePort;
	    	state=State.SYN_SENT;
	    	simplySendPacket(constructInternalPacket(Flag.SYN));
	    	KtnDatagram recievedPacket=receiveAck();
	    	this.remotePort = recievedPacket.getSrc_port();
			lastValidPacketReceived=recievedPacket;
			Thread.sleep(50);
			sendAck(recievedPacket, false);
			Thread.sleep(500);
			state=State.ESTABLISHED;
    	} catch (Exception e) {
    		state=State.CLOSED;
    		e.printStackTrace();
    	}
    }

    /**
     * Listen for, and accept, incoming connections.
     * 
     * @return A new ConnectionImpl-object representing the new connection.
     * @see Connection#accept()
     */
    public Connection accept() throws IOException, SocketTimeoutException {
    	if (state!=State.CLOSED && state != State.LISTEN) {
    		throw new IOException("Socket isn't closed");
    	}
    	//Listen for a new connection
    	state=State.LISTEN;
    	KtnDatagram recievedPacket=null;
    	while (!isValid(recievedPacket)) {
    		recievedPacket=receivePacket(true);
    	}
    	// If we received a syn, we can establish a new connection! Hurray!
        if (recievedPacket.getFlag()==Flag.SYN) {
        	ConnectionImpl newConnection=new ConnectionImpl(generateNewPortNumber()); //Generate a random, new, unused port number for the connection
        	//Set the address and port that we want to connect to
        	newConnection.remoteAddress=recievedPacket.getSrc_addr(); 
	    	newConnection.remotePort=recievedPacket.getSrc_port();
        	newConnection.state=State.SYN_RCVD;
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	newConnection.sendAck(recievedPacket, true);
        	newConnection.lastValidPacketReceived = newConnection.receiveAck();
        	newConnection.state=State.ESTABLISHED;
        	return newConnection; 
        }
        //If we get down here, we have not received a syn
        throw new IOException("Did not recieve syn");
    }
    
    private int generateNewPortNumber() {
    	int port = (int)(Math.random()*200) + 8337;
    	while(usedPorts.containsKey(port)){
    		port = (int)(Math.random()*200) + 8337;
    	}
    	return port;
    }

    /**
     * Send a message from the application.
     * 
     * @param msg
     *            - the String to be sent.
     * @throws ConnectException
     *             If no connection exists.
     * @throws IOException
     *             If no ACK was received.
     * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
     * @see no.ntnu.fp.net.co.Connection#send(String)
     */
    public void send(String msg) throws ConnectException, IOException {
    	if (state!=State.ESTABLISHED) {
    		throw new ConnectException("Error sending, connection not established");
    	}
    	KtnDatagram sendPacket=constructDataPacket(msg);
    	int retry = RETRY;
    	//Retry in case we don't receive the correct ack 
    	while (retry-->0) {
    		KtnDatagram ackPacket=sendDataPacketWithRetransmit(sendPacket);
    		if (ackPacket.getAck()==sendPacket.getSeq_nr()) {
    			return; //Successfully recieved the correct ack packet, so we are done here
    		}
    	}
    	throw new IOException("Did not receive correct ack after "+RETRY+" tries!");
    }

    /**
     * Wait for incoming data.
     * 
     * @return The received data's payload as a String.
     * @see Connection#receive()
     * @see AbstractConnection#receivePacket(boolean)
     * @see AbstractConnection#sendAck(KtnDatagram, boolean)
     */
    public String receive() throws ConnectException, IOException {
    	if (state!=State.ESTABLISHED) {
    		throw new ConnectException("Connection must be established to receive something");
    	}
        KtnDatagram recievedPacket= null;
        do { 
        	recievedPacket = receivePacket(false);
        } while (recievedPacket == null);
        if (isValid(recievedPacket) && recievedPacket.getSeq_nr()>lastValidPacketReceived.getSeq_nr()) { //Check if the packet we recieved is a valid packet, and had new data for us
        	try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	sendAck(recievedPacket, false);
        	lastValidPacketReceived=recievedPacket;
        	return recievedPacket.toString();
        }
        else {
        	sendAck(lastValidPacketReceived, false); //Ack the last received packet if the packet recieved was not valid
        	return null;
        }
    }

    /**
     * Close the connection.
     * 
     * @see Connection#close()
     */
    public void close() throws IOException {
    	if (state!=State.ESTABLISHED) {
    		throw new IOException("No connection is established");
    	}
    	if (disconnectRequest == null){

    		state=State.FIN_WAIT_1;
    		KtnDatagram finPacket=constructInternalPacket(Flag.FIN);
    		try {
    			Thread.sleep(100);
    			simplySendPacket((finPacket));
    		} catch (ClException e) {
    			e.printStackTrace();
    		} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		KtnDatagram ackPacket = receiveAck();
    		state=State.FIN_WAIT_2;
    		
    		KtnDatagram recieveFinPacket = receivePacket(true);
    		sendAck(recieveFinPacket, false);
    		state=State.CLOSED;
    	}
    	else {
    		try {
    			sendAck(disconnectRequest, false);
    		}
    		catch (Exception e){
    			e.printStackTrace();
    		}
    		state=State.CLOSE_WAIT;
    		KtnDatagram finPacket = constructInternalPacket(Flag.FIN);
    		try {
    			simplySendPacket((finPacket));
    		} catch (ClException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		state=State.LAST_ACK;
    		try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		receiveAck();
    		
    		state=State.CLOSED;
    	}
    }

    /**
     * Test a packet for transmission errors. This function should only called
     * with data or ACK packets in the ESTABLISHED state.
     * 
     * @param packet
     *            Packet to test.
     * @return true if packet is free of errors, false otherwise.
     */
    protected boolean isValid(KtnDatagram packet) {
    	if (packet==null) return false;
        long checksum=packet.calculateChecksum();
        return checksum==packet.getChecksum();
    }
}
