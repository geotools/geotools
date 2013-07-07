/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Turns a {@link SimpleFeatureIterator} into a Iterator<SimpleFeature>
 * 
 * @author Andrea Aime - GeoSolutions
 *
 */
class SimpleFeatureIteratorIterator implements Iterator<SimpleFeature>, SimpleFeatureIterator {
    
    SimpleFeatureIterator fi;
    
    public SimpleFeatureIteratorIterator(SimpleFeatureIterator fi) {
        super();
        this.fi = fi;
    }

    @Override
    public boolean hasNext() {
        return fi.hasNext();
    }

    @Override
    public SimpleFeature next() {
        return fi.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void close() {
        fi.close();
    }

}
