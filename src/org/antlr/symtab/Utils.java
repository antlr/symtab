package org.antlr.symtab;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
	/** Return first ancestor node up the chain towards the root that has ruleName.
	 *  Search includes the current node.
	 */
	public static ParserRuleContext getAncestor(Parser parser, ParserRuleContext ctx, String ruleName) {
		int ruleIndex = parser.getRuleIndex(ruleName);
		return getAncestor(ctx, ruleIndex);
	}

	/** Return first ancestor node up the chain towards the root that has the rule index.
	 *  Search includes the current node.
	 */
	public static ParserRuleContext getAncestor(ParserRuleContext t, int ruleIndex) {
		while ( t!=null ) {
			if ( t.getRuleIndex() == ruleIndex ) {
				return t;
			}
			t = t.getParent();
		}
		return null;
	}

	/** Return first ancestor node up the chain towards the root that is clazz.
	 *  Search includes the current node.
	 */
	public static ParserRuleContext getFirstAncestorOfType(ParserRuleContext t, Class<?> clazz) {
		while ( t!=null ) {
			if ( t.getClass()==clazz ) {
				return t;
			}
			t = t.getParent();
		}
		return null;
	}

	public static Field[] getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		while ( clazz!=null && clazz != Object.class ) {
			for (Field f : clazz.getDeclaredFields()) {
				fields.add(f);
			}
		    clazz = clazz.getSuperclass();
		}
		return fields.toArray(new Field[fields.size()]);
	}

	public static Field[] getAllAnnotatedFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		while ( clazz!=null && clazz != Object.class ) {
			for (Field f : clazz.getDeclaredFields()) {
				if ( f.getAnnotations().length>0 ) {
					fields.add(f);
				}
			}
		    clazz = clazz.getSuperclass();
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/** Order of scopes not guaranteed but is currently breadth-first according
	 *  to nesting depth. Gets ScopedSymbols only.
	 */
	public static void getAllNestedScopedSymbols(Scope scope, List<Scope> scopes) {
		scopes.addAll(scope.getNestedScopedSymbols());
		for (Scope s : scope.getNestedScopedSymbols()) {
			getAllNestedScopedSymbols(s, scopes);
		}
	}

	/** Order of scopes not guaranteed but is currently breadth-first according
	 *  to nesting depth. Gets ScopedSymbols and non-ScopedSymbols.
	 */
	public static void getAllNestedScopes(Scope scope, List<Scope> scopes) {
		scopes.addAll(scope.getNestedScopes());
		for (Scope s : scope.getNestedScopes()) {
			getAllNestedScopes(s, scopes);
		}
	}

	/** Return a string of scope names with the "stack" growing to the left
	 *  E.g., myblock:mymethod:myclass.
	 *  String includes arg scope in string.
	 */
	public static String toScopeStackString(Scope scope, String separator) {
		List<Scope> scopes = scope.getEnclosingPathToRoot();
		return joinScopeNames(scopes, separator);
	}

	/** Return a string of scope names with the "stack" growing to the right.
	 *  E.g., myclass:mymethod:myblock.
	 *  String includes arg scope in string.
	 */
	public static String toQualifierString(Scope scope, String separator) {
		List<Scope> scopes = scope.getEnclosingPathToRoot();
		Collections.reverse(scopes);
		return joinScopeNames(scopes, separator);
	}

	public static String toString(Scope s, int level) {
		if ( s==null ) return "";
		StringBuilder buf = new StringBuilder();
		buf.append(tab(level));
		buf.append(s.getName());
		buf.append("\n");
		level++;
		for (Symbol sym : s.getSymbols()) { // print out all symbols but not scopes
			if ( !(sym instanceof Scope) ) {
				buf.append(tab(level));
				buf.append(sym);
				buf.append("\n");
			}
		}
		for (Scope nested : s.getNestedScopes()) { // includes named scopes and local scopes
			buf.append(toString(nested, level));
		}
		return buf.toString();
	}

	public static String toString(Scope s) {
		return toString(s, 0);
	}

	//  Generic filtering, mapping, joining that should be in the standard library but aren't

	public static <T> T findFirst(List<T> data, Predicate<T> pred) {
		if ( data!=null ) for (T x : data) {
			if ( pred.test(x) ) {
				return x;
			}
		}
		return null;
	}

	public static <T> List<T> filter(List<T> data, Predicate<T> pred) {
		List<T> output = new ArrayList<>();
		if ( data!=null ) for (T x : data) {
			if ( pred.test(x) ) {
				output.add(x);
			}
		}
		return output;
	}

	public static <T> Set<T> filter(Collection<T> data, Predicate<T> pred) {
		Set<T> output = new HashSet<>();
		for (T x : data) {
			if ( pred.test(x) ) {
				output.add(x);
			}
		}
		return output;
	}

	public static <T,R> List<R> map(Collection<T> data, Function<T,R> getter) {
		List<R> output = new ArrayList<>();
		if ( data!=null ) for (T x : data) {
			output.add(getter.apply(x));
		}
		return output;
	}

	public static <T,R> List<R> map(T[] data, Function<T,R> getter) {
		List<R> output = new ArrayList<>();
		if ( data!=null ) for (T x : data) {
			output.add(getter.apply(x));
		}
		return output;
	}

	public static <T> String join(Collection<T> data, String separator) {
		return join(data.iterator(), separator, "", "");
	}

	public static <T> String join(Collection<T> data, String separator, String left, String right) {
		return join(data.iterator(), separator, left, right);
	}

	public static <T> String join(Iterator<T> iter, String separator, String left, String right) {
		StringBuilder buf = new StringBuilder();

		while(iter.hasNext()) {
			buf.append(iter.next());
			if(iter.hasNext()) {
				buf.append(separator);
			}
		}

		return left+buf.toString()+right;
	}

	public static <T> String join(T[] array, String separator) {
		StringBuilder builder = new StringBuilder();

		for(int i = 0; i < array.length; ++i) {
			builder.append(array[i]);
			if(i < array.length - 1) {
				builder.append(separator);
			}
		}

		return builder.toString();
	}

	public static String tab(int n) {
		StringBuilder buf = new StringBuilder();
		for (int i=1; i<=n; i++) buf.append("    ");
		return buf.toString();
	}

	public static String joinScopeNames(List<Scope> scopes, String separator) {
		if ( scopes==null || scopes.size()==0 ) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		buf.append(scopes.get(0).getName());
		for (int i = 1; i<scopes.size(); i++) {
			Scope s = scopes.get(i);
			buf.append(separator);
			buf.append(s.getName());
		}
		return buf.toString();
	}
}
