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
package org.geotools.util;

import junit.framework.TestCase;

public class BooleanConverterFactoryTest extends TestCase {

	BooleanConverterFactory factory;
	
	protected void setUp() throws Exception {
		factory = new BooleanConverterFactory();
	}
	
	public void testFromString() throws Exception {
		assertEquals( Boolean.TRUE, convert( "true" ) );
		assertEquals( Boolean.TRUE, convert( "1" ) );
		assertEquals( Boolean.FALSE, convert( "false" ) );
		assertEquals( Boolean.FALSE, convert( "0" ) );
		
	}
	
	public void testFromInteger() throws Exception {
		assertEquals( Boolean.TRUE, convert( new Integer( 1 ) ) );
		assertEquals( Boolean.FALSE, convert( new Integer( 0 ) ) );
	}
	
	Boolean convert( Object value ) throws Exception {
		return (Boolean) factory.createConverter( value.getClass(), Boolean.class, null ).convert( value, Boolean.class );
	}
}
