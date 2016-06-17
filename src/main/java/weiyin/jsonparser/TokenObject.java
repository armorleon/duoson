package weiyin.jsonparser;

/**
 * The class to hold token
 * 
 * @author Wei Yin
 *
 */
public class TokenObject {
	/**
	 * Token's type
	 */
	private TokenType type;
	
	/**
	 * The real token object
	 */
	private Object token;
	
	/**
	 * Constructor
	 * 
	 * @param type
	 * @param token
	 */
	public TokenObject(TokenType type, Object token) {
		this.type = type;
		this.token = token;
	}

	/**
	 * @return the type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TokenType type) {
		this.type = type;
	}

	/**
	 * @return the token
	 */
	public Object getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Object token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TokenObject [type=" + type + ", token=" + token + "]";
	}
	
}
