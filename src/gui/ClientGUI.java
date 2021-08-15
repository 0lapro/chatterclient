package chatterclient.src.gui;

import chatterclient.src.interfaces.MessageListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import chatterclient.src.interfaces.ChatControl;

/**
 * ClientGUI provides a user interface for sending and receiving messages to and
 * from the messenger.
 *
 * @author 0laprogrmr@gmail.com
 */
public class ClientGUI {

    private static final long SERIAL_VERSION_UID = 1L;

    private JFrame messengerFrame;
    private JPanel messagePanel;
    private JPanel buttonPanel;
    private JMenuBar menuBar;
    private JMenu menu;//for connecting/disconnecting server
    private JTextArea messageArea;// displays messages
    private JTextArea inputArea;// inputs messages
    private JButton connectButton;// button for connecting
    private JMenuItem connectMenuItem;// menu item for connecting
    private JButton disconnectButton;// button for disconnecting
    private JMenuItem disconnectMenuItem;// menu item for disconnecting
    private JButton sendButton;// sends messages
    private JLabel statusBar;// label for connection status
    private Box box;
    private String userName;// userName to add to outgoing messages
    private ChatControl chatController;// communicates with server
    private MessageListener messageListener;// receives incoming messages
    private Icon connectIcon;// create ImageIcon for connect buttons
    private Icon disconnectIcon;//create ImageIcon for disconnect buttons
    private Icon sendIcon;// create Icon for sendButton
    private boolean serverClosed;//indicates when server closes
    private boolean serverStopped;//indicates when server stops

    /**
     * ClientGUI constructor
     *
     * @param control
     */
    public ClientGUI(ChatControl control) {
        setChatController(control);//set the ChatControl
        setMessageListener(new MyMessageListener());//create MyMessageListener for receiving messages
        makeIcons();
        makeLabels();
        makeMenuItems();
        makeButtons();
        makeInputArea();
        makeMessageArea();
        makePanels();
        makeBoxes();
        makeMenuBar();
        makeMessengerFrame();
        addListeners();
    }//end ClientGUI constructor

    private void makeIcons() {
        setConnectIcon(new ImageIcon(getClass().getResource("../assets/images/connect.png")));// create ImageIcon for connect buttons
        setDisconnectIcon(new ImageIcon(getClass().getResource("../assets/images/disconnect.png")));//create ImageIcon for disconnect buttons
        setSendIcon(new ImageIcon(getClass().getResource("../assets/images/send.png")));// create Icon for sendButton
    }

