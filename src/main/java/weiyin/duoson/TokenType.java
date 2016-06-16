package weiyin.duoson;

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
	
//	OBJ_KEY("OBJ_KEY"),
//	OBJ_VALUE("OBJ_VALUE");
//	OBJECT_MAP("OBJECT_MAP"),
	OBJECT("OBJECT");
	
	String value;
	TokenType(String value) {
		this.value = value;
	}
	
	String getValue() {
		return this.value;
	}
}
