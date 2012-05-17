package com.cse755;

/**
 * Class to represent an S-expression or an atom. If atom is non-null, contents
 * of leftChild and rightChild should be ignored because this object represents
 * an atom.
 * 
 * @author Dan Ziemba
 */
public class SExpression {

	private Atom atom;
	private SExpression leftChild;
	private SExpression rightChild;

	/**
	 * Create empty s-expression.
	 */
	public SExpression() {
	}
	
	/**
	 * Create an s-expression that is just an atom.
	 */
	public SExpression(Atom atom) {
		this.atom = atom;
	}

	/**
	 * @return the atom
	 */
	public final Atom getAtom() {
		return atom;
	}

	/**
	 * @param atom
	 *            the atom to set. Don't do this if left or right child are set.
	 */
	public final void setAtom(Atom atom) {
		this.atom = atom;
		if (this.leftChild != null || this.rightChild != null) {
			// TODO: remove warnings
			System.out.println("WARNING: Atom set on non-empty s-expression");
		}
	}

	/**
	 * @return the leftChild
	 */
	public final SExpression getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild
	 *            the s-expression to set as left child
	 */
	public final void setLeftChild(SExpression leftChild) {
		this.leftChild = leftChild;
		if (this.atom != null) {
			// TODO: remove warnings
			System.out.println("WARNING: Left-child set on atom");
		}
	}

	/**
	 * @return the rightChild
	 */
	public final SExpression getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild
	 *            the s-expression to set as right child
	 */
	public final void setRightChild(SExpression rightChild) {
		this.rightChild = rightChild;
		if (this.atom != null) {
			// TODO: remove warnings
			System.out.println("WARNING: Right-child set on atom");
		}
	}

}
