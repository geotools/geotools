/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.data.SimpleFeatureReader;

/**
 * Gives a {@link SimpleFeatureReader} interface to a feature collection, for compatibility with
 * {@link org.geotools.data.store.ContentFeatureSource}.
 */
public class CollectionReader implements SimpleFeatureReader {
    private final SimpleFeatureCollection fc;
    private final SimpleFeatureIterator iterator;

    public CollectionReader(SimpleFeatureCollection fc) {
        this.fc = fc;
        this.iterator = fc.features();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return fc.getSchema();
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public void close() {
        iterator.close();
    }
}
