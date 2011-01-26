package org.geotools.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A linked HashMap set up for easy construction.
 * <p>
 * Example: <code>KVP map = new KVP("foo",1,"bar,2);</code>
 * 
 * @author jody
 *
 * @source $URL$
 */
public class KVP extends LinkedHashMap<String, Object> {
    /**
     * 
     */
    private static final long serialVersionUID = -387821381125137128L;

    /**
     * A linked HashMap set up for easy construction.
     * <p>
     * Example: <code>KVP map = new KVP("foo",1,"bar,2);</code>
     * 
     * @param pairs
     */
    public KVP(Object... pairs) {
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Pairs was not an even number");
        }
        for (int i = 0; i < pairs.length; i += 2) {
            String key = (String) pairs[i];
            Object value = pairs[i + 1];
            add(key, value);
        }
    }
    /**
     * An additive version of put; will add additional values resulting
     * in a list.
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void add(String key, Object value) {
        if( containsKey(key) ) {
            Object existing = get( key );
            if( existing instanceof List<?>){
                List<Object> list = (List<Object>) existing;
                list.add( value );
            }
            else {
                List<Object> list = new ArrayList<Object>();
                list.add( existing );
                list.add( value );
                put( key, list );
            }
        }
        else {
            put(key, value);
        }
    }
}
