package org.antlr.symtab;

/** A parameter is just kind of variable used as an argument to a
 *  function or method.
 */
public class ParameterSymbol extends VariableSymbol {
	public ParameterSymbol(String name) {
		super(name);
	}
}
