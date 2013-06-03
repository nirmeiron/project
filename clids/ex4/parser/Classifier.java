package clids.ex4.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clids.ex4.compile.MethodBlock;
import clids.ex4.compile.MethodCall;
import clids.ex4.compile.Variable;
import clids.ex4.exceptions.AssignmentToFinalException;
import clids.ex4.exceptions.InvalidValueException;
import clids.ex4.misc.ToolBox;
import clids.ex4.misc.Type;

/**
 * 
 * @author Nir&Ira
 * 
 */
public class Classifier {
	/**
	 * this method checks whether the second var can be assigned to the first
	 * var. throws exception if first is final, and return false if doesn't fit
	 * because of the type. if fits, its true;
	 * 
	 * @param first
	 * @param second
	 * @return
	 * @throws AssignmentToFinalException
	 */
	public static boolean legalAssignment(Variable first, Variable second)
			throws AssignmentToFinalException {
		if (first.isFinal())
			throw new AssignmentToFinalException();
		return legalTypeAssignment(first.getType(), second.getType());
	}

	/**
	 * this method checks whether a variable of type second var be assigned to a
	 * variable of type first return false if cannot be assigned. if can,
	 * returns true;
	 * 
	 * @param first
	 * @param second
	 * @return
	 * @throws AssignmentToFinalException
	 */
	public static boolean legalTypeAssignment(Type first, Type second) {
		return ((first.equals(second))
				|| (first.equals(Type.DOUBLE) && second.equals(Type.INT)) || ((first
				.equals(Type.BOOLEAN)) && (second.equals(Type.DOUBLE) || second
				.equals(Type.INT))));
	}

	/**
	 * checks if the line is a comment line
	 * 
	 * @param line
	 *            - the line
	 * @return true iff the line starts with "//"
	 */
	public static boolean isComment(String line) {
		return (line.matches(Regex.COMMENT_LINE) || line.equals(""));
	}

	/**
	 * checks if the given line is of the form "method_name(type1 param1,...){
	 * 
	 * @param line
	 *            = the String line to check
	 * @return true iff the line is of the right form
	 */
	public static boolean isMethodLine(String line) {
		Pattern methodPattern = Pattern.compile(Regex.METHOD_DEC_LINE);
		Matcher methodMatcher = methodPattern.matcher(line);
		return methodMatcher.matches();
	}

	/**
	 * checks if the given line is of the form
	 * "final(?) Type var_name = value, var_name,..;"
	 * 
	 * @param line
	 *            the line to check
	 * @return true iff the line is of the right form
	 */
	public static boolean isDeclerationLine(String line) {
		Pattern dP = Pattern.compile(Regex.MULTI_DEC_LINE);
		Matcher dM = dP.matcher(line);
		return dM.matches();
	}

	/**
	 * checks if the given line is of the form "var_nam = value/other_var"
	 * 
	 * @param line
	 *            the line to check
	 * @return true iff the line is of the right form
	 */
	public static boolean isAssignmentLine(String line) {
		Pattern aP = Pattern.compile(Regex.ASSIGN_LINE);
		Matcher aM = aP.matcher(line);
		return aM.matches();
	}

	/**
	 * checks if the given line is of the form
	 * "method_name(param1, param2,...);"
	 * 
	 * @param line
	 *            the line to check
	 * @return true iff the line is of the right form
	 */
	public static boolean isMethodCall(String line) {

		Pattern mCP = Pattern.compile(Regex.METHOD_CALL);
		Matcher mCM = mCP.matcher(line);
		return mCM.matches();
	}

	/**
	 * checks if the given line is a conditional line, means if line or while
	 * line.
	 * 
	 * @param line
	 *            - the line to check
	 * @return true iff the line is of the right form
	 */
	public static boolean isConditional(String line) {
		Pattern conditionPattern = Pattern.compile(Regex.CONDITION_LINE);
		Matcher conditionMatcher = conditionPattern.matcher(line);
		return conditionMatcher.matches();
	}

	/**
	 * returns if the method call represented by MethodCall to the method
	 * MethodBlock is valid, given an array list of all previously declared vars
	 * 
	 * @param mb
	 *            - the method block
	 * @param mc
	 *            - the method call
	 * @param exsistingVars
	 *            - a list of all previously declared vars
	 * @return true iff the call is legal
	 */
	public static boolean legalMethodCall(MethodBlock mb, MethodCall mc,
			ArrayList<Variable> exsistingVars) {
		LinkedList<Type> method = mb.getParams();
		LinkedList<String> call = mc.getParams();
		if (method.size() != call.size())
			return false;
		for (int i = 0; i < method.size(); i++) {
			Type mParam = method.get(i);
			String cParamName = (call.get(i));
			if (ToolBox.isValue(cParamName))
				try {
					if (!ToolBox.getTypeFromString(cParamName).equals(mParam))
						return false;
					else {// cParamName must be a varName
						for (Variable var : exsistingVars)
							if (var.getName().equals(cParamName)) {
								if (!var.getType().equals(mParam))
									return false;
								continue;
							}
					}
				} catch (InvalidValueException e) {
					// assumption: will not get here
				}
		}
		return true;
	}

	/**
	 * checks if a given method has a valid return statement, which must be the
	 * last code line(non comment or empty) in the method's body
	 * 
	 * @param method
	 *            - the method to chesk
	 * @param data
	 *            - the code's data
	 * @return true iff the last code line in the method body is "return;"
	 */
	public static boolean hasReturnStatement(MethodBlock method, String[] data) {
		int lineNum = method.getEndLine() - 1;
		while (isComment(data[lineNum]))
			lineNum--;
		return data[lineNum].matches(Regex.RETURN_LINE);
	}

	public static boolean isReturnStatement(String string) {
		return string.matches(Regex.RETURN_LINE);
	}
}
