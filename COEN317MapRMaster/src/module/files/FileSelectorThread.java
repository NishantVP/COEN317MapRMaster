/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.files;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import module.communication.NewConnectionListenerThread;


public class FileSelectorThread implements Runnable {
	
	private Thread t;
	private String threadName = "FileSelector Thread";
	
	private String chunkPath = "/home/nishant/Desktop/COEN317/chunks";
	private File selectedFile;
	private long totalChunks;
	
	private List<Chunk> allFileChunksList;
	private List<Chunk> sentFileChunksList;
	private List<Chunk> processedFileChunksList;
	
	private static final int NUMBER_OF_LINES_PER_CHUNK = 30;
	// = new ArrayList<Chunk>();
	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	
	public FileSelectorThread() {
		this.allFileChunksList = new ArrayList<Chunk>();
		this.sentFileChunksList = new ArrayList<Chunk>();
		this.processedFileChunksList = new ArrayList<Chunk>();
		
	}
	
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
		
		//Open UI for file chooser
    	Container c = new Container();
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    	int result = fileChooser.showOpenDialog(c);
    	
    	if (result == JFileChooser.APPROVE_OPTION) {
    		selectedFile = fileChooser.getSelectedFile();
    		System.out.println("Selected file: " + selectedFile.getAbsolutePath());
    		splitFile();
    	}
    	else {
    		System.out.println("File not selected");
    	}
    	
    	new NewConnectionListenerThread(allFileChunksList,sentFileChunksList,processedFileChunksList).start(); 
    	
	}
	
	private void splitFile(){
		int countLines = 0;
		String line = null;
		int chunkNumer = 0;
		String chunkfileContent = "";
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		String extentionRemoved = selectedFile.getName().split("\\.")[0];
		chunkPath = chunkPath +extentionRemoved +"/";
		//int i = 0;
		
		try {
			fileReader = new FileReader(selectedFile);
			bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				countLines++;
				chunkNumer = countLines/20;
				
				File theDir = new File(chunkPath);
            	// if the directory does not exist, create it
            	if (!theDir.exists()) {
            	    System.out.println("creating new directory: ");
            	    boolean result = false;

            	    try {
            	        theDir.mkdir();
            	        result = true;
            	    } 
            	    catch(SecurityException se){
            	        //handle it
            	    }        
            	    if(result) {    
            	        System.out.println("DIR created");  
            	    }
            	}
            	chunkfileContent = chunkfileContent +"\n"+ line;
            	
            	if(countLines%NUMBER_OF_LINES_PER_CHUNK == 0) {
            		
            		writeLineToFile(chunkfileContent,chunkPath,chunkNumer);
            		chunkfileContent = "";
            		//i++;
            		String chunkFileName = chunkPath +chunkNumer +".txt";
            		//Add newly created chunk to the list
            		allFileChunksList.add(new Chunk(chunkFileName,chunkNumer));
            	}
			}
			writeLineToFile(chunkfileContent,chunkPath,chunkNumer);
            // Always close files.
            bufferedReader.close();     
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("inside if i:" +i);
		
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

}
