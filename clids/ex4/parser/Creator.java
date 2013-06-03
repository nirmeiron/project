package clids.ex4.parser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clids.ex4.compile.CodeBlock;
import clids.ex4.compile.MethodBlock;
import clids.ex4.compile.MethodCall;
import clids.ex4.compile.Variable;
import clids.ex4.exceptions.IleagalBracketsException;
import clids.ex4.exceptions.InvalidValueException;
import clids.ex4.exceptions.NoValueForFinalVarException;
import clids.ex4.exceptions.VarAlreadyExistsException;
import clids.ex4.misc.ToolBox;
import clids.ex4.misc.Type;

//general comment - all parse method assume validity of input, at least for their analysis
public class Creator {

	/**
	 * receives string that represents a declaration of one or more variables,
	 * some of which may also by initialized, and given there are no errors,
	 * returns an array list containing all of the declared and/or initialized
	 * variables
	 * 
	 * @param isFinal
	 *            - whether the variables declared in this line are final
	 * @param type
	 *            - the type of the variables
	 * @param decLine
	 *            - the string representing the variables names and values
	 * @return an array list containing all of the declared and/or initialized
	 *         variables
	 * @throws InvalidValueException
	 *             if at least one of the values is not suitable to the given
	 *             type, as a given explicit value, or as another pre-declared
	 *             variable with a not suitable type
	 * @throws NoValueForFinalVarException
	 *             if the variables are final, but no values was given to at
	 *             least one of them
	 */
	public static LinkedList<Variable> parseDecLine(String decLine, int line)
			throws Exception {
		LinkedList<Variable> newVariables = new LinkedList<Variable>();
		Pattern dP = Pattern.compile(Regex.MULTI_DEC_LINE);
		Matcher dM = dP.matcher(decLine);
		dM.matches();
		// if and only if the first word expression has caught something
		boolean isFinal = dM.group(1) != null;
		// gets the type of the variables
		Type type = ToolBox.getTypeFromName(dM.group(2));
		// gets the rest of the line, the variables' names and values
		int declarationsStartIndex = decLine.indexOf(dM.group(2))
				+ dM.group(2).length();
		String decs = decLine.substring(declarationsStartIndex);
		// removes ';'
		decs = decs.split(";")[0];
		// removes spaces
		decs = decs.replaceAll("\\s", "");
		// splits to individual declarations
		String[] decsArr = decs.split(",");
		for (String varName : decsArr) {
			Variable newVar;
			// the variable declaration contains "=", which means it is assigned
			if (varName.indexOf('=') != -1) {
				String value = varName.split("=")[1];
				varName = varName.split("=")[0];
				if (ToolBox.isValue(value))
					if (!Classifier.legalTypeAssignment(type,
							ToolBox.getTypeFromString(value)))
						throw new InvalidValueException();
				newVar = new Variable(isFinal, type, varName, line);
				// the case where ToolBox.isVarName(value) = true is handled by
				// parseVarNamesDec
			} else if (isFinal)
				throw new NoValueForFinalVarException();
			else
				newVar = new Variable(isFinal, type, varName);
			newVariables.add(newVar);
		}
		for (int i = 0; i < newVariables.size(); i++)
			for (int j = 0; j < newVariables.size(); j++)
				if (i != j
						&& newVariables.get(i).getName()
								.equals(newVariables.get(j).getName()))
					throw new VarAlreadyExistsException();
		return newVariables;
	}

	/**
	 * receives string that represents a declaration of one or more variables,
	 * some of which may also by initialized. if some of the initialized
	 * variable are set by assignment to other, already declared variables,
	 * return an string linked list containing the names of these pre declared
	 * variables. the first cell in this returned linked list is the (string)
	 * type of the declaration
	 * 
	 * @param decLine
	 *            - the line to parse
	 * @return a linked list whose first cell is the type of the variable, and
	 *         the others(if exist) are the names of the pre declared variables
	 *         used to initialize the variables in this line
	 * @throws Exception
	 */
	public static LinkedList<String> parseVarNamesDec(String decLine)
			throws Exception {
		LinkedList<String> preDecVars = new LinkedList<String>();
		Pattern dP = Pattern.compile(Regex.MULTI_DEC_LINE);
		Matcher dM = dP.matcher(decLine);
		dM.matches();
		// sets the first cell
		preDecVars.add(dM.group(2));
		// gets the rest of the line, the variables' names and values
		int decsStartIndex = decLine.indexOf(dM.group(2))
				+ dM.group(2).length();
		String decs = decLine.substring(decsStartIndex);
		// removes spaces
		decs = decs.replaceAll("\\s", "");
		// removes ';'
		decs = decs.substring(0, decs.length() - 1);

		// splits to individual declarations
		String[] decsArr = decs.split(",");
		for (String var : decsArr)
			if (var.indexOf('=') != -1 && !ToolBox.isValue(var.split("=")[1])) {
				String value = var.split("=")[1];
				preDecVars.add(value);
			}
		return preDecVars;
	}

