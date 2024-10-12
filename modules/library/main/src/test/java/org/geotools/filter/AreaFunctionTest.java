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

import java.util.Arrays;
import java.util.logging.Logger;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Unit test for expressions. This is a complimentary test suite with the filter test suite.
 *
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 */
public class AreaFunctionTest {

    /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(AreaFunctionTest.class);
    /** Feature on which to preform tests */
    private static SimpleFeature testFeature = null;

    /** Schema on which to preform tests */
    private static SimpleFeatureType testSchema = null;

    boolean setup = false;

    /**
     * Sets up a schema and a test feature.
     *
     * @throws SchemaException If there is a problem setting up the schema.
     * @throws IllegalFeatureException If problem setting up the feature.
     */
    @Before
    public void setUp() throws SchemaException, IllegalAttributeException {
        if (setup) {
            return;
        }
        prepareFeatures();

        setup = true;
    }

    // HACK - this is cut and pasted from filter module tests.  Should be
    // in a test support module.
    protected void prepareFeatures() throws SchemaException, IllegalAttributeException {
        // _log.getLoggerRepository().setThreshold(Level.INFO);
        // Create the schema attributes
        LOGGER.finer("creating flat feature...");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(null);
        ftb.add("testGeometry", Polygon.class);
        LOGGER.finer("created geometry attribute");

        ftb.add("testBoolean", Boolean.class);
        LOGGER.finer("created boolean attribute");

        ftb.add("testCharacter", Character.class);
        ftb.add("testByte", Byte.class);
        ftb.add("testShort", Short.class);
        ftb.add("testInteger", Integer.class);
        ftb.add("testLong", Long.class);
        ftb.add("testFloat", Float.class);
        ftb.add("testDouble", Double.class);
        ftb.add("testString", String.class);
        ftb.setName("testSchema");
        testSchema = ftb.buildFeatureType();

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[5];
        coords[0] = new Coordinate(0, 0);
        coords[1] = new Coordinate(10, 0);
        coords[2] = new Coordinate(10, 10);
        coords[3] = new Coordinate(0, 10);
        coords[4] = new Coordinate(0, 0);

        // Builds the test feature
        Object[] attributes = new Object[10];
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        LinearRing ring = gf.createLinearRing(coords);
        attributes[0] = gf.createPolygon(ring, null);
        attributes[1] = Boolean.TRUE;
        attributes[2] = Character.valueOf('t');
        attributes[3] = Byte.valueOf("10");
        attributes[4] = Short.valueOf("101");
        attributes[5] = Integer.valueOf(1002);
        attributes[6] = Long.valueOf(10003);
        attributes[7] = Float.valueOf(10000.4f);
        attributes[8] = Double.valueOf(100000.5);
        attributes[9] = "test string data";

        // Creates the feature itself
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
        LOGGER.finer("...flat feature created");

        // _log.getLoggerRepository().setThreshold(Level.DEBUG);
    }

    static org.geotools.api.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /** Tests the min function expression. */
    @Test
    public void testAreaFunction() throws IllegalFilterException {

        PropertyName a = filterFactory.property("testGeometry");

        AreaFunction area = new AreaFunction();
        area.setParameters(Arrays.asList(new org.geotools.api.filter.expression.Expression[] {a}));
        Assert.assertEquals(100d, ((Double) area.evaluate(testFeature)).doubleValue(), 0);
    }
}
