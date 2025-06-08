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
package org.geotools.xml.ogc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterDOMParser;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.DocumentWriter;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.filter.FilterSchema;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This code uses the filter parser to generate test cases, and runs it through the encoder.
 *
 * <p>TODO create the filters manually, and check the output.
 *
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 * @author David Zwiers
 */
public class XMLEncoderTest {

    /** Standard logging instance */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(XMLEncoderTest.class);

    /** Constructor with test name. */
    String dataFolder = "";

    public XMLEncoderTest() {
        // _log.getLoggerRepository().setThreshold(Level.DEBUG);
        LOGGER.finer("running XMLEncoderTests");

        dataFolder = System.getProperty("dataFolder");

        if (dataFolder == null) {
            try {
                TestData.file(this, null);
            } catch (IOException e) {
                LOGGER.finer("data folder is unavailable" + dataFolder);
            }
        }
        if (dataFolder == null) {
            // then we are being run by maven
            dataFolder = System.getProperty("basedir");
            dataFolder = "file:////" + "tests/unit/testData"; // url.toString();
            LOGGER.finer("data folder is " + dataFolder);
        }
    }

    @Test
    public void test1() throws Exception {
        Filter test = parseDocument("test1.xml");
        Assert.assertNotNull(test);
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);
        // System.out.println( output );
        InputStream stream = new ByteArrayInputStream(output.toString().getBytes(StandardCharsets.UTF_8));

        Object o = DocumentFactory.getInstance(stream, new HashMap<>(), Level.FINEST);
        Assert.assertNotNull(o);
        Assert.assertEquals(test, o);
        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test3a() throws Exception {
        parseDocument("test3a.xml");

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test3b() throws Exception {
        Filter test = parseDocument("test3b.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test4() throws Exception {
        Filter test = parseDocument("test4.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test5() throws Exception {
        Filter test = parseDocument("test5.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test8() throws Exception {
        Filter test = parseDocument("test8.xml");

        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // System.out.println(output);
        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test9() throws Exception {
        Filter test = parseDocument("test9.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test12() throws Exception {
        Filter test = parseDocument("test12.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test13() throws Exception {
        Filter test = parseDocument("test13.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test14() throws Exception {
        Filter test = parseDocument("test14.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // LOGGER.fine("parsed filter is: " + test);
    }

    @Test
    public void test28() throws Exception {
        Filter test = parseDocument("test28.xml");
        StringWriter output = new StringWriter();
        DocumentWriter.writeFragment(test, FilterSchema.getInstance(), output, null);

        // System.out.println(output);
        //        LOGGER.fine("parsedfilter is: " + test);
    }

    public Filter parseDocument(String uri) throws Exception {
        Filter filter = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(TestData.getResource(this, uri).toExternalForm());

        //        LOGGER.fine("exporting " + uri);
        // first grab a filter node
        NodeList nodes = dom.getElementsByTagName("Filter");

        for (int j = 0; j < nodes.getLength(); j++) {
            Element filterNode = (Element) nodes.item(j);
            NodeList list = filterNode.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++) {
                child = list.item(i);

                // _log.getLoggerRepository().setThreshold(Level.INFO);
                if (child == null || child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                filter = FilterDOMParser.parseFilter(child);

                StringWriter output = new StringWriter();
                DocumentWriter.writeFragment(filter, FilterSchema.getInstance(), output, null);

                //                System.out.println(output);
            }
        }

        return filter;
    }

    // TODO test or ( null, and( fidFilter, null ) ) filter
    @Test
    public void testStrictHintComplexFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        PropertyIsNull null1 = ff.isNull(ff.property("name"));
        PropertyIsNull null2 = ff.isNull(ff.property("geom"));

        HashSet<FeatureId> set = new HashSet<>();
        set.add(ff.featureId("FID.1"));
        Filter filter = ff.or(null2, ff.and(null1, ff.id(set)));

        StringWriter output = new StringWriter();
        XMLHandlerHints hints = new XMLHandlerHints();
        hints.put(XMLHandlerHints.FILTER_COMPLIANCE_STRICTNESS, XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        DocumentWriter.writeFragment(filter, FilterSchema.getInstance(), output, hints);
        String string = output.toString().replaceAll("\\s", "");
        String xml = "<Filterxmlns=\"http://www.opengis.net/ogc\"xmlns:gml=\"http://www.opengis.net/gml\">"
                + "<PropertyIsNull><PropertyName>geom</PropertyName></PropertyIsNull>"
                + "<Filter><FeatureIdfid=\"FID.1\"/></Filter>"
                + "</Filter>";
        Assert.assertEquals(xml, string);

        // Note:  Round trip doesn't work in this case because request may returns more features
        // than "filter" will accept
    }

    @Test
    public void testStrictHintOR() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        HashSet<FeatureId> set = new HashSet<>();
        set.add(ff.featureId("FID.1"));
        set.add(ff.featureId("FID.2"));
        Filter filter = ff.id(set);

        StringWriter output = new StringWriter();
        XMLHandlerHints hints = new XMLHandlerHints();
        hints.put(XMLHandlerHints.FILTER_COMPLIANCE_STRICTNESS, XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        DocumentWriter.writeFragment(filter, FilterSchema.getInstance(), output, hints);
        String string = output.toString().replaceAll("\\s", "");
        // The following test fails with Java 1.6. May be caused by some iteration order dependent
        // code.
        if (TestData.isBaseJavaPlatform()) {
            Assert.assertEquals(
                    "<Filterxmlns=\"http://www.opengis.net/ogc\"xmlns:gml=\"http://www.opengis"
                            + ".net/gml\"><FeatureIdfid=\"FID.1\"/><FeatureIdfid=\"FID.2\"/></Filter>",
                    string);
        }
        ByteArrayInputStream byteStream =
                new ByteArrayInputStream(output.toString().getBytes());
        Filter roundTrip = (Filter) DocumentFactory.getInstance(byteStream, null, Level.OFF);
        Assert.assertEquals(filter, roundTrip);
    }
}
