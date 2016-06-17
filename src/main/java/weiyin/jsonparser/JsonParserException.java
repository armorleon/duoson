package weiyin.jsonparser;

/**
 * The exception class for json parser
 * 
 * @author Wei Yin
 *
 */
public class JsonParserException extends Exception {
	
	/**
	 * 
	 * @param msg
	 */
	public JsonParserException(String msg) {
		super(msg);
	}
	
	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public JsonParserException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * 
	 */
	public JsonParserException() {
		super();
	}

	/**
	 * @param cause
	 */
	public JsonParserException(Throwable cause) {
		super(cause);
	}
	
	
}
