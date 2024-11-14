/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.filter.function.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

public class IntegerDivisionTest {

    private final String FUNCTION_NAME = IntegerDivisionFunction.NAME.getName();
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testDivInvalidInitNoArgs() {
        try {
            ff.function(FUNCTION_NAME);
        } catch (RuntimeException e) {
            assertEquals("Unable to find function " + FUNCTION_NAME, e.getMessage());
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void testDivInvalidInitOneArg() {
        try {
            ff.function(FUNCTION_NAME, ff.literal(13));
        } catch (RuntimeException e) {
            assertEquals("Unable to find function " + FUNCTION_NAME, e.getMessage());
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void testDivInvalidInitThreeArgs() {
        try {
            ff.function(FUNCTION_NAME, ff.literal(13), ff.literal(14), ff.literal(15));
        } catch (RuntimeException e) {
            assertEquals("Unable to find function " + FUNCTION_NAME, e.getMessage());
            return;
        }

        fail("Exception not thrown");
    }

    @Test
    public void testIntegerDivision() {
        Function function = ff.function(FUNCTION_NAME, ff.literal(13), ff.literal(4));
        assertEquals(3, function.evaluate(null));
    }

    @Test
    public void testIntegerDivisionConversion() {
        // debatable, but the geotools conversion system is lenient
        Function function = ff.function(FUNCTION_NAME, ff.literal(13.2), ff.literal(4.2));
        assertEquals(3, function.evaluate(null));
    }
}
