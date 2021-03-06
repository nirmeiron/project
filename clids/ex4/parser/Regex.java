package clids.ex4.parser;

/**
 * a class that stores many regular expressions used in the program
 * 
 * @author t6631696
 * 
 */
public class Regex {

	// Basics;
	public static final String SPACE = "[\\s\\t]*";
	public static final String MUST_SPACE = "[\\s]+";
	public static final String METHOD_NAME = "(?:[a-zA-Z])+\\w*";
	public static final String VAR_NAME = "_?" + METHOD_NAME;
	public static final String COMMENT_LINE = SPACE + "//.*";
	public static final String RETURN_LINE = SPACE + "return" + SPACE + ";";

	// Values:
	public static final String INT_RE = "-?\\d+";// V
	public static final String DOUBLE_RE = INT_RE + "(?:\\.\\d+)?";
	public static final String STRING_RE = "\".*\"";
	public static final String BOOLEAN_RE = "(?:" + DOUBLE_RE + "|true|false)";
	public static final String CHAR_RE = "\'.\'";
	public static final String VALUE = "(?:(?:" + CHAR_RE + ")|(?:"
			+ BOOLEAN_RE + ")|(?:" + STRING_RE + "))";

	// Variables declarations:
	public static final String TYPE_RE = "(int|double|String|boolean|char)";
	public static final String DECLARATION = "(" + VAR_NAME + ")" + SPACE
			+ "(?:(=" + SPACE + "(?:" + VALUE + "|" + VAR_NAME + ")" + ")?)";
	public static final String MULTI_DEC = DECLARATION + "(?:" + SPACE + ","
			+ SPACE + DECLARATION + ")*";

	public static final String MULTI_DEC_LINE = SPACE + "(final" + MUST_SPACE
			+ ")?" + TYPE_RE + MUST_SPACE + SPACE + MULTI_DEC + SPACE + ";"
			+ SPACE;

	public static final String ASSIGN_LINE = DECLARATION + ";" + SPACE;

	// methods declarations:
	public static final String PARAM = "(?:final)?" + SPACE + "(" + TYPE_RE
			+ ")" + MUST_SPACE + VAR_NAME;
	public static final String MULTI_PARAMS = "(" + PARAM + "(?:" + SPACE + ","
			+ SPACE + PARAM + SPACE + ")*)?";

	public static final String METHOD_DEC_LINE = SPACE + "void" + MUST_SPACE
			+ "(" + METHOD_NAME + ")" + SPACE + "\\(" + SPACE + MULTI_PARAMS
			+ SPACE + "\\)" + SPACE + "\\{" + SPACE;

	// methods calls:
	public static final String CALL_PARAM = "(?:(?:" + VAR_NAME + ")|(?:"
			+ VALUE + "))";
	public static final String CALL_MULTI_PARAMS = CALL_PARAM + "(?:" + SPACE
			+ "," + SPACE + CALL_PARAM + ")*";
	public static final String METHOD_CALL = SPACE + "(" + METHOD_NAME + ")"
			+ SPACE + "\\(" + SPACE + "(?:" + SPACE + CALL_MULTI_PARAMS + SPACE
			+ ")?\\)" + SPACE + ";" + SPACE;

	// boolean expressions:
	public static final String CONDITION = "(?:" + BOOLEAN_RE + "|" + VAR_NAME
			+ ")";
	public static final String OR = "\\|\\|";
	public static final String AND = "&&";
	public static final String LOGICAL = "(" + AND + "|" + OR + ")";

	public static final String MULTI_CONDITIONS = CONDITION + "(?:" + SPACE
			+ LOGICAL + SPACE + CONDITION + SPACE + ")*";

	public static final String PREFIX_CONDITION = "(if|while)";

	public static final String CONDITION_LINE = SPACE + PREFIX_CONDITION
			+ SPACE + "\\(" + SPACE + "(" + MULTI_CONDITIONS + SPACE + ")\\)"
			+ SPACE + "\\{" + SPACE;

}