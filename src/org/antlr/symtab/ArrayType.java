package org.antlr.symtab;

/** An element within a type type such is used in C or Java where we need to
 *  indicate the type is an array of some element type like float[] or User[].
 *  It also tracks the size as some types indicate the size of the array.
 */
public class ArrayType implements Type {
	protected final Type elemType;
	protected final int numElems; // some languages allow you to point at arrays of a specific size

	public ArrayType(Type elemType) {
		this.elemType = elemType;
		this.numElems = -1;
	}

	public ArrayType(Type elemType, int numElems)
	{
		this.elemType = elemType;
		this.numElems = numElems;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int getTypeIndex() { return -1; }

	@Override
	public String toString() {
		return elemType+"[]";
	}
}
