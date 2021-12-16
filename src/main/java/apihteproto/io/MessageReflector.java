package apihteproto.io;

import java.util.HashMap;
import java.util.Map;

import apihteproto.gen.ClassCollector;
import apihteproto.gen.type.ClassType;
import apihteproto.gen.type.MsgClassType;
import org.dom4j.DocumentException;

/**
 * @author changsong
 * created on 2021/12/16 17:35
 */
public class MessageReflector {
	private final Map<Integer, String> msgNames = new HashMap<>();

	public void init(String rootXml) throws DocumentException {
		ClassCollector collector = new ClassCollector();
		collector.collectFromFile(rootXml);

		for (ClassType aClass : collector.getClasses()) {
			if (aClass instanceof MsgClassType) {
				msgNames.put(Integer.parseInt(((MsgClassType) aClass).msgId), aClass.pack + "." + aClass.name);
			}
		}
	}

	public Message create(byte[] bytes) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		BinaryBuffer in = BinaryBuffer.wrap(bytes);
		int msgId = in.readInt();
		if (msgId <= 0) {
			throw new IllegalAccessException("Create message failed, msgid error, msgid = " + msgId);
		}
		in.position(0);

		String className = msgNames.get(msgId);
		if (className == null) {
			return null;
		}
		Class<?> clz = Class.forName(className);
		Message message = (Message) clz.newInstance();
		message.decode(in);
		return message;
	}
}
