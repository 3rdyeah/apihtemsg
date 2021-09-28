package apihteproto.io;

import java.io.Serializable;

/**
 * @author 3rdyeah
 * created on 2020/4/17 15:49
 */
public abstract class Message implements Serializable {
	public static final int UNKNOWN_SIZE = -1;
	public static final int MAX_SIZE = 16384;

	private BinaryBuffer buffer;

	// Will not encode to buffer
	// eg. you can set extParam to a netty.Channel when you recieve a new Message
	// so that you can response a message by this channel
	private Object extParam = null;

	public Message() {
		this.buffer = BinaryBuffer.allocate(size());
	}

	public abstract int getMsgId();
	public abstract void process();

	public BinaryBuffer encode() {
		return encode(getBuffer());
	}

	public abstract BinaryBuffer encode(BinaryBuffer out);
	public abstract void decode(BinaryBuffer in);

	// if attribute types contain String、array or collection, will set size as MAX_SIZE
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
