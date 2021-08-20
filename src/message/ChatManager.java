package chatterclient.src.message;

import chatterclient.src.runner.MessageSender;
import chatterclient.src.interfaces.MessageListener;
import static chatterclient.src.interfaces.ServiceConstants.DISCONNECT_STRING;
import chatterclient.src.runner.MessageReceiver;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import chatterclient.src.interfaces.ChatControl;

/**
 * Used by client to communicate with MessengerServer using Sockets and
 * MulticastSockets.
 *
 * @author 0laprogrmr@gmail.com
 */
public class ChatManager implements ChatControl {

    private Socket clientSocket; // Socket for outgoing messages
    private String serverAddress; // messenger server address
    private MessageReceiver receiver; // receives multicast messages
    private boolean connected; // connection status
    private ExecutorService serverExecutor; // executor for server
    private int serverPortNo;
    private boolean connectionDropped = true;
    private boolean disconnectClicked;
    private MessageListener msgListener;
    // private Queue<String> msgQueue;

    /**
     * @param address
     * @param portNo
     */
    public ChatManager(String address, int portNo) {
        this.serverAddress = address; // store server address
        this.serverExecutor = Executors.newCachedThreadPool();
        this.serverPortNo = portNo;
    } // end ChatManager constructor

    public void reconnect() {
        if (connected()) {
            return; // if already connected, return immediately
        }
        try{// open Socket connection to MessengerServer
            setClientSocket(new Socket(InetAddress.getByName(getServerAddress()), getServerPortNo()));//Determine the IP address of the server, given the hostname and pass it as argument with the server port.
            getServerExecutor().execute(getReceiver());//execute Runnable
            setConnected(true);//update connected flag
        } // end try
        catch (IOException | NullPointerException e) {
            e.getMessage();
        } // end catch
    }

    /**
     * Connect to server and create the packet receiver runnable/task
     *
     * @param listener
     */
    @Override
    public void connect(MessageListener listener) {
        if (connected()) {
            return; // if already connected, return immediately
        }
        try {// open Socket connection to MessengerServer
            setMsgListener(listener);
            //Determine the IP address of the server, given the hostname.
            setClientSocket(new Socket(InetAddress.getByName(getServerAddress()), getServerPortNo()));
            setReceiver(new MessageReceiver(getMsgListener()));//create Runnable for receiving incoming messages
            getServerExecutor().execute(getReceiver());//execute Runnable
            setConnected(true);//update connected flag
        } // end try // end try
        catch (IOException ioException) {
            ioException.getMessage();
        } // end catch
    } // end method connect

    /**
     * Disconnect from server and unregister given MessageListener.
     *
     */
    @Override
    public void disconnect() {
        if (!connected()) {
            return;//if not connected, return immediately.
        }
        try {//stop listener and disconnect from server
            //notify server that client is disconnecting
            Runnable disconnecter = new MessageSender(getClientSocket(), "", DISCONNECT_STRING);
            Future<?> disconnecting = getServerExecutor().submit(disconnecter);
            disconnecting.get(); // wait for disconnect message to be sent
            getReceiver().stopListening(); // stop receiver
            getClientSocket().close(); // close outgoing Socket
        } // end try
        catch (ExecutionException | InterruptedException | IOException |
                RejectedExecutionException | NullPointerException ex) {
            System.out.println("\nServer disconnected with the following exception"
             + "\n************************************************\n" + ex.getMessage());
        }// end catch
        setConnected(false); // update connected flag
    } // end method disconnect

    /**
     * Sends message to server. The flow starts from ClientGUI which sends msg via
     * chat mgr (i.e. an instance of this class via this method. Then this method
     * creates and passes the msg sender (runnable/task) to server executor to
     * execute.
     *
     * @param from
     * @param message
     */
    @Override
    public void sendMessage(String from, String message) {
        if (!connected() && disconnectClicked()) {
            return;//if not connected, return immediately
        }
        reconnect();
        //This statement controls the task of sending msg, creates and starts
        //new MessageSender/Runnable/Task and gives it to server executor threads to execute.
        getServerExecutor().execute(new MessageSender(getClientSocket(), from, message));
    }//end method sendMessage

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public MessageReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    public boolean connected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public ExecutorService getServerExecutor() {
        return this.serverExecutor;
    }

    public void setServerExecutor(ExecutorService serverExecutor) {
        this.serverExecutor = serverExecutor;
    }

    public int getServerPortNo() {
        return this.serverPortNo;
    }

    public void setServerPortNo(int serverPortNo) {
        this.serverPortNo = serverPortNo;
    }

    @Override
    public boolean connectionDropped() {
        return this.connectionDropped;
    }

    @Override
    public void setConnectionDropped(boolean connectionDropped) {
        this.connectionDropped = connectionDropped;
    }

    @Override
    public void setDisconnectClicked(boolean disconnectClicked) {
        this.disconnectClicked = disconnectClicked;
    }

    @Override
    public boolean disconnectClicked() {
        return this.disconnectClicked;
    }

    public MessageListener getMsgListener() {
        return this.msgListener;
    }

    public void setMsgListener(MessageListener msgListener) {
        this.msgListener = msgListener;
    }
}//end method ChatManager

