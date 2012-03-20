package no.ntnu.fp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	
	@SuppressWarnings("deprecation")
	public Client() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 8000);
		fromServer = new DataInputStream(socket.getInputStream());
		toServer = new DataOutputStream(socket.getOutputStream());
		toServer.writeChars("test");
		toServer.flush();
		
		String result = fromServer.readLine();
		System.out.println(result);
	}
	
	public static void main(String[] args) {
		try {
			new Client();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
