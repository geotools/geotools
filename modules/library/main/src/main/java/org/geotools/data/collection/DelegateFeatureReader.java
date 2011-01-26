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
package org.geotools.data.collection;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 * A FeatureReader that wraps up a normal FeatureIterator.
 * <p>
 * This class is useful for faking (and testing) the Resource based
 * API against in memory datastructures. You are warned that to
 * complete the illusion that Resource based IO is occuring content
 * will be duplicated.
 * </p>
 * @author Jody Garnett, Refractions Research, Inc.
 * @source $URL$
 */
public class DelegateFeatureReader<T extends FeatureType, F extends Feature> implements FeatureReader<T, F> {
	FeatureIterator<F> delegate;
	T schema;
	
	public DelegateFeatureReader( T featureType, FeatureIterator<F> features ){
		this.schema = featureType;
		this.delegate = features;
	}
	
	public T getFeatureType() {
		return schema;
	}

	public F next() throws IOException, IllegalAttributeException, NoSuchElementException {
		if (delegate == null) {
            throw new IOException("Feature Reader has been closed");
        }		
        try {
        	F feature = delegate.next();
        	// obj = schema.duplicate( obj );
        	return feature;        	
        } catch (NoSuchElementException end) {
            throw new DataSourceException("There are no more Features", end);
        }		
	}

	public boolean hasNext() throws IOException {
		return delegate != null && delegate.hasNext();			
	}

	public void close() throws IOException {
		if( delegate != null ) delegate.close();
		delegate = null;
        schema = null;
	}
	

}
