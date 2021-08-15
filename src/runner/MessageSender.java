package chatterclient.src.runner;

import static chatterclient.src.interfaces.ServiceConstants.MESSAGE_SEPARATOR;
import java.io.IOException;
import java.util.Formatter;
import java.net.Socket;

/**
 * Client uses this Runnable to send messages to the chat server in a separate
 * runnable.
 *
 * @author 0laprogrmr@gmail.com
 */
public class MessageSender implements Runnable {

    private final Socket clientSocket; // client socket over which to send message to the server.
    private final String messageToSend; //chat message to send.
    private Formatter outputFormatter;

    public MessageSender(Socket socket, String userName, String message) {
        clientSocket = socket;
        messageToSend = userName + MESSAGE_SEPARATOR + message;// build message to be sent
    } // end MessageSender constructor

    // send message and end
    @Override
    public void run() {
        sendMsg();
    } // end method run

    public void sendMsg() {
        try {// send message and flush formatter
            setOutputFormatter(new Formatter(clientSocket.getOutputStream()));
            getOutputFormatter().format("%s\n", messageToSend); //format msg and send.
            getOutputFormatter().flush(); // flush output
        }//end try
        catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
        }// end catch
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public String getMessageToSend() {
        return this.messageToSend;
    }

    public Formatter getOutputFormatter() {
        return this.outputFormatter;
    }

    public void setOutputFormatter(Formatter outputFormatter) {
        this.outputFormatter = outputFormatter;
    }
} // end class MessageSender
