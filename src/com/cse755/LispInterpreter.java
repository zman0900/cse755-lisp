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
		LispTokenizer tk = new LispTokenizer(new InputStreamReader(System.in));
		LispParser parser = new LispParser(tk);
		LispInterpreter interp = new LispInterpreter(parser);

		// TODO: Remove testing code
		try {
			while (tk.tryAdvance()) {
				System.out.println(tk.getToken());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
