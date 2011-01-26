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
package org.geotools.filter.expression;

import java.util.Map;

import junit.framework.TestCase;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class SimpleFeaturePropertyAccessorFactoryTest extends TestCase {

	SimpleFeaturePropertyAccessorFactory factory;
	
	protected void setUp() throws Exception {
		factory = new SimpleFeaturePropertyAccessorFactory();
	}
	
	public void test() {
		
		//make sure features are supported
		assertNotNull( factory.createPropertyAccessor( SimpleFeature.class, "xpath", null, null ) );
		assertNotNull( factory.createPropertyAccessor( SimpleFeatureType.class, "xpath", null, null ) );
		assertNull( factory.createPropertyAccessor( Map.class , "xpath", null, null ) );
		
		//make sure only simple xpath
		assertNull( factory.createPropertyAccessor( SimpleFeature.class, "@xpath", null, null )  );
		assertNull( factory.createPropertyAccessor( SimpleFeatureType.class, "@xpath", null, null )  );
		
		assertNull( factory.createPropertyAccessor( SimpleFeature.class, "/xpath", null, null ) );
		assertNull( factory.createPropertyAccessor( SimpleFeatureType.class, "/xpath", null, null ) );
		
		assertNull( factory.createPropertyAccessor( SimpleFeature.class, "*[0]", null, null ) );
		assertNull( factory.createPropertyAccessor( SimpleFeatureType.class, "*[0]", null, null ) );
	}
	
	
	
}
