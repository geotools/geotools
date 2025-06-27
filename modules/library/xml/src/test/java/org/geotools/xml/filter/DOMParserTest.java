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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterDOMParser;
import org.geotools.filter.FilterTestSupport;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
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

    /** Constructor with test name. */
    String dataFolder = "";

    boolean setup = false;

    public DOMParserTest() {
        FilterTestSupport.LOGGER.finer("running DOMParserTests");
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            // then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder = "file:////" + dataFolder + "/tests/unit/testData";
            FilterTestSupport.LOGGER.fine("data folder is " + dataFolder);
        }
    }

    @Override
    @Before
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
        attributes[1] = Boolean.TRUE;
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
        FilterTestSupport.testFeature = SimpleFeatureBuilder.build(FilterTestSupport.testSchema, attributes, null);
    }

    @Test
    public void test1() throws Exception {
        Filter test = parseDocument("test1.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test2() throws Exception {
        Filter test = parseDocument("test2.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test3a() throws Exception {
        Filter test = parseDocument("test3a.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    public void Xtest3b() throws Exception {
        Filter test = parseDocument("test3b.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test4() throws Exception {
        Filter test = parseDocument("test4.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test8() throws Exception {
        Filter test = parseDocument("test8.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test9() throws Exception {
        Filter test = parseDocument("test9.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test11() throws Exception {
        Filter test = parseDocument("test11.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test12() throws Exception {
        Filter test = parseDocument("test12.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test13() throws Exception {
        Filter test = parseDocument("test13.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test14() throws Exception {
        Filter test = parseDocument("test14.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test15() throws Exception {
        Filter test = parseDocument("test15.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test16() throws Exception {
        Filter test = parseDocument("test16.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void test27() throws Exception {
        Filter test = parseDocument("test27.xml");
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void testDWithin() throws Exception {
        Filter test = parseDocument("dwithin.xml");
        assertTrue(test instanceof DWithin);
        DWithin dw = (DWithin) test;
        assertEquals("the_geom", ((PropertyName) dw.getExpression1()).getPropertyName());
        assertTrue(((Literal) dw.getExpression2()).getValue() instanceof Point);
        assertEquals(5000.0, dw.getDistance(), 0d);
        assertEquals("metre", dw.getDistanceUnits());
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void testDWithinQualified() throws Exception {
        Filter test = parseDocument("dwithin-qualified.xml");
        assertTrue(test instanceof DWithin);
        DWithin dw = (DWithin) test;
        assertEquals("the_geom", ((PropertyName) dw.getExpression1()).getPropertyName());
        assertTrue(((Literal) dw.getExpression2()).getValue() instanceof Point);
        assertEquals(5000.0, dw.getDistance(), 0d);
        assertEquals("metre", dw.getDistanceUnits());
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void testBeyond() throws Exception {
        Filter test = parseDocument("beyond.xml");
        assertTrue(test instanceof Beyond);
        Beyond bd = (Beyond) test;
        assertEquals("the_geom", ((PropertyName) bd.getExpression1()).getPropertyName());
        assertTrue(((Literal) bd.getExpression2()).getValue() instanceof Point);
        assertEquals(5000.0, bd.getDistance(), 0d);
        assertEquals("metre", bd.getDistanceUnits());
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void testCrosses() throws Exception {
        Filter test = parseDocument("crosses.xml");
        assertTrue(test instanceof Crosses);
        Crosses cr = (Crosses) test;
        assertEquals("the_geom", ((PropertyName) cr.getExpression1()).getPropertyName());
        assertTrue(((Literal) cr.getExpression2()).getValue() instanceof LineString);
        FilterTestSupport.LOGGER.fine("parsed filter is " + test);
    }

    @Test
    public void testIntersectsCRS() throws Exception {
        Filter test = parseDocument("intersectsCRS.xml");
        assertTrue(test instanceof Intersects);
        Intersects cr = (Intersects) test;
        assertEquals("geom", ((PropertyName) cr.getExpression1()).getPropertyName());
        Polygon p = (Polygon) ((Literal) cr.getExpression2()).getValue();
        assertTrue(p.getUserData() instanceof CoordinateReferenceSystem);
        int epsg = CRS.lookupEpsgCode((CoordinateReferenceSystem) p.getUserData(), false);
        assertEquals(32631, epsg);
    }

    @Test
    public void test28() throws Exception {
        Id filter = (Id) parseDocumentFirst("test28.xml");
        Set<Object> fids = filter.getIDs();

        assertEquals(3, fids.size());
        assertTrue(fids.contains("FID.3"));
        assertTrue(fids.contains("FID.2"));
        assertTrue(fids.contains("FID.1"));
    }

    @Test
    public void testNotEqual() throws Exception {
        PropertyIsNotEqualTo filter = (PropertyIsNotEqualTo) parseDocumentFirst("testNotEqual.xml");

        assertTrue(filter.isMatchingCase());
    }

    @Test
    public void testLikeWithExpression() throws Exception {
        Filter filter = parseDocument("like-expression.xml");
        assertTrue(filter instanceof PropertyIsLike);
        PropertyIsLike like = (PropertyIsLike) filter;
        Function function = (Function) like.getExpression();
        assertEquals("env", function.getName());
        assertEquals("key", function.getParameters().get(0).evaluate(null, String.class));
        assertEquals("value", function.getParameters().get(1).evaluate(null, String.class));
    }

    public Filter parseDocument(String uri) throws Exception {
        org.geotools.api.filter.Filter filter = null;
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

                if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                filter = FilterDOMParser.parseFilter(child);
                assertNotNull("Null filter returned", filter);
                FilterTestSupport.LOGGER.finer("filter: " + filter.getClass().toString());
                FilterTestSupport.LOGGER.fine("parsed: " + filter.toString());
                FilterTestSupport.LOGGER.finer("result " + filter.evaluate(FilterTestSupport.testFeature));
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

                if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                filter = FilterDOMParser.parseFilter(child);
                assertNotNull("Null filter returned", filter);
                FilterTestSupport.LOGGER.finer("filter: " + filter.getClass().toString());
                FilterTestSupport.LOGGER.fine("parsed: " + filter.toString());
                FilterTestSupport.LOGGER.finer("result " + filter.evaluate(FilterTestSupport.testFeature));

                return filter;
            }
        }

        return filter;
    }
}
