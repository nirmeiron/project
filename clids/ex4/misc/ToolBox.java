package clids.ex4.misc;

//hey ira
//hey nir
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clids.ex4.compile.Variable;

import clids.ex4.exceptions.InvalidValueException;
import clids.ex4.exceptions.MessageException;
import clids.ex4.parser.Regex;

public class ToolBox {

	public static boolean legalBrackets(String[] data) {
		int counter = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length(); j++) {
				char current = data[i].charAt(j);
				if (current == '{')
					counter++;
				else if (current == '}') {
					counter--;
					if (counter < 0)
						return false;
				}
			}
		}
		return counter == 0;
	}

	public static LinkedList<Variable> merge(LinkedList<Variable> list1,
			LinkedList<Variable> list2) {
		LinkedList<Variable> result = new LinkedList<Variable>();
		for (Variable var : list1) {
			result.add(new Variable(var));
		}
		for (Variable var : list2) {
			result.add(new Variable(var));
		}
		return result;
	}

	public static MessageException editMessage(String className, int i) {
		String[] parts = className.split("\\.");
		return new MessageException(parts[parts.length - 1] + " in line: " + i);
	}

	public static String[] importData(String fileName) throws IOException {
		File theFile = new File(fileName);
		ArrayList<String> data = new ArrayList<>();
		BufferedReader in = new BufferedReader(new FileReader(
				theFile.getAbsoluteFile()));
		String lineOfData;
		while ((lineOfData = in.readLine()) != null) {
			data.add(lineOfData);
		}
		in.close();
		return data.toArray(new String[data.size()]);
	}

	/**
	 * returns the type of a given value string(of the form "3" or "true").
	 * 
	 * @param value
	 * @return it's Type
	 * @throws InvalidValueException
	 */
	public static Type getTypeFromString(String value)
			throws InvalidValueException {
		if (value.matches(Regex.INT_RE))
			return Type.INT;
		else if (value.matches(Regex.DOUBLE_RE))
			return Type.DOUBLE;
		else if (value.matches(Regex.CHAR_RE))
			return Type.CHAR;
		else if (value.matches(Regex.STRING_RE))
			return Type.STRING;
		else if (value.matches(Regex.BOOLEAN_RE))
			return Type.BOOLEAN;
		else
			throw new InvalidValueException();
	}

	/**
	 * converts String(of the form "String" or "int") to Type
	 * 
	 * @param typeName
	 * @return
	 * @throws InvalidValueException
	 */
	public static Type getTypeFromName(String typeName)
			throws InvalidValueException {
		switch (typeName) {
		case "char":
			return Type.CHAR;
		case "String":
			return Type.STRING;
		case "boolean":
			return Type.BOOLEAN;
		case "int":
			return Type.INT;
		case "double":
			return Type.DOUBLE;
		default:
			throw new InvalidValueException();
		}
	}

	/**
	 * converts Type to String
	 * 
	 * @param var
	 * @return
	 * @throws InvalidValueException
	 */
	public static String getStringFromType(Variable var)
			throws InvalidValueException {
		switch (var.getType()) {
		case CHAR:
			return "char";
		case STRING:
			return "String";
		case BOOLEAN:
			return "boolean";
		case INT:
			return "int";
		case DOUBLE:
			return "double";
		default:
			throw new InvalidValueException();
		}
	}

	/**
	 * prints the variables' namees
	 * 
	 * @param vars
	 * @throws InvalidValueException
	 */
	public static void printData(ArrayList<Variable> vars)
			throws InvalidValueException {
		System.out.print((vars.get(0).isFinal() ? "final " : "")
				+ ToolBox.getStringFromType(vars.get(0)));
		for (Variable var : vars) {
			System.out.print(" " + var.getName());
		}
	}

	/**
	 * this method gets a List of Variables and copies them to a new list.
	 * creating each variable a duplicate.
	 * 
	 * @param oldies
	 * @return
	 */
	public static LinkedList<Variable> copyListOfVars(
			LinkedList<Variable> oldies) {
		LinkedList<Variable> result = new LinkedList<Variable>();
		for (int i = 0; i < oldies.size(); i++) {
			result.add(new Variable(oldies.get(i)));
		}
		return result;
	}

	/**
	 * returns whether the given string is a valid variable name
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isVarName(String name) {
		Pattern vP = Pattern.compile(Regex.VAR_NAME);
		Matcher vM = vP.matcher(name);
		return vM.matches();
	}

	

	public static int existsInList(Variable member, LinkedList<Variable> list) {
		if (list == null)
			return -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(member))
				return i;
		}
		return -1;
	}

	public static int existsInList(String varName, LinkedList<Variable> list) {
		return existsInList(new Variable(false, null, varName), list);
	}
}
