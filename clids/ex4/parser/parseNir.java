package clids.ex4.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import clids.ex4.compile.CodeBlock;
import clids.ex4.compile.MethodBlock;
import clids.ex4.exceptions.InvalidValueException;
import clids.ex4.misc.ToolBox;
import clids.ex4.misc.Type;

public class parseNir {

	public static void main(String[] args) {
		String[] data = null;
		try {
			data = ToolBox.importData("test501.sjava");
		} catch (IOException e) {
			System.out.println("until when");
			e.printStackTrace();
		}
		try {
			MethodBlock mb = Creator.parseMethodLine(data, 0);
			System.out.println(mb.getName());
			LinkedList<Type> types = mb.getParams();
			for (Type type : types)
				System.out.print(type + " ");
		} catch (InvalidValueException e) {
			System.out.println(e.getMessage());
		}
	}
}
