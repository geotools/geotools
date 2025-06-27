/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;

/** Read contents from MemoryDataStore. */
public class MemoryFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    SimpleFeatureType featureType;
    Iterator<SimpleFeature> iterator;

    public MemoryFeatureReader(ContentState state, Query query) throws IOException {
        featureType = state.getFeatureType();
        MemoryEntry entry = (MemoryEntry) state.getEntry();

        final List<SimpleFeature> internalCollection =
                new ArrayList<>(entry.getMemory().values());
        iterator = internalCollection.iterator();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
        if (iterator == null) {
            throw new IOException("Feature Reader has been closed");
        }

        try {
            return SimpleFeatureBuilder.copy(iterator.next());
        } catch (NoSuchElementException end) {
            throw new DataSourceException("There are no more Features", end);
        }
    }

    @Override
    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    @Override
    public void close() {
        if (iterator != null) {
            iterator = null;
        }

        if (featureType != null) {
            featureType = null;
        }
    }
}
