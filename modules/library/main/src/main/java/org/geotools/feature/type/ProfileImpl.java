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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

/**
 * A "sub" Schema used to select types for a specific use.
 *
 * <p>This class uses a custom key set to subset a parent Schema, and is used as the return type of
 * {@link SchemaImpl.profile}.
 *
 * <p>This Schema is <b>not</b> mutable, serving only as a view, you may however define a more
 * specific subset if needed.
 *
 * <p>Schema is often used to place limitation on expressed content (as in the case of the GML Level
 * 0 Profile), or used to define a non conflicting set of "bindings" for the TypeBuilder(s).
 *
 * @author Jody Garnett, Refractions Research Inc.
 */
public class ProfileImpl implements Schema {

    /** Parent Schema */
    private Schema parent;

    /** Keyset used by this profile (immutable). */
    private Set<Name> profile;

    /** Profile contents (created in a lazy fashion). */
    private Map<Name, AttributeType> contents = null;

    /** Subset parent schema with profile keys. */
    public ProfileImpl(Schema parent, Set<Name> profile) {
        this.parent = parent;

        this.profile = Collections.unmodifiableSet(profile);
    }

    @Override
    public Set<Name> keySet() {
        return profile;
    }

    @Override
    public String getURI() {
        return parent.getURI();
    }

    @Override
    public Schema profile(Set<Name> profile) {
        if (!this.profile.containsAll(profile)) {
            Set<Name> set = new TreeSet<>(profile);
            set.removeAll(this.profile);
            throw new IllegalArgumentException("Unable to profile the following names: " + set);
        }
        return parent.profile(profile);
    }

    @Override
    public int size() {
        return profile.size();
    }

    @Override
    public boolean isEmpty() {
        return profile.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return profile.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public AttributeType get(Object key) {
        if (profile.contains(key)) {
            return parent.get(key);
        }
        return null;
    }

    @Override
    public AttributeType put(Name key, AttributeType value) {
        throw new UnsupportedOperationException("Profile not mutable");
    }

    @Override
    public AttributeType remove(Object key) {
        throw new UnsupportedOperationException("Profile not mutable");
    }

    @Override
    public void putAll(Map<? extends Name, ? extends AttributeType> t) {
        throw new UnsupportedOperationException("Profile not mutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Profile not mutable");
    }

    @Override
    public void add(AttributeType type) {
        throw new UnsupportedOperationException("Profile not mutable");
    }

    // public Collection values() {
    @Override
    public Collection<AttributeType> values() {
        return contents().values();
    }

    // public Set<Name> entrySet() {
    @Override
    public Set<Entry<Name, AttributeType>> entrySet() {
        return contents().entrySet();
    }

    private synchronized Map<Name, AttributeType> contents() {
        if (contents == null) {
            contents = new LinkedHashMap<>();
            for (Name key : profile) {
                contents.put(key, parent.get(key));
            }
        }
        return contents;
    }
}
