package gen.type;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import gen.CodeFormater;
import gen.TypeManager;

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
			size += var.size();
		}
		return size;
	}

	public String classCode() {
		return String.format("package %s;\r\n\r\n", pack) +
				extImport() +
				"\r\n" +
				CodeFormater._NO_EDIT_BEGIN +
				"\r\n" +
				defineClassCode() +
				"\r\n" +
				CodeFormater._NO_EDIT_END +
				"\r\n" +
				processCode() +
				"\r\n" +
				CodeFormater._NO_EDIT_BEGIN +
				"\r\n" +
				variableCode() +
				"\r\n\r\n" +
				setterCode() +
				"\r\n\r\n" +
				getterCode() +
				"\r\n\r\n" +
				sizeCode() +
				"\r\n\r\n" +
				"}\r\n" +
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

	public String sizeCode() {
		return String.format(CodeFormater._SIZE, size());
	}
}
