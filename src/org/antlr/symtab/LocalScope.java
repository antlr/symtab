package org.antlr.symtab;

import java.util.ArrayList;
import java.util.List;

/** A scope object typically associated with {...} code blocks */
public class LocalScope extends BaseScope {
	public List<Scope> nestedScopes = new ArrayList<>();

	public LocalScope(Scope enclosingScope) {
		super(enclosingScope);
	}

	@Override
	public String getName() {
		return "local";
	}
}
