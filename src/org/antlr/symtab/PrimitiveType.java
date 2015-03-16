package org.antlr.symtab;

public class PrimitiveType implements Type {
	protected String name;

	public PrimitiveType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
