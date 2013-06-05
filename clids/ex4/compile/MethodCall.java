package clids.ex4.compile;

import java.util.LinkedList;

/**
 * a class that represents a call to a method
 * 
 * @author Nir & Ira
 * 
 */
public class MethodCall {
	private String name;
	private LinkedList<String> params;

	/**
	 * a constructor that gets the name of the called method, and a list of
	 * parameters provided to it
	 * 
	 * @param name
	 *            - the name of the called method
	 * @param params
	 *            - a list of parameters provided to the called method
	 */
	public MethodCall(String name, LinkedList<String> params) {
		this.name = name;
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
	public LinkedList<String> getParams() {
		return params;
	}
}
