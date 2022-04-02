package apihtemsg.gen.type;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import apihtemsg.gen.CodeFormater;
import apihtemsg.gen.TypeManager;
import apihtemsg.io.Message;

/**
 * @author 3rdyeah
 * created on 2021/8/4 17:48
 */
public class ClassType implements Type {
	public String pack;
	public String name;
	public String extclass;
	public String extImport;
	public String processCode;

	public Set<Type> vars = new TreeSet<>(new Comparator<Type>() {
		@Override
		public int compare(Type o1, Type o2) {
			int ret = TypeManager.getTypeOrder(o1) - TypeManager.getTypeOrder(o2);
			if (ret == 0) {
				if (o1.hashCode() == o2.hashCode()) {
					return 0;
				}
				return o1.hashCode() < o2.hashCode() ? -1 : 1;
			}
			return ret;
		}
	});

	@Override
	public String variableCode() {
		int index = 0;
		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			index++;
			sb.append(var.variableCode());
			if (index < vars.size()) {
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	@Override
	public String setterCode() {
		int index = 0;
		StringBuilder sb = new StringBuilder();
		Set<Type> setVars = new HashSet<>();
		for (Type var : vars) {
			if (var instanceof CollectType) {
				continue;
			}
			setVars.add(var);
		}
		for (Type var : setVars) {
			index++;
			sb.append(var.setterCode());
			if (index < setVars.size()) {
				sb.append("\r\n\r\n");
			}
		}
		return sb.toString();
	}

	@Override
	public String getterCode() {
		int index = 0;
		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			index++;
			sb.append(var.getterCode());
			if (index < vars.size()) {
				sb.append("\r\n\r\n");
			}
		}
		return sb.toString();
	}

	public String importCode() {
		if (extclass == null || extclass.equalsIgnoreCase("")) {
			return "";
		}
		return "import" + extclass;
	}

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			String prevTmp = prev + "\t\t";
			sb.append(var.encodeCode(bufferName, prevTmp));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			String prevTmp = prev + "\t\t";
			sb.append(var.decodeCode(bufferName, prevTmp));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	@Override
	public int size() {
		int size = 0;
		for (Type var : vars) {
			if (var.size() <= 0) {
				return -1;
			}
			size += var.size();
		}
		return size;
	}

	public String classCode() {
		return String.format("package %s;\r\n\r\n", pack) +
				extImport() +
				"\n" +
				CodeFormater._NO_EDIT_BEGIN +
				"\n" +
				defineClassCode() +
				"\n" +
				CodeFormater._NO_EDIT_END +
				"\n" +
				processCode() +
				"\n" +
				CodeFormater._NO_EDIT_BEGIN +
				"\n" +
				variableCode() +
				"\n\n" +
				setterCode() +
				"\n\n" +
				getterCode() +
				"\n\n" +
				hashcode() +
				"\n\n" +
				sizeCode() +
				"\n\n" +
				"}\n" +
				CodeFormater._NO_EDIT_END;
	}

	public String extImport() {
		if (extImport != null) {
			return extImport;
		} else {
			return CodeFormater._EXT_IMPORT_BEGIN + "\r\n\r\n" + CodeFormater._EXT_IMPORT_END;
		}
	}

	public String processCode() {
		if (processCode != null) {
			return processCode;
		} else {
			return CodeFormater._FUNC_PROCESS;
		}
	}

	public String defineClassCode() {
		if (extclass == null || extclass.equalsIgnoreCase("")) {
			return String.format("public class %s {", name);
		} else {
			return String.format("public class %s extends %s {", name, extclass);
		}
	}

	public String hashcode() {
		if (vars.size() <= 0) {
			return CodeFormater.HASH_CODE_ZEOR;
		}

		StringBuilder sb = new StringBuilder();
		for (Type var : vars) {
			if (var instanceof BasicType) {
				String varType = ((BasicType) var).type;
				String varName = ((BasicType) var).name;
				if (varType.equalsIgnoreCase("string")) {
					sb.append("\t\th += ").append(varName).append(".hashCode();\n");
				} else if (varType.equalsIgnoreCase("boolean")) {
					sb.append("\t\th += ").append(varName).append(" ? 1 : 0;\n");
				} else {
					sb.append("\t\th += ").append(varName).append(";\n");
				}
			} else {
				sb.append("\t\th += ").append(var.getName()).append(".hashCode();\n");
			}
		}
		return String.format(CodeFormater.HASH_CODE, sb);
	}

	public String sizeCode() {
		int size = size();
		if (size <= Message.UNKNOWN_SIZE) {
			return String.format(CodeFormater._SIZE, "MAX_SIZE");
		}
		return String.format(CodeFormater._SIZE, size);
	}

	@Override
	public String getName() {
		return name;
	}
}
