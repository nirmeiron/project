package clids.ex4.compile;

import java.util.LinkedList;

import clids.ex4.misc.Type;
// hey 
public class MethodCall {
	private String name;
	private LinkedList<String> params;

	/**
	 * @param name
	 * @param params
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
