/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

public class ExpressionTypeVisitorTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    ExpressionTypeVisitor visitor;

    SimpleFeatureType ft;

    @Before
    public void setup() throws Exception {
        ft =
                DataUtilities.createType(
                        "test",
                        "theGeom:LineString,b:java.lang.Byte,s:java.lang.Short,i:java.lang.Integer,l:java.lang.Long,d:java.lang.Double,label:String");
        visitor = new ExpressionTypeVisitor(ft);
    }

    @Test
    public void testNumbers() {
        Add add = ff.add(ff.literal(new Byte((byte) 1)), ff.property("s"));
        assertEquals(Short.class, add.accept(visitor, null));
        Multiply mul = ff.multiply(ff.property("l"), ff.property("s"));
        assertEquals(Long.class, mul.accept(visitor, null));
        Divide div = ff.divide(ff.literal(new BigInteger("10")), ff.property("s"));
        assertEquals(BigInteger.class, div.accept(visitor, null));
        Subtract sub = ff.subtract(ff.literal(new BigInteger("10")), ff.property("d"));
        assertEquals(BigDecimal.class, sub.accept(visitor, null));
    }

    @Test
    public void testFunction() {
        Function func = ff.function(("abs"), ff.literal(10));
        assertEquals(Integer.class, func.accept(visitor, null));
    }

    @Test
    public void testProperty() {
        PropertyName pn = ff.property("s");
        assertEquals(Short.class, pn.accept(visitor, null));
    }
}
