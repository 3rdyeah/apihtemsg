package gen.type;

import gen.CodeFormater;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:45
 */
public class BeanType extends ClassType {

	public BeanType() {
		this.extclass = "Coder";
	}

	@Override
	public String importCode() {
		return CodeFormater._IMPORT_BYTEBUFFER + "\r\n" + CodeFormater._IMPORT_CODER;
	}

	@Override
	public String classCode() {
		return String.format("package %s;\r\n\r\n", pack) +
				importCode() +
				"\r\n\r\n" +
				defineClassCode() +
				"\r\n" +
				variableCode() +
				"\r\n\r\n" +
				setterCode() +
				"\r\n\r\n" +
				getterCode() +
				"\r\n\r\n" +
				String.format(CodeFormater._MSG_ENCODE, encodeCode("out", "")) +
				"\r\n\r\n" +
				String.format(CodeFormater._MSG_DECODE, decodeCode("in", "")) +
				"\r\n\r\n" +
				sizeCode() +
				"\r\n\r\n" +
				"}\r\n";
	}

	@Override
	public String sizeCode() {
		return String.format(
				"\tpublic int size() {\r\n" +
				"\t\treturn %s;\r\n" +
				"\t}",
				size());
	}
}
