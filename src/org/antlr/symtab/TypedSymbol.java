package org.antlr.symtab;

public interface TypedSymbol {
	Type getType();
	void setType(Type type);
}
