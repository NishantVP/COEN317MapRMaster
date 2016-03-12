package module.sneha.testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class ReducerJob{
	
	private Thread r;
	public static HashMap input;
	private static ArrayList<MapOutputClass> fromShuffler;
	public static StringBuilder stream;

	public static String reduceAndroid(String inputFromShuffler){
		fromShuffler = new ArrayList<MapOutputClass>();
		System.out.println("REDUCER INPUT STREAM : "+inputFromShuffler);
		String[] buffer = inputFromShuffler.split(",");
		String[]redKeyval;
		
		for(int i=0;i<buffer.length;i++){
			redKeyval = buffer[i].split("=");
			fromShuffler.add(new MapOutputClass(redKeyval[0],new Integer(redKeyval[1])));
		}
		
		Iterator itr = fromShuffler.iterator();
		input = new HashMap<String,Integer>();
		while(itr.hasNext()){
			MapOutputClass tempo = (MapOutputClass) itr.next();
			String tempstr =tempo.getStringKey();
			if(input.containsKey(tempstr)){
				Integer count = (Integer) input.get(tempstr);
				count++;
				input.put(tempstr, count);
			}
			else{
				input.put(tempstr, 1);
			}
		}
		return buildStream().toString();
	}
	
	public static StringBuilder buildStream(){
		stream = new StringBuilder();
		Set mapEntry = input.entrySet();
		Iterator mapItr = mapEntry.iterator();
		while(mapItr.hasNext()){
			stream.append(mapItr.next()+",");
		}
		
		System.out.println("REDUCER OUTPUT STREAM : "+stream);
		return stream;
		
	}

	
}
