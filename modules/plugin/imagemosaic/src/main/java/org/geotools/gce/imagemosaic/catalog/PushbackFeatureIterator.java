/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A feature iterator allowing to push back one feature (will be used to map the results of a join)
 *
 * @author Andrea Aime - GeoSolutions
 */
class PushbackFeatureIterator implements SimpleFeatureIterator {

    SimpleFeatureIterator delegate;

    SimpleFeature last;

    SimpleFeature current;

    public PushbackFeatureIterator(SimpleFeatureIterator delegate) {
        this.delegate = delegate;
    }

    public boolean hasNext() {
        return current != null || delegate.hasNext();
    }

    public SimpleFeature next() throws NoSuchElementException {
        if (current != null) {
            last = current;
            current = null;
        } else {
            last = delegate.next();
        }

        return last;
    }

    /**
     * Pushes back the last feature returned by next(). Will throw an {@link IllegalStateException}
     * if there is no feature to push back. Only a single pushBack call can be performed between two
     * calls to next()
     */
    public void pushBack() {
        if (last != null) {
            current = last;
            last = null;
        } else {
            throw new IllegalStateException("There is no feature to push back");
        }
    }

    public void close() {
        delegate.close();
    }
}
