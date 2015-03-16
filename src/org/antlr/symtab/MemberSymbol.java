package org.antlr.symtab;

/** A symbol within an aggregate like a class or struct. Each
 *  symbol in an aggregate knows its slot number so we can order
 *  elements in memory, for example, or keep overridden method slots
 *  in sync for vtables.
 */
public interface MemberSymbol extends Symbol {
	int getSlotNumber();		// index giving order in the aggregate (from 0)
}
