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

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * A convenience class for dealing with FeatureCollection Iterators. DOES NOT
 * implement Iterator.
 * <p>
 * We are sorry but this does not implement Iteartor<Feature>, although it should
 * be a drop in replacement when Geotools is able to upgrade to Java 5.
 * </p>
 * @author Ian Schneider
 * @source $URL$
 */
public class FeatureIteratorImpl<F extends Feature> implements FeatureIterator<F> {
    /** The iterator from the FeatureCollection<SimpleFeatureType, SimpleFeature> to return features from. */
     java.util.Iterator<F> iterator;
     FeatureCollection<? extends FeatureType, F> collection;
     
    /**
     * Create a new FeatureIterator<SimpleFeature> using the Iterator from the given
     * FeatureCollection.
     *
     * @param collection The FeatureCollection<SimpleFeatureType, SimpleFeature> to perform the iteration on.
     */
    public FeatureIteratorImpl(FeatureCollection<? extends FeatureType, F> collection) {
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
     * Required so FeatureCollection<SimpleFeatureType, SimpleFeature> classes can implement close( FeatureIterator<SimpleFeature> ).
     */
    public void close(){
        if( iterator != null ){
            collection.close( iterator );
            iterator = null;
            collection = null;
        }
    }
}
