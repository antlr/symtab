package org.antlr.symtab;

/** A "typedef int I;" in C results in a TypeAlias("I", ptrToIntegerType) */
public class TypeAlias extends BaseSymbol implements Type {
	protected Type targetType;
	public TypeAlias(String name, Type targetType) {
		super(name);
		this.targetType = targetType;
	}
	
	@Override
	public int getTypeIndex() { return -1; }

	public Type getTargetType() {
		return targetType;
	}
}
