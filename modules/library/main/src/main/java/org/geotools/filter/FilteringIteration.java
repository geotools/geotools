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

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollectionIteration;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;


/**
 * DOCUMENT ME!
 *
 * @author Ian Schneider
 * @source $URL$
 */
public class FilteringIteration extends FeatureCollectionIteration {
    /**
     * Creates a new instance of FilteringIteration
     *
     * @param filter DOCUMENT ME!
     * @param collection DOCUMENT ME!
     */
    public FilteringIteration(org.opengis.filter.Filter filter, FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        super(new FilterHandler(filter), collection);
    }

    public static void filter(FeatureCollection<SimpleFeatureType, SimpleFeature> features, Filter filter) {
        FilteringIteration i = new FilteringIteration(filter, features);
        i.iterate();
    }

    protected void iterate(Iterator iterator) {
        ((FilterHandler) handler).iterator = iterator;
        super.iterate(iterator);
    }

    static class FilterHandler implements Handler {
        Iterator iterator;
        final org.opengis.filter.Filter filter;

        public FilterHandler(org.opengis.filter.Filter filter) {
            this.filter = filter;
        }

        public void endFeature(SimpleFeature f) {
        }

        public void endFeatureCollection(
            org.geotools.feature.FeatureCollection<SimpleFeatureType, SimpleFeature> fc) {
        }

        public void handleAttribute(AttributeDescriptor type,
            Object value) {
        }

        public void handleFeature(SimpleFeature f) {
            if (!filter.evaluate(f)) {
                iterator.remove();
            }
        }

        public void handleFeatureCollection(
            org.geotools.feature.FeatureCollection<SimpleFeatureType, SimpleFeature> fc) {
        }
    }
}
