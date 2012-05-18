package com.cse755;

import java.util.Locale;

/**
 * This is used to represent info about each Lisp token.
 * 
 * @author Dan Ziemba
 */
public class Token {

	/**
	 * Enum of possible types of Tokens. NUMBER and WORD are both atoms.
	 * 
	 * @author Dan Ziemba
	 */
	public enum TokenType {
		NUMBER, WORD, DOT, OPEN_PAREN, CLOSE_PAREN, EOF
	}

	/**
	 * Actual token read from input.
	 */
	private final String value;

	/**
	 * Type of the token.
	 */
	public final TokenType type;

	/**
	 * Line number where token was found in input file.
	 */
	public final int lineNumber;

	/**
	 * Numerical value of token. If token is not of type NUMBER, this will be 0.
	 */
	public final int numVal;

	/**
	 * String value of token in all upper case. If token is not if type WORD
	 * this will be null.
	 */
	public final String wordVal;

	/**
	 * Create a new Token of type WORD.
	 * 
	 * @param value
	 *            Word that the Token represents
	 * @param lineNumber
	 *            Source line number
	 */
	public Token(String value, int lineNumber) {
		this.value = value;
		this.type = TokenType.WORD;
		this.lineNumber = lineNumber;
		this.numVal = 0;
		this.wordVal = this.value.toUpperCase(Locale.ENGLISH);
	}

	/**
	 * Create a new Token of type NUMBER.
	 * 
	 * @param value
	 *            Number the Token represents
	 * @param lineNumber
	 *            Source line number
	 */
	public Token(int value, int lineNumber) {
		this.value = Integer.toString(value);
		this.type = TokenType.NUMBER;
		this.lineNumber = lineNumber;
		this.numVal = value;
		this.wordVal = null;
	}

	/**
	 * Create a new non WORD or NUMBER Token.
	 * 
	 * @param value
	 *            Value the Token represents
	 * @param type
	 *            Type of the Token
	 * @param lineNumber
	 *            Source line number
	 */
	public Token(char value, TokenType type, int lineNumber) {
		this.value = String.valueOf(value);
		this.type = type;
		this.lineNumber = lineNumber;
		this.numVal = 0;
		this.wordVal = null;
	}
	
	/**
	 * Create a new EOF Token.
	 */
	public Token(int lineNumber) {
		this.value = null;
		this.type = TokenType.EOF;
		this.lineNumber = lineNumber;
		this.numVal = 0;
		this.wordVal = null;
	}

	@Override
	public String toString() {
		return "Token [type=" + String.format("%1$#" + 11 + "s", type) + ", lineNumber=" + lineNumber
				+ ", numVal=" + numVal + ", wordVal=" + wordVal + "]";
	}
}
