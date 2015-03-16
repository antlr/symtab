package org.antlr.symtab;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
	public static ParserRuleContext getAncestor(Parser parser, ParserRuleContext ctx, String ruleName) {
		int ruleIndex = parser.getRuleIndex(ruleName);
		return getAncestor(ctx, ruleIndex);
	}

	/** Return first ancestor node up the chain towards the root that has the rule index.
	 *  Search includes the current node.
	 */
	public static ParserRuleContext getAncestor(ParserRuleContext ctx, int ruleIndex) {
		while ( ctx!=null ) {
			if ( ctx.getRuleIndex() == ruleIndex ) {
				return ctx;
			}
			ctx = ctx.getParent();
		}
		return null;
	}

	public static <T> List<T> filter(List<T> data, Predicate<T> pred) {
		List<T> output = new ArrayList<>();
		for (T x : data) {
			if ( pred.test(x) ) {
				output.add(x);
			}
		}
		return output;
	}

	public static <T,R> List<R> map(List<T> data, Function<T,R> getter) {
		List<R> output = new ArrayList<>();
		for (T x : data) {
			output.add(getter.apply(x));
		}
		return output;
	}

	public static <T,R> List<R> map(T[] data, Function<T,R> getter) {
		List<R> output = new ArrayList<>();
		for (T x : data) {
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
		buf.append(scopes.get(0).getScopeName());
		for (int i = 1; i<scopes.size(); i++) {
			Scope s = scopes.get(i);
			buf.append(separator);
			buf.append(s.getScopeName());
		}
		return buf.toString();
	}

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
	public static String asScopeStackString(Scope scope, String separator) {
		List<Scope> scopes = scope.getEnclosingPathToRoot();
		return joinScopeNames(scopes, separator);
	}

	/** Return a string of scope names with the "stack" growing to the right.
	 *  E.g., myclass:mymethod:myblock.
	 *  String includes arg scope in string.
	 */
	public static String asQualifierString(Scope scope, String separator) {
		List<Scope> scopes = scope.getEnclosingPathToRoot();
		Collections.reverse(scopes);
		return joinScopeNames(scopes, separator);
	}

//	public static List<? extends Tree> getFirstAncestorsOfType(Tree t, Class<?> clazz) {
//		if ( t.getParent()==null ) return Collections.emptyList();
//		List<Tree> ancestors = new ArrayList<Tree>();
//		t = t.getParent();
//		while ( t!=null ) {
//			ancestors.add(0, t); // insert at start
//			t = t.getParent();
//		}
//		return ancestors;
//	}

}
