package weiyin.jsonparser;

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
	OBJECT("OBJECT");
	
	String value;
	TokenType(String value) {
		this.value = value;
	}
	
	String getValue() {
		return this.value;
	}
}
