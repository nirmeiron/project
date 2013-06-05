package clids.ex4.compile;

import java.util.LinkedList;

import clids.ex4.misc.Type;

/**
 * a class that represents a general method block
 * 
 * @author Nir & Ira
 * 
 */
public class MethodBlock extends CodeBlock {
	private String name;
	private LinkedList<Type> params;

	/**
	 * a constructor that get the method's name, a list of variables declared
	 * previously, the main code block,
	 * 
	 * @param methodName
	 *            - the method's name
	 * @param paramsList
	 *            - a list of variables declared previously
	 * @param main
	 *            - the main code block
	 * @param params
	 *            - and a list of types from the signature
	 */
	public MethodBlock(String methodName, LinkedList<Variable> paramsList,
			CodeBlock main, LinkedList<Type> params) {
		super(main);
		this.name = methodName;
		this.localMembers.addAll(paramsList);
		this.name = methodName;
		this.params = params;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the params
	 */
	public LinkedList<Type> getParams() {
		return params;
	}

	/**
	 * a method that checks if the method block's parameters match in type and
	 * order to a given list of types
	 * 
	 * @param other
	 *            - the type list to compare to
	 * @return whether the lists match
	 */
	public boolean matches(LinkedList<Type> other) {
		if (other == null && this.params == null) {
			return true;
		}
		if (other == null || this.params == null) {
			return false;
		}
		if (other.size() != this.params.size())
			return false;
		for (int i = 0; i < other.size(); i++) {
			if (!this.params.get(i).equals(other.get(i))) {
				return false;
			}
		}
		return true;
	}

}
