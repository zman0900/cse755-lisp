package com.cse755;

import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * The actual interpreter.
 * 
 * @author Dan Ziemba
 */
public class LispInterpreter {

	/**
	 * 
	 */
	public LispInterpreter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args Not used
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LispLexer lex = new LispLexer(new InputStreamReader(System.in));
		
		try {
			while (lex.tryAdvance()) {
				System.out.println(lex.getToken());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
