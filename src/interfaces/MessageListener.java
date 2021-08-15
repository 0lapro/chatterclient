package chatterclient.src.interfaces;

/**
 * MessageListener is an interface for classes that wish to receive new chat
 * messages.
 * 
 * Listens to chat messages coming from clients.
 *
 * @author 0laprogrmr@gmail.com
 */
public interface MessageListener {
    void receiveMessage(String from, String message);// receive new chat message
} // end interface MessageListener
