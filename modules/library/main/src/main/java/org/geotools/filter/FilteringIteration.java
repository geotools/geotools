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
package org.geotools.filter;

import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.FilteringFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollectionIteration;
import org.geotools.feature.FeatureIterator;
import org.opengis.filter.Filter;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.PropertyDescriptor;


/**
 * Run through the provided collection only returning features that pass the provided
 * filter.
 *
 * @author Ian Schneider
 *
 *
 * @source $URL$
 * @deprecated Please use {@link FilteringFeatureCollection}
 */
public class FilteringIteration extends FeatureCollectionIteration {
    /**
     * Creates a new instance of FilteringIteration
     *
     * @param filter DOCUMENT ME!
     * @param collection DOCUMENT ME!
     */
    public FilteringIteration(org.opengis.filter.Filter filter, FeatureCollection<?,?> collection) {
        super(new FilterHandler(filter),
              new FilteringFeatureCollection(collection,filter) );
    }

    public static void filter(FeatureCollection<?,?> features, Filter filter) {
        FilteringIteration i = new FilteringIteration(filter, features);
        i.iterate();
    }

    protected void iterate(FeatureIterator<?> iterator) {
        ((FilterHandler) handler).iterator = iterator;
        super.iterate(iterator);
    }
    
    static class FilterHandler implements Handler {
        FeatureIterator<?> iterator;
        final org.opengis.filter.Filter filter;

        public FilterHandler(org.opengis.filter.Filter filter) {
            this.filter = filter;
        }

        public void endFeature(Feature f) {
        }

        public void endFeatureCollection( FeatureCollection<?,?> fc) {
        }

        public void handleAttribute(PropertyDescriptor type,
            Object value) {
        }

        public void handleFeature(Feature f) {
            if (!filter.evaluate(f)) {
                // iterator.remove();
                // this shoudl not occur
            }
        }

        public void handleFeatureCollection(
            FeatureCollection<?,?> fc) {
        }
    }
}
