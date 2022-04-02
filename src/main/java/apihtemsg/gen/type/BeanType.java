package apihtemsg.gen.type;

import apihtemsg.gen.CodeFormater;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:45
 */
public class BeanType extends ClassType {

	@Override
	public String importCode() {
		return CodeFormater._IMPORT_BINARYBUFFER;
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
				hashcode() +
				"\r\n\r\n" +
				sizeCode() +
				"\r\n\r\n" +
				"}\r\n";
	}
}
