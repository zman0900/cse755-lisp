package com.cse755;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.text.ParseException;
import com.cse755.Token.TokenType;

/**
 * This reads the input and generates a series of {@link Token}s.
 * 
 * @author Dan Ziemba
 */
public class LispTokenizer {

	private BufferedReader in;
	private IntStreamTokenizer tk;
	private Token token;

	/**
	 * @param in
	 *            A stream to read from stdin or a file
	 */
	public LispTokenizer(InputStreamReader in) {
		this.in = new BufferedReader(in);
		this.tk = new IntStreamTokenizer(this.in);
		this.tk.eolIsSignificant(true);

		// Force all these to be tokens
		this.tk.ordinaryChar('(');
		this.tk.ordinaryChar(')');
		this.tk.ordinaryChar('.');
		this.tk.ordinaryChar(' ');
		this.tk.ordinaryChar('\t');

		// Don't handle comments
		this.tk.slashSlashComments(false);
		this.tk.slashStarComments(false);
	}

	private void createNumberToken(boolean positive, double value) {
		int num = (int) value;
		if (!positive) {
			num = num * -1;
		}
		token = new Token(num, tk.lineno());
	}

	/**
	 * Advances the Tokenizer to the next {@link Token}.
	 * 
	 * @return True if the next Token was successfully parsed
	 * @throws ParseException
	 */
	public void prepareNext() throws ParseException {
		int tokenValue;
		try {
			tokenValue = tk.nextToken();
			if (tokenValue == StreamTokenizer.TT_EOF) {
				// No more tokens
				token = new Token(tk.lineno());
				return;
			} else {
				switch (tokenValue) {
				case StreamTokenizer.TT_NUMBER:
					createNumberToken(true, tk.nval);
					return;
				case StreamTokenizer.TT_WORD:
					token = new Token(tk.sval, tk.lineno());
					return;
				default:
					switch (tokenValue) {
					case '(':
						token = new Token('(', TokenType.OPEN_PAREN,
								tk.lineno());
						return;
					case ')':
						token = new Token(')', TokenType.CLOSE_PAREN,
								tk.lineno());
						return;
					case '.':
						throw new ParseException(
								"Found unexpected symbol '.' on line "
										+ tk.lineno(), 0);
					case '+':
						tokenValue = tk.nextToken();
						if (tokenValue == StreamTokenizer.TT_NUMBER) {
							createNumberToken(true, tk.nval);
							return;
						} else {
							throw new ParseException(
									"Found unexpected symbol '+' on line "
											+ tk.lineno(), 0);
						}
					case '-':
						tokenValue = tk.nextToken();
						if (tokenValue == StreamTokenizer.TT_NUMBER) {
							createNumberToken(false, tk.nval);
							return;
						} else {
							throw new ParseException(
									"Found unexpected symbol '-' on line "
											+ tk.lineno(), 0);
						}
					case ' ':
					case '\t':
					case StreamTokenizer.TT_EOL:
						// Is a dot next?
						int val;
						do {
							val = tk.nextToken();
							if (val == '.') {
								// Found period, check for following space
								val = tk.nextToken();
								if (val == ' ' || val == '\t'
										|| val == StreamTokenizer.TT_EOL) {
									// Proper dot token
									token = new Token('.', TokenType.DOT,
											tk.lineno());
									return;
								} else {
									throw new ParseException(
											"Found unexpected symbol '.' on line "
													+ tk.lineno(), 0);
								}
							}
							// else not dot, skip rest of space
						} while (val == ' ' || val == '\t'
								|| val == StreamTokenizer.TT_EOL);

						// Got to a different token
						tk.pushBack();
						this.prepareNext();
						return;
					default:
						throw new ParseException("Found unexpected symbol '"
								+ (char) tokenValue + "' on line "
								+ tk.lineno(), 0);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// IO error, this breaks things
			return;
		}
	}

	/**
	 * Returns the current {@link Token}.
	 * 
	 * @return The current {@link Token}
	 */
	public Token getToken() {
		return token;
	}

}
