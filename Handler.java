import java.io.*;
import java.net.*;
import java.util.*;
public class Handler {
	
	private Dictionary<String, BufferedWriter> dictonary = new Hashtable<String, BufferedWriter>();
    
    public void process(Socket client,  Vector<String> messageQueue,  ArrayList<BufferedWriter> socketConnections) throws java.io.IOException {
		BufferedReader fromClient = null;
		
		try {
			String line;
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while ( (line = fromClient.readLine()) != null){
			messageQueue.add(line);
			int space1 = line.indexOf(' ');
			String protocol = line.substring(0, space1);
			String username = line.substring(space1);
			
			if (protocol.equals("JOIN") ) {
			
				OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());
				BufferedWriter toClient = new BufferedWriter(writer);
				socketConnections.add(toClient);
				
				dictonary.put(username, toClient);
				System.out.println("JOIN"+socketConnections);
			}
			else if (protocol.equals("LEAVE")){
					socketConnections.remove(dictonary.remove(username));
					System.out.println("LEAVE"+socketConnections);
					
				}
			}
			
   		}
           catch (IOException ioe) {
			System.err.println(ioe);

		}
		finally {
			// close streams and socket
			if (fromClient != null)
				fromClient.close();
		}
    }
}