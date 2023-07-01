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

import static org.junit.Assert.assertNotEquals;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Literal;

/**
 * Tests the literal expressions.
 *
 * @author James Macgill
 */
public class LiteralTest {

    FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    @Test
    public void testValidConstruction() throws Exception {
        new LiteralExpressionImpl(Double.valueOf(10));
        new LiteralExpressionImpl("Label");
        new LiteralExpressionImpl(Integer.valueOf(10));
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        new LiteralExpressionImpl(gf.createGeometryCollection(null));
    }

    @Test
    public void testConversion() throws Exception {
        Assert.assertEquals("abc", ff.literal("abc").evaluate(null));
        Assert.assertEquals(Integer.valueOf(12), ff.literal("12").evaluate(null, Integer.class));
        Assert.assertEquals(Double.valueOf(12.0), ff.literal("12.0").evaluate(null, Double.class));
        Assert.assertEquals(Double.valueOf(12.5), ff.literal("12.5").evaluate(null, Double.class));
        Assert.assertEquals(
                Long.valueOf(Long.MAX_VALUE),
                ff.literal(Long.MAX_VALUE + "").evaluate(null, Long.class));
        BigInteger doubleMaxLong =
                BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
        Assert.assertEquals(
                doubleMaxLong,
                ff.literal(doubleMaxLong.toString()).evaluate(null, BigInteger.class));
    }

    @Test
    public void testDateEquality() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 6, 15);
        Date d1 = cal.getTime();
        cal.set(2012, 8, 15);
        Date d2 = cal.getTime();
        Date d3 = cal.getTime();
        Literal l1 = ff.literal(d1);
        Literal l2 = ff.literal(d2);
        assertNotEquals(l1, l2);

        Literal l3 = ff.literal(d3);
        Assert.assertEquals(l2, l3);
    }
}
