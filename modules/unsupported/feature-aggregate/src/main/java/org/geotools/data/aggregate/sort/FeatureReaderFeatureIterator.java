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
package org.geotools.data.aggregate.sort;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A simple feature iterator wrapping a feature reader
 */
class FeatureReaderFeatureIterator implements SimpleFeatureIterator {
    SimpleFeatureReader reader;

    public FeatureReaderFeatureIterator(SimpleFeatureReader reader) {
        this.reader = reader;
    }

    public boolean hasNext() {
        try {
            return reader.hasNext();
        } catch (IOException e) {
            throw new RuntimeException("Reader failed", e);
        }
    }

    public SimpleFeature next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return reader.next();
        } catch (Exception e) {
            throw new RuntimeException("Reader failed", e);
        }
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            // we tried, n problem
        }
    }
};
