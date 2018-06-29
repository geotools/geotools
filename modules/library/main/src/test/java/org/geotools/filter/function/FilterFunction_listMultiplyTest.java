/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;

/** @author Tobias Warneke */
public class FilterFunction_listMultiplyTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    protected SimpleFeatureType dataType;
    protected SimpleFeature[] testFeatures;
    protected SimpleFeatureCollection featureCollection;
    private String[] dashArrays = null;
    private String[] expectedDashArrays = null;

    @Before
    public void setUp() throws Exception {
        dataType =
                DataUtilities.createType(
                        "listMultiply.test1", "id:0,geom:Point,dynamic_dasharray:String");

        dashArrays = new String[] {"5 10", "15 30"};
        expectedDashArrays = new String[] {"10.0 20.0", "30.0 60.0"};

        testFeatures = new SimpleFeature[dashArrays.length];
        GeometryFactory fac = new GeometryFactory();

        for (int i = 0; i < dashArrays.length; i++) {
            testFeatures[i] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {
                                i + 1, fac.createPoint(new Coordinate(i, i)), dashArrays[i]
                            },
                            "obj:" + i);
        }
    }

    @After
    public void tearDown() {}

    @Test
    public void testFunctionMetaInfo() {
        FunctionName functionName = ff.functionName("listMultiply", 2);
        assertNotNull(functionName);
    }

    /** Test of evaluate method, of class FilterFunction_listMultiply. */
    @Test
    public void testEvaluate() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(2), ff.literal("1 2 3"));
        Object evaluate = func.evaluate(null);
        assertTrue(evaluate instanceof String);
        assertEquals("2.0 4.0 6.0", evaluate.toString());
    }

    @Test
    public void testEvaluate2() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(2.5), ff.literal("1 2 3"));
        Object evaluate = func.evaluate(null);
        assertTrue(evaluate instanceof String);
        assertEquals("2.5 5.0 7.5", evaluate.toString());
    }

    @Test
    public void testEvaluateNull() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(2.5), ff.literal((String) null));
        Object evaluate = func.evaluate(null);
        assertNull(evaluate);
    }

    @Test
    public void testEvaluate4() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(1), ff.literal("1 2 3"));
        Object evaluate = func.evaluate(null);
        assertTrue(evaluate instanceof String);
        assertEquals("1.0 2.0 3.0", evaluate.toString());
    }

    @Test
    public void testEvaluate5() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function(
                                "listMultiply", ff.literal(2), ff.property("dynamic_dasharray"));

        for (int i = 0; i < testFeatures.length; i++) {
            Object evaluate = func.evaluate(testFeatures[i]);
            assertTrue(evaluate instanceof String);
            assertEquals(expectedDashArrays[i], evaluate.toString());
        }
    }

    @Test
    public void testEvaluateNullDasharray() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply) ff.function("listMultiply", ff.literal(2.5), null);
        Object evaluate = func.evaluate(null);
        assertNull(evaluate);
    }

    @Test
    public void testEvaluateEmptyDasharray() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(2.5), ff.literal(""));
        Object evaluate = func.evaluate(null);
        assertNull(evaluate);
    }

    @Test
    public void testEvaluateMultipleSpaces() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(2), ff.literal(" 1 2   3 "));
        Object evaluate = func.evaluate(null);
        assertTrue(evaluate instanceof String);
        assertEquals("2.0 4.0 6.0", evaluate.toString());
    }

    @Test
    public void testEvaluateSingleValue() {
        FilterFunction_listMultiply func =
                (FilterFunction_listMultiply)
                        ff.function("listMultiply", ff.literal(2), ff.literal(" 1"));
        Object evaluate = func.evaluate(null);
        assertTrue(evaluate instanceof String);
        assertEquals("2.0", evaluate.toString());
    }
}
