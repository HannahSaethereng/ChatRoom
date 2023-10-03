import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.Vector;

public class Server {

    public static final int DEFAULT_PORT = 5555;

    private static final Executor exec = Executors.newCachedThreadPool();

	// arraylist to hold all client socket connections, this will be passed to connection and from there to handler
	public static ArrayList <BufferedWriter> socketConnections = new ArrayList <BufferedWriter>();

	//vector queue to hold message
	public static Vector<String> messageQueue = new Vector<String>();
	
	public static void main(String[] args) throws IOException {
		ServerSocket sock = null; 
		Socket client = null;
		
		try {
			sock = new ServerSocket(DEFAULT_PORT);
			Runnable broadcast = new BroadcastThread(socketConnections, messageQueue); 
			exec.execute(broadcast);

			while (true) {
				client = sock.accept();
				//OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());
				//BufferedWriter toClient = new BufferedWriter(writer);
				//socketConnections.add(toClient);
				Runnable task = new Connection(client, messageQueue, socketConnections);
				exec.execute(task);

			}
		}
		catch (IOException ioe) { System.err.println(ioe); }
		finally {
			if (sock != null)
				sock.close();
		}
	}
}
