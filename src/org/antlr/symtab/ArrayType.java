package org.antlr.symtab;

public class ArrayType implements Type {
	protected Type elemType;
	protected int size; // some languages allow you to point at arrays of a specific size

	public ArrayType(Type elemType) {
		this.elemType = elemType;
	}

	public ArrayType(Type elemType, int size)
	{
		this.elemType = elemType;
		this.size = size;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public String toString() {
		return elemType+"[]";
	}
}
