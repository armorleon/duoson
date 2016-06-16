package weiyin.duoson;

public class TokenObject {
	private TokenType type;
	private Object token;
	
	public TokenObject(TokenType type, Object token) {
		this.type = type;
		this.token = token;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public Object getToken() {
		return token;
	}

	public void setToken(Object token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TokenObject [type=" + type + ", token=" + token + "]";
	}
	
}
