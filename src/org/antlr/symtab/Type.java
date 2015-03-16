package org.antlr.symtab;

/** This is mostly just a tag that indicates the implementing object
 *  is a kind of type. Every type knows its name. In languages like C
 *  where we need a tree-like structure to represent a type, one
 *  could return a string representation as the name.
 */
public interface Type {
	public String getName();
}
