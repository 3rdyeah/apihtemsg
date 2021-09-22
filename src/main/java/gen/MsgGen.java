package gen;

/**
 * @author 3rdyeah
 * created on 2021/8/5 10:16
 */
public class MsgGen {
	public static String rootFile = "./protocol/xmsg/root.xml";
	public static String targetDir = "./src/main/java";

	public static void main(String[] args) throws Exception {
		String rootFile = MsgGen.rootFile;
		String targetDir = MsgGen.targetDir;

		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("-root")) {
				rootFile = args[i+1];
			} else if (args[i].equals("-target")) {
				targetDir = args[i+1];
			}
		}

		ClassCollector collector = new ClassCollector(targetDir);
		collector.collectFromFile(rootFile);
		collector.gen();
	}
}
