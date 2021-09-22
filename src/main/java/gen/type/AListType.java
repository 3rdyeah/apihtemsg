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
	public String encodeCode(String byteBuf, String prev) {
		if (TypeManager.isCollectType(type)) {
			throw new IllegalArgumentException("collection type can not be value, name = " + name + ", type = " + type);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s.writeInt(%s.size());", prev, byteBuf, name));
		sb.append("\r\n");
		sb.append(String.format("%sfor (%s var : %s) {", prev, type, name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.write%s(%s);", prev, byteBuf, TypeManager.operaType(type), "var"));
		} else if (TypeManager.getTypeId(type) == TypeManager.BYTES) {
			sb.append(String.format("%s\t%s.writeInt(%s.length);\r\n", prev, byteBuf, name));
			sb.append(String.format("%s\t%s.writeBytes(%s);", prev, byteBuf, name));
		} else {
			sb.append(String.format("%s\tvar.encode(%s)", prev, byteBuf));
		}
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}

	@Override
	public String decodeCode(String byteBuf, String prev) {
		if (TypeManager.isCollectType(type)) {
			throw new IllegalArgumentException("collection type can not be value, name = " + name + ", type = " + type);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sint %sLen = %s.readInt();", prev, name, byteBuf));
		sb.append("\r\n");
		sb.append(String.format("%sfor (int i = 0; i < %sLen; i++) {", prev, name));
		sb.append("\r\n");
		if (TypeManager.getTypeId(type) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s value = %s.read%s();\r\n", prev, type, byteBuf, TypeManager.operaType(type)));
		} else if (TypeManager.getTypeId(type) == TypeManager.BYTES) {
			sb.append(String.format("%s\tint bytesLen = %s.readInt();\r\n", prev, byteBuf));
			sb.append(String.format("%s\tbyte[] value = new byte[bytesLen];\r\n", prev));
			sb.append(String.format("%s\t%s.readBytes(value);", prev, byteBuf));
		} else {
			sb.append(String.format("%s\t%s value = new %s();\r\n", prev, type, type));
			sb.append(String.format("%s\t%s.decode(%s);\r\n", prev, type, byteBuf));
		}
		sb.append(String.format("%s\t%s.add(value);\r\n", prev, name));
		sb.append(prev).append("}");
		return sb.toString();
	}
}
