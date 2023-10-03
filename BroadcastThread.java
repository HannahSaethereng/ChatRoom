import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class BroadcastThread implements Runnable
{
    private ArrayList<BufferedWriter> socketConnections;
    private Vector<String> messageQueue;

    public BroadcastThread(ArrayList<BufferedWriter> socketConnections, Vector<String> messageQueue) {
        this.socketConnections = socketConnections;
        this.messageQueue = messageQueue;
    }

    public void run() {
        while (true) {
            // sleep for 1/10th of a second
            try { Thread.sleep(100); } catch (InterruptedException ignore) { }
            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
            while (!messageQueue.isEmpty()){
                //get messsage from client and remove it immeditiately
                String message = messageQueue.remove(0);
                
                //looping trought messages and writing it to all clients
                for (int i = 0; i < socketConnections.size(); i++){
                    BufferedWriter client = socketConnections.get(i);
                    
                    try {
                        client.write(message + "\r\n");
                        client.flush();
                    } 
                    catch (IOException e) {
                        e.printStackTrace();
                        //socketConnections.remove(i);
                    }
                }
                
            }
        }
    }
} 