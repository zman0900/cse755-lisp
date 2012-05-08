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
public class LispLexer {

	private BufferedReader in;
	private StreamTokenizer tk;
	private Token token;

	/**
	 * @param in
	 *            A stream to read from stdin or a file
	 */
	public LispLexer(InputStreamReader in) {
		this.in = new BufferedReader(in);
		this.tk = new StreamTokenizer(this.in);
		this.tk.eolIsSignificant(false);

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
	 * Advances the Lexer to the next {@link Token}.
	 * 
	 * @return True if the next Token was successfully parsed
	 * @throws ParseException
	 */
	public boolean tryAdvance() throws ParseException {
		int tokenValue;
		try {
			tokenValue = tk.nextToken();
			if (tokenValue == StreamTokenizer.TT_EOF) {
				// No more tokens
				return false;
			} else {
				switch (tokenValue) {
				case StreamTokenizer.TT_NUMBER:
					createNumberToken(true, tk.nval);
					return true;
				case StreamTokenizer.TT_WORD:
					token = new Token(tk.sval, tk.lineno());
					return true;
				default:
					switch (tokenValue) {
					case '(':
						token = new Token('(', TokenType.OPEN_PAREN,
								tk.lineno());
						return true;
					case ')':
						token = new Token(')', TokenType.CLOSE_PAREN,
								tk.lineno());
						return true;
					case '.':
						token = new Token('.', TokenType.DOT, tk.lineno());
						return true;
					case '+':
						tokenValue = tk.nextToken();
						if (tokenValue == StreamTokenizer.TT_NUMBER) {
							createNumberToken(true, tk.nval);
							return true;
						} else {
							throw new ParseException(
									"Found unexpected symbol '+' on line "
											+ tk.lineno(), 0);
						}
					case '-':
						tokenValue = tk.nextToken();
						if (tokenValue == StreamTokenizer.TT_NUMBER) {
							createNumberToken(false, tk.nval);
							return true;
						} else {
							throw new ParseException(
									"Found unexpected symbol '-' on line "
											+ tk.lineno(), 0);
						}
					case ' ':
					case '\t':
						token = new Token(' ', TokenType.WHITESPACE,
								tk.lineno());
						// Skip rest of whitespace
						int val;
						do {
							val = tk.nextToken();
						} while (val == ' ' || val == '\t');
						// Don't skip next token
						tk.pushBack();
						return true;
					default:
						throw new ParseException("Found unexpected symbol '"
								+ (char) tokenValue + "' on line "
								+ tk.lineno(), 0);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
