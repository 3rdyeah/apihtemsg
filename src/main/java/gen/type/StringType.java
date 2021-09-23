package gen.type;

import gen.CodeFormater;

/**
 * @author 3rdyeah
 * created on 2021/9/23 10:33
 */
public class StringType extends BasicType {

	@Override
	public String variableCode() {
		if (value == null) {
			return CodeFormater.formatVariableWithValue("String", name, "\"\"");
		}
		return CodeFormater.formatVariableWithValue("String", name, value);
	}

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		return String.format("%swriteString(%s, %s);", prev, bufferName, name);
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		return String.format("%s%s = readString(%s);", prev, name, bufferName);
	}

	@Override
	public int size() {
		return 0;
	}
}
