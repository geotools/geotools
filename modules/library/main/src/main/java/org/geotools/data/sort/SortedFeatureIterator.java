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
package org.geotools.data.sort;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

import org.geotools.data.simple.DelegateSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;

/**
 * Sorts features based on the specified sorting order
 *
 * @source $URL$
 */
public class SortedFeatureIterator implements SimpleFeatureIterator {

    FeatureReaderFeatureIterator delegate;

    /**
     * Checks if the schema and the sortBy are suitable for merge/sort. All attributes need to be
     * {@link Serializable}, all sorting attributes need to be {@link Comparable}
     * 
     * @param schema
     * @param sortBy
     * @return
     */
    public static final boolean canSort(SimpleFeatureType schema, SortBy[] sortBy) {
        return MergeSortDumper.canSort(schema, sortBy);
    }

    public SortedFeatureIterator(SimpleFeatureIterator iterator, SimpleFeatureType schema,
            SortBy[] sortBy, int maxFeatures) throws IOException {
        DelegateSimpleFeatureReader reader = new DelegateSimpleFeatureReader(schema, iterator);
        SimpleFeatureReader sorted = new SortedFeatureReader(reader, sortBy, maxFeatures);
        this.delegate = new FeatureReaderFeatureIterator(sorted);
    }

    public boolean hasNext() {
        return delegate.hasNext();
    }

    public SimpleFeature next() throws NoSuchElementException {
        return delegate.next();
    }

    public void close() {
        delegate.close();

    }
}
