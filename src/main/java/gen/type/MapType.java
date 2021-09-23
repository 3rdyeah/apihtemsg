package gen.type;

import gen.CodeFormater;
import gen.TypeManager;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:46
 */
public class MapType extends CollectType {

	public String getCollectType() {
		return "java.util.HashMap";
	}

	public String getFullType() {
		return String.format("%s<%s, %s>", getCollectType(), TypeManager.wrap(type), TypeManager.wrap(value));
	}

	@Override
	public String variableCode() {
		String value = String.format("new %s<>()", getCollectType());
		return CodeFormater.formatVariableWithValue(getFullType(), name, value);
	}

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (TypeManager.isCollectType(type)) {
			throw new IllegalArgumentException("collection type can not be key of map, name = " + name + ", type = " + type);
		}
		if (TypeManager.isCollectType(value)) {
			throw new IllegalArgumentException("collection type can not be value of map, name = " + name + ", value = " + value);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s.putInt(%s.size());", prev, bufferName, name));
		sb.append("\r\n");
		sb.append(String.format("%sfor (java.util.Map.Entry<%s, %s> entry : %s.entrySet()) {", prev, TypeManager.wrap(type), TypeManager.wrap(value), name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.put%s(entry.getKey());", prev, bufferName, TypeManager.operaType(type)));
		} else if (TypeManager.getTypeId(type) == TypeManager.BOOLEAN) {
			sb.append(String.format("%s\twriteBoolean(%s, entry.getKey());", prev, bufferName));
		} else if (TypeManager.getTypeId(type) == TypeManager.STRING) {
			sb.append(String.format("%s\twriteString(%s, entry.getKey());", prev, bufferName));
		} else {
			sb.append(String.format("%s\tentry.getKey().encode(%s);", prev, bufferName));
		}
		sb.append("\r\n");

		if (TypeManager.getTypeId(value) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.put%s(entry.getValue());", prev, bufferName, TypeManager.operaType(value)));
		} else if (TypeManager.getTypeId(value) == TypeManager.BOOLEAN) {
			sb.append(String.format("%s\twriteBoolean(%s, entry.getValue());", prev, bufferName));
		} else if (TypeManager.getTypeId(value) == TypeManager.STRING) {
			sb.append(String.format("%s\twriteString(%s, entry.getValue());", prev, bufferName));
		} else if (TypeManager.getTypeId(value) == TypeManager.BYTES) {
			sb.append(String.format("%s\t%s.putInt(entry.getValue().length);\r\n", prev, bufferName));
			sb.append(String.format("%s\t%s.put(entry.getValue());", prev, bufferName));
		} else {
			sb.append(String.format("%s\tentry.getValue().encode(%s);", prev, bufferName));
		}
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		if (TypeManager.isCollectType(type) || TypeManager.isBytes(type)) {
			throw new IllegalArgumentException("collection type can not be key of map, name = " + name + ", type = " + type);
		}
		if (TypeManager.isCollectType(value)) {
			throw new IllegalArgumentException("collection type can not be value of map, name = " + name + ", value = " + value);
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sint %sLen = %s.getInt();", prev, name, bufferName));
		sb.append("\r\n");
		sb.append(String.format("%sfor (int i = 0; i < %sLen; i++) {", prev, name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s key = %s.get%s();", prev, type, bufferName, TypeManager.operaType(type)));
		} else if (TypeManager.getTypeId(type) == TypeManager.BOOLEAN) {
			sb.append(String.format("%s\tboolean key = readBoolean(%s);", prev, bufferName));
		} else if (TypeManager.getTypeId(type) == TypeManager.STRING) {
			sb.append(String.format("%s\tString key = readString(%s);", prev, bufferName));
		} else {
			sb.append(String.format("%s\t%s key = new %s();\r\n", prev, type, type));
			sb.append(String.format("%s\t\tkey.decode(%s);\r\n", prev, bufferName));
		}
		sb.append("\r\n");

		if (TypeManager.getTypeId(value) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s value = %s.get%s();", prev, value, bufferName, TypeManager.operaType(value)));
		} else if (TypeManager.getTypeId(value) == TypeManager.BOOLEAN) {
			sb.append(String.format("%s\tboolean value = readBoolean(%s);", prev, bufferName));
		} else if (TypeManager.getTypeId(value) == TypeManager.STRING) {
			sb.append(String.format("%s\tString value = readString(%s);", prev, bufferName));
		} else if (TypeManager.getTypeId(value) == TypeManager.BYTES) {
			sb.append(String.format("%s\tint bytesLen = %s.getInt();\r\n", prev, bufferName));
			sb.append(String.format("%s\tbyte[] value = new byte[bytesLen];\r\n", prev));
			sb.append(String.format("%s\t%s.get(value);", prev, bufferName));
		} else {
			sb.append(String.format("%s\t%s value = new %s();\r\n", prev, value, value));
			sb.append(String.format("%s\tvalue.decode(%s);\r\n", prev, bufferName));
		}
		sb.append("\r\n");
		sb.append(String.format("%s\t%s.put(%s, %s);", prev, name, "key", "value"));
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}
}
