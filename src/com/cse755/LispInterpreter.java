package com.cse755;

import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * The actual interpreter.
 * 
 * @author Dan Ziemba
 */
public class LispInterpreter {

	private LispParser parser;

	/**
	 * 
	 */
	public LispInterpreter(LispParser parser) {
		this.parser = parser;
	}

	/**
	 * Interprets and runs the Lisp program specified on stdin and displays the
	 * output on stdout.
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(String[] args) {
		LispLexer lex = new LispLexer(new InputStreamReader(System.in));
		LispParser parser = new LispParser(lex);
		LispInterpreter interp = new LispInterpreter(parser);

		// TODO: Remove testing code
		try {
			while (lex.tryAdvance()) {
				System.out.println(lex.getToken());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
