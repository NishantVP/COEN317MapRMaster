/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.communication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import module.files.Chunk;
import module.files.FileWritter;

/**
 * @author nishant
 *
 */
public class ClientReducerThread implements Runnable {
	
	private Thread t;
	private String threadName = "Client Reducer Thread";
	
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private int StoCPort;
	
	private String MapOutPut;
	
	
	private String chunkToRead = "/home/nishant/Desktop/COEN317/chunksebook/1.txt";
	
	public ClientReducerThread(int port,String mapop) {
		this.StoCPort = port;
		this.MapOutPut = mapop;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	
	public void start () {
		System.out.println("--Starting " +  threadName );
	    if (t == null) {
	    	t = new Thread (this, threadName);
	        t.start ();
	     }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("--Running " +  threadName );
		
		
		try {
	        serverSocket = new ServerSocket(StoCPort);  //Server socket
	        

	    } catch (IOException e) {
	        System.out.println("Could not listen on port: "+StoCPort);
	    }

	    System.out.println("Server started. Listening to the port "+StoCPort);

	    while (true) {
	        
	    	try {
	        	
	            clientSocket = serverSocket.accept();   //accept the client connection
	            
                // sending to client (pwrite object)
	            OutputStream ostream = clientSocket.getOutputStream(); 
	            PrintWriter pwrite = new PrintWriter(ostream, true);
				
				// receiving from server ( receiveRead  object)
				InputStream istream = clientSocket.getInputStream();
				BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
				
				String receiveMessage;
				String sendMessage = "This is from PC by Nishant";
				  
				// The name of the file to open.
			     //String fileName = this.fileName;//"/home/nishant/Documents/OSfilesendTest.txt";
			     String line2 = null;
			     String line3 = "---fileSendingFinishedByServer---";
			 
			     	pwrite.println(MapOutPut);             
			     	pwrite.flush();
		            pwrite.println(line3);             
					pwrite.flush();
				  
				  while(true)
				  {
				    if((receiveMessage = receiveRead.readLine()) != null)  
				    {
				       System.out.println("Reduce output from Mobile: " +receiveMessage);
				       FileWritter.appendReducerOutput(receiveMessage,1);
				    }
				    else {
				    	break;
				    }
				   
				  }
				  //System.out.println("Out of while loop");  
				  
	        } catch (IOException ex) {
	            System.out.println("Problem in message reading");
	        }
	    }

	}

}
