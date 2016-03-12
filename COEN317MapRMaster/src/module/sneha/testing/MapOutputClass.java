package module.sneha.testing;

public class MapOutputClass {

	private String str;
	private Integer integer;
	public MapOutputClass(String str, Integer i){
		this.str = str;
		this.integer = i;
		
	}
	
	public String getStringKey(){
		return this.str;
	}
	public Integer getIntegerValue(){
		return this.integer;
	}
	public MapOutputClass getTuple(){
		return this;
	}
}
