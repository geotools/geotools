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

import java.util.ArrayList;

import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Categorize function against the Symbology Encoding 1.1 specs.
 * 
 * @author Jody
 * @author mbedward
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/render/src/test/java/org/geotools/filter/CategorizeFunctionTest.java $
 */
public class CategorizeFunctionTest extends SEFunctionTestBase {

    @Before
    public void setup() {
        parameters = new ArrayList<Expression>();
    }

    @Test
    public void testFindCategorizeFunction() throws Exception {
        System.out.println("   testFindCategorizeFunction");

        Literal fallback = ff2.literal("NOT_FOUND");
        parameters.add(ff2.property("value"));
        parameters.add(ff2.literal(0));

        Function fn = finder.findFunction("categorize", parameters, fallback);
        Object result = fn.evaluate(feature(0));

        assertFalse("Could not locate 'categorize' function", result.equals(fallback.getValue()));
    }

    @Test
    public void testNoThresholds() throws Exception {
        System.out.println("   testNoThresholds");

        final int categoryValue = 42;

        parameters.add(ff2.property("value"));
        parameters.add(ff2.literal(categoryValue));
        Function fn = finder.findFunction("categorize", parameters);

        Integer result = fn.evaluate(feature(0), Integer.class);
        assertEquals(categoryValue, result.intValue());
    }

    /**
     * Test categorization with "succeeding" thresholds (the default behaviour).
     * A succeeding threshold is whose value belongs to the next category.
     */
    @Test
    public void testSucceedingThresholds() throws Exception {
        System.out.println("   testSucceedingThresholds");

        final String[] categories = {"low", "mid", "high", "super"};
        final Double[] thresholds = {0.0, 50.0, 100.0};
        setupParameters(categories, thresholds);

        Function fn = finder.findFunction("categorize", parameters);

        String result;
        int i;
        for (i = 0 ;i < thresholds.length; i++) {
            // below threshold: should be in category i
            result = fn.evaluate(feature(thresholds[i].intValue() - 1), String.class);
            assertEquals(categories[i], result);

            // at threshold boundary: should be in category i+1
            result = fn.evaluate(feature(thresholds[i].intValue()), String.class);
            assertEquals(categories[i+1], result);
        }

        // above last threshold
        result = fn.evaluate(feature(thresholds[thresholds.length - 1].intValue() + 1), String.class);
        assertEquals(categories[categories.length - 1], result);
    }

    /**
     * Test categorization with "preceding" thresholds (the default behaviour).
     * A succeeding threshold is whose value belongs to the next category.
     */
    @Test
    public void testPrecedingThresholds() throws Exception {
        System.out.println("   testPrecedingThresholds");

        final String[] categories = {"low", "mid", "high", "super"};
        final Double[] thresholds = {0.0, 50.0, 100.0};
        setupParameters(categories, thresholds);
        parameters.add(ff2.literal("preceding"));

        Function fn = finder.findFunction("categorize", parameters);

        String result;
        int i;
        for (i = 0 ;i < thresholds.length; i++) {
            // below threshold: should be in category i
            result = fn.evaluate(feature(thresholds[i].intValue() - 1), String.class);
            assertEquals(categories[i], result);

            // at threshold boundary: should still be in category i
            result = fn.evaluate(feature(thresholds[i].intValue()), String.class);
            assertEquals(categories[i], result);
        }

        // above last threshold
        result = fn.evaluate(feature(thresholds[thresholds.length - 1].intValue() + 1), String.class);
        assertEquals(categories[categories.length - 1], result);
    }

    /** Test if RasterData Expression works */
    @Ignore("WRITE ME")
    @Test
    public void testRasterData() {
        // empty
    }

    private void setupParameters(Object[] categories, Object[] thresholds) {
        if (thresholds.length != categories.length - 1) {
            throw new IllegalArgumentException("should be n thresholds and n+1 categories");
        }

        parameters = new ArrayList<Expression>();
        parameters.add(ff2.property("value"));

        for (int i = 0; i < thresholds.length; i++) {
            parameters.add(ff2.literal(categories[i]));
            parameters.add(ff2.literal(thresholds[i]));
        }
        parameters.add(ff2.literal(categories[categories.length - 1]));
    }

}
