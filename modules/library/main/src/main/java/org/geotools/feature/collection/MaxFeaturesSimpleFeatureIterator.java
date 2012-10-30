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
package org.geotools.feature.collection;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * SimpleFeatureIterator wrapper which can use a start and end bounds to
 * cap the number of returned features.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/main/src/main/java/org/geotools/
 *         data/store/MaxFeaturesIterator.java $
 */
public class MaxFeaturesSimpleFeatureIterator implements SimpleFeatureIterator {

    SimpleFeatureIterator delegate;

    long start;
    long end;
    long counter;

    public MaxFeaturesSimpleFeatureIterator(SimpleFeatureIterator iterator, long max) {
        this( iterator, 0, max);
    }

    public MaxFeaturesSimpleFeatureIterator(SimpleFeatureIterator delegate, long start, long max) {
        this.delegate = delegate;
        this.start = start;
        this.end = start + max;
        counter = 0;
    }

    public SimpleFeatureIterator getDelegate() {
        return delegate;
    }

    public boolean hasNext() {
        if (counter < start) {
            // skip to just before start if needed
            skip();
        }
        return delegate.hasNext() && counter < end;
    }

    public SimpleFeature  next() {
        if (counter < start) {
            // skip to just before start if needed
            skip();
        }
        if (counter <= end) {
            counter++;
            SimpleFeature next = delegate.next();
            return next;
        }
        return null;
    }

    private void skip() {
        if (counter < start) {
            while (delegate.hasNext() && counter < start) {
                counter++;
                @SuppressWarnings("unused")
                SimpleFeature skip = delegate.next(); // skip!
            }
        }
    }

    @Override
    public void close() {
        delegate.close();
    }
}
