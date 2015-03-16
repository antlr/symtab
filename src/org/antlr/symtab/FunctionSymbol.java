package org.antlr.symtab;

import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionSymbol extends SymbolWithScope implements TypedSymbol {
	protected ParserRuleContext defNode;
	protected Type retType;

	public FunctionSymbol(String name) {
		super(name);
	}

	public void setDefNode(ParserRuleContext defNode) {
		this.defNode = defNode;
	}

	@Override
	public Type getType() {
		return retType;
	}

	@Override
	public void setType(Type type) {
		retType = type;
	}

	public String toString() { return name+":"+super.toString(); }
}
