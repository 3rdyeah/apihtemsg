package apihtemsg.io;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import apihtemsg.util.ClassFinder;
import java.lang.reflect.Field;

public class Reflector {
	private final Map<Integer, String> classes = new HashMap<>();
	public final ClassFinder.Callback FINDER_CALLBACK = new Classback();

	public void init(String root) {
		ClassFinder.create()
				.packige(root)
				.registry(ApihteMsg.class, FINDER_CALLBACK)
				.build();
	}

	public Message create(byte[] bytes) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		BinaryBuffer in = BinaryBuffer.wrap(bytes);
		int msgId = in.readInt();
		if (msgId <= 0) {
			throw new IllegalArgumentException("Create message failed, msgid error, msgid = " + msgId);
		}
		in.position(0);

		String className = classes.get(msgId);

		Class<?> clz = Class.forName(className);
		Message message = (Message) clz.newInstance();
		message.decode(in);
		return message;
	}

	public class Classback implements ClassFinder.Callback {
		@Override
		public void callback(Collection<Class<?>> clazzs) {
			for (Class<?> clazz : clazzs) {
				if (clazz.getAnnotation(ApihteMsg.class) == null) {
					continue;
				}
				try {
					Field field = clazz.getDeclaredField("MSG_ID");
					classes.put((Integer) field.get(null), clazz.getName());
				} catch (Exception e) {
					System.out.println("No method named getMsgId in class " + clazz.getName());
					e.printStackTrace();
				}
			}
		}
	}
}
