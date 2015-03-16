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
}
