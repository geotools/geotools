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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DelegateSimpleFeatureIterator;

/**
 * FeatureReader<SimpleFeatureType, SimpleFeature> that reads features from a java.util.collection
 * of features, an array of features or a FeatureCollection.
 *
 * @author jones
 */
public class CollectionFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private SimpleFeatureIterator features;
    private SimpleFeatureType type;
    private boolean closed = false;

    /**
     * Create a new instance.
     *
     * @param featuresArg a colleciton of features. <b>All features must be of the same
     *     FeatureType</b>
     * @param typeArg the Feature type of of the features.
     */
    public CollectionFeatureReader(
            Collection<SimpleFeature> featuresArg, SimpleFeatureType typeArg) {
        assert !featuresArg.isEmpty();
        this.features = new DelegateSimpleFeatureIterator(featuresArg.iterator());
        this.type = typeArg;
    }

    /**
     * Create a new instance.
     *
     * @param featuresArg a FeatureCollection. <b>All features must be of the same FeatureType</b>
     * @param typeArg the Feature type of of the features.
     */
    public CollectionFeatureReader(SimpleFeatureCollection featuresArg, SimpleFeatureType typeArg) {
        assert !featuresArg.isEmpty();
        this.features = featuresArg.features();
        this.type = typeArg;
    }

    /**
     * Create a new instance.
     *
     * @param featuresArg an of features. <b>All features must be of the same FeatureType</b>
     */
    public CollectionFeatureReader(SimpleFeature... featuresArg) {
        assert featuresArg.length > 0;
        Iterator<SimpleFeature> iterator = Arrays.asList(featuresArg).iterator();
        this.features = new DelegateSimpleFeatureIterator(iterator);
        type = featuresArg[0].getFeatureType();
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        return type;
    }

    /** @see FeatureReader#next() */
    @Override
    public SimpleFeature next()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        if (closed) {
            throw new NoSuchElementException("Reader has been closed");
        }

        return features.next();
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() throws IOException {
        return features != null && features.hasNext() && !closed;
    }

    /** @see FeatureReader#close() */
    @Override
    public void close() throws IOException {
        closed = true;

        if (features != null) {
            features.close();
            features = null;
        }
    }
}
