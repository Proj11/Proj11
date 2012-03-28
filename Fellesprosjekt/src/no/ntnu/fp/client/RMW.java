package no.ntnu.fp.client;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RMW extends Thread {

    private boolean isRunning;
    private Client aConnection;
    private List<MessageListener> messageListenerList;

    public RMW(Client aConnection) {
        isRunning = false;
        this.aConnection = aConnection;
        messageListenerList = new ArrayList<MessageListener>();
    }

    public void addMessageListener(MessageListener listener) {
        messageListenerList.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListenerList.remove(listener);
    }

    public void run() {
        isRunning = true;
        int counter = 0;
        try {
            while (isRunning) {
                String message = aConnection.receive();
                Iterator<MessageListener> iterator = messageListenerList.iterator();
                while (iterator.hasNext()) {
                    MessageListener listener = iterator.next();
                    listener.messageReceived(message);
                }
            }
        }
        catch (EOFException e) {
            try {
                aConnection.close();
                isRunning = false;
                for (MessageListener ml : messageListenerList) {
                    ml.connectionClosed(aConnection);
                }
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
    }

    public interface MessageListener {

        public void messageReceived(String message);

        public void connectionClosed(Client aConnection);

    }
}
