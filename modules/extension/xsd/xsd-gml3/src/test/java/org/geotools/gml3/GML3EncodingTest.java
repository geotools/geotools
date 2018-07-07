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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.bindings.TEST;
import org.geotools.gml3.bindings.TestConfiguration;
import org.geotools.xml.Encoder;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** @source $URL$ */
public class GML3EncodingTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("test", TEST.TestFeature.getNamespaceURI());
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
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
        assertTrue(
                dom.getDocumentElement()
                        .getAttribute("srsName")
                        .startsWith("urn:x-ogc:def:crs:EPSG:"));

        gml.setSrsSyntax(SrsSyntax.OGC_URN);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertTrue(
                dom.getDocumentElement()
                        .getAttribute("srsName")
                        .startsWith("urn:ogc:def:crs:EPSG::"));

        gml.setSrsSyntax(SrsSyntax.OGC_HTTP_URI);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        assertTrue(
                dom.getDocumentElement()
                        .getAttribute("srsName")
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
        SimpleFeatureType ft =
                DataUtilities.createType(
                        TEST.TestFeature.getNamespaceURI(),
                        TEST.TestFeature.getLocalPart(),
                        "the_geom:Point,data:String");
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        ft,
                        new Object[] {
                            new WKTReader().read("POINT(0 0)"), "One " + ((char) 0x7) + " test"
                        },
                        "123");
        SimpleFeatureCollection fc = DataUtilities.collection(feature);

        TestConfiguration configuration = new TestConfiguration();
        Encoder encoder = new Encoder(configuration);
        String result = encoder.encodeAsString(feature, TEST.TestFeature);

        // System.out.println(result);

        Document dom = XMLUnit.buildControlDocument(result);
        assertXpathEvaluatesTo("One  test", "//test:data", dom);
    }
}
