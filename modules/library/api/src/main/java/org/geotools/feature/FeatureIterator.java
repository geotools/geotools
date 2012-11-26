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
package org.geotools.feature;

import java.io.Closeable;

import org.opengis.feature.Feature;

/**
 * Streaming access to features, required to {@link #close()} after use.
 * <p>
 * FeatureIterator is a drop in replacement for Iterator<Feature> supporting a close method.
 * <p>
 * Sample use:<pre> FeatureIterator<SimpleFeature> i = featureCollection.features()
 * try {
 *    while( i.hasNext() ){
 *        SimpleFeature feature = i.next();
 *    }
 * }
 * finally {
 *    i.close();
 * }
 * </pre>
 * @author Ian Schneider
 *
 * @source $URL$
 */
public interface FeatureIterator<F extends Feature> extends Closeable {
    /**
     * Does another Feature exist in this Iteration.
     * <p>
     * Iterator defin: Returns true if the iteration has more elements. (In other words, returns true if next would return an element rather than throwing an exception.)
     * </p>
     * @return true if more Features exist, false otherwise.
     */
    public boolean hasNext();

    /**
     * Get the next Feature in this iteration.
     *
     * @return The next Feature
     *
     * @throws java.util.NoSuchElementException If no more Features exist.
     */
    public F next() throws java.util.NoSuchElementException;

    /**
     * Closes this iterator and releases any system resources associated
     * with it.
     */
    public void close(); // default implementation here does not throw IOException
}