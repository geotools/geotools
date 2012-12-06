/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * A convenience class for dealing with wrapping a Collection Iterator up
 * as a FeatureIterator.
 * <p>
 * Note this does not implement Iterator (FeatureIterator is a separate class).
 * <p>
 * This class will check to see if the provided Iterator implements {@link Closeable}.
 * @author Ian Schneider
 *
 * @source $URL$
 */
public class FeatureIteratorImpl<F extends Feature> implements FeatureIterator<F> {
     /** The iterator from the SimpleFeatureCollection to return features from. */
     Iterator<F> iterator;
     Collection<F> collection;
    /**
     * Create a new SimpleFeatureIterator using the Iterator from the given
     * FeatureCollection.
     *
     * @param collection The SimpleFeatureCollection to perform the iteration on.
     */
    public FeatureIteratorImpl(Collection<F> collection) {
        this.collection = collection;
        this.iterator = collection.iterator();
    }

    /**
     * Does another Feature exist in this Iteration.
     * <p>
     * Iterator defin: Returns true if the iteration has more elements. (In other words, returns true if next would return an element rather than throwing an exception.)
     * </p>
     * @return true if more Features exist, false otherwise.
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Get the next Feature in this iteration.
     *
     * @return The next Feature
     *
     * @throws java.util.NoSuchElementException If no more Features exist.
     */
    public F next() throws java.util.NoSuchElementException {
        return (F) iterator.next();
    }
    /**
     * Required so SimpleFeatureCollection classes can implement close( SimpleFeatureIterator ).
     */
    public void close(){
        if( iterator != null){
            DataUtilities.close( iterator );
            iterator = null;
            collection = null;
        }
    }
}
