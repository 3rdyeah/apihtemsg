package gen.type;

import gen.CodeFormater;
import gen.TypeManager;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:45
 */
public class AListType extends CollectType {

	public String getCollectType() {
		return "java.util.ArrayList";
	}

	public String getFullType() {
		return String.format("%s<%s>", getCollectType(), TypeManager.wrap(this.type));
	}

	@Override
	public String variableCode() {
		String value = String.format("new %s<>()", getCollectType());
		return CodeFormater.formatVariableWithValue(getFullType(), name, value);
	}

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (TypeManager.isCollectType(type)) {
			throw new IllegalArgumentException("collection type can not be value, name = " + name + ", type = " + type);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s.putInt(%s.size());", prev, bufferName, name));
		sb.append("\r\n");
		sb.append(String.format("%sfor (%s var : %s) {", prev, type, name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.put%s(var);", prev, bufferName, TypeManager.operaType(type)));
		} else if (TypeManager.getTypeId(type) == TypeManager.BOOLEAN) {
			sb.append(String.format("%s\twriteBoolean(%s, var);", prev, bufferName));
		} else if (TypeManager.getTypeId(type) == TypeManager.STRING) {
			sb.append(String.format("%s\twriteString(%s, var);", prev, bufferName));
		} else if (TypeManager.getTypeId(type) == TypeManager.BYTES) {
			sb.append(String.format("%s\t%s.putInt(%s.length);\r\n", prev, bufferName, name));
			sb.append(String.format("%s\t%s.put(%s);", prev, bufferName, name));
		} else {
			sb.append(String.format("%s\tvar.encode(%s);", prev, bufferName));
		}
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		if (TypeManager.isCollectType(type)) {
			throw new IllegalArgumentException("collection type can not be value, name = " + name + ", type = " + type);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sint %sLen = %s.getInt();", prev, name, bufferName));
		sb.append("\r\n");
		sb.append(String.format("%sfor (int i = 0; i < %sLen; i++) {", prev, name));
		sb.append("\r\n");
		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s value = %s.get%s();\r\n", prev, type, bufferName, TypeManager.operaType(type)));
		} else if (TypeManager.getTypeId(type) == TypeManager.BOOLEAN) {
			sb.append(String.format("%s\tboolean value = readBoolean(%s);", prev, bufferName));
		} else if (TypeManager.getTypeId(type) == TypeManager.STRING) {
			sb.append(String.format("%s\tString value = readString(%s);", prev, bufferName));
		} else if (TypeManager.getTypeId(type) == TypeManager.BYTES) {
			sb.append(String.format("%s\tint bytesLen = %s.getInt();\r\n", prev, bufferName));
			sb.append(String.format("%s\tbyte[] value = new byte[bytesLen];\r\n", prev));
			sb.append(String.format("%s\t%s.get(value);", prev, bufferName));
		} else {
			sb.append(String.format("%s\t%s value = new %s();\r\n", prev, type, type));
			sb.append(String.format("%s\tvalue.decode(%s);\r\n", prev, bufferName));
		}
		sb.append(String.format("%s\t%s.add(value);\r\n", prev, name));
		sb.append(prev).append("}");
		return sb.toString();
	}
}
