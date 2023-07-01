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

import static org.junit.Assert.assertThrows;

import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.filter.expression.Function;

/**
 * Unit test for AndFunction
 *
 * @author Erwan Bocher, CNRS, 2020
 */
public class AndFunctionTest {

    @Test
    public void testAndFunction1() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function equalsTo = ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function andFunction = ff.function("and", equalsTo, equalsTo);
        Assert.assertTrue((Boolean) andFunction.evaluate(new Object()));
    }

    @Test
    public void testAndFunction2() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function equalsTo_left =
                ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function equalsTo_right =
                ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
        Function andFunction = ff.function("and", equalsTo_left, equalsTo_right);
        Assert.assertFalse((Boolean) andFunction.evaluate(new Object()));
    }

    @Test
    public void testAndFunction3() throws IllegalFilterException {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    FilterFactoryImpl ff = new FilterFactoryImpl();
                    Function abs_left = ff.function("abs", ff.literal(-12));
                    Function equalsTo_right =
                            ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
                    Function andFunction = ff.function("and", abs_left, equalsTo_right);
                    andFunction.evaluate(new Object());
                });
    }

    @Test
    public void testAndFunction4() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function geom =
                ff.function(
                        "geomFromWKT",
                        ff.literal("POLYGON ((150 330, 220 330, 220 230, 150 230, 150 330))"));
        Function geom_area = ff.function("area", geom);
        Function equalsTo_left = ff.function("greaterThan", geom_area, ff.literal(0));
        Function equalsTo_right =
                ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function andFunction = ff.function("and", equalsTo_left, equalsTo_right);
        Assert.assertTrue((Boolean) andFunction.evaluate(new Object()));
    }

    @Test
    public void testAndFunction5() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function geom =
                ff.function(
                        "geomFromWKT",
                        ff.literal("POLYGON ((150 330, 220 330, 220 230, 150 230, 150 330))"));
        Function geom_area = ff.function("area", geom);
        Function equalsTo_left = ff.function("greaterThan", geom_area, ff.literal(0));
        Function equalsTo_right =
                ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function andFunction = ff.function("and", equalsTo_left, equalsTo_right);
        Function if_then_elseFunction =
                ff.function("if_then_else", andFunction, ff.literal(10), ff.literal(-1));
        Assert.assertEquals(10, if_then_elseFunction.evaluate(new Object()));
    }
}
