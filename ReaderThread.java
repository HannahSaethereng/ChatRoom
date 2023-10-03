/**
 /**
 * This thread is passed a socket that it reads from. Whenever it gets input
 * it writes it to the ChatScreen text area using the displayMessage() method.
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

public class ReaderThread implements Runnable
{
	Socket server;
	BufferedReader fromServer;
	ChatScreen screen;

	public ReaderThread(Socket server, ChatScreen screen) {
		this.server = server;
		this.screen = screen;
	}

	public void run() {
		try {
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

			while (true) {
				String message = fromServer.readLine();
				String username = null;
				String newMessage = null;
				String text = null;
		
				// parsing protocol, username and message and makes it more pretty
				int space1 = message.indexOf(' ');
				int space2 = message.indexOf(' ', space1+1);
				String protocol = message.substring(0, space1);

				if (protocol.equals("JOIN")){
					username = message.substring(space1);
					newMessage = username + " joinded the chatroom!";
				}
				else if (protocol.equals("SEND")){
					username = message.substring(space1, space2);
					text = message.substring(space2);
					newMessage = username + ":" + text;
				}
				else if (protocol.equals("LEAVE")){
					username = message.substring(space1);
					newMessage = username + " left the chatroom";
				}
				// now display it on the display area
				screen.displayMessage(newMessage);
			}
		}
		catch (IOException ioe) { System.out.println(ioe); }

	}
}