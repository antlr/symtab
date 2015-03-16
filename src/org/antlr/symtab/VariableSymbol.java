package org.antlr.symtab;

public class VariableSymbol extends BaseSymbol implements TypedSymbol {
	public VariableSymbol(Scope scope, String name) {
		super(scope, name);
	}

	@Override
	public void setType(Type type) {
		super.setType(type);
	}

	@Override
	public String toString() {
		String s = "";
		s = scope.getScopeName()+".";
		if ( type!=null ) return '<'+s+getName()+"."+type+'>';
		return s+getName();
	}
}
