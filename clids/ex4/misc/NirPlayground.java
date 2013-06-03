package clids.ex4.misc;

import java.io.IOException;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

import clids.ex4.compile.CodeBlock;
import clids.ex4.exceptions.*;

import clids.ex4.exceptions.InvalidValueException;
import clids.ex4.misc.Type;
import clids.ex4.compile.*;

public class NirPlayground {

	private static final String VAR_NAME_RE = "_?(?:[a-zA-Z])+\\w*";
	private static final String SPACE_RE = "\\s*+";
	private static final String MUST_SPACE_RE = "\\s+";
	private static final String METHOD_NAME_RE = "(?:[a-zA-Z])+\\w*";
	private static final String TYPE_RE = "(int|double|String|boolean|char)";

	public static final String PARAM_RE = "(" + TYPE_RE + ")" + MUST_SPACE_RE
			+ VAR_NAME_RE;
	private static final String MULTI_PARAMS_RE = "(" + PARAM_RE + "(?:,"
			+ SPACE_RE + PARAM_RE + SPACE_RE + ")*)?";

	public static final String METHOD_LINE_RE = SPACE_RE + "void"
			+ MUST_SPACE_RE + "(" + METHOD_NAME_RE + ")\\(" + SPACE_RE
			+ MULTI_PARAMS_RE + SPACE_RE + "\\)";

	public static void main(String[] args) {
		String[] data = null;
		try {
			data = ToolBox.importData("test502.sjava");
		} catch (IOException e) {
			System.out.println("until when");
			e.printStackTrace();
		}
		Manager man=new Manager();
		try {
			man.compile(data);
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}
}
