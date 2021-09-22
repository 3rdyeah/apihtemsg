package gen.type;

import gen.CodeFormater;
import gen.TypeManager;

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
	public String encodeCode(String byteBuf, String prev) {
		if (prev == null) {
			prev = "";
		}
		return String.format("%s%s.put%s(%s);", prev, byteBuf, TypeManager.operaType(type), name);
	}

	@Override
	public String decodeCode(String byteBuf, String prev) {
		return String.format("%s%s = %s.get%s();", prev, name, byteBuf, TypeManager.operaType(type));
	}

	@Override
	public int size() {
		if (type.equalsIgnoreCase("byte")) {
			return Byte.SIZE;
		} else if (type.equalsIgnoreCase("short")) {
			return Short.SIZE;
		} else if (type.equalsIgnoreCase("int")) {
			return Integer.SIZE;
		} else if (type.equalsIgnoreCase("long")) {
			return Long.SIZE;
		} else if (type.equalsIgnoreCase("float")) {
			return Float.SIZE;
		} else if (type.equalsIgnoreCase("double")) {
			return Double.SIZE;
		} else if (type.equalsIgnoreCase("char")) {
			return Character.SIZE;
		} else if (type.equalsIgnoreCase("bytes")) {
			return 0;
		}
		return 0;
	}
}
