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

import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.geotools.test.TestData;
import org.xml.sax.helpers.ParserAdapter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * Unit test for sax parser.
 *
 * @author James MacGill, CCG
 * @source $URL$
 */
public class ParserTest extends FilterTestSupport {
    /** Standard logging instance */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** Test suite for this test case */
    TestSuite suite = null;

    /** Constructor with test name. */
    String dataFolder = "";
    boolean setup = false;

    public ParserTest(String testName) {
        super(testName);
        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            //then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder = "file:////" + dataFolder + "/tests/unit/testData"; //url.toString();
            LOGGER.fine("data folder is " + dataFolder);
        }
    }

    /**
     * Main for test runner.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Required suite builder.
     *
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(ParserTest.class);

        return suite;
    }

    public void setUp() throws SchemaException, IllegalAttributeException {
        super.setUp();

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.init(testSchema);
        ftb.add("testZeroDouble", Double.class);
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
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
    }

    public void test1() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test1.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test2() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test2.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test3a() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test3a.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test3b() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test3b.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test4() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test4.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test5() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test5.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test6() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test6.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test8() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test8.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test9() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test9.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test11() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test11.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test12() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test12.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test13() throws Exception {
        org.opengis.filter.Filter test = parseDocument( "test13.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test14() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test14.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test15() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test15.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test16() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test16.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test17() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test17.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    /* 18 and 19 have multiple filters, and only last one will print,
       but logicSAXParser was messing up with multiple filters, and
       I used these tests to fix it.  To make these effective the
       parser test should be able to get multiple filters.  And shouldn't
       we also be checking the filters generated programmatically, so they
       fail if things mess up?  I don't have time right now, but maybe
       some time soon...cholmes */
    public void test18() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test18.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test19() throws Exception {
        org.opengis.filter.Filter test = parseDocument( "test19.xml");
        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    public void test20() throws Exception {
        org.opengis.filter.Filter test = parseDocument("test20.xml");

        LOGGER.fine("filter: " + test.getClass().toString());
        LOGGER.fine("parsed: " + test.toString());
    }

    //public void test27()
    //throws Exception {
    //      Filter test = parseDocument(dataFolder+"/test27.xml");
    //      LOGGER.fine("filter: " + test.getClass().toString());
    //      LOGGER.fine("parsed: " + test.toString());
    //} 
    public org.opengis.filter.Filter parseDocument(String uri) throws Exception {
        LOGGER.finest("about to create parser");

        SAXParserFactory factory = SAXParserFactory.newInstance();

        // chains all the appropriate filters together (in correct order)
        //  and initiates parsing
        TestFilterHandler filterHandler = new TestFilterHandler();
        FilterFilter filterFilter = new FilterFilter(filterHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterFilter);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        //XMLReader parser = XMLReaderFactory.createXMLReader(/*"org.apache.xerces.parsers.SAXParser"*/); 
        // uncomment to use xerces parser
        //parser.setContentHandler(documentFilter);
        //parser.parse(uri);
        SAXParserFactory fac = SAXParserFactory.newInstance();
        SAXParser parser = fac.newSAXParser();

        ParserAdapter p = new ParserAdapter(parser.getParser());
        p.setContentHandler(documentFilter);
        LOGGER.fine("just made parser, " + uri);
        p.parse(TestData.getResource(this, uri).toExternalForm());
        LOGGER.finest("just parsed: " + uri);

        return filterHandler.getFilter();
    }
}
