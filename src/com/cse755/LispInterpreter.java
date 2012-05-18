package com.cse755;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * The actual interpreter.
 * 
 * @author Dan Ziemba
 */
public class LispInterpreter {

	private class EvalException extends Exception {
		public EvalException(String message) {
			super(message);
		}
	}

	private static boolean debug = false;

	private LispParser parser;
	private Map<String, SExpression> functions;

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
					System.out.println(se.getPrintable());
					System.out.println("OUTPUT:");
				}
				SExpression result = eval(se,
						new HashMap<String, SExpression>());
				System.out.println(result.getPrintable());
			}
		} catch (ParseException e) {
			System.out.println("ERROR: " + e.getLocalizedMessage());
		} catch (EvalException e) {
			System.out.println("ERROR: " + e.getLocalizedMessage());
		}
	}

	private SExpression eval(SExpression exp, Map<String, SExpression> variables)
			throws EvalException {
		if (exp.isAtom()) {
			Atom a = exp.getAtom();
			if (a.isNil() || a.isNumber()) {
				return exp;
			} else {
				// Atom is a word
				if (a.wordValue().equals("T")) {
					return exp;
				} else if (variables.containsKey(a.wordValue())) {
					return variables.get(a.wordValue());
				} else {
					throw new EvalException("Unbound variable '"
							+ a.wordValue() + "' at line " + a.lineNumber());
				}
			}
		} else {
			// Exp is a list
			if (exp.getLeftChild().isAtom()) {
				Atom car = exp.getLeftChild().getAtom();
				if (car.wordValue().equals("QUOTE")) {
					// TODO: check that right is 1 element list
					return exp.getRightChild();
				} else if (car.wordValue().equals("COND")) {
					// TODO: implement evcon
				} else if (car.wordValue().equals("DEFUN")) {
					// TODO: implement adding functions
				} else {
					// TODO: implement applying functions
				}
 			}
		}

		throw new EvalException("Couldn't evaluate expression: "
				+ exp.getPrintable());
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
