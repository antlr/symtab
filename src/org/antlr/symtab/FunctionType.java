package org.antlr.symtab;

/** For C types like "void (*)()", we need that to be a pointer to a function
 *  returning void. This is the "to a function returning void" part of the type
 *  tree.
 */
public class FunctionType implements Type {
	protected Type returnType;
	public FunctionType(Type returnType) {
		this.returnType = returnType;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public String toString() {
		return "*"+ returnType;
	}
}
