package clids.ex4.compile;

import java.util.LinkedList;
import clids.ex4.misc.*;
import clids.ex4.parser.Classifier;
import clids.ex4.parser.Creator;
import clids.ex4.exceptions.*;

/**
 * a class that represents a code block
 * 
 * @author Nir and Ira
 * 
 */

public class CodeBlock {
	// INSTANCE VARIABLES:
	protected LinkedList<Variable> localMembers;
	protected LinkedList<Variable> oldMembers;
	protected LinkedList<CodeBlock> subCB;
	protected int startLine;
	protected int endLine;

	/**
	 * a constructor that gets a start line, end line and a list of the "old"
	 * variables - variables declared before this CodeBlock.
	 * 
	 * @param start
	 *            - the start line
	 * @param end
	 *            - the end line
	 * @param oldies
	 *            - the other variables
	 */
	public CodeBlock(int start, int end, LinkedList<Variable> oldies) {
		this.startLine = start;
		this.endLine = end;
		this.localMembers = new LinkedList<Variable>();
		this.subCB = new LinkedList<CodeBlock>();
		this.oldMembers = ToolBox.copyListOfVars(oldies);
	}

	/**
	 * a constructor that builds a new CodeBlock only with start and end line
	 * 
	 * @param start
	 *            - the start line
	 * @param end
	 *            - the end line
	 */
	public CodeBlock(int start, int end) {
		this.startLine = start;
		this.endLine = end;
		this.localMembers = new LinkedList<Variable>();
		this.subCB = new LinkedList<CodeBlock>();
		this.oldMembers = new LinkedList<Variable>();
	}

	/**
	 * CopyConstructor that gets an other CodeBlock copies only the limits of
	 * other (start and end)
	 * 
	 * @param other
	 *            - the CodeBlock to be copied
	 */
	public CodeBlock(CodeBlock other) {
		this.startLine = other.startLine;
		this.endLine = other.endLine;
		this.localMembers = new LinkedList<Variable>();
		this.subCB = new LinkedList<CodeBlock>();
		this.oldMembers = new LinkedList<Variable>();
	}

	// אולי לא צריך את המתודה הבאה !! ******************************************
	/**
	 * this method inserts a list of variables to the oldMember making a copy of
	 * each variable.
	 * 
	 * @param oldies
	 */
	public void insertOldMembers(LinkedList<Variable> oldies) {
		this.oldMembers = new LinkedList<Variable>();
		for (Variable var : oldies) {
			this.oldMembers.add(new Variable(var));
		}
	}

	/**
	 * insert a new member that was declared, if it's legal.
	 * 
	 * @param member
	 * @throws VarAlreadyExistsException
	 */
	public void declareLocalMember(Variable member)
			throws VarAlreadyExistsException {

		// if the declaration is on an exists variable:
		int i = ToolBox.existsInList(member, this.localMembers);
		if (i != -1) {
			throw new VarAlreadyExistsException();
		}

		int j = ToolBox.existsInList(member, this.oldMembers);
		if (j != -1) {
			removeFromOld(j);
		}
		this.localMembers.add(member);
	}

	public void addSubCB(CodeBlock sub) {
		this.subCB.add(sub);
	}

