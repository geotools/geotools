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
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Tests the literal expressions.
 *
 * @author James Macgill
 *
 *
 * @source $URL$
 */
public class LiteralTest extends TestCase {
    
    FilterFactory ff;

    @Override
    protected void setUp() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
    }
    
    public void testValidConstruction() throws Exception {
        Literal a = new LiteralExpressionImpl(new Double(10));
        Literal b = new LiteralExpressionImpl("Label");
        Literal c = new LiteralExpressionImpl(new Integer(10));
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Literal d = new LiteralExpressionImpl(gf.createGeometryCollection(null));
    }

    public void testInvalidConstruction1() throws Exception {
        try {
            Literal a = new LiteralExpressionImpl(new Double(10));
            Literal b = new LiteralExpressionImpl(a);
        } catch (IllegalFilterException ife) {
            return;
        }
    }
    
    public void testConversion() throws Exception {
        assertEquals("abc", ff.literal("abc").evaluate(null));
        assertEquals(new Integer(12), ff.literal("12").evaluate(null, Integer.class));
        assertEquals(new Double(12.0), ff.literal("12.0").evaluate(null, Double.class));
        assertEquals(new Double(12.5), ff.literal("12.5").evaluate(null, Double.class));
        assertEquals(new Long(Long.MAX_VALUE), ff.literal(Long.MAX_VALUE + "").evaluate(null, Long.class));
        BigInteger doubleMaxLong = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
        assertEquals(doubleMaxLong, ff.literal(doubleMaxLong.toString()).evaluate(null, BigInteger.class));
    }
    
    public void testDateEquality() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 6, 15);
        Date d1 = cal.getTime();
        cal.set(2012, 8, 15);
        Date d2 = cal.getTime();
        Date d3 = cal.getTime();
        Literal l1 = ff.literal(d1);
        Literal l2 = ff.literal(d2);
        assertFalse(l1.equals(l2));
        
        Literal l3 = ff.literal(d3);
        assertTrue(l2.equals(l3));
    }
}
