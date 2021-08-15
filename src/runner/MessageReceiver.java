package chatterclient.src.runner;

import chatterclient.src.interfaces.MessageListener;
import static chatterclient.src.interfaces.ServiceConstants.MESSAGE_SEPARATOR;
import static chatterclient.src.interfaces.ServiceConstants.MESSAGE_SIZE;
import static chatterclient.src.interfaces.ServiceConstants.MULTICAST_ADDRESS;
import static chatterclient.src.interfaces.ServiceConstants.MULTICAST_LISTENING_PORT;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

/**
 * Client uses this Runnable to receive datagram packets from the server 
 * MessageReceiver listens for DatagramPackets containing messages from the
 * server. Receives datagram packets.
 *
 * @author 0laprogrmr@gmail.com
 */
public class MessageReceiver implements Runnable {

    private MessageListener messageListener; // receives messages
    private MulticastSocket multicastSocket; // receive broadcast messages
    private InetAddress multicastGroup; // InetAddress of multicast group
    private boolean listening = true; // terminates MessageReceiver

    public MessageReceiver(MessageListener listener) {
        this.messageListener = listener; // set MessageListener

        try { // connect MulticastSocket to multicast address and port 
            // create new MulticastSocket
            multicastSocket = new MulticastSocket(MULTICAST_LISTENING_PORT);
            // use InetAddress to get multicast group
            multicastGroup = InetAddress.getByName(MULTICAST_ADDRESS);
            // join multicast group to receive messages
            multicastSocket.joinGroup(multicastGroup);
            // set 5 second timeout when waiting for new packets
            multicastSocket.setSoTimeout(5000);
        } // end try
        catch (IOException iOe) {
            iOe.getMessage();
        } // end catch
    } // end MessageReceiver constructor

    // listen for messages from multicast group 
    @Override
    public void run() {
        // listen for messages until stopped
        while (listening) {
            // create buffer for incoming message
            byte[] buffer = new byte[MESSAGE_SIZE];
            // create DatagramPacket for incoming message
            DatagramPacket packet = new DatagramPacket(buffer, MESSAGE_SIZE);
            try {// receive new DatagramPacket (blocking call)
                multicastSocket.receive(packet);
            } // end try
            catch (SocketTimeoutException sTe) {
                continue; // continue to next iteration to keep listening
            } // end catch
            catch (IOException iOe) {
                iOe.getMessage();
                break;
            } // end catch
            String message = new String(packet.getData());// put message data in a String
            message = message.trim(); // trim whitespace from message
            StringTokenizer tokenizer = new StringTokenizer(message, MESSAGE_SEPARATOR);// tokenize message to retrieve user name and message body

            if (tokenizer.countTokens() == 2) {// ignore messages that do not contain a user name and message body
                // send message to MessageListener
                messageListener.receiveMessage(tokenizer.nextToken(), tokenizer.nextToken()); //username and message body
            } // end if
        } // end while

        try {
            multicastSocket.leaveGroup(multicastGroup); // leave group
            multicastSocket.close(); // close MulticastSocket
        } // end try
        catch (IOException iOe) {
            iOe.getMessage();
        } // end catch
    } // end method run

    // stop listening for new messages
    public void stopListening() {
        this.listening = false;
    } // end method stopListening

    public MessageListener getMessageListener() {
        return this.messageListener;
    }
} // end class MessageReceiver
