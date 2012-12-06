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

import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A convenience class for wrapping a Collection Iterator up as a SimpleFeatureCollection.
 * <p>
 * Note this does not implement Iterator (FeatureIterator is a separate class).
 * 
 * @author Ian Schneider
 *
 *
 * @source $URL$
 */
public class SimpleFeatureIteratorImpl extends FeatureIteratorImpl<SimpleFeature> implements SimpleFeatureIterator {
     
    /**
     * Create a new SimpleFeatureIterator using the Iterator from the given
     * FeatureCollection.
     *
     * @param collection The SimpleFeatureCollection to perform the iteration on.
     */
    public SimpleFeatureIteratorImpl(Collection<SimpleFeature> collection) {
    	super( collection );
    }

}
