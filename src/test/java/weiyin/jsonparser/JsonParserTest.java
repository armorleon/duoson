package weiyin.jsonparser;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * JasonParser test class
 * 
 * @author Wei Yin
 *
 */
public class JsonParserTest {

	private Object testJson(String input) {
		System.out.println("INPUT_STRING:================================\n" + input);
		Object result = null;
		try {
			result = JsonParser.parse(input);
			System.out.println("OUTPUT_OBJECT:================================\n" + result.toString() + "\n\n");
		} catch (JsonParserException e) {
			e.printStackTrace();
			fail();
		}
		return result;
	}
	
	@Test
	public void testGoodJson1() {
		String input = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",  \r\n \"flag\"   :    true,   \"value\" :    30,  \r\n\t\"size\" : \"500    300\"\r\n\t}, \"desk\":  23.07\r\t\t\n ,\"list\":[{\"list1\": \"120\"},{\"list2\":null}]\r\n}";
		String expected = "{debug=on, desk=23.07, window={flag=true, size=500    300, title=sample, value=30}, list=[{list1=120}, {list2=null}]}"; 
		Object result = testJson(input);
		Assert.assertEquals(expected, result.toString());
		
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
		String expected = "{firstName=John, lastName=Smith, phoneNumber=[{number=212 555-1234, type=home}, {number=646 555-4567, type=fax}], age=25}";
		Object result = testJson(input);
		Assert.assertEquals(expected, result.toString());
	}
	
	@Test
	public void testGoodJson3() {
		String input = "{\r\n\"a\" : []}";
		String expected = "{a=[]}";
		Object result = testJson(input);
		Assert.assertEquals(expected, result.toString());

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
        
        String expected = "{=END, --float--=2.5, B=false, null=null, TEST=true, num =1}";
        
        for (int i = 0; i < inputs.length; i++) {
    		Object result = testJson(inputs[i]);
    		Assert.assertEquals(expected, result.toString());

        }
	}
	
	
	@Test (expected = JsonParserException.class)
	public void testBadJson1() throws JsonParserException {
		String input = "{";
		System.out.println("INPUT_STRING:================================\n" + input);
		Object result = JsonParser.parse(input);
	}
	
	@Test
	public void testExample1() {
		String input = "{\"debug\": \"on\" ," +
							"\"window\": {" +
								"\"title\":\"sample\", " +
								"\"size\" :  500 ," +
								"\"nextWindow\" : [ "
												+ "	{ \"title1  \": null  },"
												+ " {\"title2\": 220.008}"
												+ "]" +
							"}" +
						"}";		
		Map<String, Object> result = (Map<String, Object>) testJson(input);
		Assert.assertEquals("on", result.get("debug"));
		Map<String, Object> window = (Map<String, Object>) result.get("window");
		Assert.assertEquals("sample", window.get("title"));
		Assert.assertEquals(500, window.get("size"));
		List<Object> list = (List<Object>) window.get("nextWindow");
		Map<String, Object> title2Map = (Map<String, Object>) list.get(1);
		Assert.assertEquals(220.008, title2Map.get("title2"));
	}
}
