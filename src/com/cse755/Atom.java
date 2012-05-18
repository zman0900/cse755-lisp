package com.cse755;

import com.cse755.Token.TokenType;

/**
 * Class to represent an atom, be it word or number.
 * 
 * @author Dan Ziemba
 */
public class Atom {

	private String wordVal;
	private Integer numVal;
	private int lineNum;

	/**
	 * Create a new NIL atom
	 * 
	 * @param lineNumber
	 *            line in source file
	 */
	public Atom(int lineNumber) {
		this.lineNum = lineNumber;
	}

	/**
	 * Create a new atom from the given token. If the token is of type WORD and
	 * its value is 'NIL' then a NIL token will be created.
	 * 
	 * @param t
	 *            the token
	 */
	public Atom(Token t) {
		if (t.type == TokenType.NUMBER) {
			this.numVal = Integer.valueOf(t.numVal);
		} else if (t.type == TokenType.WORD) {
			if (!t.wordVal.equalsIgnoreCase("NIL")) {
				this.wordVal = t.wordVal;
			}
		}
		this.lineNum = t.lineNumber;
	}

	public boolean isNumber() {
		return (numVal != null);
	}

	public boolean isWord() {
		return (wordVal != null);
	}

	public boolean isNil() {
		return (numVal == null && wordVal == null);
	}

	public int numValue() {
		return numVal.intValue();
	}

	public String wordValue() {
		return wordVal;
	}

	public int lineNumber() {
		return lineNum;
	}

	@Override
	public String toString() {
		return "Atom [" + (wordVal != null ? "wordVal=" + wordVal + ", " : "")
				+ (numVal != null ? "numVal=" + numVal + ", " : "")
				+ "lineNum=" + lineNum + "]";
	}

	public void print() {
		if (isNil()) {
			System.out.print("NIL");
		} else if (isWord()) {
			System.out.print(wordVal);
		} else {
			System.out.print(numVal);
		}
	}

}
