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
package org.geotools.data.jdbc;

import java.util.logging.Logger;
import junit.framework.TestCase;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Common filter testing code factored up here.
 *
 * @author Chris Holmes
 */
public abstract class SQLFilterTestSupport extends TestCase {
    /** Standard logging instance */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SQLFilterTestSupport.class);

    /** Schema on which to preform tests */
    protected static SimpleFeatureType testSchema = null;

    /** Schema on which to preform tests */
    protected static SimpleFeature testFeature = null;

    protected boolean setup = false;

    /**
     * Creates a new instance of TestCaseSupport
     *
     * @param name what to call this...
     */
    public SQLFilterTestSupport(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        if (setup) {
            return;
        } else {
            prepareFeatures();
        }

        setup = true;
    }

    protected void prepareFeatures() throws SchemaException, IllegalAttributeException {
        // _log.getLoggerRepository().setThreshold(Level.INFO);
        // Create the schema attributes
        LOGGER.finer("creating flat feature...");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.add("testGeometry", LineString.class);
        ftb.add("testBoolean", Boolean.class);
        ftb.add("testCharacter", Character.class);
        ftb.add("testByte", Byte.class);
        ftb.add("testShort", Short.class);
        ftb.add("testInteger", Integer.class);
        ftb.add("testLong", Long.class);
        ftb.add("testFloat", Float.class);
        ftb.add("testDouble", Double.class);
        ftb.add("testString", String.class);
        ftb.add("testZeroDouble", Double.class);
        ftb.add("testArray", String[].class);
        ftb.setName("testSchema");
        testSchema = ftb.buildFeatureType();

        GeometryFactory geomFac = new GeometryFactory();

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Builds the test feature
        Object[] attributes = new Object[10];
        attributes[0] = geomFac.createLineString(coords);
        attributes[1] = Boolean.valueOf(true);
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
}
