package org.antlr.symtab;

/** A generic programming language symbol */
public interface Symbol {
    String getName();
	Scope getScope();
	void setScope(Scope scope); // set scope (not enclosing) for this symbol; who contains it?
	int getInsertionOrderNumber(); // index showing insertion order from 0
	void setInsertionOrderNumber(int i);
	String getFullyQualifiedName(String scopePathSeparator);

	// to satisfy adding symbols to sets, hashtables
	int hashCode();
	boolean equals(Object o);
}
