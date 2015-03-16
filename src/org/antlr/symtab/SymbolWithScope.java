package org.antlr.symtab;

import java.util.Collections;
import java.util.List;

/** An abstract base class that houses common functionality for
 *  symbols like classes and functions that are both symbols and scopes.
 *  There is some common cut and paste functionality with {@link BaseSymbol}
 *  because of a lack of multiple inheritance in Java but it is minimal.
 */
public abstract class SymbolWithScope extends BaseScope implements Symbol, Scope {
	protected final String name; // All symbols at least have a name
	protected int index; 	// insertion order from 0; compilers often need this

	public SymbolWithScope(String name) {
        this.name = name;
	}

	@Override public String getName() { return name; }
	@Override public Scope getScope() { return enclosingScope; }
	@Override public void setScope(Scope scope) { setEnclosingScope(scope); }

	public Scope getParentScope() { return getEnclosingScope(); }
    public Scope getEnclosingScope() { return enclosingScope; }

	public String getQualifiedName() {
		return enclosingScope+"."+name;
	}

	@Override
	public int getInsertionOrderNumber() {
		return index;
	}

	@Override
	public void setInsertionOrderNumber(int i) {
		this.index = i;
	}

	@Override
	public int getNumberOfSymbols() {
		return symbols.size();
	}

	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof Symbol) ) {
			return false;
		}
        if ( obj==this ) {
			return true;
		}
		return name.equals(((Symbol)obj).getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String getFullyQualifiedName(String scopePathSeparator) {
		List<Scope> path = getEnclosingPathToRoot();
		Collections.reverse(path);
		String qualifier = Utils.joinScopeNames(path, scopePathSeparator);
		return qualifier + scopePathSeparator + name;
	}
}
