package weiyin.jsonparser;

/**
 * The enum class to hold all token types
 * 
 * @author Wei Yin
 *
 */
public enum TokenType {
	
	QUOTE("\""),
	
	LEFT_MAP("{"),
	
	RIGHT_MAP("}"),
	
	LEFT_LIST("["),
	
	RIGHT_LIST("]"),
	
	COMMA(","),
	
	COLON(":"),
	
	STRING(""),
	
	BOOLEAN(""),
	
	NULL("NULL"),
	
	NUMBER("NUMBER"),
	
	OBJECT("OBJECT");	//OBJECT is a virtual token
	
	private String value;
	
	/**
	 * Enum constructor
	 * 
	 * @param value
	 */
	TokenType(String value) {
		this.value = value;
	}
	
	/**
	 * Getter method
	 * 
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
}
