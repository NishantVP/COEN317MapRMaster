/**
 * COEN 317 Distributed Computing (Winter 2016)
 * Final Project: MapReduce with Android Workers
 * Nishant Phatangare, Sneha Shirsat
 */
package module.testing;

/**
 * @author nishant
 *
 */
public class MyPair {

	public String key;
	public Integer value;
	
	public MyPair(String str, int i){
		this.key = str;
		this.value = new Integer(i);
	}
	
	public String getKey(){
		return this.key;
	}
	public int getInteger(){
		return this.value.intValue();
	}
}