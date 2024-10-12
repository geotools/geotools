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

import org.geotools.api.filter.expression.Function;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for OrFunction
 *
 * @author Erwan Bocher, CNRS, 2020
 */
public class OrFunctionTest {

    @Test
    public void testOrFunction1() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function equalsTo = ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function orFunction = ff.function("or", equalsTo, equalsTo);
        Assert.assertTrue((Boolean) orFunction.evaluate(new Object()));
    }

    @Test
    public void testOrFunction2() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function equalsTo_left = ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function equalsTo_right = ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
        Function orFunction = ff.function("or", equalsTo_left, equalsTo_right);
        Assert.assertTrue((Boolean) orFunction.evaluate(new Object()));
    }

    @Test
    public void testOrFunction3() throws IllegalFilterException {
        assertThrows(IllegalArgumentException.class, () -> {
            FilterFactoryImpl ff = new FilterFactoryImpl();
            Function abs_left = ff.function("abs", ff.literal(-12));
            Function equalsTo_right = ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
            Function orFunction = ff.function("or", abs_left, equalsTo_right);
            orFunction.evaluate(new Object());
        });
    }

    @Test
    public void testOrFunction4() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function geom =
                ff.function("geomFromWKT", ff.literal("POLYGON ((150 330, 220 330, 220 230, 150 230, 150 330))"));
        Function geom_area = ff.function("area", geom);
        Function equalsTo_left = ff.function("greaterThan", geom_area, ff.literal(0));
        Function equalsTo_right = ff.function("equalTo", ff.literal("string1"), ff.literal("string1"));
        Function orFunction = ff.function("or", equalsTo_left, equalsTo_right);
        Assert.assertTrue((Boolean) orFunction.evaluate(new Object()));
    }

    @Test
    public void testOrFunction5() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function geom =
                ff.function("geomFromWKT", ff.literal("POLYGON ((150 330, 220 330, 220 230, 150 230, 150 330))"));
        Function geom_area = ff.function("area", geom);
        Function equalsTo_left = ff.function("greaterThan", geom_area, ff.literal(0));
        Function equalsTo_right = ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
        Function orFunction = ff.function("or", equalsTo_left, equalsTo_right);
        Assert.assertTrue((Boolean) orFunction.evaluate(new Object()));
    }

    @Test
    public void testAndFunction6() throws IllegalFilterException {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Function geom =
                ff.function("geomFromWKT", ff.literal("POLYGON ((150 330, 220 330, 220 230, 150 230, 150 330))"));
        Function geom_area = ff.function("area", geom);
        Function equalsTo_left = ff.function("greaterThan", geom_area, ff.literal(0));
        Function equalsTo_right = ff.function("equalTo", ff.literal("string1"), ff.literal("string2"));
        Function orFunction = ff.function("or", equalsTo_left, equalsTo_right);
        Function if_then_elseFunction = ff.function("if_then_else", orFunction, ff.literal(10), ff.literal(-1));
        Assert.assertEquals(10, if_then_elseFunction.evaluate(new Object()));
    }
}
