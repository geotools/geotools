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

import junit.framework.TestCase;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class AttributeExpressionTest extends TestCase {

	public void testFeature() {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		
		typeBuilder.setName( "test" );
		typeBuilder.setNamespaceURI( "http://www.geotools.org/test" );
		typeBuilder.add( "foo", Integer.class );
		typeBuilder.add( "bar", Double.class );
		
		SimpleFeatureType type = typeBuilder.buildFeatureType();
		
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
		builder.add( new Integer( 1 ) );
		builder.add( new Double( 2.0 ) );

		SimpleFeature feature = builder.buildFeature( "fid" );
		
		AttributeExpressionImpl ex = new AttributeExpressionImpl( "foo" );
		assertEquals( new Integer( 1 ), ex.evaluate( feature ) );
		
		ex = new AttributeExpressionImpl( "@id" );
		assertEquals( "fid", ex.evaluate( feature ) );
	}
	
	
}
