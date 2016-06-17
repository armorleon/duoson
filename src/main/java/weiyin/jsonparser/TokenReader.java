package weiyin.jsonparser;

import java.util.ArrayList;
import java.util.HashMap;

public class TokenReader {
	
	private int cursor = 0;
	private char[] buffer;
	private int loadSize = 0;
	
	public TokenReader(String input) {
		this.buffer = input.toCharArray();
		this.loadSize = input.length();
		cursor = 0;
	}
	
	public boolean hasNext() {
		return cursor < loadSize;
	}
	
	public boolean isWhiteSpace(char a) {
		return a == '\r' || a == '\n' || a == '\t' || a == ' '; 
	}

	public boolean isKeyToken(char a) {
		return a == '\"' || a == '[' || a == ']' || a == '{' || a == '}' || a == ':' || a == ','; 
	}
	
	private TokenObject prevToken = null;
	
	public TokenObject nextToken() {
		if (cursor >= loadSize) {
			return null;
		}
		
		TokenObject nextObject = null;	
		while (cursor < loadSize) {
			char c = buffer[cursor];
			switch(c) {
			case '\r':
			case '\n':
			case '\t':
			case ' ':
				cursor++;
				break;
			case '\"':
				nextObject = new TokenObject(TokenType.QUOTE, null);
				cursor++;
				break;
			case '[':
				nextObject = new TokenObject(TokenType.LEFT_LIST, new ArrayList<Object>());
				cursor++;
				break;
			case ']':
				nextObject = new TokenObject(TokenType.RIGHT_LIST, null);
				cursor++;
				break;
			case '{':
				nextObject = new TokenObject(TokenType.LEFT_MAP, new HashMap<String, Object>());
				cursor++;
				break;
			case '}':
				nextObject = new TokenObject(TokenType.RIGHT_MAP, null);
				cursor++;
				break;
			case ':':
				nextObject = new TokenObject(TokenType.COLON, null);
				cursor++;
				break;
			case ',':
				nextObject = new TokenObject(TokenType.COMMA, null);
				cursor++;
				break;
			default:
				int start = cursor;
				int end = cursor;
				while (end < loadSize) {
					if (isKeyToken(buffer[end]) || (prevToken.getType() != TokenType.QUOTE && isWhiteSpace(buffer[end]))) {
						break;
					}
					end++;
				}
				cursor = end;
				
				String strObj = new String(buffer, start, end - start);
				if (prevToken.getType() != TokenType.QUOTE) {
					if (strObj.equalsIgnoreCase("null")) {
						nextObject = new TokenObject(TokenType.NULL, null);
					} else if (strObj.equalsIgnoreCase("true") || strObj.equalsIgnoreCase("false")) {
						nextObject = new TokenObject(TokenType.BOOLEAN, Boolean.parseBoolean(strObj));
					} else {
						nextObject = new TokenObject(TokenType.NUMBER, Double.parseDouble(strObj));
					}
				} else {
					nextObject = new TokenObject(TokenType.STRING, strObj);
				}
				break;
			}
			
			if (nextObject != null) {
				prevToken = nextObject;
				break;
			}
			
		}
		
		return nextObject;
	}
	
	public static void main(String[] args) {
		String b = "[   {\t}  ,  [ \r\n{  }   , {}]";
		String a = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",  \r\n \"flag\"   :    true,   \"value\" :    30,  \r\n\t\"size\" : \"500\t\t300\"\r\n\t}, \"desk\":  23.07\r\t\t\n}";
		TokenReader tr = new TokenReader(a);
		while (tr.hasNext()) {
			System.out.println(tr.nextToken());
		}
	}
}
