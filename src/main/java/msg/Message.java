package msg;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * @author 3rdyeah
 * created on 2020/4/17 15:49
 */
public abstract class Message implements Serializable {

	private ByteBuf byteBuf;
	private Object sender = null;
	private Object receiver = null;
	private Channel channel = null;

	public Message() {
		this.byteBuf = Unpooled.buffer(10);
	}

	public abstract int getMsgId();
	public abstract void process();

	public abstract ByteBuf encode();
	public abstract void encode(ByteBuf out);
	public abstract void decode(ByteBuf in);

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public Object getSender() {
		return sender;
	}

	public void setSender(Object sender) {
		this.sender = sender;
	}

	public Object getReceiver() {
		return receiver;
	}

	public void setReceiver(Object receiver) {
		this.receiver = receiver;
	}

	public ByteBuf getByteBuf() {
		return byteBuf;
	}
}
