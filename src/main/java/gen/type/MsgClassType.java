package gen.type;

import gen.CodeFormater;

/**
 * @author 3rdyeah
 * created on 2021/8/4 18:13
 */
public class MsgClassType extends ClassType {
	public String msgId;

	public MsgClassType() {
		this.extclass = "Message";
	}

	@Override
	public String importCode() {
		return CodeFormater._IMPORT_MSG;
	}

	@Override
	public String encodeCode(String byteBuf, String prev) {
		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			sb.append(var.encodeCode(byteBuf, "\t\t"));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	@Override
	public String decodeCode(String byteBuf, String prev) {
		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			sb.append(var.decodeCode(byteBuf, "\t\t"));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	@Override
	public String classCode() {

		String variableCode = variableCode();
		if (!variableCode.equalsIgnoreCase("")) {
			variableCode = variableCode + "\r\n\r\n";
		}
		String setterCode = setterCode();
		if (!setterCode.equalsIgnoreCase("")) {
			setterCode = setterCode + "\r\n\r\n";
		}
		String getterCode = getterCode();
		if (!getterCode.equalsIgnoreCase("")) {
			getterCode = getterCode + "\r\n\r\n";
		}

		return String.format("package %s;\r\n\r\n", pack) +
				extImport() +
				"\r\n" +
				CodeFormater._NO_EDIT_BEGIN +
				"\r\n" +
				importCode() +
				"\r\n\r\n" +
				String.format("public class %s extends %s {", name, extclass) +
				"\r\n" +
				CodeFormater._NO_EDIT_END +
				"\r\n" +
				processCode() +
				"\r\n" +
				CodeFormater._NO_EDIT_BEGIN +
				"\r\n\r\n" +
				String.format("\tpublic static final int MSG_ID = %s;", msgId) +
				"\r\n\r\n" +
				CodeFormater._MSG_ID_GETTER +
				"\r\n\r\n" +
				variableCode +
				setterCode +
				getterCode +
				CodeFormater._MSG_ENCODE_EXT +
				"\r\n\r\n" +
				String.format(CodeFormater._MSG_ENCODE, encodeCode("out", "")) +
				"\r\n\r\n" +
				String.format(CodeFormater._MSG_DECODE, decodeCode("in", "")) +
				"\r\n\r\n" +
				sizeCode() +
				"\r\n}\r\n" +
				CodeFormater._NO_EDIT_END;
	}
}
