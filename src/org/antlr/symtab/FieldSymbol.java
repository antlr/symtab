package org.antlr.symtab;

/** A field symbol is just a variable that lives inside an aggregate like a
 *  class or struct.
 */
public class FieldSymbol extends VariableSymbol implements MemberSymbol {
	protected int slot;

	public FieldSymbol(String name) {
		super(name);
	}

	@Override
	public int getSlotNumber() { return slot; }
}
