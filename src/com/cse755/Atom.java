package com.cse755;

import com.cse755.Token.TokenType;

/**
 * Class to represent an atom, be it word or number.
 * 
 * @author Dan Ziemba
 */
public class Atom {

	private Token t;

	/**
	 * Create a new atom from the given token.
	 * 
	 * @param t
	 *            the token
	 */
	public Atom(Token t) {
		this.t = t;
	}

	public boolean isNumber() {
		return t.type == TokenType.NUMBER;
	}

	public boolean isWord() {
		return t.type == TokenType.WORD;
	}

	public int numValue() {
		return t.numVal;
	}

	public String wordValue() {
		return t.wordVal;
	}

}
