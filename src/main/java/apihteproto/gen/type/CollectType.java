package apihteproto.gen.type;

import apihteproto.gen.CodeFormater;

/**
 * @author 3rdyeah
 * created on 2021/8/4
 */
public abstract class CollectType implements Type {
	public String type;
	public String name;
	public String value;

	public abstract String getFullType();

	@Override
	public String variableCode() {
		return null;
	}

	@Override
	public String setterCode() {
		return CodeFormater.formatSetter(getFullType(), name);
	}

	@Override
	public String getterCode() {
		return CodeFormater.formatGetter(getFullType(), name);
	}

	@Override
	public int size() {
		return 0;
	}
}
