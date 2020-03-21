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
package org.geotools.xml.filter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterDOMParser;
import org.geotools.test.TestData;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMParserTestSuite extends TestSuite {

    /** Standard logging instance */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DOMParserTestSuite.class);

    /** Schema on which to preform tests */
    protected static SimpleFeatureType testSchema = null;

    /** Schema on which to preform tests */
    protected static SimpleFeature testFeature = null;

    protected boolean setup = false;

    public DOMParserTestSuite() {
        super("DOM Parser Test Suite");
    }

    static void prepareFeatures() throws SchemaException, IllegalAttributeException {
        // _log.getLoggerRepository().setThreshold(Level.INFO);
        // Create the schema attributes
        LOGGER.finer("creating flat feature...");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(null);
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
        ftb.setName("testSchema");
        testSchema = ftb.buildFeatureType();
        GeometryFactory geomFac = new GeometryFactory();

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Builds the test feature
        Object[] attributes = new Object[11];
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
        attributes[10] = Double.valueOf(0.0);
        // Creates the feature itself
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
        LOGGER.finer("...flat feature created");

        // _log.getLoggerRepository().setThreshold(Level.DEBUG);
    }

    /**
     * The individual tests are defined as xml files!
     *
     * @return Test
     */
    public static Test suite() {
        DOMParserTestSuite suite = new DOMParserTestSuite();

        try {
            suite.prepareFeatures();

            File dir = TestData.file(DOMParserTestSuite.class, "test9.xml").getParentFile();

            File tests[] =
                    dir.listFiles(
                            new FileFilter() {
                                public boolean accept(File pathname) {
                                    return pathname.toString().endsWith("test20.xml");
                                }
                            });
            for (int i = 0; i < tests.length; i++) {
                File test = tests[i];
                suite.addTest(suite.new DomTestXml(test.getName()));
            }
            // .. etc..
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        // System.out.println(suite.countTestCases() + " xml filter tests found");
        return suite;
    }

    /** Quick test of a single xml document */
    class DomTestXml implements Test {
        String document;

        public DomTestXml(String document) {
            this.document = document;
        }

        public String toString() {
            return document;
        }

        public int countTestCases() {
            return 1;
        }

        public void run(TestResult result) {
            // System.out.println(document);
            result.startTest(this);
            Protectable p =
                    new Protectable() {
                        public void protect() throws Throwable {
                            DomTestXml.this.runBare();
                        }
                    };
            result.runProtected(this, p);
            result.endTest(this);
        }

        public void runBare() throws Throwable {
            Throwable exception = null;
            try {
                runTest();
            } catch (Throwable running) {
                exception = running;
            }
            if (exception != null) throw exception;
        }

        public void runTest() throws Throwable {
            Filter filter = parseDocument(document);
            assertNotNull(filter);
            LOGGER.fine("Parsed filter is " + filter);
        }

        public Filter parseDocument(String uri) throws Exception {
            Filter filter = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document dom = db.parse(TestData.getResource(this, uri).toExternalForm());
            LOGGER.fine("parsing " + uri);

            // first grab a filter node
            NodeList nodes = dom.getElementsByTagName("Filter");

            for (int j = 0; j < nodes.getLength(); j++) {
                Element filterNode = (Element) nodes.item(j);
                NodeList list = filterNode.getChildNodes();
                Node child = null;

                for (int i = 0; i < list.getLength(); i++) {
                    child = list.item(i);

                    if ((child == null) || (child.getNodeType() != Node.ELEMENT_NODE)) {
                        continue;
                    }

                    filter = FilterDOMParser.parseFilter(child);
                    assertNotNull("Null filter returned", filter);
                    LOGGER.finer("filter: " + filter.getClass().toString());
                    LOGGER.fine("parsed: " + filter.toString());
                    LOGGER.finer("result " + filter.evaluate(testFeature));
                }
            }

            return filter;
        }
    }
}
