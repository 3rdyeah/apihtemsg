package gen;

import java.util.LinkedList;
import java.util.List;

import gen.type.BeanType;
import gen.type.ClassType;
import gen.type.MsgClassType;
import gen.type.Type;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 类型收集器
 *
 * @author 3rdyeah
 * created on 2021/9/14 18:05
 */
public class ClassCollector {
	private List<ClassType> classes = new LinkedList<>();

	public List<ClassType> getClasses() {
		return classes;
	}

	public void collectFromFile(String path) throws DocumentException {
		SAXReader xReader = new SAXReader();
		Document document = xReader.read(path);

		Element root = document.getRootElement();
		collectFromElement(root);
	}

	private void collectFromElement(Element root) throws DocumentException {
		collectClass(root);
		String path = getDir(root);

		List<Element> nss = root.elements("namespace");
		for (Element element : nss) {
			collectClass(element);

			List<Element> subEles = element.elements("include");
			for (Element subEle : subEles) {
				String file = subEle.attributeValue("href");
				if (file == null) {
					continue;
				}
				collectFromFile(path + "/" + file);
			}

			collectFromElement(element);
		}
	}

	private void collectClass(Element ele) {

		List<Element> msgs = ele.elements("msg");
		for (Element msg : msgs) {
			classes.add(ctorClassType(msg));
		}

		List<Element> beans = ele.elements("bean");
		for (Element bean : beans) {
			classes.add(ctorClassType(bean));
		}
	}

	private ClassType ctorClassType(Element element) {
		ClassType classType;

		String className = element.attributeValue("name");
		String qname = element.getQName().getName();
		if (qname.equalsIgnoreCase("msg")) {
			MsgClassType msgType = new MsgClassType();
			msgType.msgId = element.attributeValue("id");

			if (msgType.msgId == null || msgType.msgId.equalsIgnoreCase("")) {
				throw new RuntimeException("Create class type failed, class type = " + qname + ", class name = " + className + ", no msgid");
			}

			classType = msgType;
		} else if (qname.equalsIgnoreCase("bean")) {
			classType = new BeanType();
		} else {
			throw new RuntimeException("Create class type failed, class type = " + qname + ", class name = " + className);
		}
		classType.name = className;
		classType.pack = element.getParent().attributeValue("name");

		List<Element> attr = element.elements("attr");
		for (Element ele : attr) {
			Type attrObj = TypeManager.getTypeObj(ele);
			classType.vars.add(attrObj);
		}

		return classType;
	}

	private String getDir(Element element) {
		if (element == null) {
			return null;
		}
		String path = element.getDocument().getName();
		return path.substring(0, path.lastIndexOf("/"));
	}
}
