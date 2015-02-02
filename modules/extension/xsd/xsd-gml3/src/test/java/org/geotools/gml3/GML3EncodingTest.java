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
package org.geotools.gml3;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.TestCase;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.xsd.XSDSchema;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.bindings.TEST;
import org.geotools.gml3.bindings.TestConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 *
 * @source $URL$
 */
public class GML3EncodingTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("test", TEST.TestFeature.getNamespaceURI());
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    boolean isOffline() throws Exception {
        // this test will only run if network is available
        URL url = new URL("http://schemas.opengis.net");

        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);
            conn.getInputStream().read();
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public void testWithConfiguration() throws Exception {
        if (isOffline()) {
            return;
        }

        TestConfiguration configuration = new TestConfiguration();

        // first parse in test data
        Parser parser = new Parser(configuration);
        SimpleFeatureCollection fc = (SimpleFeatureCollection) parser.parse(TestConfiguration.class
                .getResourceAsStream("test.xml"));
        assertNotNull(fc);

        XSDSchema schema = TEST.getInstance().getSchema();
        assertNotNull(schema);

        Encoder encoder = new Encoder(configuration, schema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        encoder.write(fc, TEST.TestFeatureCollection, output);

        validate(output.toByteArray(), configuration);
    }

    public void testWithApplicationSchemaConfiguration() throws Exception {
        if (isOffline()) {
            return;
        }

        // The schema location needs to be a well formed URI/URL, a file path is not sufficient.
        String schemaLocation = TestConfiguration.class.getResource("test.xsd").toString();

        ApplicationSchemaConfiguration configuration = new ApplicationSchemaConfiguration(
                TEST.NAMESPACE, schemaLocation);

        // first parse in test data
        Parser parser = new Parser(configuration);
        SimpleFeatureCollection fc = (SimpleFeatureCollection) parser.parse(TestConfiguration.class
                .getResourceAsStream("test.xml"));
        assertNotNull(fc);

        XSDSchema schema = TEST.getInstance().getSchema();
        assertNotNull(schema);

        Encoder encoder = new Encoder(configuration, schema);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        encoder.write(fc, TEST.TestFeatureCollection, output);

        validate(output.toByteArray(), configuration);
    }

    void validate(byte[] data, Configuration configuration) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema s = sf.newSchema(new URI(configuration.getSchemaFileURL()).toURL());

        Validator v = s.newValidator();

        final ArrayList errors = new ArrayList();
        DefaultHandler handler = new DefaultHandler() {
            public void error(SAXParseException e) throws SAXException {
                System.out.println(e.getMessage());
                errors.add(e);
            }

            public void fatalError(SAXParseException e) throws SAXException {
                System.out.println(e.getMessage());
                errors.add(e);
            }
        };

        v.setErrorHandler(handler);
        v.validate(new StreamSource(new ByteArrayInputStream(data)));

        assertTrue(errors.isEmpty());
    }

    public void testEncodeFeatureWithBounds() throws Exception {
        SimpleFeature feature = GML3MockData.feature();
        TestConfiguration configuration = new TestConfiguration();
        // configuration.getProperties().add( org.geotools.gml2.GMLConfiguration.NO_FEATURE_BOUNDS
        // );

        Encoder encoder = new Encoder(configuration);
        Document dom = encoder.encodeAsDOM(feature, TEST.TestFeature);

        assertEquals(1, dom.getElementsByTagName("gml:boundedBy").getLength());
    }

    public void testEncodeFeatureWithNoBounds() throws Exception {
        SimpleFeature feature = GML3MockData.feature();
        TestConfiguration configuration = new TestConfiguration();
        configuration.getProperties().add(org.geotools.gml2.GMLConfiguration.NO_FEATURE_BOUNDS);

        Encoder encoder = new Encoder(configuration);
        Document dom = encoder.encodeAsDOM(feature, TEST.TestFeature);

        assertEquals(0, dom.getElementsByTagName("gml:boundedBy").getLength());
    }

    public void testEncodeWithNoSrsDimension() throws Exception {
        GMLConfiguration gml = new GMLConfiguration();
        Document dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertTrue(dom.getDocumentElement().hasAttribute("srsDimension"));

        gml.getProperties().add(GMLConfiguration.NO_SRS_DIMENSION);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertFalse(dom.getDocumentElement().hasAttribute("srsDimension"));
    }

    public void testEncodeSrsSyntax() throws Exception {
        GMLConfiguration gml = new GMLConfiguration();
        Document dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertTrue(dom.getDocumentElement().getAttribute("srsName")
                .startsWith("urn:x-ogc:def:crs:EPSG:"));

        gml.setSrsSyntax(SrsSyntax.OGC_URN);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertTrue(dom.getDocumentElement().getAttribute("srsName")
                .startsWith("urn:ogc:def:crs:EPSG::"));

        gml.setSrsSyntax(SrsSyntax.OGC_HTTP_URI);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertTrue(dom.getDocumentElement().getAttribute("srsName")
                .startsWith("http://www.opengis.net/def/crs/EPSG/0/"));
    }

    public void testEncodeFeatureWithNullValues() throws Exception {
        SimpleFeatureType type = buildTestFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add("theName");
        builder.add("theDescription");
        builder.add(GML3MockData.point());
        builder.add(null);
        builder.add(null);

        SimpleFeature feature = builder.buildFeature("fid.1");

        TestConfiguration configuration = new TestConfiguration();
        Encoder encoder = new Encoder(configuration);
        Document dom = encoder.encodeAsDOM(feature, TEST.TestFeature);
        NodeList countList = dom.getElementsByTagName("test:count");
        Node count = countList.item(0);
        assertEquals("true", count.getAttributes().getNamedItem("xs:nil").getTextContent());
        NodeList dateList = dom.getElementsByTagName("test:date");
        Node date = dateList.item(0);
        assertEquals("true", date.getAttributes().getNamedItem("xs:nil").getTextContent());

        // now force the XSD prefix
        encoder = new Encoder(configuration);
        encoder.getNamespaces().declarePrefix("xsd", "http://www.w3.org/2001/XMLSchema");
        dom = encoder.encodeAsDOM(feature, TEST.TestFeature);
        countList = dom.getElementsByTagName("test:count");
        count = countList.item(0);
        assertEquals("true", count.getAttributes().getNamedItem("xsd:nil").getTextContent());
        dateList = dom.getElementsByTagName("test:date");
        date = dateList.item(0);
        assertEquals("true", date.getAttributes().getNamedItem("xsd:nil").getTextContent());
    }

    private SimpleFeatureType buildTestFeatureType() {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName(TEST.TestFeature.getLocalPart());
        typeBuilder.setNamespaceURI(TEST.TestFeature.getNamespaceURI());

        typeBuilder.add("name", String.class);
        typeBuilder.add("description", String.class);
        typeBuilder.add("geom", Point.class);
        typeBuilder.nillable(true);
        typeBuilder.add("count", Integer.class);
        typeBuilder.nillable(true);
        typeBuilder.add("date", Date.class);
        typeBuilder.add("data", String.class);
        typeBuilder.add("decimal", BigDecimal.class);

        SimpleFeatureType type = typeBuilder.buildFeatureType();
        return type;
    }

    public void testEncodeBigDecimal() throws Exception {
        SimpleFeatureType type = buildTestFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add("theName");
        builder.add("theDescription");
        builder.add(GML3MockData.point());
        builder.add(null);
        builder.add(null);
        builder.add(null);
        builder.add(new BigDecimal("0.000000015"));

        SimpleFeature feature = builder.buildFeature("fid.1");

        TestConfiguration configuration = new TestConfiguration();
        Encoder encoder = new Encoder(configuration);
        encoder.setIndentSize(2);
        String xml = encoder.encodeAsString(feature, TEST.TestFeature);

        // System.out.println(xml);
        Document dom = XMLUnit.buildControlDocument(xml);
        assertXpathEvaluatesTo("0.000000015", "//test:decimal", dom);
    }

    @Test
    public void testRemoveInvalidXMLChars() throws Exception {
        SimpleFeatureType ft = DataUtilities.createType(TEST.TestFeature.getNamespaceURI(),
                TEST.TestFeature.getLocalPart(), "the_geom:Point,data:String");
        SimpleFeature feature = SimpleFeatureBuilder
                .build(ft, new Object[] { new WKTReader().read("POINT(0 0)"),
                        "One " + ((char) 0x7) + " test" }, "123");
        SimpleFeatureCollection fc = DataUtilities.collection(feature);

        TestConfiguration configuration = new TestConfiguration();
        Encoder encoder = new Encoder(configuration);
        String result = encoder.encodeAsString(feature, TEST.TestFeature);

        // System.out.println(result);

        Document dom = XMLUnit.buildControlDocument(result);
        assertXpathEvaluatesTo("One  test", "//test:data", dom);
    }
}
