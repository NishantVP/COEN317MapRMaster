/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.List;

import module.files.Chunk;
import module.sneha.testing.ShufflerJob;

/**
 * @author nishant
 *
 */
public class NewConnectionListenerThread implements Runnable {
	
	
	private Thread t;
	private String threadName = "New Connection Listener Thread";
	
	private List<Chunk> allFileChunksList;
	private List<Chunk> sentFileChunksList;
	private List<Chunk> processedFileChunksList;
	
	private static String pythonScriptPath = "/home/nishant/JavaWorkspace/GitRepos/COEN317MapRMaster/COEN317MapRMaster/SaveServerIP.py";
	public static final String GREETING = "Hello I must be going.\r\n";
	
	private static ServerSocketChannel ssc;
	private static String ServerIP;          //IP address of the server
	private static int    ListenPORT = 9999; //default listening port
	private static ServerSocketChannel serverSocketChannel;
	private int StoCPort = 50000; //Server to Client data exchange port //Always Even
	private int CtoSPort = 50001; //Client to Server data exchange port //Always Odd
	
	private int chunkNoToSend = 0;
	
	public NewConnectionListenerThread (List<Chunk> all,List<Chunk> sent,List<Chunk> processed) {
		this.allFileChunksList = all;
		this.sentFileChunksList = sent;
		this.processedFileChunksList = processed;
	}
	
	public void start () {
		System.out.println("--Starting " +  threadName );
	    if (t == null) {
	    	t = new Thread (this, threadName);
	        t.start ();
	     }
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("--Running " +  threadName );
		
		ServerIP = getFilterIPAddresses();
		
		//System.out.println("List Testing: " +allFileChunksList.get(0).getChunkFilePathName());
		
		for(int i=0; i<allFileChunksList.size(); i++) {
			System.out.println("Path of all chunks: " +allFileChunksList.get(i).getChunkFilePathName());
		}
		
		try {
			saveServerIPToCloud();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  
		  ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
	  
		  try {
			  ssc = ServerSocketChannel.open();
			  ssc.socket().bind(new InetSocketAddress(ListenPORT));
			  ssc.configureBlocking(false);
		  } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		  }
		  
		  try {
			  while (true) {
			      //System.out.println("Waiting for New mobile");
	
			      SocketChannel sc;
				
					sc = ssc.accept();
				
			      
			      if (sc == null) {
			        try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			      } 
			      else {
			        System.out.println("Incoming connection from: " + sc.socket().getRemoteSocketAddress());
			        String FifthReducerOP = "";
			        String FifthResultPath = "/home/nishant/Desktop/COEN317/FinalOutput/";
			        String workerFunction;
			        
			        System.out.println("Total Chunks: " +allFileChunksList.size());
			        System.out.println("Sent Chunks: " +sentFileChunksList.size());
			        System.out.println("Processed Chunks: " +processedFileChunksList.size());
			        
			        /*if(chunkNoToSend == 5) {
			        	//FifthReducerOP = ReducerJob.reduceAndroid(ShufflerJob.getShufflerOutput());
			        	//writeLineToFile(FifthReducerOP,FifthResultPath,1);
			        	 new ClientReducerThread(StoCPort,ShufflerJob.getShufflerOutput()).start();
			        	 workerFunction = "reduce"; //decideMapperOrReducer();
			        }*/
			        if(processedFileChunksList.size() == allFileChunksList.size()) {
			        	 new ClientReducerThread(StoCPort,ShufflerJob.getShufflerOutput()).start();
			        	 workerFunction = "reduce"; //decideMapperOrReducer();
			        }
			        else {
			        	  workerFunction = "map"; //decideMapperOrReducer();
			        	 
			        	  chunkNoToSend = processedFileChunksList.size();
			        	  System.out.println("ChunkToMapper:" +chunkNoToSend);
			        	 
			        	  new ClientMapperThread(StoCPort,
					        		allFileChunksList.get(chunkNoToSend),
					        		allFileChunksList,
					        		sentFileChunksList,
					        		processedFileChunksList).start();
					        
					        //chunkNoToSend++;
					        
			        }
			        
			        String PortNumberToSend = Integer.toString(StoCPort);

			        String DataForNewWorker = PortNumberToSend + "," +workerFunction;
			        
			        buffer = ByteBuffer.wrap(DataForNewWorker.getBytes());
			        
			        CtoSPort = CtoSPort + 2;
			        StoCPort = StoCPort + 2;
			        
			        //System.out.println("Outgoing Buffer " +buffer);
			        buffer.rewind();
			        //System.out.println("Outgoing Buffer " +buffer);
			        //sc.write(buffer);
			        while(buffer.hasRemaining()) {
			            sc.write(buffer);
			        	//System.out.println("Outgoing Buffer " +buffer);
			        }
			        sc.close();
			      }
			  }
		  } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  
		
	}
	
	//Filter IP address
	public static String getFilterIPAddresses() {
		String myIP = null;
		Enumeration<?> e;		
		try {
			
			e = NetworkInterface.getNetworkInterfaces();
			//for each IP address check for the valid IP address
			while(e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				
				//get all the available IP addresses of the Server 
				Enumeration<?> ee = n.getInetAddresses();
				 
				//get the valid IP address 
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();	  
					if(i.getHostAddress().contains(".")) {
					  if(!i.getHostAddress().contains("127.0.")) {
						  myIP = i.getHostAddress();
						  System.out.println(myIP);
					  }
					}
				}
			}
		  } catch (SocketException e2) {
			// TODO Auto-generated catch block
				e2.printStackTrace();
		  }
		return myIP;
	}
	
		private static void writeLineToFile(String line, String path, int chunkNumber) {
		
		String chunkNumberString = Integer.toString(chunkNumber);
		// The name of the file to open.
        String fileName = path +chunkNumberString +".txt";
        File file = new File(fileName);
		try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(file);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
		
	}

	
	  
	private static void saveServerIPToCloud() throws IOException {
		// set up the command and parameter
		String Port = Integer.toString(ListenPORT);
		
		String[] cmd = new String[6];
		cmd[0] = "python"; // check version of installed python: python -V
		cmd[1] = pythonScriptPath;
		cmd[2] = "NishantServer";
		cmd[3] = ServerIP;
		cmd[4] = Port; //port
		cmd[5] = "true"; //running
		 
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		 
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while((line = bfr.readLine()) != null) {
			// display each output line form python script
			System.out.println("ObjIDinParse: " +line);
		}
	}
	
	

}
