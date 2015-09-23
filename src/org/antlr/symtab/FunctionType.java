package org.antlr.symtab;

import java.util.List;

/** For C types like "void (*)(int)", we need that to be a pointer to a function
 *  taking a single integer argument returning void.
 */
public class FunctionType implements Type {
	protected final Type returnType;
	protected final List<Type> argumentTypes;

	public FunctionType(Type returnType, List<Type> argumentTypes) {
		this.returnType = returnType;
		this.argumentTypes = argumentTypes;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public int getTypeIndex() { return -1; }

	public List<Type> getArgumentTypes() {
		return argumentTypes;
	}

	@Override
	public String toString() {
		return "*"+ returnType;
	}
}
