package org.antlr.symtab;

public class PrimitiveType implements Type {
	protected String name;
	protected int typeIndex;

	public PrimitiveType(String name) {
		this.name = name;
	}

	@Override
	public int getTypeIndex() { return typeIndex; }

	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}

	@Override
	public String getName() {
		return name;
	}
}
