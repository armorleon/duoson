package weiyin.jsonparser;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * JasonParser test class
 * 
 * @author Wei Yin
 *
 */
public class JsonParserTest {

	private void testJson(String input) {
		System.out.println("INPUT_STRING:================================\n" + input);
		try {
			Object result = JsonParser.parse(input);
			System.out.println("OUTPUT_OBJECT:================================\n" + result.toString() + "\n\n");
		} catch (JsonParserException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGoodJson1() {
		String input = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",  \r\n \"flag\"   :    true,   \"value\" :    30,  \r\n\t\"size\" : \"500    300\"\r\n\t}, \"desk\":  23.07\r\t\t\n ,\"list\":[{\"list1\": \"120\"},{\"list2\":null}]\r\n}";
		testJson(input);
	}

	@Test
	public void testGoodJson2() {
		String input = "{\r\n" +
				   "\"firstName\": \"John\", \"lastName\": \"Smith\", \"age\": 25,\r\n" +
				   "\"phoneNumber\": [\r\n" +
				       "{ \"type\": \"home\", \"number\": \"212 555-1234\" },\r\n"+
				       "{ \"type\": \"fax\", \"number\": \"646 555-4567\" }\r\n" +
				       "]\r\n"+
			"}";
		testJson(input);
	}
	
	@Test
	public void testGoodJson3() {
		String input = "{\r\n\"a\" : []}";
		testJson(input);
	}
	
	@Test
	public void testGoodJson4() {
        String[] inputs = {
                "{\"TEST\":true,\" num \":1,\"B\":false,\"--float--\":2.5,\"null\":null,\"\":\"END\"}",
                "{ \"TEST\": true,\" num \": 1, \"B\" : false ,\"--float--\"\t: 2.5 , \"null\"  : null, \"\":\t \t\"END\" }",
                " { \"TEST\": \ntrue, \" num \"\r:\n1,\t\"B\"\n:false , \"--float--\": 2.5 ,\n\"null\"\n:null, \"\"\r:\"END\" } ",
                "\n{\n  \"TEST\": true,\t\" num \":\t 1,\"B\"\n:\n false ,\n\n\"--float--\"\n:\n2.5 ,\"null\"\n:\nnull, \"\"\n:\n\"END\"\r}\n\t",
                "\r{\t\"TEST\":\n \n \r true,    \n\" num \":1,  \"B\":  false \n, \"--float--\"\r \r:\r2.5 ,\t\"null\" :null ,\n\"\": \"END\"\n }\t"
        };
        for (String input : inputs) {
        	testJson(input);
        }
	}
	
	
	@Test (expected = JsonParserException.class)
	public void testBadJson1() throws JsonParserException {
		String input = "{";
		System.out.println("INPUT_STRING:================================\n" + input);
		Object result = JsonParser.parse(input);
	}
}
