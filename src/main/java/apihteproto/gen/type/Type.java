package apihteproto.gen.type;

/**
 * @author 3rdyeah
 * created on 2021/8/4
 */
public interface Type {
	String variableCode();
	String setterCode();
	String getterCode();
	String encodeCode(String bufferName, String prev);
	String decodeCode(String bufferName, String prev);

	// if type is String、array or collection, will set size as UNKNOWN_SIZE
	int size();
}
