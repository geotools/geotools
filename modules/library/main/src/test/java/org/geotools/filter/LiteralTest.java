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
 *
 *    Created on June 21, 2002, 12:24 PM
 */
package org.geotools.filter;


import java.math.BigInteger;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Tests the literal expressions.
 *
 * @author James Macgill
 * @source $URL$
 */
public class LiteralTest extends TestCase {
    
    FilterFactory ff;

    @Override
    protected void setUp() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
    }
    
    public void testValidConstruction() throws Exception {
        LiteralExpression a = new LiteralExpressionImpl(new Double(10));
        LiteralExpression b = new LiteralExpressionImpl("Label");
        LiteralExpression c = new LiteralExpressionImpl(new Integer(10));
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        LiteralExpression d = new LiteralExpressionImpl(gf.createGeometryCollection(null));
    }

    public void testInvalidConstruction1() throws Exception {
        try {
            LiteralExpression a = new LiteralExpressionImpl(new Double(10));
            LiteralExpression b = new LiteralExpressionImpl(a);
        } catch (IllegalFilterException ife) {
            return;
        }
    }
    
    public void testConversion() throws Exception {
        assertEquals("abc", ff.literal("abc").evaluate(null));
        assertEquals(new Integer(12), ff.literal("12").evaluate(null));
        assertEquals(new Double(12.0), ff.literal("12.0").evaluate(null));
        assertEquals(new Double(12.5), ff.literal("12.5").evaluate(null));
        assertEquals(new Long(Long.MAX_VALUE), ff.literal(Long.MAX_VALUE + "").evaluate(null));
        BigInteger doubleMaxLong = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
        assertEquals(doubleMaxLong, ff.literal(doubleMaxLong.toString()).evaluate(null));

    }
}
