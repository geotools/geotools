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

import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterDOMParser;
import org.geotools.filter.FilterTestSupport;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Intersects;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tests for the DOM parser.
 *
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 */
public class DOMParserTest extends FilterTestSupport {
    /** Feature on which to preform tests */
    private Filter filter = null;

    /** Test suite for this test case */
    TestSuite suite = null;

    /** Constructor with test name. */
    String dataFolder = "";

    boolean setup = false;

    public DOMParserTest(String testName) {
        super(testName);
        FilterTestSupport.LOGGER.finer("running DOMParserTests");
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            // then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder = "file:////" + dataFolder + "/tests/unit/testData";
            FilterTestSupport.LOGGER.fine("data folder is " + dataFolder);
        }
    }

    /**
     * Main for test runner.
     *
     * @param args the passed in arguments (not used).
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void setUp() throws SchemaException {
        super.setUp();

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.init(FilterTestSupport.testSchema);
        ftb.setCRS(null);
        ftb.add("testZeroDouble", Double.class);
        FilterTestSupport.testSchema = ftb.buildFeatureType();

        GeometryFactory geomFac = new GeometryFactory();

        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Builds the test feature
        Object[] attributes = new Object[11];
        attributes[0] = geomFac.createLineString(coords);
        attributes[1] = new Boolean(true);
        attributes[2] = new Character('t');
        attributes[3] = new Byte("10");
        attributes[4] = new Short("101");
        attributes[5] = new Integer(1002);
        attributes[6] = new Long(10003);
        attributes[7] = new Float(10000.4);
        attributes[8] = new Double(100000.5);
        attributes[9] = "test string data";
        attributes[10] = new Double(0.0);

        // Creates the feature itself
        FilterTestSupport.testFeature =
                SimpleFeatureBuilder.build(FilterTestSupport.testSchema, attributes, null);
    }

    /**
     * Required suite builder.
     *
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        // _log.getLoggerRepository().setThreshold(Level.INFO);
        TestSuite suite = new TestSuite(DOMParserTest.class);

        return suite;
    }

    public void test1() throws Exception {
        Filter test = parseDocument("test1.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test2() throws Exception {
        Filter test = parseDocument("test2.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test3a() throws Exception {
        Filter test = parseDocument("test3a.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void Xtest3b() throws Exception {
        Filter test = parseDocument("test3b.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test4() throws Exception {
        Filter test = parseDocument("test4.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test8() throws Exception {
        Filter test = parseDocument("test8.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test9() throws Exception {
        Filter test = parseDocument("test9.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test11() throws Exception {
        Filter test = parseDocument("test11.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test12() throws Exception {
        Filter test = parseDocument("test12.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test13() throws Exception {
        Filter test = parseDocument("test13.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test14() throws Exception {
        Filter test = parseDocument("test14.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test15() throws Exception {
        Filter test = parseDocument("test15.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test16() throws Exception {
        Filter test = parseDocument("test16.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void test27() throws Exception {
        Filter test = parseDocument("test27.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void testDWithin() throws Exception {
        Filter test = parseDocument("dwithin.xml");
        TestCase.assertTrue(test instanceof DWithin);
        DWithin dw = (DWithin) test;
        TestCase.assertEquals("the_geom", ((PropertyName) dw.getExpression1()).getPropertyName());
        TestCase.assertTrue(((Literal) dw.getExpression2()).getValue() instanceof Point);
        TestCase.assertEquals(5000.0, dw.getDistance());
        TestCase.assertEquals("metre", dw.getDistanceUnits());
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void testDWithinQualified() throws Exception {
        Filter test = parseDocument("dwithin-qualified.xml");
        TestCase.assertTrue(test instanceof DWithin);
        DWithin dw = (DWithin) test;
        TestCase.assertEquals("the_geom", ((PropertyName) dw.getExpression1()).getPropertyName());
        TestCase.assertTrue(((Literal) dw.getExpression2()).getValue() instanceof Point);
        TestCase.assertEquals(5000.0, dw.getDistance());
        TestCase.assertEquals("metre", dw.getDistanceUnits());
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void testBeyond() throws Exception {
        Filter test = parseDocument("beyond.xml");
        TestCase.assertTrue(test instanceof Beyond);
        Beyond bd = (Beyond) test;
        TestCase.assertEquals("the_geom", ((PropertyName) bd.getExpression1()).getPropertyName());
        TestCase.assertTrue(((Literal) bd.getExpression2()).getValue() instanceof Point);
        TestCase.assertEquals(5000.0, bd.getDistance());
        TestCase.assertEquals("metre", bd.getDistanceUnits());
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void testCrosses() throws Exception {
        Filter test = parseDocument("crosses.xml");
        TestCase.assertTrue(test instanceof Crosses);
        Crosses cr = (Crosses) test;
        TestCase.assertEquals("the_geom", ((PropertyName) cr.getExpression1()).getPropertyName());
        TestCase.assertTrue(((Literal) cr.getExpression2()).getValue() instanceof LineString);
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void testIntersectsCRS() throws Exception {
        Filter test = parseDocument("intersectsCRS.xml");
        TestCase.assertTrue(test instanceof Intersects);
        Intersects cr = (Intersects) test;
        TestCase.assertEquals("geom", ((PropertyName) cr.getExpression1()).getPropertyName());
        Polygon p = (Polygon) ((Literal) cr.getExpression2()).getValue();
        TestCase.assertTrue(p.getUserData() instanceof CoordinateReferenceSystem);
        int epsg = CRS.lookupEpsgCode((CoordinateReferenceSystem) p.getUserData(), false);
        TestCase.assertEquals(32631, epsg);
    }

    public void test28() throws Exception {
        Id filter = (Id) parseDocumentFirst("test28.xml");
        Set<Object> fids = filter.getIDs();

        TestCase.assertEquals(3, fids.size());
        TestCase.assertTrue(fids.contains("FID.3"));
        TestCase.assertTrue(fids.contains("FID.2"));
        TestCase.assertTrue(fids.contains("FID.1"));
    }

    public void testNotEqual() throws Exception {
        PropertyIsNotEqualTo filter = (PropertyIsNotEqualTo) parseDocumentFirst("testNotEqual.xml");

        TestCase.assertTrue(filter.isMatchingCase());
    }

    public Filter parseDocument(String uri) throws Exception {
        org.opengis.filter.Filter filter = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document dom = db.parse(TestData.getResource(this, uri).toExternalForm());
        FilterTestSupport.LOGGER.fine("parsing " + uri);

        // first grab a filter node
        NodeList nodes = dom.getElementsByTagName("Filter");
        if (nodes.getLength() == 0) {
            nodes = dom.getElementsByTagName("ogc:Filter");
        }

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
                TestCase.assertNotNull("Null filter returned", filter);
                FilterTestSupport.LOGGER.finer("filter: " + filter.getClass().toString());
                FilterTestSupport.LOGGER.fine("parsed: " + filter.toString());
                FilterTestSupport.LOGGER.finer(
                        "result " + filter.evaluate(FilterTestSupport.testFeature));
            }
        }

        return filter;
    }

    public Filter parseDocumentFirst(String uri) throws Exception {
        Filter filter = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(TestData.getResource(this, uri).toExternalForm());
        FilterTestSupport.LOGGER.fine("parsing " + uri);

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
                TestCase.assertNotNull("Null filter returned", filter);
                FilterTestSupport.LOGGER.finer("filter: " + filter.getClass().toString());
                FilterTestSupport.LOGGER.fine("parsed: " + filter.toString());
                FilterTestSupport.LOGGER.finer(
                        "result " + filter.evaluate(FilterTestSupport.testFeature));

                return filter;
            }
        }

        return filter;
    }
}
