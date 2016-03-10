/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.testing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
public class ShufflerJob {

	
	private static List<MapOutputClass> list;
	private static HashMap map;
	private static List<MyPair> listPair;
	
	private static StringBuilder shufflerSortedOutput;
//	private static StringBuilder mapSoFar = new StringBuilder();

	public static void combineStreams(String addStr){
		
		listPair = new ArrayList<MyPair>();
		shufflerSortedOutput = new StringBuilder();
		map = new HashMap<String,Integer>();
		String[] buffer = addStr.toString().split(",");
		//System.out.println("Buffer: " +buffer[0]);
		//System.out.println("BufferLength: " +buffer.length);
		String[]keyVal;
		for(int i=0;i<buffer.length-1;i++){
			keyVal = buffer[i].split("=");
			//System.out.println("key0 = "+keyVal[0]);
			//System.out.println("i = "+i);
			listPair.add(new MyPair(keyVal[0], Integer.parseInt(keyVal[1])));
			
			
		}
		Collections.sort(listPair, new Comparator<MyPair>(){

			@Override
			public int compare(MyPair mp1, MyPair mp2) {
				// TODO Auto-generated method stub
				if(mp1.key==null || mp2.key==null){
					return 0;
				}
				else if(mp1.key==null){
					return -1;
				}
				else if(mp2.key==null){
					return 1;
				}
				else{
					return mp1.key.toUpperCase().compareTo(mp2.key.toUpperCase());
					
				}
				
			}
			
		});
		
		for(int i=0;i<listPair.size();i++){
			shufflerSortedOutput.append(listPair.get(i).getKey()+"="+listPair.get(i).getInteger()+",");
		}
	}

	public static String getShufflerOutput(){
		return shufflerSortedOutput.toString();
	}
}
