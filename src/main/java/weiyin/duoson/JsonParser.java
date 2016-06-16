package weiyin.duoson;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

//assume
/*

'{
	"debug" : "on",
	"window" : {
		"title" : "sample",
		"size" : 500
	}
}'

{
	   "firstName": "John", "lastName": "Smith", "age": 25,
	   "phoneNumber": [
	       { "type": "home", "number": "212 555-1234" },
	       { "type": "fax", "number": "646 555-4567" }
	       ]
}
*/

public class JsonParser {
	public static Object parse(String json) throws JsonParserException {
		
		String a = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",\r\n\t\"size\" : 500\r\n\t}\r\n}";
		
		Deque<TokenObject> stack = new LinkedList<TokenObject>();
		
		/**
		 * public enum TokenType {
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
	DOUBLE("DOUBLE"),
	
		 */
		int n = 0;
		TokenReader reader = new TokenReader(json);
		while (reader.hasNext()) {
			TokenObject nextObj = reader.nextToken();
			
			switch(nextObj.getType()) {
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
				n++;
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
					stack.offer(new TokenObject(TokenType.OBJECT, stack.pop().getToken()));
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
//					if (!stack.isEmpty() && stack.peek().getType() == TokenType.LEFT_LIST) {
//						((ArrayList<Object>)stack.peek().getToken()).add(obj.getToken());
//					} else 
//					{
						stack.push(obj);
//					}
				}
				break;
				
			}
			
			
		}
		
//		System.out.println(a);

	
		return stack.peek().getToken();
	}
	
	
	public static void main(String[] args) throws JsonParserException {
		String a = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",  \r\n \"flag\"   :    true,   \"value\" :    30,  \r\n\t\"size\" : \"500    300\"\r\n\t}, \"desk\":  23.07\r\t\t\n ,\"list\":[{\"list1\": \"120\"},{\"list2\":null}]\r\n}";
		System.out.println(a);
		Object result = JsonParser.parse(a);
		System.out.println(result.toString());
	}
}
