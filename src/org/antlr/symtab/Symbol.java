package org.antlr.symtab;

/** A generic programming language symbol. A symbol has to have a name and
 *  a scope in which it lives. It also helps to know the order in which
 *  symbols are added to a scope because this often translates to
 *  register or parameter numbers.
 */
public interface Symbol {
    String getName();
	Scope getScope();
	void setScope(Scope scope); // set scope (not enclosing) for this symbol; who contains it?
	int getInsertionOrderNumber(); // index showing insertion order from 0
	void setInsertionOrderNumber(int i);

	// to satisfy adding symbols to sets, hashtables
	int hashCode();
	boolean equals(Object o);
}
