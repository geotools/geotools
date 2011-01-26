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

package org.geotools.data;

import java.util.Iterator;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;

/**
 * Decorator for Iterator&lt;Feature&gt; to provide a FeatureIterator&lt;Feature&gt; that adds a
 * close method that does nothing. This class exists only to satisfy the {@link FeatureCollection}
 * API. All iteration is performed using the iterator passed to the constructor.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.6
 */

public class SampleDataAccessFeatureIterator implements FeatureIterator<Feature> {

    private final Iterator<Feature> iterator;

    /**
     * Constructor.
     * 
     * @param iterator
     *            iterator to which all iteration is delegated.
     */
    public SampleDataAccessFeatureIterator(Iterator<Feature> iterator) {
        this.iterator = iterator;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * @see java.util.Iterator#next()
     */
    public Feature next() {
        return iterator.next();
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        iterator.remove();
    }

    /**
     * Nothing to close.
     * 
     * @see org.geotools.feature.FeatureIterator#close()
     */
    public void close() {
        // do nothing
    }

}
