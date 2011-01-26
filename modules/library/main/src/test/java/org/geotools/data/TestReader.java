/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 * @source $URL$
 */
class TestReader implements FeatureReader<SimpleFeatureType, SimpleFeature>{

    /**
	 * 
	 */
	private SimpleFeatureType type;
	private SimpleFeature feature;

    public TestReader(SimpleFeatureType type, SimpleFeature f) {
        this.type = type;
		this.feature=f;
    }
    
    public SimpleFeatureType getFeatureType() {
        return type;
    }

    public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
        next=false;
        return feature;
    }

    boolean next=true;
    public boolean hasNext() throws IOException {
        return next;
    }

    public void close() throws IOException {
    }
    
}
