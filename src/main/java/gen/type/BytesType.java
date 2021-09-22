package gen.type;

import gen.CodeFormater;

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
	public String encodeCode(String byteBuf, String prev) {
		if (prev == null) {
			prev = "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sif (%s != null && %s.length > 0) {\r\n", prev, name, name));
		sb.append(String.format("%s\t%s.writeInt(%s.length);\r\n", prev, byteBuf, name));
		sb.append(String.format("%s\t%s.writeBytes(%s);\r\n", prev, byteBuf, name));
		sb.append(String.format("%s}", prev));
		return sb.toString();
	}

	@Override
	public String decodeCode(String byteBuf, String prev) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sif (%s.readableBytes() > 0) {\r\n", prev, byteBuf));
		sb.append(String.format("%s\tint %sLen = %s.readInt();\r\n", prev, name, byteBuf));
		sb.append(String.format("%s\tbyte[] %s = new byte[%sLen];\r\n", prev, name, name));
		sb.append(String.format("%s\t%s.readBytes(%s);", prev, byteBuf, name));
		sb.append(String.format("%s}", prev));
		return sb.toString();
	}
}
