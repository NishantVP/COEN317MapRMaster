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
import module.sneha.testing.ShufflerJob;

/**
 * @author nishant
 *
 */
public class ClientMapperThread implements Runnable {
	
	private Thread t;
	private String threadName = "Client Mapper Thread";
	
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private int StoCPort;
	
	private List<Chunk> allFileChunksList;
	private List<Chunk> sentFileChunksList;
	private List<Chunk> processedFileChunksList;
	
	private long currentChunkNumber;
	
	private Chunk chunkMetaData;
	
	private String chunkToRead = "/home/nishant/Desktop/COEN317/chunksebook/1.txt";
	
	public ClientMapperThread(int port,Chunk chunk,List<Chunk> all,List<Chunk> sent,List<Chunk> processed) {
		this.StoCPort = port;
		this.chunkMetaData = chunk;
		this.allFileChunksList = all;
		this.sentFileChunksList = sent;
		this.processedFileChunksList = processed;
		this.chunkToRead = chunkMetaData.getChunkFilePathName();
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
				  
				  // FileReader reads text files in the default encoding.
		            FileReader fileReader = 
		                new FileReader(chunkToRead);

		            // Always wrap FileReader in BufferedReader.
		            BufferedReader bufferedReader = 
		                new BufferedReader(fileReader);
		            
		            String line3 = "---fileSendingFinishedByServer---";
		            System.out.println("file being sent - " +chunkToRead); 
		            while((line2 = bufferedReader.readLine()) != null) {
					  //line3 = line3 + line2;
					  pwrite.println(line2);             
					  pwrite.flush();
					  //System.out.println(line2);
		            } 	
		            // Always close files.
		            bufferedReader.close();  
		            pwrite.println(line3);             
					pwrite.flush();
					addChunkToSentList();
					
				  while(true)
				  {
				    if((receiveMessage = receiveRead.readLine()) != null)  
				    {
				       if(!chunkMetaData.isProcessed()){
				    	   ShufflerJob.combineStreams(receiveMessage);
				    	   System.out.println("Map output from Mobile: " +receiveMessage);
				    	   System.out.println("Output from Shuffler:" +ShufflerJob.getShufflerOutput());
				       }
				       else {
				    	   System.out.println("Some other Mapper already processed this chunk");
				       }
				       
				       addChunkToProcessedList();
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
	
	private synchronized void addChunkToSentList() {
		chunkMetaData.setSent(true);
		sentFileChunksList.add(chunkMetaData);
		
	}
	private synchronized void addChunkToProcessedList() {
		chunkMetaData.setProcessed(true);
		processedFileChunksList.add(chunkMetaData);
	}

}