	// !
	public void compile(String[] data) throws MessageException,
			NoReturnStatementException {
		for (int i = this.startLine + 1; i < this.endLine; i++) {
			try {
				if (Classifier.isComment(data[i])
						|| Classifier.isReturnStatement(data[i])) {
					continue;
				}
				if (Classifier.isMethodLine(data[i])) {
					throw new UnexpectedLineException();
				}
				if (Classifier.isConditional(data[i])) {
					LinkedList<String> usedVars = Creator
							.parseVarsCondition(data[i]);
					if (usedVars != null && usedVars.size() > 0) {
						for (String current : usedVars) {
							current = current.replaceAll("\\s", "");
							if (!Classifier.isValue(current)) {

								int indexInLocal = ToolBox.existsInList(
										current, this.localMembers);
								int indexInOlds = ToolBox.existsInList(current,
										this.oldMembers);
								if (indexInLocal == -1 && indexInOlds == -1) {
									throw new VarNotExistsException();
								}
								Variable var;
								if (indexInLocal != -1) {
									var = this.localMembers.get(indexInLocal);
								} else {
									var = this.oldMembers.get(indexInOlds);
								}
								if (!Classifier.legalTypeAssignment(
										Type.BOOLEAN, var.getType())) {
									throw new MustBeBooleanException();
								}
								if (var.getIntialisationLine() == -1)
									throw new NotInitializedAssignException();
							}
						}
					}
					CodeBlock subBlock = Creator.parseCBLimits(data, i);
					subBlock.addOldVariables(ToolBox.merge(this.oldMembers,
							this.localMembers));
					addSubCB(subBlock);
					i = subBlock.getEndLine();
					continue;
				}
				if (Classifier.isDeclerationLine(data[i])) {
					// all values that are assigned to some Variables:
					LinkedList<String> assigned = Creator
							.parseVarNamesDec(data[i]);
					Type decType = ToolBox.getTypeFromName(assigned.get(0));
					for (int j = 1; j < assigned.size(); j++) {
						int placeInOlds = ToolBox.existsInList(new Variable(
								false, decType, assigned.get(j)),
								this.oldMembers);
						int placeInLocal = ToolBox.existsInList(new Variable(
								false, decType, assigned.get(j)),
								this.localMembers);
						if (placeInOlds == -1 && placeInLocal == -1) {
							throw new VarNotExistsException();
						}
						if (placeInLocal != -1) {
							if (this.localMembers.get(placeInLocal)
									.getIntialisationLine() == -1)
								throw new NotInitializedAssignException();
						} else {
							if (this.oldMembers.get(placeInOlds)
									.getIntialisationLine() == -1)
								throw new NotInitializedAssignException();
						}
					}

					LinkedList<Variable> vars = Creator
							.parseDecLine(data[i], i);
					for (Variable current : vars) {
						int indexInLocal = ToolBox.existsInList(current,
								this.localMembers);
						if (indexInLocal != -1) {
							throw new DoubleDeclarationException();
						}
						int indexInOlds = ToolBox.existsInList(current,
								this.oldMembers);
						if (indexInOlds != -1) {
							removeFromOld(indexInOlds);
						}
						this.localMembers.add(current);
					}
					continue;
				}
				if (Classifier.isAssignmentLine(data[i])) {
					String[] result = Creator.parseAssignLine(data[i]);
					String varName = result[0];
					String value = result[1];

					int indexInLocal = ToolBox.existsInList(varName,
							this.localMembers);
					int indexInOlds = ToolBox.existsInList(varName,
							this.oldMembers);
					if (indexInLocal == -1 && indexInOlds == -1) {
						throw new VarNotExistsException();
					}
					Variable first;
					if (indexInLocal != -1) {
						first = this.localMembers.get(indexInLocal);
					} else
						first = this.oldMembers.get(indexInOlds);

					if (first.isFinal()) {
						throw new AssignmentToFinalException();
					}
					Type second = getTypeFromValue(value);

					if (!Classifier
							.legalTypeAssignment(first.getType(), second))
						throw new IlegalAssignmentException();
					continue;
				}
				if (Classifier.isMethodCall(data[i])) {
					MethodCall call = Creator.parseMethodCall(data[i]);
					MethodBlock method = methodFinder(call.getName());
					if (method == null) {
						throw new MethodDoesntExistsException();
					}
					LinkedList<Type> callTypes = new LinkedList<Type>();
					LinkedList<String> callValues = call.getParams();
					if (callValues.size() > 0) {
						for (String value : callValues) {
							callTypes.add(getTypeFromValue(value));
						}
					}
					if (!method.matches(callTypes)) {
						throw new ParamsDoesntMatchException();
					}
					continue;
				}
				throw new UnexpectedLineException();

			} catch (Exception e2) {
				String className = e2.toString();// stuff to do !
				throw ToolBox.editMessage(className, i);
			}
		}
		for (CodeBlock block : this.subCB) {
			block.compile(data);
		}

	}

	private MethodBlock methodFinder(String name) {
		for (MethodBlock method : Manager.allMethods) {
			if (method.getName().equals(name))
				return method;
		}
		return null;
	}

	private Type getTypeFromVarName(String value) throws VarNotExistsException {
		Type result;
		try {
			result = ToolBox.getTypeFromName(value);
		} catch (InvalidValueException e) {
			int indexInLocal2 = ToolBox.existsInList(value, this.localMembers);
			int indexInOlds2 = ToolBox.existsInList(value, this.oldMembers);

			if (indexInLocal2 == -1 && indexInOlds2 == -1) {
				throw new VarNotExistsException();
			}
			if (indexInLocal2 != -1) {
				result = this.localMembers.get(indexInLocal2).getType();
			} else {
				result = this.oldMembers.get(indexInOlds2).getType();
			}
		}
		return result;
	}

	private Type getTypeFromValue(String value) throws VarNotExistsException {
		Type result;
		try {
			result = ToolBox.getTypeFromString(value);
		} catch (InvalidValueException e) {
			int indexInLocal = ToolBox.existsInList(value, this.localMembers);
			int indexInOlds = ToolBox.existsInList(value, this.oldMembers);

			if (indexInLocal == -1 && indexInOlds == -1) {
				throw new VarNotExistsException();
			}
			if (indexInLocal != -1) {
				result = this.localMembers.get(indexInLocal).getType();
			} else {
				result = this.oldMembers.get(indexInOlds).getType();
			}
		}
		return result;
	}

	/**
	 * insert a new assignment.
	 * 
	 * @param member
	 * @throws AssignToUndeclaredException
	 * @throws AssignmentToFinalException
	 * @throws TypeAssignmentException
	 */
	public void assignLocalMember(Variable member)
			throws AssignToUndeclaredException, AssignmentToFinalException,
			TypeAssignmentException {
		int index1 = ToolBox.existsInList(member, this.localMembers);
		int index2 = ToolBox.existsInList(member, this.oldMembers);
		int index = Math.max(index1, index2);
		if (index1 == -1 && index2 == -1) {
			throw new AssignToUndeclaredException();
		}
		LinkedList<Variable> list;
		if (index1 != -1) {
			list = this.localMembers;
		} else
			list = this.oldMembers;
		Variable current = list.get(index);
		if (current.isFinal())
			throw new AssignmentToFinalException();
		if (!Classifier.legalAssignment(current, member)) {
			throw new TypeAssignmentException();
		}
		current.setIntialisationLine(member.getIntialisationLine());
	}

	private void removeFromOld(int index) {
		this.oldMembers.remove(index);
	}

	public void addOldVariables(LinkedList<Variable> vars) {
		this.oldMembers.addAll(vars);

	}

	public int getStartLine() {

		return this.startLine;
	}

	public int getEndLine() {
		return this.endLine;
	}
}