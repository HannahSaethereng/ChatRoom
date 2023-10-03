/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne 
 */

import java.net.*;
import java.util.ArrayList;
import java.util.Vector;
import java.io.*;

public class Connection implements Runnable
{
	private Socket	client;
	private static Handler handler = new Handler();
	private Vector<String> messageQueue;
	private ArrayList<BufferedWriter> socketConnections;
	
	public Connection(Socket client, Vector<String> messageQueue, ArrayList<BufferedWriter> socketConnections) {
		this.client = client;
		this.messageQueue = messageQueue;
		this.socketConnections = socketConnections;
	}

    /**
     * This method runs in a separate thread.
     */	
	public void run() { 
		try {
			handler.process(client, messageQueue, socketConnections);
		}
		catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
	}
}