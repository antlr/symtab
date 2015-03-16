package org.antlr.symtab;

/** A scope object typically associated with {...} code blocks */
public class LocalScope extends BaseScope {
	public LocalScope(Scope enclosingScope) {
		super(enclosingScope);
	}

	@Override
	public String getScopeName() {
		return "local";
	}
}
