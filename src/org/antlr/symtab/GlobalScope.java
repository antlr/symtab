package org.antlr.symtab;

/** A scope associated with globals. */
public class GlobalScope extends BaseScope {
	public GlobalScope(Scope scope) { super(scope); }
    public String getName() { return "global"; }
}
