package clids.ex4.compile;

import clids.ex4.misc.Type;

/**
 * a class that represents a variable that was declared in s-java, and hold all
 * of its characteristics that are required for compiling checking
 * 
 * @author Nir and Ira
 * 
 */
public class Variable {

	// INSTANCE VARIABLE
	private String name;
	private Type type;
	private boolean isFinal;
	private int intialisationLine;

	// CONSTRUCTORs
	public Variable(boolean isFinal, Type type, String name,
			int intialisationLine) {
		this.intialisationLine = intialisationLine;
		this.isFinal = isFinal;
		this.name = name;
		this.type = type;
	}

	public Variable(boolean isFinal, Type type, String name) {
		this.intialisationLine = -1;
		this.isFinal = isFinal;
		this.name = name;
		this.type = type;
	}

	public Variable(Variable other) {
		this.intialisationLine = other.intialisationLine;
		this.isFinal = other.isFinal;
		this.name = other.name;
		this.type = other.type;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the intialisationLine
	 */
	public int getIntialisationLine() {
		return intialisationLine;
	}

	/**
	 * @param intialisationLine
	 *            the intialisationLine to set
	 */
	public void setIntialisationLine(int intialisationLine) {
		this.intialisationLine = intialisationLine;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return whether is Final
	 */
	public boolean isFinal() {
		return isFinal;
	}

	public boolean equals(Variable other) {
		return this.name == other.name;
	}

	// DELETE ME::::::
	public String toString() {
		return this.name + ", " + this.type + ", " + this.isFinal;
	}
}
