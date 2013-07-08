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

import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A simple wrapper that limits the number of features returned by a given
 * {@link SimpleFeatureIterator}
 * 
 * @author Andrea Aime - GeoSolutions
 */
class MaxFeaturesIterator implements SimpleFeatureIterator {

    SimpleFeatureIterator wrapped;

    int count;

    public MaxFeaturesIterator(SimpleFeatureIterator wrapped, int count) {
        this.wrapped = wrapped;
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return count > 0 && wrapped.hasNext();
    }

    @Override
    public SimpleFeature next() throws NoSuchElementException {
        SimpleFeature result = wrapped.next();
        count--;
        return result;
    }

    @Override
    public void close() {
        wrapped.close();
    }

}
