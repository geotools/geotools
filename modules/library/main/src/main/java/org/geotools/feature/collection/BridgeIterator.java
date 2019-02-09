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
package org.geotools.feature.collection;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;

/**
 * A closeable iterator wrapped around a provided feature iterator.
 *
 * @see DataUtilities#iterator(FeatureIterator)
 * @see DataUtilities#close(Iterator)
 * @author Jody Garnett (LISAsoft)
 */
public class BridgeIterator<F extends Feature> implements Iterator<F>, Closeable {
    FeatureIterator<F> delegate;

    public BridgeIterator(FeatureIterator<F> features) {
        this.delegate = features;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public F next() {
        return delegate.next();
    }

    @Override
    public void remove() {}

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
