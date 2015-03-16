package org.antlr.symtab;

public class PointerType implements Type {
	protected Type targetType;
	public PointerType(Type targetType) {
		this.targetType = targetType;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public String toString() {
		return "*"+ targetType;
	}
}
