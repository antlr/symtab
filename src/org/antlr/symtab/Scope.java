package org.antlr.symtab;

import java.util.List;
import java.util.Set;

public interface Scope {
    String getScopeName();

    /** Where to look next for symbols if just one parent; superclass or enclosing scope. */
    Scope getParentScope();

	/** Multiple superclass or interfaces and the like... */
	List<Scope> getParentScopes();

	/** Return (inclusive) list of all scopes on path to root scope.
	 *  The first element is the current scope and the last is the root scope.
	 */
	List<Scope> getEnclosingPathToRoot();

    /** Scope in which this scope defined. null if no enclosing scope */
    Scope getEnclosingScope();

	/** What scope encloses this scope. E.g., if this scope is a function,
	 *  the enclosing scope could be a class. The BaseScope class automatically
	 *  adds this to nested scope list of s.
	 */
	void setEnclosingScope(Scope s);

	/** Return all immediately enclosed scopes. E.g., a class would return
	 *  all nested classes and any methods.
	 */
	List<Scope> getNestedScopes();

	List<? extends Symbol> getSymbols();
	List<? extends Symbol> getAllSymbols();
	Set<String> getSymbolNames();

	/** Number of symbols in this specific scope */
	int getNumberOfSymbols();

    /** Define a symbol in the current scope, throw IllegalArgumentException
	 *  if sym already defined in this scope. Set insertion order number of
	 *  sym.
	 */
	void define(Symbol sym) throws IllegalArgumentException;

    /** Look up name in this scope or in parent scope if not here */
    Symbol resolve(String name);

	/** Get symbol if directly in this scope */
	Symbol getSymbol(String name);
}
