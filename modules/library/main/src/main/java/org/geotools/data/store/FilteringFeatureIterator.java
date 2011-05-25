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
package org.geotools.data.store;

import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 * Decorates a SimpleFeatureIterator  with one that filters content.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FilteringFeatureIterator implements SimpleFeatureIterator {

    /**
     * delegate iterator
     */
    protected SimpleFeatureIterator delegate;
    /**
     * The Filter
     */
    protected Filter filter;
    /**
     * Next feature
     */
    protected SimpleFeature next;
    
    public FilteringFeatureIterator( SimpleFeatureIterator delegate, Filter filter ) {
        this.delegate = delegate;
        this.filter = filter;
    }
    
    public boolean hasNext() {
        if ( next != null ) {
            return true;
        }
        
        while( delegate.hasNext() ) {
            SimpleFeature peek = (SimpleFeature) delegate.next();
            if ( filter.evaluate( peek ) ) {
                next = peek;
                break;
            }
        }
        
        return next != null;
    }

    public SimpleFeature next() throws NoSuchElementException {
        SimpleFeature f = next;
        next = null;
        return f;
    }
    
    public void close() {
        delegate.close();
        delegate = null;
        next = null;
        filter = null;
    }


}
