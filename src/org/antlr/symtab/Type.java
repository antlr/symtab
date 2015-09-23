package org.antlr.symtab;

/** This interface is a tag that indicates the implementing object
 *  is a kind of type. Every type knows its name. In languages like C
 *  where we need a tree-like structure to represent a type, one
 *  could return a string representation as the name.
 *
 *  The types are typically things like struct or classes and primitive types,
 *  as well as the type trees used for languages like C.
 */
public interface Type {
	public String getName();

	/** It is useful during type computation and code gen to assign an int
	 *  index to the primitive types and possibly user-defined types like
	 *  structs and classes.
	 *
	 *  @return Return 0-indexed type index else -1 if no index.
	 */
	public int getTypeIndex();
}
