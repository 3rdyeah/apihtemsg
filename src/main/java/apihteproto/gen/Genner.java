package apihteproto.gen;

import java.util.List;

import apihteproto.gen.type.ClassType;
import apihteproto.gen.type.MsgClassType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author 3rdyeah
 * created on 2021/9/26 15:52
 */
public class Genner {
	private final String targetDir;
	private final String reflectorPack;

	public Genner(String targetDir, String reflectorPack) {
		targetDir = targetDir.replace("\\", "/");
		if (targetDir.endsWith("/")) {
			targetDir = targetDir.substring(0, targetDir.length() - 1);
		}

		this.targetDir = targetDir;
		this.reflectorPack = reflectorPack;
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
		StringBuilder cases = new StringBuilder();
		for (ClassType aClass : classes) {
			makeJava(aClass);
			if (aClass instanceof MsgClassType) {
				String msgId = ((MsgClassType) aClass).msgId;
				String className = aClass.pack + "." + aClass.name;
				cases.append(String.format(CodeFormater.CASE_RET, msgId, className)).append("\n");
			}
		}

		String reflectorCode = String.format(CodeFormater.REFLECTOR, reflectorPack, cases);
		String path = targetDir + "/" + reflectorPack.replace(".", "/") + "/" + "MessageReflector.java";
		File reflectorFile = new File(path);
		if (!reflectorFile.exists()) {
			try {
				File parent = reflectorFile.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new RuntimeException("create file failed, make dirs error, file = " + reflectorFile.getAbsolutePath());
				}
				if (!reflectorFile.createNewFile()) {
					throw new RuntimeException("create file failed, file = " + reflectorFile.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try (FileOutputStream fos = new FileOutputStream(reflectorFile)) {
			fos.write(reflectorCode.getBytes());
			fos.flush();
			System.out.println("Output file " + reflectorFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
