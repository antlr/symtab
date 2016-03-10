package org.antlr.symtab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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

	/** Return the ClassSymbol associated with superClassName or null if
	 *  superclass is not resolved looking up the enclosing scope chain.
	 */
	public ClassSymbol getSuperClassScope() {
		if ( superClassName!=null ) {
			if ( getEnclosingScope()!=null ) {
				Symbol superClass = getEnclosingScope().resolve(superClassName);
				if ( superClass instanceof ClassSymbol ) {
					return (ClassSymbol)superClass;
				}
			}
		}
		return null;
	}

	/** Multiple superclass or interface implementations and the like... */
	public List<ClassSymbol> getSuperClassScopes() {
		ClassSymbol superClassScope = getSuperClassScope();
		if ( superClassScope!=null ) {
			return Collections.singletonList(superClassScope);
		}
		return null;
	}

	@Override
	public Symbol resolve(String name) {
		Symbol s = resolveMember(name);
		if ( s!=null ) {
			return s;
		}
		// if not a member, check any enclosing scope. it might be a global variable for example
		Scope parent = getEnclosingScope();
		if ( parent != null ) return parent.resolve(name);
		return null; // not found
	}

	/** Look for a member with this name in this scope or any super class.
	 *  Return null if no member found.
	 */
	@Override
	public Symbol resolveMember(String name) {
		Symbol s = symbols.get(name);
		if ( s instanceof MemberSymbol ) {
			return s;
		}
		// walk superclass chain
		List<ClassSymbol> superClassScopes = getSuperClassScopes();
		if ( superClassScopes!=null ) {
			for (ClassSymbol sup : superClassScopes) {
				s = sup.resolveMember(name);
				if ( s instanceof MemberSymbol ) {
					return s;
				}
			}
		}
		return null;
	}

	/** Look for a field with this name in this scope or any super class.
	 *  Return null if no field found.
	 */
	@Override
	public Symbol resolveField(String name) {
		Symbol s = resolveMember(name);
		if ( s instanceof FieldSymbol ) {
			return s;
		}
		return null;
	}

	/** Look for a method with this name in this scope or any super class.
	 *  Return null if no method found.
	 */
	public MethodSymbol resolveMethod(String name) {
		Symbol s = resolveMember(name);
		if ( s instanceof MethodSymbol ) {
			return (MethodSymbol)s;
		}
		return null;
	}

	public void setSuperClass(String superClassName) {
		this.superClassName = superClassName;
		nextFreeMethodSlot = getNumberOfMethods();
	}

	public String getSuperClassName() {
		return superClassName;
	}

	@Override
	public void setSlotNumber(Symbol sym) {
		if ( sym instanceof MethodSymbol ) {
			MethodSymbol msym = (MethodSymbol)sym;
			// handle inheritance. If not found in this scope, check superclass
			// if any.
			ClassSymbol superClass = getSuperClassScope();
			if ( superClass !=null ) {
				MethodSymbol superMethodSym = superClass.resolveMethod(sym.getName());
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
		ClassSymbol superClassScope = getSuperClassScope();
		if ( superClassScope!=null) {
			methods.addAll(superClassScope.getMethods());
		}
		methods.removeAll(getDefinedMethods()); // override method from superclass
		methods.addAll( getDefinedMethods() );
		return methods;
	}

	@Override
	public List<? extends FieldSymbol> getFields() {
		List<FieldSymbol> fields = new ArrayList<>();
		ClassSymbol superClassScope = getSuperClassScope();
		if ( superClassScope!=null ) {
			fields.addAll( superClassScope.getFields() );
		}
		fields.addAll( getDefinedFields() );
		return fields;
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
		ClassSymbol superClassScope = getSuperClassScope();
		if ( superClassScope!=null ) {
			n += superClassScope.getNumberOfMethods();
		}
		n += getNumberOfDefinedMethods();
		return n;
	}

	public int getNumberOfFields() {
		int n = 0;
		ClassSymbol superClassScope = getSuperClassScope();
		if ( superClassScope!=null ) {
			n += superClassScope.getNumberOfFields();
		}
		n += getNumberOfDefinedFields();
		return n;
	}

	@Override
	public String toString() {
		return name+":"+super.toString();
	}
}
