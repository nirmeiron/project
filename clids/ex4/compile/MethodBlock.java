package clids.ex4.compile;

import java.util.LinkedList;

import clids.ex4.exceptions.MessageException;
import clids.ex4.exceptions.NoReturnStatementException;
import clids.ex4.misc.Type;
import clids.ex4.parser.Classifier;

//
public class MethodBlock extends CodeBlock {
	private String name;
	private LinkedList<Type> params;

	public MethodBlock(int start, int end, LinkedList<Variable> oldies,
			String name, LinkedList<Type> params) {
		super(start, end, oldies);
		this.name = name;
		this.params = params;
	}

	public MethodBlock(String methodName, LinkedList<Variable> paramsList,
			CodeBlock skeleton, LinkedList<Type> params) {
		super(skeleton);
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

	public boolean equals(MethodBlock other) {
		return this.name.equals(other.name);
	}

	public boolean equals(MethodCall other) {
		return this.name.equals(other.getName());
	}

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

	@Override
	public void compile(String[] data) throws MessageException,
			NoReturnStatementException {
		if (!Classifier.hasReturnStatement(this, data))
			throw new NoReturnStatementException();
		this.endLine--;
		super.compile(data);
	}

}
