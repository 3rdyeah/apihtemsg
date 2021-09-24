package io;

import java.io.Serializable;

/**
 * @author 3rdyeah
 * created on 2020/4/17 15:49
 */
public abstract class Message implements Serializable {

	private BinaryBuffer buffer;

	// Message sender should set it's info to this attribute if need a response from target
	private Object sender = null;

	// Message sender set receiver info to this attribute
	// if you have a transit server, transit server can transmit message by this attribute
	private Object receiver = null;

	// eg. you can set msgParam to a netty.Channel when you recieve a new Message
	// so that you can response a message by this channel
	private Object msgParam = null;

	public Message() {
		this.buffer = BinaryBuffer.allocate(size());
	}

	public abstract int getMsgId();
	public abstract void process();

	public abstract BinaryBuffer encode();
	public abstract void encode(BinaryBuffer out);
	public abstract void decode(BinaryBuffer in);
	public abstract int size();

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

	public BinaryBuffer getBuffer() {
		return buffer;
	}

	public Object getMsgParam() {
		return msgParam;
	}

	public void setMsgParam(Object msgParam) {
		this.msgParam = msgParam;
	}
}
