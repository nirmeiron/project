package clids.ex4.compile;

import clids.ex4.compile.Variable;

import java.util.LinkedList;
import clids.ex4.exceptions.DoubleDeclarationException;
import clids.ex4.exceptions.MessageException;
import clids.ex4.exceptions.NotInitializedAssignException;
import clids.ex4.exceptions.UnexpectedLineException;
import clids.ex4.exceptions.VarNotExistsException;
import clids.ex4.parser.*;
import clids.ex4.misc.*;

/**
 * a class that runs the program
 * 
 * @author Nir & ira
 * 
 */
public class Manager {
	public static LinkedList<MethodBlock> allMethods;
	public LinkedList<Variable> allVars;

	/**
	 * a simple constructor
	 */
	public Manager() {
		Manager.allMethods = new LinkedList<MethodBlock>();
		this.allVars = new LinkedList<Variable>();
	}

	/**
	 * a method that performs the compilation on the entire given code
	 * 
	 * @param data
	 *            - the code to compile
	 * @throws MessageException
	 */
	public void compile(String[] data) throws MessageException {
		if (!ToolBox.legalBrackets(data))
			throw new MessageException("IlegalBrackts");

		for (int i = 0; i < data.length; i++) {
			data[i] = data[i].trim();
			try {
				if (Classifier.isComment(data[i]) || data[i].equals("")) {
					continue;
				}
				if (Classifier.isMethodLine(data[i])) {
					MethodBlock method = Creator.parseMethodLine(data, i);
					i = method.getEndLine();
					Manager.allMethods.add(method);
					continue;
				}
				if (Classifier.isDeclerationLine(data[i])) {
					LinkedList<String> assigned = Creator
							.parseVarNamesDec(data[i]);
					if (assigned.size() > 1) {// i changed 1 to 2
						Type decType = ToolBox.getTypeFromName(assigned.get(0));

						for (int j = 1; j < assigned.size(); j++) {
							int place = ToolBox.existsInList(new Variable(
									false, decType, assigned.get(j)),
									this.allVars);
							if (place == -1) {
								throw new VarNotExistsException();
							}
							if (this.allVars.get(place).getIntialisationLine() == -1) {
								throw new NotInitializedAssignException();
							}
							this.allVars.get(place);// HERE !!

						}
					}
					LinkedList<Variable> vars = Creator
							.parseDecLine(data[i], i);
					for (Variable current : vars) {
						int index = ToolBox.existsInList(current, this.allVars);
						if (index != -1) {
							throw new DoubleDeclarationException();
						}
					}
					this.allVars.addAll(vars);
					continue;
				}
				throw new UnexpectedLineException();
			} catch (Exception e2) {
				String classValues = e2.toString();
				throw ToolBox.editMessage(classValues, i);
			}
		}
		for (MethodBlock method : Manager.allMethods) {
			method.addOldVariables(this.allVars);
			try {
				method.compile(data);
			} catch (Exception e) {// handle this shit
									// **********************************
				throw new MessageException(
						"NoReturnStatementException, in line:"
								+ method.getStartLine());
			}
		}

	}
}
