package com.cse755;

import java.util.Locale;

/**
 * This is used to represent info about each Lisp token.
 * 
 * @author Dan Ziemba
 */
public class Token {
	
	/**
	 * Possible types of Tokens. NUMBER and WORD are both atoms.
	 * 
	 * @author Dan Ziemba
	 */
	public enum TokenType {
		NUMBER,
		WORD,
		DOT,
		OPEN_PAREN,
		CLOSE_PAREN
	}
	
	/**
	 * Actual token read from input.
	 */
	public final String value;
	
	/**
	 * Type of the token.
	 */
	public final TokenType type;
	
	/**
	 * Line number where token was found in input file.
	 */
	public final int lineNumber;
	
	/**
	 * Numerical value of token. If token is not of type NUMBER, this will
	 * be 0.
	 */
	public final int numVal;
	
	/**
	 * String value of token in all upper case.  If token is not if type WORD
	 * this will be null.
	 */
	public final String wordVal;

	/**
	 * @param value Literal input value
	 * @param type Kind of token
	 * @param lineNumber Source line number
	 */
	public Token(String value, TokenType type, int lineNumber) {
		this.value = value;
		this.type = type;
		this.lineNumber = lineNumber;
		
		switch (this.type) {
		case NUMBER:
			this.numVal = Integer.parseInt(this.value);
			this.wordVal = null;
			break;
		case WORD:
			this.numVal = 0;
			this.wordVal = this.value.toUpperCase(Locale.ENGLISH);
			break;
		default:
			this.numVal = 0;
			this.wordVal = null;
			break;
		}
	}
}
