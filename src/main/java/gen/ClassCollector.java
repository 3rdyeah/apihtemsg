package gen;

import java.util.LinkedList;
import java.util.List;

import gen.type.BeanType;
import gen.type.ClassType;
import gen.type.MsgClassType;
import gen.type.Type;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	private String rootDir;

	public ClassCollector(String rootDir) {
		this.rootDir = rootDir;
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

	private void makeJava(ClassType classType) {
		String path = rootDir + "/" + classType.pack.replace(".", "/") + "/" + classType.name + ".java";

		File file = new File(path);
		if (file.exists()) {
			byte[] bytes = new byte[(int)file.length()];

			try (FileInputStream fis = new FileInputStream(file)) {
				if (fis.read(bytes) == -1) {
					throw new RuntimeException("read context failed, file = " + file.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String context = new String(bytes);
			classType.processCode = subString(context, CodeFormater._EDIT_BEGIN, CodeFormater._EDIT_END);
			classType.extImport = subString(context, CodeFormater._EXT_IMPORT_BEGIN, CodeFormater._EXT_IMPORT_END);
			if (!classType.extImport.contains("import")) {
				classType.extImport = null;
			}
		} else {
			try {
				File parent = file.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new RuntimeException("create file failed, make dirs error, file = " + file.getAbsolutePath());
				}
				if (!file.createNewFile()) {
					throw new RuntimeException("create file failed, file = " + file.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try (FileOutputStream fos = new FileOutputStream(file)) {
			byte[] bytes = classType.classCode().getBytes();
			fos.write(bytes);
			fos.flush();
			System.out.println("Output file " + file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String subString(String src, String begin, String end) {
		int head = src.indexOf(begin);
		int tail = src.indexOf(end) + end.length();
		return src.substring(head, tail);
	}

	public void gen() {
		if (classes.size() <= 0) {
			return;
		}
		for (ClassType aClass : classes) {
			makeJava(aClass);
		}
	}
}
