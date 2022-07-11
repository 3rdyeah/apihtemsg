package apihtemsg.io;

import java.io.Serializable;

/**
 * @author 3rdyeah
 * created on 2020/4/17 15:49
 */
public abstract class Message implements Serializable, Runnable {
	public static final int UNKNOWN_SIZE = -1;
	public static final int MAX_SIZE = 16384;

	private final BinaryBuffer buffer;

	// Will not encode to buffer
	// eg. you can set extParam to a netty.Channel when you recieve a new Message
	// so that you can response a message by this channel
	transient private Object extParam = null;

	public Message() {
		this.buffer = BinaryBuffer.allocate(size());
	}

	public abstract int getMsgId();
	public abstract void process();

	@Override
	public void run() {
		try {
			process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Encode whole message, contain msgid
	 * @return Message buffer
	 */
	public BinaryBuffer encode() {
		BinaryBuffer buffer = getBuffer();
		buffer.writeInt(getMsgId());
		return _encode(getBuffer());
	}

	/**
	 * Encode attributes, without msgid
	 * @param out Output buffer
	 * @return Message attributes buffer
	 */
	protected abstract BinaryBuffer _encode(BinaryBuffer out);

	/**
	 * Decode buffer, contain msgid
	 * @param in You can convert byte[] to BinaryBuffer with BinaryBuffer.wrap(byte[] bytes);
	 */
	public void decode(BinaryBuffer in) {
		int msgId = in.readInt();
		if (msgId <= 0) {
			throw new RuntimeException("decode message error, msgid invalid, msgid = " + msgId);
		}
		_decode(in);
	}

	/**
	 * Decode attributes, without msgid
	 * @param in Input buffer
	 */
	protected abstract void _decode(BinaryBuffer in);

	/**
	 * If attribute types contain String、array or collection, will set size as MAX_SIZE
	 * @return Size as byte
	 */
	public abstract int size();

	public BinaryBuffer getBuffer() {
		return buffer;
	}

	public Object getExtParam() {
		return extParam;
	}

	public void setExtParam(Object extParam) {
		this.extParam = extParam;
	}
}
