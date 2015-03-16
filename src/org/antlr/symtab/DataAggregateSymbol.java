package org.antlr.symtab;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/** A symbol to represent a collection of data like a struct or class.
 *  Each member has a slot number indexed from 0 and we track data fields
 *  and methods with different slot sequences. A DataAggregateSymbol
 *  can also be a member of an aggregate itself (nested structs, ...).
 */
public class DataAggregateSymbol extends SymbolWithScope implements MemberSymbol, Type {
	protected ParserRuleContext defNode;
	protected int nextFreeFieldSlot = 0;  // next slot to allocate

	public DataAggregateSymbol(String name) {
		super(name);
	}

	public void setDefNode(ParserRuleContext defNode) {
		this.defNode = defNode;
	}

	public ParserRuleContext getDefNode() {
		return defNode;
	}

	@Override
	public void define(Symbol sym) throws IllegalArgumentException {
		if ( !(sym instanceof MemberSymbol) ) {
			throw new IllegalArgumentException(
				"sym is "+sym.getClass().getSimpleName()+" not MemberSymbol"
			);
		}
		super.define(sym);
		setSlotNumber(sym);
	}

	@Override
	public List<MemberSymbol> getSymbols() {
		return (List<MemberSymbol>)super.getSymbols();
	}

	public void setSlotNumber(Symbol sym) {
		if ( sym instanceof FieldSymbol) {
			FieldSymbol fsym = (FieldSymbol)sym;
			fsym.slot = nextFreeFieldSlot++;
		}
//		System.out.println(sym.getName()+" is slot "+((MemberSymbol) sym).getSlotNumber());
	}

	public MethodSymbol resolveMethod(String name) {
		Symbol sym = resolve(name);
		if ( sym instanceof MethodSymbol ) {
			return (MethodSymbol)sym;
		}
		return null;
	}

	@Override
	public int getSlotNumber() {
		return -1; // class definitions do not yield either field or method slots; they are just nested
	}
}
