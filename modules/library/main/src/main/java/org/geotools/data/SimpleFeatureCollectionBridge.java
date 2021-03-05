/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * This class is a bridge between a FeatureCollection<SimpleFeatureType,SimpleFeature> and the
 * SimpleFeatureCollection interface.
 *
 * <p>This class is package visbile and can only be created by DataUtilities.simple(
 * featureCollection ); it is under lock and key so that we can safely do an instance of check and
 * not get multiple wrappers piling up.
 *
 * @author Jody
 * @since 2.7
 */
class SimpleFeatureCollectionBridge implements SimpleFeatureCollection {

    private FeatureCollection<SimpleFeatureType, SimpleFeature> collection;

    public SimpleFeatureCollectionBridge(
            FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection) {
        if (featureCollection == null) {
            throw new NullPointerException("FeatureCollection required");
        }
        if (featureCollection instanceof SimpleFeatureCollection) {
            throw new IllegalArgumentException("Already a SimpleFeatureCollection");
        }
        this.collection = featureCollection;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // closed in the wrapper
    public SimpleFeatureIterator features() {
        final FeatureIterator<SimpleFeature> features = collection.features();
        return new SimpleFeatureIterator() {
            @Override
            public SimpleFeature next() throws NoSuchElementException {
                return features.next();
            }

            @Override
            public boolean hasNext() {
                return features.hasNext();
            }

            @Override
            public void close() {
                features.close();
            }
        };
    }

    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        return new SimpleFeatureCollectionBridge(collection.sort(order));
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        return new SimpleFeatureCollectionBridge(collection.subCollection(filter));
    }

    @Override
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        collection.accepts(visitor, progress);
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> o) {
        return collection.containsAll(o);
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return collection.getBounds();
    }

    @Override
    public String getID() {
        return collection.getID();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return collection.getSchema();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <O> O[] toArray(O[] a) {
        return collection.toArray(a);
    }
}
