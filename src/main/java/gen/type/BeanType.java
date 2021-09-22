package gen.type;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:45
 */
public class BeanType extends ClassType {

	@Override
	public String classCode() {
		return String.format("package %s;\r\n\r\n", pack) +
				String.format("public class %s extends %s {\r\n", name, extclass) +
				variableCode() +
				"\r\n" +
				setterCode() +
				"\r\n" +
				getterCode() +
				"}\r\n";
	}
}
