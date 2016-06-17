package weiyin.jsonparser;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * The parser class to handle json string
 * 
 * @author Wei Yin
 *
 */
public class JsonParser {

	public static Object parse(String json) throws JsonParserException {
		
		Deque<TokenObject> stack = new LinkedList<TokenObject>();
		TokenReader reader = new TokenReader(json);
		while (reader.hasNext()) {
			TokenObject nextObj = reader.nextToken();
			if(nextObj == null) {
				System.err.println("nextObj is nulllllllllllllll");
				break;
			}
			switch (nextObj.getType()) {
			case LEFT_MAP:
				stack.push(nextObj);
				break;
			case QUOTE:
				if (stack.peek().getType() == TokenType.QUOTE) {
					Object obj = stack.peek().getToken();
					if (obj == null) {
						obj = new String("");
					}
					stack.pop();
					stack.push(new TokenObject(TokenType.OBJECT, obj));
				} else {
					stack.push(nextObj);
				}
				break;
			case STRING:		
				if (stack.peek().getType() == TokenType.QUOTE) {
					stack.peek().setToken(nextObj.getToken());
					break;
				}
				break;
			case BOOLEAN:
			case NULL:
			case NUMBER:
				if (stack.peek().getType() == TokenType.COLON) {
					stack.push(new TokenObject(TokenType.OBJECT, nextObj.getToken()));
				}
				break;
			case COLON:
				stack.push(nextObj);
				break;
			case COMMA:
				if (stack.peek().getType() == TokenType.OBJECT) {
					Object valueObj = stack.pop().getToken();
					
					TokenObject top = stack.pop();
					if (top.getType() == TokenType.COLON) {// colon
						String keyObj = (String)stack.pop().getToken();
						Map<String, Object> map = (Map<String, Object>) stack.peek().getToken();
						map.put(keyObj, valueObj);
					} else if (top.getType() == TokenType.LEFT_LIST) {
						((ArrayList<Object>)top.getToken()).add(valueObj);
					} else {
						throw new JsonParserException("COMMA");
					}
				}
				break;
			case LEFT_LIST:
				stack.push(nextObj);
				break;
			case RIGHT_LIST:
				if (stack.peek().getType() == TokenType.LEFT_LIST) {
					stack.push(new TokenObject(TokenType.OBJECT, stack.pop().getToken()));
				}
				break;
			case RIGHT_MAP:
				if (stack.peek().getType() == TokenType.OBJECT) {
					Object valueObj = stack.pop().getToken();
					stack.pop();// colon
					String keyObj = (String)stack.pop().getToken();
					Map<String, Object> map = (Map<String, Object>) stack.peek().getToken();
					map.put(keyObj, valueObj);
					
					TokenObject obj = new TokenObject(TokenType.OBJECT, stack.pop().getToken());
					
					if (stack.isEmpty() || stack.peek().getType() == TokenType.COLON) {
						stack.push(obj);
					} else if (stack.peek().getType() == TokenType.LEFT_LIST) {
						((ArrayList<Object>)stack.peek().getToken()).add(obj.getToken());
					} else {
						throw new JsonParserException("RIGHT_MAP");
					}
				}
				break;
				
			}
		}
		
		if(stack.size() != 1 || stack.peek().getType() != TokenType.OBJECT) {
			throw new JsonParserException("ERROR JSON FORMAT");
		}
		return stack.peek().getToken();
	}
	
}