    private void makeLabels() {
        /*-----------------JLabel logic-----------------*/
        /*create JLabel for statusBar with a recessed border*/
        setStatusBar(new JLabel("Not Connected"));
        getStatusBar().setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    private void makeMenuItems() {
        /*-----------------JMenuItem logic-----------------*/
        setConnectMenuItem(new JMenuItem("Connect", getConnectIcon()));
        setDisconnectMenuItem(new JMenuItem("Disconnect", getDisconnectIcon()));
        getConnectMenuItem().setMnemonic('C');
        getDisconnectMenuItem().setMnemonic('D');
        getDisconnectMenuItem().setEnabled(false);
    }

    private void makeButtons() {
        /*-----------------JButton logic-----------------*/
        setConnectButton(new JButton("Connect", getConnectIcon()));
        setDisconnectButton(new JButton("Disconnect", getDisconnectIcon()));
        setSendButton(new JButton("Send", getSendIcon()));
        getDisconnectButton().setEnabled(false);
        getSendButton().setEnabled(false);
    }

    private void makeInputArea() {
        /*JTextArea logic*/
        setInputArea(new JTextArea(4, 20));
        getInputArea().setWrapStyleWord(true);// set wrap style to word
        getInputArea().setLineWrap(true);// enable line wrapping
        getInputArea().setEditable(false);// disable editing
    }

    private void makeMessageArea() {
        /*JTextArea logic*/
        setMessageArea(new JTextArea());
        getMessageArea().setEditable(false);// disable editing
        getMessageArea().setWrapStyleWord(true);// set wrap style to word
        getMessageArea().setLineWrap(true);// enable line wrapping
    }

    private void makePanels() {
        /*-----------------JPanel logic-----------------*/
        setButtonPanel(new JPanel());
        getButtonPanel().add(getConnectButton());
        getButtonPanel().add(getDisconnectButton());
        /*put messageArea in JScrollPane to enable scrolling*/
        setMessagePanel(new JPanel());
        getMessagePanel().setLayout(new BorderLayout(10, 10));
        getMessagePanel().add(new JScrollPane(getMessageArea()), BorderLayout.CENTER);
    }

    private void makeBoxes() {
        /*-----------------Box logic-----------------*/
        setBox(new Box(BoxLayout.X_AXIS));// create new box for layout
        getBox().add(new JScrollPane(getInputArea()));// add input area to box
        getBox().add(getSendButton());// add send button to box
        messagePanel.add(getBox(), BorderLayout.SOUTH);// add box to panel
    }

    private void makeMenuBar() {
        /*-----------------JMenuBar logic-----------------*/
        setMenu(new JMenu("Menu"));
        getMenu().setForeground(Color.WHITE);
        getMenu().setMnemonic('S');// set mnemonic for server menu
        getMenu().add(getConnectMenuItem());// add connect JMenuItems to fileMenu
        getMenu().add(getDisconnectMenuItem());// add disconnect JMenuItems to fileMenu
        setMenuBar(new JMenuBar());// create JMenuBar
        getMenuBar().add(getMenu());// add server menu to menu bar
        getMenuBar().setBackground(Color.black);
    }

    private void makeMessengerFrame() {
        /*-----------------Frame logic------------------------*/
        setMessengerFrame(new JFrame("ChatApp"));
        getMessengerFrame().setJMenuBar(getMenuBar());//add JMenuBar to the messenger frame
        getMessengerFrame().add(getButtonPanel(), BorderLayout.NORTH);// add button panel
        getMessengerFrame().add(getMessagePanel(), BorderLayout.CENTER);// add message panel
        getMessengerFrame().add(getStatusBar(), BorderLayout.SOUTH);// add status bar
        getMessengerFrame().pack();
        getMessengerFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getMessengerFrame().setSize(300, 400);//set window size
        getMessengerFrame().setVisible(true);//show window
    }

    private void addListeners() {
        /*-----------------Listener logic------------------------*/
        /*add WindowListener to disconnect when user quits*/
        getMessengerFrame().addWindowListener(new WindowAdapter() {
            //disconnect from server and exit application
            @Override
            public void windowClosing(WindowEvent event) {
                getChatController().disconnect();
                System.exit(0);
            } // end windowClosing
        } // end anonymous inner class
        );// end call to addWindowListener

        /*add ActionListener for connectButton*/
        getConnectButton().addActionListener((ActionEvent e) -> {
            connect();
        });

        /*add ActionListener for connectMenuItem*/
        getConnectMenuItem().addActionListener((ActionEvent e) -> {
            connect();
        });

        /*add ActionListener for connectButton*/
        getDisconnectButton().addActionListener((ActionEvent e) -> {
            disconnect();
        });

        /*add ActionListener for connectMenuItem*/
        getDisconnectMenuItem().addActionListener((ActionEvent e) -> {
            disconnect();
        });

        getSendButton().addActionListener((ActionEvent e) -> {
            getChatController().sendMessage(getUserName(), getInputArea().getText());// send message
            getInputArea().setText("");// clear inputArea
        });

        getInputArea().addKeyListener(new EnterListener());
    }

    /**
     * Contains code to execute when the connect button is pressed. This block
     * of code is normally in the actionPerformed but I chose to make it
     * one separate to avoid repeating it twice inside both the
     * connectButton and connectMenuItem actionPerformed methods.
     */
    private void connect() {
        getChatController().connect(getMessageListener());
        setUserName(JOptionPane.showInputDialog(getMessengerFrame(), "Enter user name:"));
        getMessageArea().setText("");//clear messageArea
        getConnectButton().setEnabled(false);//disable connect
        getConnectMenuItem().setEnabled(false);//disable connect
        getDisconnectButton().setEnabled(true);//enable disconnect
        getDisconnectMenuItem().setEnabled(true);//enable disconnect
        getSendButton().setEnabled(true);//enable send button
        getInputArea().setEditable(true);//enable editing for input area
        getInputArea().requestFocus();//set focus to input area
        getStatusBar().setText("Connected: " + getUserName());//set text
        getChatController().setDisconnectClicked(false);
    }

    /**
     * Contains code to execute when the connect button is pressed. This block
     * of code is normally in the actionPerformed but I chose to make it
     * one separate to avoid repeating it twice inside both the
     * connectButton and connectMenuItem actionPerformed methods.
     */
    private void disconnect() {
        getChatController().disconnect();
        getChatController().setDisconnectClicked(true);
        getSendButton().setEnabled(false);
        getDisconnectButton().setEnabled(false);
        getDisconnectMenuItem().setEnabled(false);
        getInputArea().setEditable(false);
        getConnectButton().setEnabled(true);
        getConnectMenuItem().setEnabled(true);
        getStatusBar().setText("Not Connected");
    }

    public JFrame getMessengerFrame() {
        return messengerFrame;
    }

    public void setMessengerFrame(JFrame messengerFrame) {
        this.messengerFrame = messengerFrame;
    }

    public JPanel getMessagePanel() {
        return messagePanel;
    }

    public void setMessagePanel(JPanel messagePanel) {
        this.messagePanel = messagePanel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getMenu() {
        return menu;
    }

    public void setMenu(JMenu menu) {
        this.menu = menu;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public void setMessageArea(JTextArea messageArea) {
        this.messageArea = messageArea;
    }

    public JTextArea getInputArea() {
        return inputArea;
    }

    public void setInputArea(JTextArea inputArea) {
        this.inputArea = inputArea;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public void setConnectButton(JButton connectButton) {
        this.connectButton = connectButton;
    }

    public JMenuItem getConnectMenuItem() {
        return connectMenuItem;
    }

    public void setConnectMenuItem(JMenuItem connectMenuItem) {
        this.connectMenuItem = connectMenuItem;
    }

    public JButton getDisconnectButton() {
        return disconnectButton;
    }

    public void setDisconnectButton(JButton disconnectButton) {
        this.disconnectButton = disconnectButton;
    }

    public JMenuItem getDisconnectMenuItem() {
        return disconnectMenuItem;
    }

    public void setDisconnectMenuItem(JMenuItem disconnectMenuItem) {
        this.disconnectMenuItem = disconnectMenuItem;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(JLabel statusBar) {
        this.statusBar = statusBar;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ChatControl getChatController() {
        return chatController;
    }

    private void setChatController(ChatControl chatController) {
        this.chatController = chatController;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    private void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public Icon getConnectIcon() {
        return connectIcon;
    }

    public void setConnectIcon(Icon connectIcon) {
        this.connectIcon = connectIcon;
    }

    public Icon getDisconnectIcon() {
        return disconnectIcon;
    }

    public void setDisconnectIcon(Icon disconnectIcon) {
        this.disconnectIcon = disconnectIcon;
    }

    public Icon getSendIcon() {
        return sendIcon;
    }

    public void setSendIcon(Icon sendIcon) {
        this.sendIcon = sendIcon;
    }

    public boolean serverClosed() {
        return serverClosed;
    }

    public void setServerClosed(boolean serverStatus) {
        this.serverClosed = serverStatus;
    }

    public boolean serverStopped() {
        return serverStopped;
    }

    public void setServerStopped(boolean serverStatus) {
        this.serverStopped = serverStatus;
    }

    public static long getSerialVersionUid() {
        return SERIAL_VERSION_UID;
    }

    private class EnterListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                getChatController().sendMessage(getUserName(), getInputArea().getText());// send message
                getInputArea().setText("");// clear inputArea
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            try {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    getInputArea().setText("");// clear inputArea
                }
            } catch (java.lang.IllegalArgumentException iAe) {
                System.out.println(iAe.getMessage());
            }
        }
    }

    // MyMessageListener listens for new messages from ChatControl and 
    // displays messages in messageArea using MessageDisplayer.
    private class MyMessageListener implements MessageListener {

        // when received, display new messages in messageArea
        @Override
        public void receiveMessage(String from, String message) {
            // append message using MessageDisplayer
            SwingUtilities.invokeLater(new MessageDisplayer(from, message));
        } // end messageReceived
    } // end MyMessageListener inner class

    // Displays new message by appending message to JTextArea.  Should
    // be executed only in Event thread; modifies live Swing component
    private class MessageDisplayer implements Runnable {

        private final String fromUser;// user from which message came
        private final String messageBody;// body of message

        // MessageDisplayer constructor
        public MessageDisplayer(String from, String body) {
            fromUser = from;// store originating user
            messageBody = body;// store message body
        } // end MessageDisplayer constructor

        // display new message in messageArea
        @Override
        public void run() {
            // append new message
            getMessageArea().append("\n" + fromUser + "> " + messageBody);
        } // end run      
    } // end MessageDisplayer inner class
} // end class ClientGUI

