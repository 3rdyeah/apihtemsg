package gen.type;

/**
 * @author 3rdyeah
 * created on 2021/9/23 17:50
 */
public class BooleanType extends BasicType {

	@Override
	public String encodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		return String.format("%swriteBoolean(%s, %s);", prev, bufferName, name);
	}

	@Override
	public String decodeCode(String bufferName, String prev) {
		if (prev == null) {
			prev = "";
		}
		return String.format("%s%s = readBoolean(%s);", prev, name, bufferName);
	}

	@Override
	public int size() {
		return Byte.SIZE;
	}
}
