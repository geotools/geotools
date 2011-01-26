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
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.expression.Expression;

public class FilterFactoryBeforeTest extends TestCase {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testAfter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        
        Expression left = ff.literal(2);
        Expression right = ff.literal(1);
        
        PropertyIsGreaterThan filter = ff.greater( left, right );
        
        assertTrue( filter.evaluate( null ) );
        assertTrue( filter instanceof PropertyIsGreaterThan );
    }
}