	/**
	 * gets a line of a conditional. returns the list of variable names in the
	 * condition, in order to check that they are legal in the compiler.
	 * 
	 * @param line
	 *            - the string to check
	 * @return linked list of strings - representing names
	 */
	public static LinkedList<String> parseVarNameConditional(String line) {
		LinkedList<String> result = new LinkedList<String>();
		int start, end;
		start = line.indexOf('(') + 1;
		end = line.indexOf(')');
		String conditions = line.substring(start, end);
		String[] parts = conditions.split(Regex.LOGICAL);

		Pattern boolPattern = Pattern.compile(Regex.BOOLEAN_RE);

		for (String current : parts) {
			Matcher boolMatcher = boolPattern.matcher(current);
			if (!boolMatcher.matches()) {
				result.add(current);
			}
		}

		return result;
	}

	/**
	 * receives a String the represents an assignment of a variable, of the form
	 * "varName=value". returns a string array with two cells, the first being
	 * the varName, and the second being the assigned value
	 * 
	 * @param assignLine
	 *            - the line to parse
	 * @return a string array with two cells, the first being the varName, and
	 *         the second being the assigned value
	 */
	public static String[] parseAssignLine(String assignLine) {
		assignLine = assignLine.replaceAll("\\s", "");
		String varName = assignLine.split("=")[0];
		String value = assignLine.substring(varName.length() + 1,
				assignLine.indexOf(';'));
		String[] result = { varName, value };
		return result;
	}

	/**
	 * parses a method call line, of the form "methodName(varName,value,...);"
	 * 
	 * @param methodCall
	 *            the line to parse
	 * @return a methodCall object, with the name of the called method, and,
	 *         according to their appearance order, the input parameters, some
	 *         if which are explicit values("STR", 9.3, true), and others may be
	 *         variable names
	 */
	public static MethodCall parseMethodCall(String methodCall) {
		LinkedList<String> result = new LinkedList<String>();
		String methodName = methodCall.split("\\(")[0];
		String params = methodCall.split("\\(")[1].split("\\)")[0];
		String[] parameters = params.split(",");
		for (String param : parameters)
			result.add(param);
		return new MethodCall(methodName, result);
	}

	/**
	 * this method gets the data array of strings and a start line of a
	 * CodeBlock. it searches for the closer '}' end therefore for the endLine.
	 * returns a CodeBlock with this start and end.
	 * 
	 * @param data
	 * @param start
	 * @return
	 * @throws IleagalBracketsException 
	 */
	public static CodeBlock parseCBLimits(String[] data, int start) throws IleagalBracketsException {
		int end = -1, counter = 0;
		for (int i = start; i < data.length; i++) {
			if (data[i].indexOf('{') != -1) {
				counter++;
			} else if (data[i].indexOf('}') != -1) {
				counter--;
				if (counter == 0) {
					end = i;
					break;
				}
			}
		}
		if (counter != 0) {
			throw new IleagalBracketsException();
		}
		CodeBlock result = new CodeBlock(start, end);
		return result;
	}

	/**
	 * receives a String array of lines representing an entire method, and the
	 * line in which it started. returns a MethodBlock object containing the
	 * method's name, start and end lines, and input parameters types in their
	 * appearance order
	 * 
	 * @param data
	 *            - the entire method
	 * @param startLine
	 *            - the start line
	 * @return MethodBlock object representing the parsed method
	 * @throws InvalidValueException
	 * @throws IleagalBracketsException 
	 * @throws VarAlreadyExistsException 
	 */
	public static MethodBlock parseMethodLine(String[] data, int startLine)
			throws InvalidValueException, IleagalBracketsException, VarAlreadyExistsException {

		String methodLine = data[startLine];
		CodeBlock cbMethod = Creator.parseCBLimits(data, startLine);

		Pattern mP = Pattern.compile(Regex.METHOD_DEC_LINE);
		Matcher mM = mP.matcher(methodLine);
		LinkedList<Variable> paramsList = new LinkedList<Variable>();
		LinkedList<Type> typesList = new LinkedList<Type>();

		mM.matches();
		String methodName = mM.group(1);
		String paramsLine = mM.group(2);

		if (paramsLine != null) {
			String[] params = paramsLine.split(",");

			for (String param : params) {
				boolean isFinal = param.replaceAll("\\s", "").startsWith(
						"final");
				Pattern pP = Pattern.compile(Regex.PARAM);
				Matcher pM = pP.matcher(param);
				pM.matches();
				Type paramType = (ToolBox.getTypeFromName(pM.group(1)));
				String paramName = param.split(pM.group(1))[1].split("\\s")[1];
				Variable var = new Variable(isFinal, paramType, paramName,
						startLine);
				paramsList.add(var);
				typesList.add(paramType);
			}
		}
		for (int i = 0; i < paramsList.size(); i++)
			for (int j = 0; j < paramsList.size(); j++)
				if (i != j
						&& paramsList.get(i).getName()
								.equals(paramsList.get(j).getName()))
					throw new VarAlreadyExistsException();
		return new MethodBlock(methodName, paramsList, cbMethod, typesList);
	}
}
