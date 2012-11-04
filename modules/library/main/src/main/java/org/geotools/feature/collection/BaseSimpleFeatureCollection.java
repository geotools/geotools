/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.EmptyFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

public abstract class BaseSimpleFeatureCollection extends
    BaseFeatureCollection<SimpleFeatureType, SimpleFeature> implements SimpleFeatureCollection {
    
    protected BaseSimpleFeatureCollection(SimpleFeatureType schema) {
        super( schema );
    }
    
    /**
     * Subclasses required to implement this method to traverse FeatureCollection contents.
     * <p>
     * Note that {@link SimpleFeatureIterator#close()} is available to clean up
     * after any resource use required during traversal.
     */
    public abstract SimpleFeatureIterator features();
    
    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        if( filter == Filter.EXCLUDE) {
            return new EmptyFeatureCollection(schema);
        }
        // Formally new SubFeatureCollection(this, filter);
        return new FilteringSimpleFeatureCollection( this, filter );        
    }

    public SimpleFeatureCollection sort(SortBy order) {
        // Formally new SubFeatureList(this, order);
        return new SortedSimpleFeatureCollection(this,new SortBy[] { order });
    }
}
