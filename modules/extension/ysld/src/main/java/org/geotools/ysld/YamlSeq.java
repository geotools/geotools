/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ysld;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper around a parsed Yaml sequence.
 *
 * <p>Wrapper requires {@link List} as sequence order is required.
 */
public class YamlSeq extends YamlObject<List<Object>> implements Iterable<YamlObject<Object>> {

    /**
     * Quick inline sequence creation.
     *
     * @return sequence of provided values.
     */
    public static YamlSeq from(Object... values) {
        return new YamlSeq(Arrays.asList(values));
    }

    /**
     * Internal cast to List used by constructor.
     *
     * @return List
     * @throws IllegalArgumentException List required
     */
    @SuppressWarnings("unchecked")
    static List<Object> cast(Object obj) {
        if (obj instanceof List) {
            return (List<Object>) obj;
        } else if (obj.getClass().isArray()) {
            return Arrays.asList((Object[]) obj);
        }
        throw new IllegalArgumentException(obj + " is not a list");
    }

    /**
     * Yaml mapping.
     *
     * @param obj Wrapped {@link List}
     * @throws IllegalArgumentException {@link List} is required
     */
    public YamlSeq(Object obj) {
        super(cast(obj));
    }

    /**
     * String access
     *
     * @param i index
     * @return String access, converting as needed
     */
    public String str(int i) {
        return convert(raw.get(i), String.class);
    }

    /**
     * Integer access
     *
     * @param i index
     * @return Integer access, converting as needed
     */
    public Integer integer(int i) {
        return convert(raw.get(i), Integer.class);
    }

    /**
     * Double access
     *
     * @param i index
     * @return Double access, converting as needed
     */
    public Double doub(int i) {
        return convert(raw.get(i), Double.class);
    }

    /**
     * Boolean access
     *
     * @param i index
     * @return Boolean access, converting as needed
     */
    public Boolean bool(int i) {
        return convert(raw.get(i), Boolean.class);
    }

    /**
     * Value access
     *
     * @param i index
     * @return Value access
     */
    public Object get(int i) {
        return raw.get(i);
    }

    /**
     * Yaml wrapper access
     *
     * <p>Wrappers are provided for Map and List
     *
     * @param i index
     * @return Access as Yaml wrapper
     */
    public YamlObject<?> obj(int i) {
        return YamlObject.create(raw.get(i));
    }

    /**
     * Map access
     *
     * @param i index
     * @return Map access, provided as {@link YamlMap} wrapper
     */
    public YamlMap map(int i) {
        Object obj = raw.get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof YamlMap) {
            return (YamlMap) obj;
        }
        return new YamlMap(obj);
    }

    /**
     * YamlSeq access
     *
     * @param i index
     * @return Sequence access, provided as {@link YamlSeq} wrapper
     */
    public YamlSeq seq(int i) {
        Object obj = raw.get(i);
        if (obj == null) {
            return null;
        }
        if (obj instanceof YamlSeq) {
            return (YamlSeq) obj;
        }
        return new YamlSeq(obj);
    }

    /**
     * Iterator access, requires all contents be wrapped as {@link YamlObject}.
     *
     * <p>To traverse a list of atomic values use {@link #raw()} iterator().
     *
     * @return {@link YamlObject} iterator access
     */
    @Override
    public Iterator<YamlObject<Object>> iterator() {
        final Iterator<Object> it = raw.iterator();
        return new Iterator<YamlObject<Object>>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public YamlObject<Object> next() {
                return YamlObject.create(it.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
