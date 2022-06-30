package apihtemsg.gen;

/**
 * @author 3rdyeah
 * created on 2021/8/5 10:16
 */
public class MsgGen {
	public static String rootXml = "./xml/xmsg/root.xml";
	public static String targetDir = "./src/main/java";

	public static void gen(String rootXml, String targetDir) {
		try {
			ClassCollector collector = new ClassCollector();
			collector.collectFromFile(rootXml);

			Genner genner = new Genner(targetDir);
			genner.gen(collector.getClasses());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String rootFile = MsgGen.rootXml;
		String targetDir = MsgGen.targetDir;

		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("-root")) {
				rootFile = args[i+1];
			} else if (args[i].equals("-target")) {
				targetDir = args[i+1];
			}
		}

		gen(rootFile, targetDir);
	}
}
