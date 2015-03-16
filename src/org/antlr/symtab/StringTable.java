package org.antlr.symtab;
import java.util.LinkedHashMap;

/** A unique set of strings where we can get a string's index.
 *  We can also get them back out in original order.
 */
public class StringTable {
    protected LinkedHashMap<String,Integer> table = new LinkedHashMap<String,Integer>();
    protected int index = -1;

    public int add(String s) {
        Integer I = table.get(s);
        if ( I!=null ) return I;
        index++;
        table.put(s, index);
        return index;
    }

    public String[] toArray() {
		if ( table.size()==0 ) return null;
        String[] a = new String[table.size()];
        int i = 0;
        for (String s : table.keySet()) a[i++] = s;
        return a;
    }

	@Override
	public String toString() {
		return table.toString();
	}
}
