package com.cse755;

import java.text.ParseException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

/**
 * Parses a series of {@link Token}s and creates a binary tree representation of
 * the S-Expressions.
 * 
 * @author Dan Ziemba
 */
public class LispParser {

	public enum Symbols {
		// Terminal symbols
		TS_ATOM, TS_DOT, TS_C_PAREN, TS_O_PAREN, TS_INVALID, TS_EOS,
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
			case EOF:
				return TS_EOS;
			default:
				return TS_INVALID; // Should never reach this
			}
		}
	}

	private LispTokenizer tk;
	private Stack<Symbols> ss;
	private Map<Symbols, Map<Symbols, Integer>> table;

	private void buildParseTable() {
		table = new EnumMap<Symbols, Map<Symbols, Integer>>(Symbols.class);
		Map<Symbols, Integer> innerMap;
		// NTS_E
		innerMap = new EnumMap<Symbols, Integer>(Symbols.class);
		innerMap.put(Symbols.TS_ATOM, 2);
		innerMap.put(Symbols.TS_O_PAREN, 3);
		table.put(Symbols.NTS_E, innerMap);
		// NTS_R
		innerMap = new EnumMap<Symbols, Integer>(Symbols.class);
		innerMap.put(Symbols.TS_ATOM, 8);
		innerMap.put(Symbols.TS_C_PAREN, 9);
		innerMap.put(Symbols.TS_O_PAREN, 8);
		table.put(Symbols.NTS_R, innerMap);
		// NTS_S
		innerMap = new EnumMap<Symbols, Integer>(Symbols.class);
		innerMap.put(Symbols.TS_ATOM, 1);
		innerMap.put(Symbols.TS_O_PAREN, 1);
		table.put(Symbols.NTS_S, innerMap);
		// NTS_X
		innerMap = new EnumMap<Symbols, Integer>(Symbols.class);
		innerMap.put(Symbols.TS_ATOM, 5);
		innerMap.put(Symbols.TS_C_PAREN, 4);
		innerMap.put(Symbols.TS_O_PAREN, 5);
		table.put(Symbols.NTS_X, innerMap);
		// NTS_Y
		innerMap = new EnumMap<Symbols, Integer>(Symbols.class);
		innerMap.put(Symbols.TS_DOT, 6);
		innerMap.put(Symbols.TS_ATOM, 7);
		innerMap.put(Symbols.TS_C_PAREN, 7);
		innerMap.put(Symbols.TS_O_PAREN, 7);
		table.put(Symbols.NTS_Y, innerMap);
	}

	private void executeRule(Integer rule) {
		switch (rule) {
		case 1: // <S> ::= <E>
			ss.pop();
			ss.push(Symbols.NTS_E);
			break;
		case 2: // <E> ::= atom
			ss.pop();
			ss.push(Symbols.TS_ATOM);
			break;
		case 3: // <E> ::= (<X>
			ss.pop();
			ss.push(Symbols.NTS_X);
			ss.push(Symbols.TS_O_PAREN);
			break;
		case 4: // <X> ::= )
			ss.pop();
			ss.push(Symbols.TS_C_PAREN);
			break;
		case 5: // <X> ::= <E><Y>
			ss.pop();
			ss.push(Symbols.NTS_Y);
			ss.push(Symbols.NTS_E);
			break;
		case 6: // <Y> ::= .<E>)
			ss.pop();
			ss.push(Symbols.TS_C_PAREN);
			ss.push(Symbols.NTS_E);
			ss.push(Symbols.TS_DOT);
			break;
		case 7: // <Y> ::= <R>)
			ss.pop();
			ss.push(Symbols.TS_C_PAREN);
			ss.push(Symbols.NTS_R);
			break;
		case 8: // <R> ::= <E><R>
			ss.pop();
			ss.push(Symbols.NTS_R);
			ss.push(Symbols.NTS_E);
			break;
		case 9: // <R> ::= empty
			ss.pop();
			break;

		default:
			// Should never reach here
			System.out.println("Invalid rule encountered!"
					+ "This should never happen!");
			break;
		}
	}

	/**
	 * Creates a new LispParser with the given LispTokenizer
	 * 
	 * @param tk
	 *            LispTokenizer that should be at its starting position
	 */
	public LispParser(LispTokenizer tk) {
		this.tk = tk;
		buildParseTable();
	}

	public void getNextSExpression() throws ParseException {
		// Init stack
		ss = new Stack<LispParser.Symbols>();
		ss.push(Symbols.TS_EOS);
		ss.push(Symbols.NTS_S);
		// Prepare first token
		if (tk.getToken() == null) {
			tk.prepareNext();
		}

		while (ss.size() > 0 && ss.peek() != Symbols.TS_EOS) {
			Token cur = tk.getToken();
			System.out.println(cur);
			System.out.println("Stack top: " + ss.peek());
			if (Symbols.lexer(cur).equals(ss.peek())) {
				// Matched symbol with top of stack
				System.out.println("Matched symbols: " + Symbols.lexer(cur));
				tk.prepareNext();
				ss.pop();
			} else {
				// Get rule for NTS on stack, or report error
				Map<Symbols, Integer> innerMap;
				if ((innerMap = table.get(ss.peek())) != null) {
					Integer rule;
					if ((rule = innerMap.get(Symbols.lexer(cur))) != null) {
						System.out.println("Got rule # " + rule);
						executeRule(rule);
					} else {
						// Didn't match rule
						throw new ParseException(
								"Input doesn't match Lisp grammar", 0);
					}
				} else {
					// Top of stack was not NTS
					throw new ParseException("Unexpected error while parsing",
							0);
				}
			}
		}
	}

}
