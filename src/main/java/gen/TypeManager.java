package gen;

import gen.type.AListType;
import gen.type.BasicType;
import gen.type.BytesType;
import gen.type.ClassType;
import gen.type.LListType;
import gen.type.MapType;
import gen.type.SetType;
import gen.type.Type;
import org.dom4j.Element;

/**
 * @author 3rdyeah
 * created on 2021/8/4 17:00
 */
public class TypeManager {
	public static final int BASIC = 1;
	public static final int CLASS = 2;
	public static final int COLLECT_LIST = 3;
	public static final int COLLECT_SET = 4;
	public static final int COLLECT_MAP = 5;
	public static final int BYTES = 5;

	public static String wrap(String basic) {
		try {
			if (basic.equalsIgnoreCase("byte")) {
				return "Byte";
			} else if (basic.equalsIgnoreCase("boolean")) {
				return "Boolean";
			} else if (basic.equalsIgnoreCase("short")) {
				return "Short";
			} else if (basic.equalsIgnoreCase("int")) {
				return "Integer";
			} else if (basic.equalsIgnoreCase("long")) {
				return "Long";
			} else if (basic.equalsIgnoreCase("float")) {
				return "Float";
			} else if (basic.equalsIgnoreCase("double")) {
				return "Double";
			} else if (basic.equalsIgnoreCase("char")) {
				return "Char";
			} else if (basic.equalsIgnoreCase("bytes")) {
				return "byte[]";
			}
			return basic;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String operaType(String basic) {
		try {
			if (basic.equalsIgnoreCase("int")) {
				return "Int";
			} else if (basic.equalsIgnoreCase("byte")) {
				return "";
			} else if (basic.equalsIgnoreCase("bytes")) {
				return "";
			} else {
				return wrap(basic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getTypeOrder(Type var) {
		if (var instanceof BasicType) {
			return 1;
		}
		if (var instanceof ClassType) {
			return 2;
		}
		if (var instanceof AListType) {
			return 3;
		}
		if (var instanceof MapType) {
			return 4;
		}
		if (var instanceof BytesType) {
			return 5;
		}
		return 0;
	}

	public static int getTypeId(String typeName) {
		if (isBasicType(typeName) || isWrapperType(typeName)) {
			return BASIC;
		} else if (isList(typeName)) {
			return COLLECT_LIST;
		} else if (isSet(typeName)) {
			return COLLECT_SET;
		} else if (isMap(typeName)) {
			return COLLECT_MAP;
		} else if (isBytes(typeName)) {
			return BYTES;
		} else {
			return CLASS;
		}
	}

	public static Type getTypeObj(Element element) {
		String typeName = element.attributeValue("type");
		if (typeName == null) {
			throw new RuntimeException("Get type object failed, type not defined, element qname = " + element.getQName());
		}
		String attrName = element.attributeValue("name");
		String attrValue = element.attributeValue("value");

		if (isBasicType(typeName) || isWrapperType(typeName)) {
			BasicType type = new BasicType();
			type.type = typeName;
			type.name = attrName;
			return type;
		} else if (typeName.equalsIgnoreCase("alist")) {
			AListType type = new AListType();
			type.type = attrValue;
			type.name = attrName;
			return type;
		} else if (typeName.equalsIgnoreCase("llist")) {
			LListType type = new LListType();
			type.type = attrValue;
			type.name = attrName;
			return type;
		} else if (isSet(typeName)) {
			SetType type = new SetType();
			type.type = attrValue;
			type.name = attrName;
			return type;
		} else if (isMap(typeName)) {
			MapType type = new MapType();
			type.type = element.attributeValue("key");
			type.value = attrValue;
			type.name = attrName;
			return type;
		} else if (isBytes(typeName)) {
			BytesType type = new BytesType();
			type.name = attrName;
			return type;
		} else {
			return null;
		}
	}

	public static boolean isWrapperType(String type) {
		return type.equalsIgnoreCase("Byte")
				|| type.equalsIgnoreCase("Boolean")
				|| type.equalsIgnoreCase("Short")
				|| type.equalsIgnoreCase("Int")
				|| type.equalsIgnoreCase("Long")
				|| type.equalsIgnoreCase("Float")
				|| type.equalsIgnoreCase("Double")
				|| type.equalsIgnoreCase("Char")
				|| type.equalsIgnoreCase("String");
	}

	public static boolean isBasicType(String type) {
		return type.equalsIgnoreCase("byte")
				|| type.equalsIgnoreCase("boolean")
				|| type.equalsIgnoreCase("short")
				|| type.equalsIgnoreCase("int")
				|| type.equalsIgnoreCase("long")
				|| type.equalsIgnoreCase("float")
				|| type.equalsIgnoreCase("double")
				|| type.equalsIgnoreCase("char")
				|| type.equalsIgnoreCase("String");
	}

	public static boolean isList(String type) {
		return type.equalsIgnoreCase("alist")
				|| type.equalsIgnoreCase("llist");
	}

	public static boolean isBytes(String type) {
		return type.equalsIgnoreCase("bytes");
	}

	public static boolean isSet(String type) {
		return type.equalsIgnoreCase("set");
	}

	public static boolean isMap(String type) {
		return type.equalsIgnoreCase("map");
	}

	public static boolean isCollectType(String type) {
		return isList(type)
				|| isSet(type)
				|| isMap(type);
	}

}
