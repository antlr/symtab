package org.antlr.symtab;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** A symbol representing a collection of data like a struct or class.
 *  Each member has a slot number indexed from 0 and we track data fields
 *  and methods with different slot sequences. A DataAggregateSymbol
 *  can also be a member of an aggregate itself (nested structs, ...).
 */
public abstract class DataAggregateSymbol extends SymbolWithScope implements MemberSymbol, Type {
	protected ParserRuleContext defNode;
	protected int nextFreeFieldSlot = 0;  // next slot to allocate
	protected int typeIndex;

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

	@Override
	public Map<String, ? extends MemberSymbol> getMembers() {
		return (Map<String, ? extends MemberSymbol>)super.getMembers();
	}

	/** get the number of fields defined specifically in this class */
	public int getNumberOfDefinedFields() {
		int n = 0;
		for (MemberSymbol s : getSymbols()) {
			if ( s instanceof FieldSymbol ) {
				n++;
			}
		}
		return n;
	}

	/** get the total number of fields visible to this class */
	public int getNumberOfFields() {
		int n = 0;
		if ( getParentScope() instanceof DataAggregateSymbol ) {
			DataAggregateSymbol parentScope = (DataAggregateSymbol)getParentScope();
			n += parentScope.getNumberOfFields();
		}
		n += getNumberOfDefinedFields();
		return n;
	}

	public List<? extends FieldSymbol> getDefinedFields() {
		List<FieldSymbol> fields = new ArrayList<>();
		for (MemberSymbol s : getSymbols()) {
			if (s instanceof FieldSymbol) {
				fields.add((FieldSymbol)s);
			}
		}
		return fields;
	}

	public List<? extends FieldSymbol> getFields() {
		List<FieldSymbol> fields = new ArrayList<>();
		if ( getParentScope() instanceof DataAggregateSymbol ) {
			DataAggregateSymbol parentScope = (DataAggregateSymbol)getParentScope();
			fields.addAll( parentScope.getFields() );
		}
		fields.addAll( getDefinedFields() );
		return fields;
	}

	public void setSlotNumber(Symbol sym) {
		if ( sym instanceof FieldSymbol) {
			FieldSymbol fsym = (FieldSymbol)sym;
			fsym.slot = nextFreeFieldSlot++;
		}
	}

	@Override
	public int getSlotNumber() {
		return -1; // class definitions do not yield either field or method slots; they are just nested
	}

	@Override
	public int getTypeIndex() { return typeIndex; }

	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}
}
