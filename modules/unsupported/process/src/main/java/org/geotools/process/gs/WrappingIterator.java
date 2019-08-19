/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2007 TOPP - www.openplans.org.
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
package org.geotools.process.gs;

import java.util.Iterator;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * An iterator wrapping a {@link SimpleFeatureIterator} and exposing its close method.
 *
 * @author Andrea Aime - OpenGeo
 */
public class WrappingIterator implements Iterator<SimpleFeature>, SimpleFeatureIterator {
    SimpleFeatureIterator delegate;

    public WrappingIterator(SimpleFeatureIterator delegate) {
        super();
        this.delegate = delegate;
    }

    public boolean hasNext() {
        return delegate.hasNext();
    }

    public SimpleFeature next() {
        return delegate.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close() {
        delegate.close();
    }
}
