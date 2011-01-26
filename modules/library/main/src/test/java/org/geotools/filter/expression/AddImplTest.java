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

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

public class AddImplTest extends TestCase {

	AddImpl add; 
	
	protected void setUp() throws Exception {
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		Expression e1 = ff.literal( 1 );
		Expression e2 = ff.literal( 2 );
		
		add = new AddImpl( e1, e2 );
	}
	
	public void testEvaluate() {
		Object result = add.evaluate( null );
		assertEquals( new Double( 3 ), result );
	}
	
	public void testEvaluateAsInteger() {
		Object result = add.evaluate( null, Integer.class );
		assertEquals( new Integer( 3 ), result );
	}
	
	public void testEvaluateAsString( ) {
		Object result = add.evaluate( null, String.class );
		assertEquals( "3.0", result );
	}
}
