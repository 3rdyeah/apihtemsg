package gen;

import java.util.List;

import gen.type.ClassType;
import gen.type.MsgClassType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author 3rdyeah
 * created on 2021/9/26 15:52
 */
public class Genner {
	private String targetDir;

	public Genner(String targetDir) {
		this.targetDir = targetDir;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	private void makeJava(ClassType classType) {
		String path = targetDir + "/" + classType.pack.replace(".", "/") + "/" + classType.name + ".java";

		File file = new File(path);
		if (!file.exists()) {
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
		} else if (classType instanceof MsgClassType) {
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

	public void gen(List<ClassType> classes) {
		if (classes == null || classes.size() <= 0) {
			return;
		}
		for (ClassType aClass : classes) {
			makeJava(aClass);
		}
	}
}
