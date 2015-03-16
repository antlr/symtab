package org.antlr.symtab;

public class TypeAlias extends BaseSymbol implements Type {
	protected Type targetType;
	public TypeAlias(String name, Type targetType) {
		super(name);
		this.targetType = targetType;
	}

	public Type getTargetType() {
		return targetType;
	}
}
