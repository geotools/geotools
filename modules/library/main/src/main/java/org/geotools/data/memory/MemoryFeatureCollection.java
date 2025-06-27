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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.feature.collection.RandomFeatureAccess;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Implement a SimpleFeatureCollection by burning memory!
 *
 * <p>Contents are maintained in a sorted TreeMap by FID, this serves as a reference implementation when exploring the
 * SimpleFeatureCollection api.
 *
 * <p>This is similar to DefaultFeatureCollection, although additional methods are supported and test cases have been
 * written. Unlike DefaultFeatureCollection the type information must be known at construction time.
 *
 * @author Jody Garnett, Refractions Research
 */
public class MemoryFeatureCollection extends AbstractFeatureCollection
        implements RandomFeatureAccess, Collection<SimpleFeature> {
    TreeMap<String, SimpleFeature> contents = new TreeMap<>();

    public MemoryFeatureCollection(SimpleFeatureType schema) {
        super(schema);
    }

    @Override
    public boolean add(SimpleFeature o) {
        SimpleFeature feature = o;
        contents.put(feature.getID(), feature);
        return true;
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public MemoryIterator openIterator() {
        return new MemoryIterator(contents.values().iterator());
    }

    static class MemoryIterator implements Iterator<SimpleFeature>, SimpleFeatureIterator {
        Iterator<SimpleFeature> it;

        MemoryIterator(Iterator<SimpleFeature> iterator) {
            it = iterator;
        }

        @Override
        public void close() {
            it = null;
        }

        @Override
        public boolean hasNext() {
            if (it == null) {
                throw new IllegalStateException();
            }
            return it.hasNext();
        }

        @Override
        public SimpleFeature next() {
            if (it == null) {
                throw new IllegalStateException();
            }
            return it.next();
        }

        @Override
        public void remove() {
            it.remove();
        }
    }

    //
    // RandomFeatureAccess
    //
    @Override
    public SimpleFeature getFeatureMember(String id) throws NoSuchElementException {
        if (contents.containsKey(id)) {
            return contents.get(id);
        }
        throw new NoSuchElementException(id);
    }

    @Override
    public SimpleFeature removeFeatureMember(String id) {
        if (contents.containsKey(id)) {
            SimpleFeature old = contents.get(id);
            contents.remove(id);
            return old;
        }
        return null;
    }
    /** Calculates the bounds of the features without caching. */
    @Override
    public ReferencedEnvelope getBounds() {
        return DataUtilities.bounds(features());
    }

    @Override
    public boolean remove(Object o) {
        return contents.values().remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends SimpleFeature> c) {
        boolean changed = false;
        for (SimpleFeature feature : c) {
            boolean added = add(feature);
            if (!changed && added) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return contents.values().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return contents.values().retainAll(c);
    }

    @Override
    public void clear() {
        contents.clear();
    }
}
