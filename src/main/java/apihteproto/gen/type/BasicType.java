package apihteproto.gen.type;

import apihteproto.gen.CodeFormater;
import apihteproto.gen.TypeManager;

/**
 * @author 3rdyeah
 * created on 2021/8/4
 */
public class BasicType implements Type {
	public String type;
	public String name;
	public String value;

	@Override
	public String variableCode() {
		if (type.equalsIgnoreCase("string")) {
			type = "String";
		}
		if (value != null && !value.equalsIgnoreCase("")) {
			return CodeFormater.formatVariableWithValue(type, name, value);
		}
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
		return String.format("%s%s.write%s(%s);", prev, bufferName, TypeManager.operaType(type), name);
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		return String.format("%s%s = %s.read%s();", prev, name, bufferName, TypeManager.operaType(type));
	}

	@Override
	public int size() {
		if (type.equalsIgnoreCase("byte")) {
			return Byte.BYTES;
		} else if (type.equalsIgnoreCase("short")) {
			return Short.BYTES;
		} else if (type.equalsIgnoreCase("int")) {
			return Integer.BYTES;
		} else if (type.equalsIgnoreCase("long")) {
			return Long.BYTES;
		} else if (type.equalsIgnoreCase("float")) {
			return Float.BYTES;
		} else if (type.equalsIgnoreCase("double")) {
			return Double.BYTES;
		} else if (type.equalsIgnoreCase("char")) {
			return Character.BYTES;
		} else if (type.equalsIgnoreCase("bytes")) {
			return 0;
		}
		return 0;
	}
}
