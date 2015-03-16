package org.antlr.symtab;

public class SymbolTable {
	public static final Type INVALID_TYPE = new InvalidType();

	public BaseScope PREDEFINED = new PredefinedScope();
	public GlobalScope GLOBALS = new GlobalScope(PREDEFINED);

	public SymbolTable() {
	}

	public void initTypeSystem() {
	}

	public void definePredefinedSymbol(Symbol s) {
		PREDEFINED.define(s);
	}

	public void defineGlobalSymbol(Symbol s) {
		GLOBALS.define(s);
	}

	public static String toString(Scope s) {
		StringBuilder buf = new StringBuilder();
		toString(buf, s, 0);
		return buf.toString();
	}

	public static void toString(StringBuilder buf, Scope s, int level) {
		buf.append(Utils.tab(level));
		buf.append(s.getScopeName());
		buf.append("\n");
		level++;
		for (Symbol sym : s.getSymbols()) {
			if ( !(sym instanceof Scope) ) {
				buf.append(Utils.tab(level));
				buf.append(sym+"\n");
			}
		}
		for (Scope nested : s.getNestedScopes()) {
			toString(buf, nested, level);
		}
		level--;
	}
}
