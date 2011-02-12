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

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;

public class IsNotEqualToImpltest extends TestCase {

	org.opengis.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory( null );
	
	public void testOperandsSameType() {
		Expression e1 = filterFactory.literal( 1 );
		Expression e2 = filterFactory.literal( 2 );
		
		PropertyIsNotEqualTo notEqual = filterFactory.notEqual( e1, e2, true );
		assertTrue( notEqual.evaluate( null ) );
	}
	
	public void testOperandsDifferentType() {
		Expression e1 = filterFactory.literal( 1 );
		Expression e2 = filterFactory.literal( "2"
				);
		
		PropertyIsNotEqualTo notEqual = filterFactory.notEqual( e1, e2, true );
		assertTrue( notEqual.evaluate( null ) );
	}
	
	public void testOperandsIntDouble() {
        Expression e1 = filterFactory.literal( 1 );
        Expression e2 = filterFactory.literal( "1.0" );
        
        PropertyIsNotEqualTo notEqual = filterFactory.notEqual( e1, e2, true );
        assertFalse( notEqual.evaluate( null ) );
    }
	
	public void testCaseSensitivity() {
		Expression e1 = filterFactory.literal( "foo" );
		Expression e2 = filterFactory.literal( "FoO" );
		
		PropertyIsNotEqualTo caseSensitive = filterFactory.notEqual( e1, e2, true );
		assertTrue( caseSensitive. evaluate( null ) );
		
		PropertyIsNotEqualTo caseInsensitive = filterFactory.notEqual( e1, e2, false );
		assertFalse( caseInsensitive. evaluate( null ) );
		
	}
}
