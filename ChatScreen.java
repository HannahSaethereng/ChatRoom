/**
 * This program is a rudimentary demonstration of Swing GUI programming.
 * Note, the default layout manager for JFrames is the border layout. This
 * enables us to position containers using the coordinates South and Center.
 *
 * Usage:
 *	java ChatScreen
 *
 * When the user enters text in the textfield, it is displayed backwards 
 * in the display area.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;


public class ChatScreen extends JFrame implements ActionListener, KeyListener
{
	private static JButton sendButton;
	private static JButton exitButton;
	private static  JTextField sendText;
	private JTextArea displayArea;
	
	//creating a buffer reader 

	private BufferedWriter writer;
	private Socket server;
	private String username;

	public ChatScreen(Socket server, String username) {
		/**
		 * a panel used for placing components
		 */
		
		this.server = server;
		this.username = username;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			writer.write("JOIN " + username + "\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JPanel p = new JPanel();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
		p.setBorder(titled);

		/**
		 * set up all the components
		 */
		sendText = new JTextField(30);
		sendButton = new JButton("Send");
		exitButton = new JButton("Exit");

		/**
		 * register the listeners for the different button clicks
		 */
		sendText.addKeyListener(this);
		sendButton.addActionListener(this);
		exitButton.addActionListener(this);

		/**
		 * add the components to the panel
		 */
		p.add(sendText);
		p.add(sendButton);
		p.add(exitButton);

		/**
		 * add the panel to the "south" end of the container
		 */
		getContentPane().add(p,"South");

		/**
		 * add the text area for displaying output. Associate
		 * a scrollbar with this text area. Note we add the scrollpane
		 * to the container, not the text area
		 */
		displayArea = new JTextArea(15,40);
		displayArea.setEditable(false);
		displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(displayArea);
		getContentPane().add(scrollPane,"Center");

		/**
		 * set the title and size of the frame
		 */
		setTitle("GUI Demo");
		pack();

		setVisible(true);
		sendText.requestFocus();

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		} );

	}

	/**
	 * Displays a message
	 */
	public void displayMessage(String message) {

		displayArea.append(message + "\n");
	}

	/**
	 * This gets the text the user entered and outputs it
	 * in the display area.
	 */
	public void displayText() {
		// write to server the message
		String message = sendText.getText().trim();

		try {
			writer.write("SEND " + username + " " + message + "\r\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		sendText.setText("");
		sendText.requestFocus();
	}

	/**
	 * This method responds to action events .... i.e. button clicks
	 * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == sendButton) 
			displayText();
		else if (source == exitButton)
			try {
				writer.write("LEAVE " + username + "\r\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
	}

	/**
	 * These methods responds to keystroke events and fulfills
	 * the contract of the KeyListener interface.
	 */

	/**
	 * This is invoked when the user presses
	 * the ENTER key.
	 */
	public void keyPressed(KeyEvent e) { 
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			displayText();
	}

	/** Not implemented */
	public void keyReleased(KeyEvent e) { }

	/** Not implemented */
	public void keyTyped(KeyEvent e) {  }


	public static void main(String[] args) {
		try {
			Socket server = new Socket(args[0], 5555);
			ChatScreen win = new ChatScreen(server, args[1]);
			Thread ReaderThread = new Thread(new ReaderThread(server, win));

			ReaderThread.start();
		}
		catch (UnknownHostException uhe) { System.out.println(uhe); }
		catch (IOException ioe) { System.out.println(ioe); }


	}
}