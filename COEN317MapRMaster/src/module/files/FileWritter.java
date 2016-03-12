/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import module.sneha.testing.MyPair;

/**
 * @author nishant
 *
 */
public class FileWritter {
	
	private static String FileNameWithPath;
	private static String FileName;
	private static String FilePath;
	
	public static synchronized void appendReducerOutput(String line, int fileNumber) {
		
		String FinalOutputPath = "/home/nishant/Desktop/COEN317/ReducerOutput/";
		
		File theDir = new File(FinalOutputPath);
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
    	
    	String FileNumberString = Integer.toString(fileNumber);
		// The name of the file to open.
        String fileName = FinalOutputPath +FileNumberString +".txt";
        File file = new File(fileName);
		try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(file);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            // Note that write() does not automatically
            // append a newline character.
            String[] buffer = line.split(",");
            
            for(int i=0;i<buffer.length;i++){
            	bufferedWriter.write(buffer[i]);
                bufferedWriter.newLine();
    		}
            
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
	
	private static synchronized void writeLineToFile(String line, String path, int chunkNumber) {	
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
