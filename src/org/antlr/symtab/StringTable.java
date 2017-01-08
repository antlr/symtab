package org.antlr.symtab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** A unique set of strings mapped to a monotonically increasing index.
 *  These indexes often useful to bytecode interpreters that have instructions
 *  referring to strings by unique integer. Indexing is from 0.
 *
 *  We can also get them back out in original order.
 *
 *  Yes, I know that this is similar to {@link String#intern()} but in this
 *  case, I need the index out not just to make these strings unique.
 */
public class StringTable {
	protected LinkedHashMap<String,Integer> table = new LinkedHashMap<String,Integer>();
	protected int index = -1; // index we have just written
	protected List<String> strings = new ArrayList<>();

	public int add(String s) {
		Integer I = table.get(s);
		if ( I!=null ) return I;
		index++;
		table.put(s, index);
		strings.add(s);
		return index;
	}

	/** Get the ith string or null if out of range */
	public String get(int i) {
		if ( i<size() && i>=0 ) {
			return strings.get(i);
		}
		return null;
	}

	public int size() { return table.size(); }

	/** Return an array, possibly of length zero, with all strings
	 *  sitting at their appropriate index within the array.
	 */
	public String[] toArray() {
		return strings.toArray(new String[strings.size()]);
	}

	/** Return a List, possibly of length zero, with all strings
	 *  sitting at their appropriate index within the array.
	 */
	public List<String> toList() {
		return strings;
	}

	public int getNumberOfStrings() {
		return index + 1;
	}

	@Override
	public String toString() {
		return table.toString();
	}
}
