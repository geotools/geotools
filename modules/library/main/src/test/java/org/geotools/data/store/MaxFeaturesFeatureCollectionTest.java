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

import java.util.Iterator;

import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class MaxFeaturesFeatureCollectionTest extends
		FeatureCollectionWrapperTestSupport {

	MaxFeaturesFeatureCollection<SimpleFeatureType, SimpleFeature> max;
	
	protected void setUp() throws Exception {
		super.setUp();
		max = new MaxFeaturesFeatureCollection<SimpleFeatureType, SimpleFeature>( delegate, 2 );
	}
	
	public void testSize() throws Exception {
		assertEquals( 2, max.size() );
	}
	
	public void testIterator() throws Exception {
	
		Iterator i = max.iterator();
		for ( int x = 0; x < 2; x++ ) {
			assertTrue( i.hasNext() );
			i.next();
		}
		
		assertFalse( i.hasNext() );
	}
}
