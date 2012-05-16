/**
 * 
 */
package com.cse755;

/**
 * @author Dan Ziemba
 *
 */
public class SExpression {
	
	private String atom;
	private SExpression leftChild;
	private SExpression rightChild;

	/**
	 * 
	 */
	public SExpression() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the atom
	 */
	public final String getAtom() {
		return atom;
	}

	/**
	 * @param atom the atom to set
	 */
	public final void setAtom(String atom) {
		this.atom = atom;
	}

	/**
	 * @return the leftChild
	 */
	public final SExpression getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public final void setLeftChild(SExpression leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * @return the rightChild
	 */
	public final SExpression getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild the rightChild to set
	 */
	public final void setRightChild(SExpression rightChild) {
		this.rightChild = rightChild;
	}
	
}
