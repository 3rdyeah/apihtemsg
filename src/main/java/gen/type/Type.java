package gen.type;

/**
 * @author 3rdyeah
 * created on 2021/8/4
 */
public interface Type {
	String variableCode();
	String setterCode();
	String getterCode();
	String encodeCode(String byteBuf, String prev);
	String decodeCode(String byteBuf, String prev);
}
