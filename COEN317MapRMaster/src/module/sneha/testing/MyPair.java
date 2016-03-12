package module.sneha.testing;


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
