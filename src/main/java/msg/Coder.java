package msg;

import java.nio.ByteBuffer;

/**
 * @author 3rdyeah
 * created on 2021/9/23 18:26
 */
public class Coder {

	public String readString(ByteBuffer in) {
		int len = in.getInt();
		if (len > 0) {
			byte[] contextBytes = new byte[len];
			return in.get(contextBytes).toString();
		}
		return null;
	}

	public void writeString(ByteBuffer out, String v) {
		if (v == null || v.equalsIgnoreCase("")) {
			out.putInt(0);
			return;
		}
		int len = v.getBytes().length;
		out.putInt(len);
		if (len > 0) {
			out.put(v.getBytes());
		}
	}

	public boolean readBoolean(ByteBuffer in) {
		return in.get() == 1;
	}

	public void writeBoolean(ByteBuffer out, boolean v) {
		out.put((byte) (v ? 1 : 0));
	}
}
