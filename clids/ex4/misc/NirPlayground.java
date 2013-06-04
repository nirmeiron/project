package clids.ex4.misc;

import clids.ex4.parser.*;


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
		String st = " if(a||b){";
		System.out.println(st.matches(Regex.CONDITION_LINE));

	}
}
