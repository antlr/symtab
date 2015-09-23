package org.antlr.symtab;

public class InvalidType implements Type {
	@Override
	public String getName() {
		return "INVALID";
	}

	@Override
	public int getTypeIndex() { return -1; }
}
