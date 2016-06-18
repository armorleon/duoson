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
				System.err.println("There are no token any more, but there are extra characters behind the last token, ignore those characters");
				break;
			}
			switch (nextObj.getType()) {
			case LEFT_MAP:
				stack.push(nextObj);
				break;
			case QUOTE:
				if (stack.peek().getType() == TokenType.QUOTE) {
					Object obj = stack.peek().getToken();
					//null means there is no String between two QUOTE, so assign "" to it
					if (obj == null) {
						obj = new String("");
					}
					//pop left QUOTE
					stack.pop();
					//and create a virtual token OBJECT for the whole string between two QUOTEs
					stack.push(new TokenObject(TokenType.OBJECT, obj));
				} else {
					//left QUOTE object's token is null
					stack.push(nextObj);
				}
				break;
			case STRING:
				//if the top is already QUOTE, just set the top's token with the current String
				if (stack.peek().getType() == TokenType.QUOTE) {
					stack.peek().setToken(nextObj.getToken());
				}
				break;
			case BOOLEAN:
			case NULL:
			case NUMBER:
				//if the top is COLON, convert the BOOLEAN, NULL, NUMBER to OBJECT
				if (stack.peek().getType() == TokenType.COLON) {
					stack.push(new TokenObject(TokenType.OBJECT, nextObj.getToken()));
				}
				break;
			case COLON:
				stack.push(nextObj);
				break;
			case COMMA:
				//COMMA means time to add to map or to list
				if (stack.peek().getType() == TokenType.OBJECT) {
					handleCommaWithObject(stack);
				}
				break;
			case LEFT_LIST:
				stack.push(nextObj);
				break;
			case RIGHT_LIST:
				if (stack.peek().getType() == TokenType.LEFT_LIST) {
					//pop LEFT_LIFT and create a virtual token OBJECT for the whole string between LEFT_LIST and RIGHT_LIST
					stack.push(new TokenObject(TokenType.OBJECT, stack.pop().getToken()));
				}
				break;
			case RIGHT_MAP:
				if (stack.peek().getType() == TokenType.OBJECT) {
					handleRightMapWithObject(stack);
				}
				break;
			}
		}
		
		//ideally, if the json string format is correct,
		//the stack should only contains one element which is OBJECT 
		if(stack.size() != 1 || stack.peek().getType() != TokenType.OBJECT) {
			throw new JsonParserException("ERROR JSON FORMAT");
		}
		
		//top's token is the real object we need to return
		return stack.peek().getToken();
	}

	/**
	 * When nextToken is COMMA and stack top is OBJECT
	 * 1. pop up valueObj and COLON
	 * 2. for COLON, pop up keyObj, insert into the map 
	 * 
	 * @param stack
	 * @throws JsonParserException
	 */
	private static void handleCommaWithObject(Deque<TokenObject> stack)
			throws JsonParserException {
		//pop value token
		Object valueObj = stack.pop().getToken();
		
		//pop could be COLON
		TokenObject top = stack.pop();
		
		//for COLON case, pop the key token and put the key-value pair into the map, case  { OBJECT : OBJECT ,
		if (top.getType() == TokenType.COLON) {
			String keyObj = (String)stack.pop().getToken();
			Map<String, Object> map = (Map<String, Object>) stack.peek().getToken();
			map.put(keyObj, valueObj);
		} 
		//error case
		else {
			throw new JsonParserException("COMMA");
		}
	}

	/**
	 * 1. pop three times to get valueObj, colon and keyObj
	 * 2. convert {OBJECT : OBJECT}  to new OBJECT
	 * 3. check the top of the stack, if colon, push new OBJECT
	 *    if LEFT_LIST, add new OBJECT to the list 
	 * 
	 * @param stack
	 * @throws JsonParserException
	 */
	private static void handleRightMapWithObject(Deque<TokenObject> stack)
			throws JsonParserException {
		//pop value token
		Object valueObj = stack.pop().getToken();
		
		//pop colon
		stack.pop();
		
		//pop key token
		String keyObj = (String)stack.pop().getToken();
		
		//The top of the stack is LEFT_MAP now, so fill map, case  { OBJECT : OBJECT }
		Map<String, Object> map = (Map<String, Object>) stack.peek().getToken();
		map.put(keyObj, valueObj);
		
		//pop LEFT_MAP and create a virtual token OBJECT using the map,  convert  {OBJECT : OBJECT}  to  OBJECT
		TokenObject obj = new TokenObject(TokenType.OBJECT, stack.pop().getToken());
		
		//top stack could be colon or LEFT_LIST
		//for COLON, put the OBJECT into the stack, case { OBJECT : {OBJECT : OBJECT}
		if (stack.isEmpty() || stack.peek().getType() == TokenType.COLON) {
			stack.push(obj);
		} 
		//for LEFT_LIST, add the value token to the list, case  [ {OBJECT : OBJECT}
		else if (stack.peek().getType() == TokenType.LEFT_LIST) {
			((ArrayList<Object>)stack.peek().getToken()).add(obj.getToken());
		} 
		//error case
		else {
			throw new JsonParserException("RIGHT_MAP");
		}
	}
	
}
