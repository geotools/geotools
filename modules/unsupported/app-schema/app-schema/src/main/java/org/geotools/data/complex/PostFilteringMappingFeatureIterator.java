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

package org.geotools.data.complex;

import java.util.NoSuchElementException;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.filter.Filter;

/**
 * An extension to {@linkplain org.geotools.data.complex.DataAccessMappingFeatureIterator} where
 * filter is present. Unlike with FilteringMappingFeatureIterator The filter is applied on
 * the complex feature
 * 
 * @author Niels Charlier, Curtin University of Technology
 */
public class PostFilteringMappingFeatureIterator implements IMappingFeatureIterator {

    protected FeatureIterator<Feature> delegate;
    protected Feature next;
    protected Filter filter;
    protected int maxFeatures;
    protected int count = 0;
    
    public PostFilteringMappingFeatureIterator(FeatureIterator<Feature> iterator, Filter filter, int maxFeatures) {
        this.delegate = iterator;
        this.filter = filter;
        this.maxFeatures = maxFeatures;
        next = getFilteredNext();
    }

    public void close() {
        delegate.close();        
    } 
  
    protected Feature getFilteredNext() {
        while (delegate.hasNext() && count < maxFeatures) {
            Feature feature = delegate.next();
            if (filter.evaluate(feature)) {
                return feature;
            }
        }
        return null;
    }

    public boolean hasNext() {    
        return next != null;
    }
        
    public Feature next() {
        if(next == null){
            throw new NoSuchElementException();
        }
        
        count++;
        Feature current = next;
        next = getFilteredNext();
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException();
        
    }

}
