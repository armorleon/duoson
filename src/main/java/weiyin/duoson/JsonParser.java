package weiyin.duoson;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

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
			throw new JsonParserException("NOT GOOD JSON");
		}
		return stack.peek().getToken();
	}
	
	
	public static void main(String[] args) throws JsonParserException {
		String a = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",  \r\n \"flag\"   :    true,   \"value\" :    30,  \r\n\t\"size\" : \"500    300\"\r\n\t}, \"desk\":  23.07\r\t\t\n ,\"list\":[{\"list1\": \"120\"},{\"list2\":null}]\r\n}";
		System.out.println(a);
		Object result = JsonParser.parse(a);
		System.out.println(result.toString());
		
		String b = "{\r\n" +
			   "\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 25,\r\n" +
			   "\"phoneNumber\": [\r\n" +
			       "{ \"type\": \"home\", \"number\": \"212 555-1234\" },\r\n"+
			       "{ \"type\": \"fax\", \"number\": \"646 555-4567\" }\r\n" +
			       "]\r\n"+
		"}";
		System.out.println(b);
		result = JsonParser.parse(b);
		System.out.println(result.toString());
		
		String c = "{\r\n\"a\" : []}";
		System.out.println(c);
		result = JsonParser.parse(c);
		System.out.println(result.toString());
			
        String[] tests = {
                "{\"TEST\":true,\" num \":1,\"B\":false,\"--float--\":2.5,\"null\":null,\"\":\"END\"}",
                "{ \"TEST\": true,\" num \": 1, \"B\" : false ,\"--float--\"\t: 2.5 , \"null\"  : null, \"\":\t \t\"END\" }",
                " { \"TEST\": \ntrue, \" num \"\r:\n1,\t\"B\"\n:false , \"--float--\": 2.5 ,\n\"null\"\n:null, \"\"\r:\"END\" } ",
                "\n{\n  \"TEST\": true,\t\" num \":\t 1,\"B\"\n:\n false ,\n\n\"--float--\"\n:\n2.5 ,\"null\"\n:\nnull, \"\"\n:\n\"END\"\r}\n\t",
                "\r{\t\"TEST\":\n \n \r true,    \n\" num \":1,  \"B\":  false \n, \"--float--\"\r \r:\r2.5 ,\t\"null\" :null ,\n\"\": \"END\"\n }\t"
        };
        int i = 0;
        for (String t : tests) {
        	
        	System.out.println(t);
        	result = JsonParser.parse(t);
        	System.out.println(result.toString());
        }
	}
	
}
