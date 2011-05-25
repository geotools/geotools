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

import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.DelegateSimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * SimpleFeatureCollection decorator which decorates a feature collection "re-typing" 
 * its schema based on attributes specified in a query.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class ReTypingFeatureCollection extends DecoratingSimpleFeatureCollection {

	SimpleFeatureType featureType;
    
	public ReTypingFeatureCollection ( FeatureCollection<SimpleFeatureType,SimpleFeature> delegate, SimpleFeatureType featureType ) {
	    this( DataUtilities.simple( delegate), featureType );
	}
	public ReTypingFeatureCollection ( SimpleFeatureCollection delegate, SimpleFeatureType featureType ) {
		super(delegate);
		this.featureType = featureType;
	}
	
	public SimpleFeatureType getSchema() {
	    return featureType;
	}
	
	public  FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
		return new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>( getSchema(), features() );
	}
	
	public SimpleFeatureIterator features() {
		return new DelegateSimpleFeatureIterator( this, iterator() );
	}

	public void close(SimpleFeatureIterator close) {
		close.close();
	}

	public Iterator<SimpleFeature> iterator() {
		return new ReTypingIterator( delegate.iterator(), delegate.getSchema(), featureType );
	}
	
	public void close(Iterator close) {
		ReTypingIterator reType = (ReTypingIterator) close;
		delegate.close( reType.getDelegate() );
	}
}
