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
	private static boolean debug = false;

	/**
	 * Creates a new LispInterpreter that reads s-expressions from the specified
	 * {@link LispParser}, evaluates them, and prints the resulting
	 * s-expressions.
	 * 
	 * @param parser
	 *            the parser to read from
	 */
	public LispInterpreter(LispParser parser) {
		this.parser = parser;
	}

	/**
	 * Call this to start the main read-eval-print loop
	 */
	public void run() {
		try {
			SExpression se;
			// Main read-eval-print loop
			while ((se = this.parser.getNextSExpression()) != null) {
				if (debug) {
					System.out.println("INPUT:");
					System.out.println(se);
					se.print();
					System.out.println("OUTPUT:");
				}
				SExpression result = eval(se);
				result.print();
			}
		} catch (ParseException e) {
			System.out.println("ERROR: " + e.getLocalizedMessage());
		}
	}

	private SExpression eval(SExpression se) {
		return se;
	}

	/**
	 * Interprets and runs the Lisp program specified on stdin and displays the
	 * output on stdout.
	 * 
	 * @param args
	 *            Use -d to enable debug output.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("-d")) {
				debug = true;
			}
		}
		LispTokenizer tk = new LispTokenizer(new InputStreamReader(System.in));
		LispParser parser = new LispParser(tk);
		LispInterpreter interp = new LispInterpreter(parser);
		interp.run();
	}

}
