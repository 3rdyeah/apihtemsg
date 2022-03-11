package apihteproto.gen;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:18
 */
public class CodeFormater {

	public static final String _IMPORT_APIHTEPROTO = "import apihteproto.io.ApihteProto;";

	public static final String _IMPORT_MSG = "import apihteproto.io.Message;";

	public static final String _IMPORT_BINARYBUFFER = "import apihteproto.io.BinaryBuffer;";

	public static final String _MSG_ENCODE_DEFAULT = "\tpublic BinaryBuffer encode() {\r\n" +
			"\t\tBinaryBuffer out = getBuffer();\r\n" +
			"\t\tencode(out);\r\n" +
			"\t\treturn out;\r\n" +
			"\t}";

	public static final String _OVERRIDE = "\t@Override";

	public static final String _MSG_ENCODE = "\tpublic BinaryBuffer _encode(BinaryBuffer out) {\r\n" +
			"%s" +
			"\t\treturn out;\r\n" +
			"\t}";

	public static final String _MSG_DECODE = "\tpublic void _decode(BinaryBuffer in) {\r\n" +
			"%s" +
			"\t}";

	public static final String _MSG_ID_GETTER = "\t@Override\r\n" +
			"\tpublic int getMsgId() {\r\n" +
			"\t\treturn MSG_ID;\r\n" +
			"\t}";

	public static final String _WARNING = "\t/**\r\n" +
			"\t * Warning!!! Warning!!! Warning!!!\r\n" +
			"\t * You can only add your code in edit area\r\n" +
			"\t */";

	public static final String _VAR_BYTEBUF = "\tprivate BinaryBuffer buffer = BinaryBuffer.allocate(size());";

	public static final String _NO_EDIT_BEGIN = "// NO EDIT AREA BEGIN!!!";
	public static final String _NO_EDIT_END = "// NO EDIT AREA END!!!";

	public static final String _EDIT_BEGIN = "// YOU CAN ADD CODE AFTER THIS ONLY";
	public static final String _EDIT_END = "// YOU CAN ADD CODE BEFORE THIS ONLY";

	public static final String _EXT_IMPORT_BEGIN = "// EXT IMPORT BEGIN";
	public static final String _EXT_IMPORT_END = "// EXT IMPORT END";

	public static final String _FUNC_PROCESS = _EDIT_BEGIN +
			"\r\n\t@Override\r\n" +
			"\tpublic void process() {\r\n" +
			"\r\n" +
			"\t}\r\n" +
			_EDIT_END;

	public static String VAR = "\tprivate %s %s;";
	public static String VAR_WITH_VALUE = "\tprivate %s %s = %s;";

	public static String formatVariable(String type, String name) {
		return String.format(VAR, type, name);
	}

	public static String formatVariableWithValue(String type, String name, String value) {
		return String.format(VAR_WITH_VALUE, type, name, value);
	}

	public static String _GETTER = "\tpublic %s get%s() {\r\n" +
			"\t\treturn %s;\r\n" +
			"\t}";

	public static String _SETTER = "\tpublic void set%s(%s %s) {\r\n" +
			"\t\tthis.%s = %s;\r\n" +
			"\t}";

	public static String formatGetter(String type, String name) {
		return String.format(_GETTER, type, name, name);
	}

	public static String formatSetter(String type, String name) {
		return String.format(_SETTER, name, type, name, name, name);
	}

	public static String _SIZE = "\tpublic int size() {\r\n" +
			"\t\treturn %s;\r\n" +
			"\t}";

	public static String CASE_RET = "\t\t\tcase %s: return \"%s\";";

	public static String REFLECTOR = "package %s;\n\n" +
			"import apihteproto.io.BinaryBuffer;\n" +
			"import apihteproto.io.Message;\n\n" +
			"/**\n" +
			" * AUTOMATIC GENERATION\n" +
			" * DO NOT EDIT THIS CLASS !!!\n" +
			" */\n" +
			"public class MessageReflector {\n\n" +
			"\tprivate static String className(int msgId) {\n" +
			"\t\tswitch (msgId) {\n" +
			"%s" +
			"\t\t}\n" +
			"\t\treturn null;\n" +
			"\t}\n\n" +
			"\tpublic static Message create(byte[] bytes) throws ClassNotFoundException, InstantiationException, IllegalAccessException {\n" +
			"\t\tBinaryBuffer in = BinaryBuffer.wrap(bytes);\n" +
			"\t\tint msgId = in.readInt();\n" +
			"\t\tif (msgId <= 0) {\n" +
			"\t\t\tthrow new IllegalAccessException(\"Create message failed, msgid error, msgid = \" + msgId);\n" +
			"\t\t}\n" +
			"\t\tin.position(0);\n" +
			"\n" +
			"\t\tString className = className(msgId);\n" +
			"\t\tif (className == null) {\n" +
			"\t\t\treturn null;\n" +
			"\t\t}\n" +
			"\t\tClass<?> clz = Class.forName(className);\n" +
			"\t\tMessage message = (Message) clz.newInstance();\n" +
			"\t\tmessage.decode(in);\n" +
			"\t\treturn message;\n" +
			"\t}\n" +
			"}\n";
}
