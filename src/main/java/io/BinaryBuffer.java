package io;

import java.nio.ByteBuffer;

/**
 * @author 3rdyeah
 * created on 2021/9/24 10:23
 */
public class BinaryBuffer {
	private ByteBuffer buffer;

	private BinaryBuffer(int capacity) {
		this.buffer = ByteBuffer.allocate(capacity);
	}

	public static BinaryBuffer allocate(int capacity) {
		return new BinaryBuffer(capacity);
	}

	public boolean readBoolean() {
		return buffer.get() == 1;
	}

	public void writeBoolean(boolean v) {
		buffer.put((byte) (v ? 1 : 0));
	}

	public byte readByte() {
		return buffer.get();
	}

	public void writeByte(byte v) {
		buffer.put(v);
	}

	public short readShort() {
		return buffer.getShort();
	}

	public void writeShort(short v) {
		buffer.putShort(v);
	}

	public int readInt() {
		return buffer.getInt();
	}

	public void writeInt(int v) {
		buffer.putInt(v);
	}

	public long readLong() {
		return buffer.getLong();
	}

	public void writeLong(long v) {
		buffer.putLong(v);
	}

	public float readFloat() {
		return buffer.getFloat();
	}

	public void writeFloat(float v) {
		buffer.putFloat(v);
	}

	public double readDouble() {
		return buffer.getDouble();
	}

	public void writeDouble(double v) {
		buffer.putDouble(v);
	}

	public char readChar() {
		return buffer.getChar();
	}

	public void writeChar(char v) {
		buffer.putChar(v);
	}

	public String readString() {
		int len = buffer.getInt();
		if (len > 0) {
			byte[] contextBytes = new byte[len];
			return buffer.get(contextBytes).toString();
		}
		return null;
	}

	public void writeString(String v) {
		if (v == null) {
			buffer.putInt(0);
			return;
		}
		int len = v.getBytes().length;
		buffer.putInt(len);
		if (len > 0) {
			buffer.put(v.getBytes());
		}
	}

	public byte[] readBytes(int size) {
		byte[] v = new byte[size];
		buffer.get(v);
		return v;
	}

	public byte[] readBytes() {
		int size = buffer.getInt();
		byte[] v = new byte[size];
		buffer.get(v);
		return v;
	}

	public void writeBytes(byte[] v) {
		buffer.putInt(v.length);
		buffer.put(v);
	}

	public int remaining() {
		return buffer.remaining();
	}
}
