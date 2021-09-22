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
	public String encodeCode(String byteBuf, String prev) {
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
		sb.append(String.format("%s%s.writeInt(%s.size());", prev, byteBuf, name));
		sb.append("\r\n");
		sb.append(String.format("%sfor (java.util.Map.Entry<%s, %s> entry : %s.entrySet()) {", prev, TypeManager.wrap(type), TypeManager.wrap(value), name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.write%s(entry.getKey());", prev, byteBuf, TypeManager.operaType(type)));
		} else {
			sb.append(String.format("%s\tentry.getKey().encode(%s)", prev, byteBuf));
		}
		sb.append("\r\n");

		if (TypeManager.getTypeId(value) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.write%s(entry.getValue());", prev, byteBuf, TypeManager.operaType(value)));
		} else if (TypeManager.getTypeId(value) == TypeManager.BYTES) {
			sb.append(String.format("%s\t%s.writeInt(entry.getValue().length);\r\n", prev, byteBuf));
			sb.append(String.format("%s\t%s.writeBytes(entry.getValue());", prev, byteBuf));
		} else {
			sb.append(String.format("%s\tentry.getValue().encode(%s)", prev, byteBuf));
		}
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}

	@Override
	public String decodeCode(String byteBuf, String prev) {
		if (TypeManager.isCollectType(type) || TypeManager.isBytes(type)) {
			throw new IllegalArgumentException("collection type can not be key of map, name = " + name + ", type = " + type);
		}
		if (TypeManager.isCollectType(value)) {
			throw new IllegalArgumentException("collection type can not be value of map, name = " + name + ", value = " + value);
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sint %sLen = %s.readInt();", prev, name, byteBuf));
		sb.append("\r\n");
		sb.append(String.format("%sfor (int i = 0; i < %sLen; i++) {", prev, name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s key = %s.read%s();", prev, type, byteBuf, TypeManager.operaType(type)));
		} else {
			sb.append(String.format("%s\t%s key = new %s();\r\n", prev, type, type));
			sb.append(String.format("%s\t\tkey.decode(%s);\r\n", prev, byteBuf));
		}
		sb.append("\r\n");

		if (TypeManager.getTypeId(value) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s value = %s.read%s();", prev, value, byteBuf, TypeManager.operaType(value)));
		} else if (TypeManager.getTypeId(value) == TypeManager.BYTES) {
			sb.append(String.format("%s\tint bytesLen = %s.readInt();\r\n", prev, byteBuf));
			sb.append(String.format("%s\tbyte[] value = new byte[bytesLen];\r\n", prev));
			sb.append(String.format("%s\t%s.readBytes(value);", prev, byteBuf));
		} else {
			sb.append(String.format("%s\t%s value = new %s();\r\n", prev, value, value));
			sb.append(String.format("%s\tvalue.decode(%s);\r\n", prev, byteBuf));
		}
		sb.append("\r\n");
		sb.append(String.format("%s\t%s.put(%s, %s);", prev, name, "key", "value"));
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}
}
