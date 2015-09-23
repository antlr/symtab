package org.antlr.symtab;

/** An element in a type tree that represents a pointer to some type,
 *  such as we need for C.  "int *" would need a PointerType(intType) object.
 */
public class PointerType implements Type {
	protected Type targetType;
	public PointerType(Type targetType) {
		this.targetType = targetType;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int getTypeIndex() { return -1; }

	@Override
	public String toString() {
		return "*"+ targetType;
	}
}
