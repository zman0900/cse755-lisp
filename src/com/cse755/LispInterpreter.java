package com.cse755;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
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
		this.functions = new HashMap<String, SExpression>();
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
		if (debug) {
			// Print function table
			Iterator<String> i = functions.keySet().iterator();
			System.out.println("Function table:");
			while (i.hasNext()) {
				String key = i.next();
				System.out.println(key + " = "
						+ functions.get(key).getPrintable());
			}
		}
	}

	private SExpression eval(SExpression exp, Map<String, SExpression> variables)
			throws EvalException {
		if (exp.isAtom()) {
			Atom a = exp.getAtom();
			if (a.isNil() || a.isNumber()) {
				return exp.clone();
			} else {
				// Atom is a word
				if (a.wordValue().equals("T")) {
					return exp.clone();
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
				if (car.isNil()) {
					throw new EvalException("NIL is not a function name");
				} else if (car.isNumber()) {
					throw new EvalException(car.numValue()
							+ " is not a function name");
				} else if (car.wordValue().equals("QUOTE")) {
					if (exp.getRightChild().getLeftChild() != null
							&& exp.getRightChild().listLength() == 1) {
						return exp.getRightChild().getLeftChild().clone();
					} else {
						throw new EvalException("QUOTE expects exactly 1 list");
					}
				} else if (car.wordValue().equals("COND")) {
					// TODO: implement evcon
				} else if (car.wordValue().equals("DEFUN")) {
					return defineFunction(exp.clone());
				} else {
					return applyFunction(car.clone(),
							evlist(exp.getRightChild().clone(), variables),
							variables);
				}
			} else {
				// First element of list is another list, this doesn't work
				throw new EvalException(exp.getLeftChild().getPrintable()
						+ " is not a function name");
			}
		}

		throw new EvalException("Couldn't evaluate expression: "
				+ exp.getPrintable());
	}

	private SExpression evlist(SExpression exp,
			Map<String, SExpression> variables) throws EvalException {
		if (exp.isList()) {
			if (exp.isAtom() && exp.getAtom().isNil()) {
				return exp.clone();
			} else {
				SExpression result = new SExpression();
				result.setLeftChild(eval(exp.getLeftChild().clone(), variables));
				result.setRightChild(evlist(exp.getRightChild().clone(),
						variables));
				return result;
			}
		} else {
			throw new EvalException("Expected a list, instead got '"
					+ exp.getPrintable() + "'");
		}
	}

	private SExpression defineFunction(SExpression exp) throws EvalException {
		SExpression inner = exp.getRightChild();
		if (inner.listLength() != 3) {
			throw new EvalException("Invalid DEFUN at line "
					+ exp.getLeftChild().getAtom().lineNumber()
					+ ", expected 4 element list of form "
					+ "'(DEFUN name (params) (body))'");
		} else if (!inner.getRightChild().getLeftChild().isFlatList()) {
			throw new EvalException("Invalid DEFUN at line "
					+ exp.getLeftChild().getAtom().lineNumber()
					+ ", params should all be atoms");
		} else if (!(inner.getLeftChild().isAtom() && inner.getLeftChild()
				.getAtom().isWord())) {
			throw new EvalException("Invalid DEFUN at line "
					+ exp.getLeftChild().getAtom().lineNumber()
					+ ", name should be a non-numeric atom");
		} else {
			// Function looks good
			SExpression func = inner.getRightChild().clone();
			functions.put(inner.getLeftChild().getAtom().wordValue(), func);
			// Return just function name
			return inner.getLeftChild().clone();
		}
	}

	/**
	 * @param function
	 * @param params
	 *            Should already be evaluated with evlist()
	 * @param variables
	 * @return
	 * @throws EvalException
	 */
	private SExpression applyFunction(Atom function, SExpression params,
			Map<String, SExpression> variables) throws EvalException {
		if (debug) {
			System.out.println("APPLY: " + function.getPrintable()
					+ " PARAMS: " + params.getPrintable());
		}
		if (function.wordValue().equals("CAR")) {
			if (params.listLength() != 1) {
				throw new EvalException("CAR expects exactly 1 list");
			} else {
				return params.getLeftChild().getLeftChild().clone();
			}
		} else if (function.wordValue().equals("CDR")) {
			if (params.listLength() != 1) {
				throw new EvalException("CDR expects exactly 1 list");
			} else {
				return params.getLeftChild().getRightChild().clone();
			}
		} else if (function.wordValue().equals("CONS")) {
			if (params.listLength() != 2) {
				throw new EvalException("CONS expects exactly 2 params");
			} else {
				SExpression result = new SExpression();
				result.setLeftChild(params.getLeftChild().clone());
				result.setRightChild(params.getRightChild().getLeftChild()
						.clone());
				return result;
			}
		} else if (function.wordValue().equals("ATOM")) {
			if (params.listLength() != 1) {
				throw new EvalException("ATOM expects exactly 1 param");
			} else {
				SExpression result = new SExpression();
				Atom a;
				if (params.getLeftChild().isAtom()) {
					a = new Atom(true, params.getLeftChild().getAtom()
							.lineNumber());
				} else {
					a = new Atom(false, function.lineNumber());
				}
				result.setAtom(a);
				return result;
			}
		} else if (function.wordValue().equals("EQ")) {
			if (params.listLength() != 2) {
				throw new EvalException("EQ expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (!(left.isAtom() && right.isAtom())) {
					throw new EvalException("EQ params should be atoms");
				} else if (!((left.getAtom().isWord() && right.getAtom()
						.isWord())
						|| (left.getAtom().isNumber() && right.getAtom()
								.isNumber()) || ((left.getAtom().isTrue() || left
						.getAtom().isNil()) && (right.getAtom().isTrue() || right
						.getAtom().isNil())))) {
					throw new EvalException(
							"EQ params should be atoms of the same type");
				} else {
					// Params look good
					SExpression result = new SExpression();
					Atom a;
					if (left.getAtom().isWord()) {
						// Word
						if (left.getAtom().wordValue()
								.equals(right.getAtom().wordValue())) {
							a = new Atom(true, left.getAtom().lineNumber());
						} else {
							a = new Atom(false, left.getAtom().lineNumber());
						}
					} else if (left.getAtom().isNumber()) {
						// Number
						if (left.getAtom().numValue()
								.equals(right.getAtom().numValue())) {
							a = new Atom(true, left.getAtom().lineNumber());
						} else {
							a = new Atom(false, left.getAtom().lineNumber());
						}
					} else {
						// Boolean
						if ((left.getAtom().isTrue() && right.getAtom()
								.isTrue())
								|| (left.getAtom().isNil() && right.getAtom()
										.isNil())) {
							a = new Atom(true, left.getAtom().lineNumber());
						} else {
							a = new Atom(false, left.getAtom().lineNumber());
						}
					}
					result.setAtom(a);
					return result;
				}
			}
		} else if (function.wordValue().equals("NULL")) {
			if (params.listLength() != 1) {
				throw new EvalException("NULL expects exactly 1 param");
			} else {
				SExpression result = new SExpression();
				Atom a;
				if (params.getLeftChild().isAtom()
						&& params.getLeftChild().getAtom().isNil()) {
					a = new Atom(true, params.getLeftChild().getAtom()
							.lineNumber());
				} else {
					a = new Atom(false, function.lineNumber());
				}
				result.setAtom(a);
				return result;
			}
		} else if (function.wordValue().equals("INT")) {
			if (params.listLength() != 1) {
				throw new EvalException("INT expects exactly 1 param");
			} else {
				SExpression result = new SExpression();
				Atom a;
				if (params.getLeftChild().isAtom()
						&& params.getLeftChild().getAtom().isNumber()) {
					a = new Atom(true, params.getLeftChild().getAtom()
							.lineNumber());
				} else {
					a = new Atom(false, function.lineNumber());
				}
				result.setAtom(a);
				return result;
			}
		} else if (function.wordValue().equals("PLUS")) {
			if (params.listLength() != 2) {
				throw new EvalException("PLUS expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a = new Atom(left.getAtom().numValue() + right.getAtom().numValue());
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("PLUS params should be integers");
				}
			}
		} else if (function.wordValue().equals("MINUS")) {
			if (params.listLength() != 2) {
				throw new EvalException("MINUS expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a = new Atom(left.getAtom().numValue() - right.getAtom().numValue());
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("MINUS params should be integers");
				}
			}
		} else if (function.wordValue().equals("TIMES")) {
			if (params.listLength() != 2) {
				throw new EvalException("TIMES expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a = new Atom(left.getAtom().numValue() * right.getAtom().numValue());
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("TIMES params should be integers");
				}
			}
		} else if (function.wordValue().equals("QUOTIENT")) {
			if (params.listLength() != 2) {
				throw new EvalException("QUOTIENT expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a = new Atom(left.getAtom().numValue() / right.getAtom().numValue());
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("QUOTIENT params should be integers");
				}
			}
		} else if (function.wordValue().equals("REMAINDER")) {
			if (params.listLength() != 2) {
				throw new EvalException("REMAINDER expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a = new Atom(left.getAtom().numValue() % right.getAtom().numValue());
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("REMAINDER params should be integers");
				}
			}
		} else if (function.wordValue().equals("LESS")) {
			if (params.listLength() != 2) {
				throw new EvalException("LESS expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a;
					if (left.getAtom().numValue() < right.getAtom().numValue()) {
						a = new Atom(true, left.getAtom().lineNumber());
					} else {
						a = new Atom(false, left.getAtom().lineNumber());
					}
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("LESS params should be integers");
				}
			}
		} else if (function.wordValue().equals("GREATER")) {
			if (params.listLength() != 2) {
				throw new EvalException("GREATER expects exactly 2 params");
			} else {
				SExpression left = params.getLeftChild();
				SExpression right = params.getRightChild().getLeftChild();
				if (left.isAtom() && right.isAtom() && left.getAtom().isNumber() && right.getAtom().isNumber()) {
					SExpression result = new SExpression();
					Atom a;
					if (left.getAtom().numValue() > right.getAtom().numValue()) {
						a = new Atom(true, left.getAtom().lineNumber());
					} else {
						a = new Atom(false, left.getAtom().lineNumber());
					}
					result.setAtom(a);
					return result;
				} else {
					throw new EvalException("GREATER params should be integers");
				}
			}
		} else {
			// User defined function

		}

		throw new EvalException("Function '" + function.wordValue()
				+ "' is not implemented");
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
