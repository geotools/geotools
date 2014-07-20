package org.geotools.ysld;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Wrapper around a parsed Yaml sequence.
 */
public class YamlSeq extends YamlObject<List<Object>> implements Iterable<YamlObject> {

    public static YamlSeq from(Object... values) {
        return new YamlSeq(Arrays.asList(values));
    }

    public YamlSeq(Object obj) {
        super(cast(obj));
    }

    static List<Object> cast(Object obj) {
        if (!(obj instanceof List)) {
            throw new IllegalArgumentException(obj + " is not a list");
        }
        return (List<Object>) obj;
    }

    public String str(int i) {
        return convert(raw.get(i), String.class);
    }

    public YamlMap map(int i) {
        return new YamlMap(raw.get(i));
    }

    public YamlSeq seq(int i) {
        return new YamlSeq(raw.get(i));
    }

    @Override
    public Iterator<YamlObject> iterator() {
        final Iterator<Object> it = raw.iterator();
        return new Iterator<YamlObject>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public YamlObject next() {
                return YamlObject.create(it.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
