package org.antlr.symtab;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collections;
import java.util.List;

/** An abstract base class used to house common functionality.
 *  You can associate a node in the parse tree that is responsible
 *  for defining this symbol.
 */
public abstract class BaseSymbol implements Symbol {
	protected final String name;   		 // All symbols at least have a name
	protected Type type;				 // If language statically typed, record type
	protected Scope scope;      		 // All symbols know what scope contains them.
	protected ParserRuleContext defNode; // points at definition node in tree
	protected int lexicalOrder; 		 // order seen or insertion order from 0; compilers often need this

	public BaseSymbol(String name) { this.name = name; }

	@Override public String getName() { return name; }
	@Override public Scope getScope() { return scope; }
	@Override public void setScope(Scope scope) { this.scope = scope; }

	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }

	public void setDefNode(ParserRuleContext defNode) {
		this.defNode = defNode;
	}

	public ParserRuleContext getDefNode() {
		return defNode;
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

	@Override
	public int getInsertionOrderNumber() {
		return lexicalOrder;
	}

	@Override
	public void setInsertionOrderNumber(int i) {
		this.lexicalOrder = i;
	}

	public String getFullyQualifiedName(String scopePathSeparator) {
		List<Scope> path = scope.getEnclosingPathToRoot();
		Collections.reverse(path);
		String qualifier = Utils.joinScopeNames(path, scopePathSeparator);
		return qualifier + scopePathSeparator + name;
	}

	public String toString() {
		String s = "";
		if ( scope!=null ) s = scope.getName()+".";
		if ( type!=null ) {
			String ts = type.toString();
			if ( type instanceof SymbolWithScope ) {
				ts = ((SymbolWithScope) type).getFullyQualifiedName(".");
			}
			return '<'+s+getName()+":"+ts+'>';
		}
		return s+getName();
	}
}
