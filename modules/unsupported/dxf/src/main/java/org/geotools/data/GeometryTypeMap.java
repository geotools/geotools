package org.geotools.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Gertjan
 */
public class GeometryTypeMap extends HashMap<String, GeometryType> {

    public GeometryTypeMap() {
        super();
    }

    public GeometryTypeMap(GeometryTypeMap map) {
        putAll(map);
    }

    public String[] toTypeNames() {
        ArrayList results = new ArrayList();

        Iterator iter = keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();

            if (key instanceof String) {
                Object value = get(key);
                if (value instanceof GeometryType) {
                    String result = ((String) key) + ((GeometryType) value).getExtension();
                    results.add(result);
                }
            }
        }
        return (String[]) results.toArray(new String[results.size()]);
    }

    public String[] toKeyArray() {
        return (String[]) keySet().toArray(new String[size()]);
    }

    public static String stripExtension(String typename, GeometryType geometryType) {
        String extension = geometryType.getExtension();
        if (typename.endsWith(extension)) {
            typename = typename.substring(0, typename.length() - extension.length());
        }

        return typename;
    }
}
