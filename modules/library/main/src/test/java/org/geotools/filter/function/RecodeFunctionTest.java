/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function;

import java.awt.Color;
import java.util.ArrayList;

import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Recode function against the Symbology Encoding 1.1 specs.
 *
 * @author mbedward
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/render/src/test/java/org/geotools/filter/CategorizeFunctionTest.java $
 */
public class RecodeFunctionTest extends SEFunctionTestBase {

    private final Integer[] ints = {1, 2, 3};
    private final String[] words = {"UPPER", "lower", "MiXeD"};
    private final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

    @Before
    public void setup() {
        parameters = new ArrayList<Expression>();
    }

    @Test
    public void testFindFunction() throws Exception {
        System.out.println("   testFindFunction");

        Literal fallback = ff2.literal("NOT_FOUND");
        setupParameters(ints, colors);
        Function fn = finder.findFunction("recode", parameters, fallback);
        Object result = fn.evaluate(feature(ints[0]));

        assertFalse("Could not locate 'recode' function",
                result.equals(fallback.getValue()));
    }

    @Test
    public void testIntToColorLookup() throws Exception {
        System.out.println("   testIntToColorLookup");

        setupParameters(ints, colors);
        Function fn = finder.findFunction("recode", parameters);

        for (int i = 0; i < ints.length; i++) {
            assertEquals(colors[i], fn.evaluate(feature(ints[i]), Color.class));
        }
    }

    @Test
    public void testCaseInsensitiveLookup() throws Exception {
        System.out.println("   testCaseInsensitiveLookup");

        setupParameters(words, ints);
        Function fn = finder.findFunction("recode", parameters);

        for (int i = 0; i < words.length; i++) {
            assertEquals(ints[i], fn.evaluate(feature(words[i].toLowerCase()), Integer.class));
            assertEquals(ints[i], fn.evaluate(feature(words[i].toUpperCase()), Integer.class));
        }
    }
    
    @Test
    public void testRecodeUndefinedValueReturnsNull() throws Exception {
        System.out.println("   testRecodeUndefinedValueReturnsNull");

        setupParameters(words, ints);
        Function fn = finder.findFunction("recode", parameters);

        final String missing = "missing";
        assertNull(fn.evaluate(feature(missing)));
    }

    /**
     * Set up parameters for the Interpolate function with a set of
     * input data and output values
     */
    private void setupParameters(Object[] data, Object[] values) {

        if (data.length != values.length) {
            throw new IllegalArgumentException("data and values arrays should be the same length");
        }

        parameters = new ArrayList<Expression>();
        parameters.add(ff2.property("value"));

        for (int i = 0; i < data.length; i++) {
            parameters.add(ff2.literal(data[i]));
            parameters.add(ff2.literal(values[i]));
        }
    }

}
