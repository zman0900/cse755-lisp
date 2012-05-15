package com.cse755;

/**
 * 
 * 
 * @author Dan Ziemba
 */
public class LispParser {

	public enum Symbols {
		// Terminal symbols
		TS_ATOM, TS_DOT, TS_C_PAREN, TS_O_PAREN, TS_INVALID,
		// Non-terminal symbols
		NTS_E, NTS_R, NTS_S, NTS_X, NTS_Y;
		
		public static Symbols lexer(Token t) {
			switch (t.type) {
			case NUMBER:
				return TS_ATOM;
			case WORD:
				return TS_ATOM;
			case DOT:
				return TS_DOT;
			case CLOSE_PAREN:
				return TS_C_PAREN;
			case OPEN_PAREN:
				return TS_O_PAREN;
			default:
				return TS_INVALID;  // Should never reach this
			}
		}
	}


	private LispTokenizer tk;
	
	/**
	 * 
	 */
	public LispParser(LispTokenizer tk) {
		this.tk = tk;
	}
	
	

}
