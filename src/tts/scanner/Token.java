package tts.scanner;

public class Token {

	public enum TokenType {
		TEXT_TEMPLATE, BOOLEAN, INTEGER, LONG_INT, FLOAT, DOUBLE, STRING, SEPARATOR, IDENTIFIER, KEY_WORD
	}

	public TokenType type;
	public Object value;

	public Token(TokenType t, Object value) {
		this.type = t;
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
