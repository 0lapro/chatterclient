/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatterclient.src.interfaces;

/**
 * Used by Client.
 * MessageManger is an interface for objects capable of managing communications
 * with a message server.
 * 
 * For connecting to, sending messages to, and disconnecting from the message server.
 * Interface methods are implicitly public and abstract, so no need to add the 
 * keywords "public" and "abstract".
 * 
 * @author 0laprogrmr@gmail.com
 */
public interface ChatControl {
    // connect to message server and route incoming messages
    // to a given MessageListener
    void connect(MessageListener listener);

    // disconnect from message server and stop routing
    // incoming messages to a given MessageListener
    void disconnect();

    // send message to message server
    void sendMessage(String from, String message);

    /**
     *
     * @return
     */
    boolean connectionDropped();

    void setConnectionDropped(boolean b);

    void setDisconnectClicked(boolean b);
    
    boolean disconnectClicked();
} // end interface ChatControl