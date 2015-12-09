package org.antlr.symtab;

public class PrimitiveType extends BaseSymbol implements Type {
	protected int typeIndex;

	public PrimitiveType(String name) {
		super(name);
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
