package org.antlr.symtab;

public class LocalScope extends BaseScope {
	public LocalScope(Scope enclosingScope) {
		super(enclosingScope);
	}

	@Override
	public String getScopeName() {
		return "local";
	}
}
