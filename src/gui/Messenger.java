package chatterclient.src.gui;

import static chatterclient.src.interfaces.ServiceConstants.SERVER_PORT;
import javax.swing.SwingUtilities;
import chatterclient.src.message.ChatManager;
import chatterclient.src.interfaces.ChatControl;

/**
 * Messenger is a chat application that uses a ClientGUI and ChatManager to
 * communicate with MessengerServer.
 * 
 * @author 0laprogrmr@gmail.com
 */
public class Messenger {
    public static void main(String args[]) {
        ChatControl chatController;
        if (args.length == 0) {//connect to localhost
            chatController = new ChatManager("localhost", SERVER_PORT);
        }//
        else {//connect using command-line arg
            chatController = new ChatManager(args[0], Integer.parseInt(args[1]));
        }
        //Lambda expression
        SwingUtilities.invokeLater(() -> {
            new ClientGUI(chatController);
        });
    } // end main
} // end class Messenger
