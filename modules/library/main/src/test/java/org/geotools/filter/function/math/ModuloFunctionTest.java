/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function.math;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

public class ModuloFunctionTest extends TestCase {

    private FilterFactory ff;

    private String functionName;

    protected void setUp() throws Exception {
        super.setUp();

        ff = CommonFactoryFinder.getFilterFactory2(null);
        functionName = ModuloFunction.NAME.getName();
    }

    public void testModuloInvalidInitNoArgs() {
        try {
            ff.function(functionName);
        } catch (RuntimeException e) {
            assertEquals("Unable to find function " + functionName, e.getMessage());
            return;
        }

        fail("Exception not thrown");
    }

    public void testModuloInvalidInitOneArg() {
        try {
            ff.function(functionName, ff.literal(13));
        } catch (RuntimeException e) {
            assertEquals("Unable to find function " + functionName, e.getMessage());
            return;
        }

        fail("Exception not thrown");
    }

    public void testModuloInvalidInitThreeArgs() {
        try {
            ff.function(functionName, ff.literal(13), ff.literal(14), ff.literal(15));
        } catch (RuntimeException e) {
            assertEquals("Unable to find function " + functionName, e.getMessage());
            return;
        }

        fail("Exception not thrown");
    }

    public void testModulo() {
        Function function = ff.function(functionName, ff.literal(13), ff.literal(4));

        assertEquals(1, function.evaluate(null));
    }

    public void testModuloNegativeDividend() {
        Function function = ff.function(functionName, ff.literal(-13), ff.literal(4));

        assertEquals(3, function.evaluate(null));
    }

    public void testModuloNegativeDivisor() {
        Function function = ff.function(functionName, ff.literal(13), ff.literal(-4));

        assertEquals(-3, function.evaluate(null));
    }

    public void testModuloFloat() {
        Function function = ff.function(functionName, ff.literal(13.6), ff.literal(4));

        assertEquals(1, function.evaluate(null));
    }

    public void testModuloOneDivisor() {
        Function function = ff.function(functionName, ff.literal(13), ff.literal(1));

        assertEquals(0, function.evaluate(null));
    }

    public void testModuloZeroDivisor() {
        Function function = ff.function(functionName, ff.literal(13), ff.literal(0));

        try {
            function.evaluate(null);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("IllegalArgumentException not thrown");
    }

}
