package gen.type;

/**
 * @author 3rdyeah
 * created on 2021/8/4 16:46
 */
public class SetType extends AListType {

	@Override
	public String getCollectType() {
		return "java.util.HashSet";
	}
}
