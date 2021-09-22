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
		return String.format("%s%s.write%s(%s);", prev, byteBuf, TypeManager.operaType(type), name);
	}

	@Override
	public String decodeCode(String byteBuf, String prev) {
		return String.format("%s%s = %s.read%s();", prev, name, byteBuf, TypeManager.operaType(type));
	}
}
