package apihtemsg.gen.type;

import apihtemsg.gen.CodeFormater;
import apihtemsg.gen.TypeManager;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:45
 */
public class AListType extends CollectType {

	public String getCollectType() {
		return "java.util.ArrayList";
	}

	public String getFullType() {
		return String.format("%s<%s>", getCollectType(), TypeManager.wrap(this.value));
	}

	@Override
	public String variableCode() {
		String value = String.format("new %s<>()", getCollectType());
		return CodeFormater.formatVariableWithValue(getFullType(), name, value);
	}

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (TypeManager.isCollectType(value)) {
			throw new IllegalArgumentException("collection type can not be value, name = " + name + ", type = " + value);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s.writeInt(%s.size());", prev, bufferName, name));
		sb.append("\r\n");
		sb.append(String.format("%sfor (%s var : %s) {", prev, value, name));
		sb.append("\r\n");

		if (TypeManager.getTypeId(value) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s.write%s(var);", prev, bufferName, TypeManager.operaType(value)));
		} else if (TypeManager.getTypeId(value) == TypeManager.BYTES) {
			sb.append(String.format("%s\t%s.writeBytesWithSize(var);\r\n", prev, bufferName));
		} else {
			sb.append(String.format("%s\tvar._encode(%s);", prev, bufferName));
		}
		sb.append("\r\n");
		sb.append(prev).append("}");
		return sb.toString();
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		if (TypeManager.isCollectType(value)) {
			throw new IllegalArgumentException("collection type can not be value, name = " + name + ", type = " + value);
		}
		if (prev == null) {
			prev = "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sint %sLen = %s.readInt();", prev, name, bufferName));
		sb.append("\r\n");
		sb.append(String.format("%sfor (int i = 0; i < %sLen; i++) {", prev, name));
		sb.append("\r\n");
		if (TypeManager.getTypeId(value) == TypeManager.BASIC) {
			sb.append(String.format("%s\t%s value = %s.read%s();\r\n", prev, TypeManager.varType(value), bufferName, TypeManager.operaType(value)));
		} else if (TypeManager.getTypeId(value) == TypeManager.BYTES) {
			sb.append(String.format("%s\tbyte[] value = %s.readBytes();\r\n", prev, bufferName));
		} else {
			sb.append(String.format("%s\t%s value = new %s();\r\n", prev, TypeManager.varType(value), value));
			sb.append(String.format("%s\tvalue._decode(%s);\r\n", prev, bufferName));
		}
		sb.append(String.format("%s\t%s.add(value);\r\n", prev, name));
		sb.append(prev).append("}");
		return sb.toString();
	}
}
