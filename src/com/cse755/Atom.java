package com.cse755;

import com.cse755.Token.TokenType;

/**
 * Class to represent an atom, be it word or number.
 * 
 * @author Dan Ziemba
 */
public class Atom implements Cloneable {

	private String wordVal;
	private Integer numVal;
	private int lineNum;

	/**
	 * Create a new NIL or T atom
	 * 
	 * @param isTrue
	 *            is atom T or NIL
	 * @param lineNumber
	 *            line in source file
	 */
	public Atom(boolean isTrue, int lineNumber) {
		this.lineNum = lineNumber;
		if (isTrue) {
			wordVal = String.valueOf('T');
		}
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

	@Override
	public Atom clone() {
		try {
			Atom copy = (Atom) super.clone();
			if (wordVal != null)
				copy.wordVal = new String(wordVal);
			if (numVal != null)
				copy.numVal = new Integer(numVal);
			return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * True if atom is a number.
	 * 
	 * @return is atom a number
	 */
	public boolean isNumber() {
		return (numVal != null);
	}

	/**
	 * True if atom is a word.
	 * 
	 * @return is atom a word
	 */
	public boolean isWord() {
		return (wordVal != null && !wordVal.equals("T"));
	}

	/**
	 * True if atom is NIL.
	 * 
	 * @return is atom NIL
	 */
	public boolean isNil() {
		return (numVal == null && wordVal == null);
	}

	/**
	 * True if atom is T.
	 * 
	 * @return is atom T
	 */
	public boolean isTrue() {
		return (wordVal != null && wordVal.equals("T"));
	}

	/**
	 * Value of atom as a Integer. Null if atom is not a number
	 * 
	 * @return the number value
	 */
	public Integer numValue() {
		return numVal;
	}

	/**
	 * Value of atom as a String. Null if atom is not a word.
	 * 
	 * @return the word value
	 */
	public String wordValue() {
		return wordVal;
	}

	/**
	 * Line number the atom was encountered or generated on.
	 * 
	 * @return the line number
	 */
	public int lineNumber() {
		return lineNum;
	}

	@Override
	public String toString() {
		return "Atom [" + (wordVal != null ? "wordVal=" + wordVal + ", " : "")
				+ (numVal != null ? "numVal=" + numVal + ", " : "")
				+ "lineNum=" + lineNum + "]";
	}

	/**
	 * Prints the value of this atom to stdout
	 */
	public String getPrintable() {
		if (isNil()) {
			return "NIL";
		} else if (isTrue()) {
			return "T";
		} else if (isWord()) {
			return wordVal;
		} else {
			return String.valueOf(numVal);
		}
	}

}
