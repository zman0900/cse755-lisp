package com.cse755;

/**
 * Class to represent an S-expression or an atom. If atom is non-null, contents
 * of leftChild and rightChild should be ignored because this object represents
 * an atom.
 * 
 * @author Dan Ziemba
 */
public class SExpression implements Cloneable {

	private Atom atom;
	private SExpression leftChild;
	private SExpression rightChild;
	private SExpression parent;
	private boolean hasDot;
	private Boolean isList = null;
	private Integer listLength = null;
	private Boolean isFlatList = null;

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

	@Override
	public SExpression clone() {
		try {
			SExpression copy = (SExpression) super.clone();
			copy.parent = null;
			if (atom != null)
				copy.atom = atom.clone();
			if (leftChild != null) {
				copy.leftChild = leftChild.clone();
				copy.leftChild.parent = copy;
			}
			if (rightChild != null) {
				copy.rightChild = rightChild.clone();
				copy.rightChild.parent = copy;
			}
			// Force recompute of these
			copy.isList = null;
			copy.listLength = null;
			copy.isFlatList = null;
			return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * The atom this s-expression represents. Null if this is not an atom.
	 * 
	 * @return the atom
	 */
	public Atom getAtom() {
		return atom;
	}

	/**
	 * Set this s-expression to represent an atom. Don't do this if left or
	 * right child are set.
	 * 
	 * @param atom
	 *            the atom to set.
	 */
	public void setAtom(Atom atom) {
		this.atom = atom;
		if (this.leftChild != null || this.rightChild != null) {
			// TODO: remove warnings
			System.out.println("WARNING: Atom set on non-empty s-expression");
		}
	}

	/**
	 * The left child of this s-expression. Null if this is an atom.
	 * 
	 * @return the left child
	 */
	public SExpression getLeftChild() {
		return leftChild;
	}

	/**
	 * Set left child. Also sets left child's parent to this. Don't do this if
	 * atom is set.
	 * 
	 * @param leftChild
	 *            the s-expression to set as left child
	 */
	public void setLeftChild(SExpression leftChild) {
		this.leftChild = leftChild;
		this.leftChild.setParent(this);
		if (this.atom != null) {
			// TODO: remove warnings
			System.out.println("WARNING: Left-child set on atom");
		}
	}

	/**
	 * The right child of this s-expression. Null if this is an atom.
	 * 
	 * @return the right child
	 */
	public SExpression getRightChild() {
		return rightChild;
	}

	/**
	 * Set right child. Also sets right child's parent to this. Don't do this if
	 * atom is set.
	 * 
	 * @param rightChild
	 *            the s-expression to set as right child
	 */
	public void setRightChild(SExpression rightChild) {
		this.rightChild = rightChild;
		this.rightChild.setParent(this);
		if (this.atom != null) {
			// TODO: remove warnings
			System.out.println("WARNING: Right-child set on atom");
		}
	}

	/**
	 * The parent of this s-expression. A value of null indicates this is the
	 * root s-expression.
	 * 
	 * @return the parent s-expression
	 */
	public SExpression getParent() {
		return parent;
	}

	/**
	 * Set the parent of this s-expression. Leave null to indicate this is root.
	 * 
	 * @param parent
	 *            the parent s-expression
	 */
	public void setParent(SExpression parent) {
		this.parent = parent;
	}

	/**
	 * True if the parsed s-expression had a dot in the middle.
	 * 
	 * @return did this have a dot
	 */
	public boolean hasDot() {
		return hasDot;
	}

	/**
	 * Set to true if the parsed s-expression had a dot in the middle.
	 * 
	 * @param hasDot
	 *            True if this s-expression had a dot in the middle
	 */
	public void setHasDot(boolean hasDot) {
		this.hasDot = hasDot;
	}

	@Override
	public String toString() {
		StringBuilder tabs = new StringBuilder();
		SExpression temp = this;
		while ((temp = temp.getParent()) != null) {
			tabs.append('\t');
		}
		return "SExpression l="
				+ listLength()
				+ " ["
				+ (atom != null ? "atom=" + atom + ", " : "")
				+ (leftChild != null ? "\n" + tabs + "\tleftChild=" + leftChild
						+ ", " : "")
				+ (rightChild != null ? "\n" + tabs + "\trightChild="
						+ rightChild : "") + "]";
	}

	/**
	 * Prints the s-expression to stdout in the standard format.
	 */
	public String getPrintable() {
		StringBuilder out = new StringBuilder();
		if (this.atom != null) {
			out.append(atom.getPrintable());
		} else if (isList()) {
			boolean isListRoot = false;
			// First node of list is always the left child of its parent
			isListRoot = (parent == null || (parent != null && parent.leftChild == this));
			if (isListRoot)
				out.append('(');
			out.append(leftChild.getPrintable());
			if (!(rightChild.isAtom() && rightChild.getAtom().isNil())) {
				out.append(' ');
				out.append(rightChild.getPrintable());
			}
			if (isListRoot)
				out.append(')');
		} else {
			out.append('(');
			out.append(leftChild.getPrintable());
			out.append(" . ");
			out.append(rightChild.getPrintable());
			out.append(')');
		}
		return out.toString();
	}

	/**
	 * True if this s-expression contains only an atom.
	 * 
	 * @return is this an atom
	 */
	public boolean isAtom() {
		return (atom != null);
	}

	/**
	 * True if this s-expression is in list format.
	 * 
	 * @return is this a list
	 */
	public boolean isList() {
		if (isList == null) {
			if (atom != null && atom.isNil()) {
				isList = true;
			} else if (rightChild != null && rightChild.isList()) {
				isList = true;
			} else {
				isList = false;
			}
		}
		return isList.booleanValue();
	}

	/**
	 * Returns the lenght of the list, or -1 if this is not a list.
	 * 
	 * @return the list length
	 */
	public int listLength() {
		if (listLength == null) {
			if (isList()) {
				if (atom != null && atom.isNil()) {
					listLength = 0;
				} else {
					listLength = 1 + rightChild.listLength();
				}
			} else {
				listLength = -1;
			}
		}
		return listLength.intValue();
	}

	public boolean isFlatList() {
		if (isFlatList == null) {
			if (isList()) {
				if (atom != null && atom.isNil()) {
					isFlatList = true;
				} else {
					isFlatList = (leftChild.isAtom() & rightChild.isFlatList());
				}
			} else {
				isFlatList = false;
			}
		}
		return isFlatList.booleanValue();
	}

}
