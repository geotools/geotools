/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.attributes;

import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.validation.RoadValidationResults;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * RangeFeatureValidationTest purpose.
 *
 * <p>Description of RangeFeatureValidationTest ...
 *
 * <p>Capabilities:
 *
 * <ul>
 *   <li>
 * </ul>
 *
 * Example Use:
 *
 * <pre><code>
 * RangeFeatureValidationTest x = new RangeFeatureValidationTest(...);
 * </code></pre>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class RangeFeatureValidationTest {
    private GeometryFactory gf;
    private RoadValidationResults results;
    private SimpleFeatureType type;
    private SimpleFeature feature;
    RangeValidation test;

    @Rule public TestName testName = new TestName();

    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        gf = new GeometryFactory();

        test = new RangeValidation();
        test.setAttribute("name");
        test.setTypeRef("road");
        test.setName("JUnit");
        test.setName("test used for junit test " + testName.getMethodName());

        type =
                DataUtilities.createType(
                        testName.getMethodName() + ".road", "id:0,*geom:LineString,name:String");

        results = new RoadValidationResults();
    }

    private SimpleFeature road(String road, int id, String name) throws IllegalAttributeException {
        Coordinate[] coords =
                new Coordinate[] {
                    new Coordinate(1, 1),
                    new Coordinate(2, 2),
                    new Coordinate(4, 2),
                    new Coordinate(5, 1)
                };
        return SimpleFeatureBuilder.build(
                type,
                new Object[] {
                    Integer.valueOf(id), gf.createLineString(coords), name,
                },
                type.getTypeName() + "." + road);
    }

    /*
     * @see TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        test = null;
    }

    @Test
    public void testSetName() {
        test.setName("foo");
        Assert.assertEquals("foo", test.getName());
    }

    @Test
    public void testGetName() {
        test.setName("bork");
        Assert.assertEquals("bork", test.getName());
    }

    @Test
    public void testSetDescription() {
        test.setDescription("foo");
        Assert.assertEquals("foo", test.getDescription());
    }

    @Test
    public void testGetDescription() {
        test.setDescription("bork");
        Assert.assertEquals("bork", test.getDescription());
    }

    @Test
    public void testGetPriority() {
        // TODO Implement getPriority().
    }

    @Test
    public void testGetMax() {
        test.setMax(100);
        Assert.assertEquals(100, test.getMax());
    }

    @Test
    public void testGetMin() {
        test.setMin(10);
        Assert.assertEquals(10, test.getMin());
    }

    @Test
    public void testGetPath() {
        test.setAttribute("path");
        Assert.assertEquals("path", test.getAttribute());
    }

    @Test
    public void testSetMax() {
        test.setMax(500);
        Assert.assertEquals(500, test.getMax());
    }

    @Test
    public void testSetMin() {
        test.setMin(5);
        Assert.assertEquals(5, test.getMin());
    }

    @Test
    public void testSetPath() {
        test.setAttribute("path2");
        Assert.assertEquals("path2", test.getAttribute());
    }

    @Test
    public void testRangeFeatureValidation() throws Exception {
        test.setTypeRef("road");
        test.setAttribute("id");
        test.setMin(0);
        Assert.assertTrue(test.validate(road("rd1", 1, "street"), type, results));
        Assert.assertTrue(test.validate(road("rd2", 0, "avenue"), type, results));
        Assert.assertFalse(test.validate(road("rd3", -1, "alley"), type, results));
    }
}
