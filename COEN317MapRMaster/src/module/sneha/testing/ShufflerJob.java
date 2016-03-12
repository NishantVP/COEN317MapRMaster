package module.sneha.testing;
import java.util.*;

public class ShufflerJob {

	
	private static List<MapOutputClass> list;
	private static HashMap map;
	private static List<MyPair> listPair;
	
	private static StringBuilder shufflerSortedOutput;
	private static StringBuilder mapSoFar = new StringBuilder();

	public static void combineStreams(String addStr){
		
		listPair = new ArrayList<MyPair>();
		shufflerSortedOutput = new StringBuilder();
		mapSoFar.append(addStr);
		map = new HashMap<String,Integer>();
		String[] buffer = mapSoFar.toString().split(",");
		
		String[]keyVal = null;
		for(int i=0;i<buffer.length;i++){
			keyVal = buffer[i].split("=");
			
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
		
		System.out.println(shufflerSortedOutput.toString());
	}

	public static String getShufflerOutput(){
		return shufflerSortedOutput.toString();
	}
}

