package clids.ex4.compile;

import clids.ex4.misc.Type;

/**
 * a class that represents a variable that was declared in s-java, and hold all
 * of its properties that are required for compile checking
 * 
 * @author Nir & Ira
 * 
 */
public class Variable {

	// INSTANCE VARIABLE
	private String name;
	private Type type;
	private boolean isFinal;
	private int initializationLine;

	/**
	 * a constructor that gets whether the variable is final, its type, its name
	 * and its initialization line
	 * 
	 * @param isFinal
	 *            - whether the variable is final
	 * @param type
	 *            - the variable's type
	 * @param name
	 *            - the variable's name
	 * @param initializationLine
	 *            the variable's
	 */
	public Variable(boolean isFinal, Type type, String name,
			int initializationLine) {
		this.initializationLine = initializationLine;
		this.isFinal = isFinal;
		this.name = name;
		this.type = type;
	}

	/**
	 * a constructor that gets whether the variable is final, its type and its
	 * name
	 * 
	 * @param isFinal
	 *            - whether the variable is final
	 * @param type
	 *            - the variable's type
	 * @param name
	 *            - the variable's name
	 * @param initializationLine
	 *            the variable's
	 */
	public Variable(boolean isFinal, Type type, String name) {
		this.initializationLine = -1;
		this.isFinal = isFinal;
		this.name = name;
		this.type = type;
	}

	/**
	 * a copy constructor that gets another variable an copies its properties
	 * 
	 * @param other
	 */
	public Variable(Variable other) {
		this.initializationLine = other.initializationLine;
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
		return initializationLine;
	}

	/**
	 * @param intialisationLine
	 *            the intialisationLine to set
	 */
	public void setIntialisationLine(int intialisationLine) {
		this.initializationLine = intialisationLine;
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

	/**
	 * 
	 * @param other
	 *            - another variable
	 * @return if the this variable and the other variable have the same name
	 */
	public boolean equals(Variable other) {
		return this.name.equals(other.name);
	}

}
