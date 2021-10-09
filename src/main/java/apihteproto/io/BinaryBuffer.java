package apihteproto.io;

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

	private BinaryBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public static BinaryBuffer allocate(int capacity) {
		return new BinaryBuffer(capacity);
	}

	public static BinaryBuffer wrap(byte[] array, int offset, int length) {
		return new BinaryBuffer(ByteBuffer.wrap(array, offset, length));
	}

	public static BinaryBuffer wrap(byte[] array) {
		return wrap(array, 0, array.length);
	}

	public boolean readBoolean() {
		return buffer.get() == 1;
	}

	public BinaryBuffer writeBoolean(boolean v) {
		buffer.put((byte) (v ? 1 : 0));
		return this;
	}

	public byte readByte() {
		return buffer.get();
	}

	public BinaryBuffer writeByte(byte v) {
		buffer.put(v);
		return this;
	}

	public short readShort() {
		return buffer.getShort();
	}

	public BinaryBuffer writeShort(short v) {
		buffer.putShort(v);
		return this;
	}

	public int readInt() {
		return buffer.getInt();
	}

	public BinaryBuffer writeInt(int v) {
		buffer.putInt(v);
		return this;
	}

	public long readLong() {
		return buffer.getLong();
	}

	public BinaryBuffer writeLong(long v) {
		buffer.putLong(v);
		return this;
	}

	public float readFloat() {
		return buffer.getFloat();
	}

	public BinaryBuffer writeFloat(float v) {
		buffer.putFloat(v);
		return this;
	}

	public double readDouble() {
		return buffer.getDouble();
	}

	public BinaryBuffer writeDouble(double v) {
		buffer.putDouble(v);
		return this;
	}

	public char readChar() {
		return buffer.getChar();
	}

	public BinaryBuffer writeChar(char v) {
		buffer.putChar(v);
		return this;
	}

	/**
	 * read string from buffer, read string length from buffer first
	 * @return
	 */
	public String readString() {
		int len = buffer.getInt();
		if (len > 0) {
			byte[] contextBytes = new byte[len];
			return buffer.get(contextBytes).toString();
		}
		return null;
	}

	/**
	 * write string to buffer, and write string length to buffer first
	 * @param v
	 * @return
	 */
	public BinaryBuffer writeString(String v) {
		if (v == null) {
			buffer.putInt(0);
			return this;
		}
		int len = v.getBytes().length;
		buffer.putInt(len);
		if (len > 0) {
			buffer.put(v.getBytes());
		}
		return this;
	}

	public byte[] readBytes(int size) {
		byte[] v = new byte[size];
		buffer.get(v);
		return v;
	}

	/**
	 * read bytes from buffer, read bytes length first
	 * @return
	 */
	public byte[] readBytes() {
		int size = buffer.getInt();
		byte[] v = new byte[size];
		buffer.get(v);
		return v;
	}

	/**
	 * direct write bytes to buffer
	 * @param v
	 * @return
	 */
	public BinaryBuffer writeBytes(byte[] v) {
		buffer.put(v);
		return this;
	}

	/**
	 * write bytes to buffer, and write bytes len to buffer first
	 * @param v
	 * @return
	 */
	public BinaryBuffer writeBytesWithSize(byte[] v) {
		buffer.putInt(v.length);
		buffer.put(v);
		return this;
	}

	public final BinaryBuffer mark() {
		buffer.mark();
		return this;
	}

	public final BinaryBuffer rewind() {
		buffer.rewind();
		return this;
	}

	public final int remaining() {
		return buffer.remaining();
	}

	public final boolean hasRemaining() {
		return buffer.hasRemaining();
	}

	public final BinaryBuffer flip() {
		buffer.flip();
		return this;
	}

	public final boolean hasArray() {
		return buffer.hasArray();
	}

	public final byte[] array() {
		return buffer.array();
	}

	public final int arrayOffset() {
		return buffer.arrayOffset();
	}

	public boolean isDirect() {
		return buffer.isDirect();
	}

	public final int position() {
		return buffer.position();
	}

	public final BinaryBuffer position(int pos) {
		buffer.position(pos);
		return this;
	}

	public BinaryBuffer compact() {
		buffer.compact();
		return this;
	}

	public BinaryBuffer slice() {
		return new BinaryBuffer(buffer.slice());
	}

	public BinaryBuffer duplicate() {
		return new BinaryBuffer(buffer.duplicate());
	}

	public BinaryBuffer asReadOnlyBuffer() {
		return new BinaryBuffer(buffer.asReadOnlyBuffer());
	}

	public final boolean isReadOnly() {
		return buffer.isReadOnly();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public int hashCode() {
		return buffer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BinaryBuffer)) {
			return false;
		}
		return buffer.equals(((BinaryBuffer) obj).buffer);
	}

	@Override
	public String toString() {
		return buffer.toString();
	}
}
