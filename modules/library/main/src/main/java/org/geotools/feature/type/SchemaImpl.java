/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.Schema;

/**
 * Implementation of Schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class SchemaImpl implements Schema {
    HashMap<Name, AttributeType> contents;
    String uri;

    /** Schema constructed w/ respect to provided URI */
    public SchemaImpl(String uri) {
        super();
        this.uri = uri;
        this.contents = new HashMap<>();
    }

    @Override
    public Set<Name> keySet() {
        return contents.keySet();
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return contents.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return contents.containsValue(value);
    }

    @Override
    public AttributeType get(Object key) {
        return contents.get(key);
    }

    @Override
    public AttributeType put(Name name, AttributeType type) {
        if (name == null) {
            throw new IllegalArgumentException("Please use a Name");
        }
        Name n = name;
        if (!n.toString().startsWith(uri.toString())) {
            throw new IllegalArgumentException("Provided name was not in schema:" + uri);
        }
        if (type == null) {
            throw new IllegalArgumentException("Please use an AttributeType");
        }
        AttributeType t = type;

        return contents.put(n, t);
    }

    @Override
    public AttributeType remove(Object key) {
        return contents.remove(key);
    }

    @Override
    public void putAll(Map<? extends Name, ? extends AttributeType> t) {
        contents.putAll(t);
    }

    @Override
    public void clear() {
        contents.clear();
    }

    @Override
    public Collection<AttributeType> values() {
        return contents.values();
    }

    @Override
    public Set<Entry<Name, AttributeType>> entrySet() {
        return contents.entrySet();
    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return contents.equals(obj);
    }

    @Override
    public String toString() {
        return contents.toString();
    }

    @Override
    public String getURI() {
        return uri;
    }

    @Override
    public void add(AttributeType type) {
        put(type.getName(), type);
    }

    @Override
    public Schema profile(Set<Name> profile) {
        return new ProfileImpl(this, profile);
    }
}
