package weiyin.jsonparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * TokenReader test class
 * 
 * @author Wei Yin
 *
 */
public class TokenReaderTest {
	
	@Before
	public void setUp() {
		System.out.println("===============================================");
	}
	
	
	@Test
	public void testInput1() {
		String a = "{\"debug\" : \"on\",\r\n\"window\" : {\r\n\t\"title\" : \"sample\",  "
				+ "\r\n \"flag\"   :    true,   \"value\" :"
				+ "    30,  \r\n\t\"size\" : \"500\t\t300\"\r\n\t}, \"desk\":  23.07\r\t\t\n}";
		try {
			TokenReader tr = new TokenReader(a);
			int count = 0;
			while (tr.hasNext()) {
				System.out.println(tr.nextToken());
				count++;
			}
			assertEquals(count, 49);
		} catch (JsonParserException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testInput2() {
		String a = "[   {\t}  ,  [ \r\n{  }   , {}]";
		try {
			TokenReader tr = new TokenReader(a);
			int count = 0;
			while (tr.hasNext()) {
				System.out.println(tr.nextToken());
				count++;
			}
			assertEquals(count, 11);
		} catch (JsonParserException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test (expected = JsonParserException.class)
	public void testException() throws JsonParserException {
		TokenReader tr = new TokenReader(null);
	}
}
