package org.antlr.symtab;

/** A scope to hold predefined symbols of your language. This could
 *  be a list of type names like int or methods like print.
 */
public class PredefinedScope extends BaseScope {
	@Override
	public String getName() {
		return "predefined";
	}
}
