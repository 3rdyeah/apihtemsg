package apihteproto.gen.type;

import apihteproto.gen.CodeFormater;

/**
 * @author 3rdyeah
 * created on 2021/8/9 10:25
 */
public class BytesType implements Type {
	public static String type = "byte[]";
	public String name;

	@Override
	public String variableCode() {
		return CodeFormater.formatVariable(type, name);
	}

	@Override
	public String setterCode() {
		return CodeFormater.formatSetter(type, name);
	}

	@Override
	public String getterCode() {
		return CodeFormater.formatGetter(type, name);
	}

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		return String.format("%s%s.writeBytesWithSize(%s);", prev, bufferName, name);
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		return String.format("%s%s = %s.readBytes();", prev, name, bufferName);
	}

	@Override
	public int size() {
		return 0;
	}
}
