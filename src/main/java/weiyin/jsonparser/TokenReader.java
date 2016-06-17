package weiyin.jsonparser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The reader class to read the input json string and return next token for each calling.
 * 
 * @author Wei Yin
 *
 */
public class TokenReader {
	
	/**
	 * The cursor which points to the current token.
	 */
	private int cursor = 0;
	
	/**
	 * The buffer memory to hold the whole json string
	 */
	private char[] buffer;
	
	/**
	 * The loaded input String size
	 */
	private int loadStringSize = 0;
	
	/**
	 * The previous token recognized
	 */
	private TokenObject prevToken = null;
	
	/**
	 * Constructor method to take input Json string
	 * @param input
	 * @throws JsonParserException
	 */
	public TokenReader(String input) throws JsonParserException {
		if (input == null) {
			throw new JsonParserException("The input string is null");
		}
		this.buffer = input.toCharArray();
		this.loadStringSize = input.length();
		this.cursor = 0;
	}
	
	/**
	 * Check whether reach the end of the input string
	 *  
	 * @return
	 */
	public boolean hasNext() {
		return cursor < loadStringSize;
	}
	
	/**
	 * The method to check whether is white space
	 * 
	 * @param a
	 * @return
	 */
	public boolean isWhiteSpace(char a) {
		return a == '\r' || a == '\n' || a == '\t' || a == ' '; 
	}

	/**
	 * The method to check whether it's a key token
	 * @param a
	 * @return
	 */
	public boolean isKeyToken(char a) {
		return a == '\"' || a == '[' || a == ']' || a == '{' || a == '}' || a == ':' || a == ','; 
	}

	/**
	 * Method to get the next token
	 * 
	 * @return
	 */
	public TokenObject nextToken() {
		if (cursor >= loadStringSize) {
			return null;
		}
		
		TokenObject nextObject = null;
		
		//loop through all token types
		while (cursor < loadStringSize) {
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
				
			//parse complex string 
			default:
				int start = cursor;
				int end = cursor;
				while (end < loadStringSize) {
					//if "reach next key token" or "reach white space and his white space is not within quota"
					if (isKeyToken(buffer[end]) || 
							(prevToken.getType() != TokenType.QUOTE && isWhiteSpace(buffer[end]))) {
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

}
