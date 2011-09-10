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
import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Bridges between {@link FeatureReader<SimpleFeatureType, SimpleFeature>} and
 * {@link SimpleFeatureReader}
 */
class SimpleFeatureReaderBrige implements SimpleFeatureReader {

    FeatureReader<SimpleFeatureType, SimpleFeature> reader;

    public SimpleFeatureReaderBrige(FeatureReader<SimpleFeatureType, SimpleFeature> reader) {
        this.reader = reader;
    }

    public SimpleFeatureType getFeatureType() {
        return reader.getFeatureType();
    }

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        return reader.next();
    }

    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    public void close() throws IOException {
        reader.close();
    }

}
