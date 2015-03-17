package org.antlr.symtab;

import java.util.LinkedHashSet;
import java.util.Set;

/** A symbol representing the class. It is a kind of data aggregate
 *  that has much in common with a struct.
 */
public class ClassSymbol extends DataAggregateSymbol {
	protected String superClassName; // null if this is Object
	protected int nextFreeMethodSlot = 0; // next slot to allocate

	public ClassSymbol(String name) {
		super(name);
	}

	@Override
	public Scope getParentScope() {
		if ( superClassName!=null ) {
			if ( getEnclosingScope()!=null ) {
				Symbol superClass = getEnclosingScope().resolve(superClassName);
				if (superClass instanceof ClassSymbol) {
					return (Scope) superClass; // parent is not enclosing scope for classes
				}
			}
		}
		return super.getEnclosingScope();
	}

	public MethodSymbol resolveMethod(String name) {
		Symbol sym = resolve(name);
		if ( sym instanceof MethodSymbol ) {
			return (MethodSymbol)sym;
		}
		return null;
	}

	public void setSuperClass(String superClassName) {
		this.superClassName = superClassName;
		nextFreeMethodSlot = getNumberOfMethods();
	}

	public void setSlotNumber(Symbol sym) {
		if ( sym instanceof MethodSymbol) {
			MethodSymbol msym = (MethodSymbol)sym;
			// handle inheritance. If not found in this scope, check superclass
			// if any.
			Scope parentScope = getParentScope();
			if ( parentScope instanceof ClassSymbol ) {
				MethodSymbol superMethodSym = ((ClassSymbol)parentScope).resolveMethod(sym.getName());
				if ( superMethodSym!=null ) {
					msym.slot = superMethodSym.slot;
				}
			}
			if ( msym.slot==-1 ) {
				msym.slot = nextFreeMethodSlot++;
			}
		}
		else {
			super.setSlotNumber(sym);
		}
	}

	/** Return the set of all methods defined within this class */
	public Set<MethodSymbol> getDefinedMethods() {
		Set<MethodSymbol> methods = new LinkedHashSet<>();
		for (MemberSymbol s : getSymbols()) {
			if ( s instanceof MethodSymbol ) {
				methods.add((MethodSymbol)s);
			}
		}
		return methods;
	}

	/** Return the set of all methods either inherited or not */
	public Set<MethodSymbol> getMethods() {
		Set<MethodSymbol> methods = new LinkedHashSet<>();
		if ( getParentScope() instanceof ClassSymbol ) {
			ClassSymbol parentScope = (ClassSymbol)getParentScope();
			methods.addAll(parentScope.getMethods());
		}
		methods.removeAll(getDefinedMethods()); // override method from superclass
		methods.addAll( getDefinedMethods() );
		return methods;
	}

	/** get the number of methods defined specifically in this class */
	public int getNumberOfDefinedMethods() {
		int n = 0;
		for (MemberSymbol s : getSymbols()) {
			if ( s instanceof MethodSymbol ) {
				n++;
			}
		}
		return n;
	}

	/** get the total number of methods visible to this class */
	public int getNumberOfMethods() {
		int n = 0;
		if ( getParentScope() instanceof ClassSymbol ) {
			ClassSymbol parentScope = (ClassSymbol)getParentScope();
			n += parentScope.getNumberOfMethods();
		}
		n += getNumberOfDefinedMethods();
		return n;
	}

	@Override
	public String toString() {
		return name+":"+super.toString();
	}
}
