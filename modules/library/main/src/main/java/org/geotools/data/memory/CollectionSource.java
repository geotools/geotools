/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.capability.FilterCapabilities;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;

/**
 * Used to quickly adapt a collection for APIs expecting to be able to query generic content.
 *
 * <p>Please note that this is read-only access.
 *
 * @author Jody Garnett
 */
public final class CollectionSource<T> {

    private Collection<T> collection;
    private CoordinateReferenceSystem crs;

    public CollectionSource(Collection<T> collection) {
        this(collection, null);
    }

    public CollectionSource(Collection<T> collection, CoordinateReferenceSystem crs) {
        this.collection = Collections.unmodifiableCollection(collection);
        this.crs = crs;
    }

    public Collection<T> content() {
        return collection;
    }

    @SuppressWarnings("DoNotCallSuggester")
    public Collection<T> content(String query, String queryLanguage) {
        throw new UnsupportedOperationException("Please help me hook up the parser!");
    }

    public Collection<T> content(Filter filter) {
        return content(filter, Integer.MAX_VALUE);
    }

    public Collection<T> content(Filter filter, int countLimit) {
        List<T> list = new ArrayList<>();
        int count = 0;
        for (Iterator<T> i = collection.iterator(); i.hasNext() && count < countLimit; ) {
            T obj = i.next();
            if (filter.evaluate(obj)) {
                list.add(obj);
                count++;
            }
        }
        return Collections.unmodifiableList(list);
    }

    public Object describe() {
        return Object.class; // TODO: be more specific
    }

    public void dispose() {
        collection = null;
    }

    public FilterCapabilities getFilterCapabilities() {
        return null;
    }

    public Name getName() {
        return new NameImpl("localhost/memory");
    }

    public void setTransaction(Transaction t) {
        // ignored
    }

    public CoordinateReferenceSystem getCRS() {
        return crs;
    }
}
