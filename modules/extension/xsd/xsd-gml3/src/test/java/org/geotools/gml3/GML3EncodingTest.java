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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.DataUtilities;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.bindings.TEST;
import org.geotools.gml3.bindings.TestConfiguration;
import org.geotools.referencing.CRS;
import org.geotools.test.xml.XmlTestSupport;
import org.geotools.xsd.Encoder;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;

public class GML3EncodingTest extends XmlTestSupport {

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(Namespace("test", TEST.TestFeature.getNamespaceURI()));
    }

    @Test
    public void testEncodeFeatureWithBounds() throws Exception {
        SimpleFeature feature = GML3MockData.feature();
        TestConfiguration configuration = new TestConfiguration();
        // configuration.getProperties().add( org.geotools.gml2.GMLConfiguration.NO_FEATURE_BOUNDS
        // );

        Encoder encoder = new Encoder(configuration);
        Document dom = encoder.encodeAsDOM(feature, TEST.TestFeature);

        Assert.assertEquals(1, dom.getElementsByTagName("gml:boundedBy").getLength());
    }

    @Test
    public void testEncodeFeatureWithNoBounds() throws Exception {
        SimpleFeature feature = GML3MockData.feature();
        TestConfiguration configuration = new TestConfiguration();
        configuration.getProperties().add(org.geotools.gml2.GMLConfiguration.NO_FEATURE_BOUNDS);

        Encoder encoder = new Encoder(configuration);
        Document dom = encoder.encodeAsDOM(feature, TEST.TestFeature);

        Assert.assertEquals(0, dom.getElementsByTagName("gml:boundedBy").getLength());
    }

    @Test
    public void testEncodeWithNoSrsDimension() throws Exception {
        GMLConfiguration gml = new GMLConfiguration();
        Document dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        Assert.assertTrue(dom.getDocumentElement().hasAttribute("srsDimension"));

        gml.getProperties().add(GMLConfiguration.NO_SRS_DIMENSION);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        Assert.assertFalse(dom.getDocumentElement().hasAttribute("srsDimension"));
    }

    @Test
    public void testEncodeSrsSyntax() throws Exception {
        GMLConfiguration gml = new GMLConfiguration();
        Document dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        Assert.assertTrue(dom.getDocumentElement().getAttribute("srsName").startsWith("urn:x-ogc:def:crs:EPSG:"));

        gml.setSrsSyntax(SrsSyntax.OGC_URN);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        Assert.assertTrue(dom.getDocumentElement().getAttribute("srsName").startsWith("urn:ogc:def:crs:EPSG::"));

        gml.setSrsSyntax(SrsSyntax.OGC_HTTP_URI);
        dom = new Encoder(gml).encodeAsDOM(GML3MockData.point(), GML.Point);
        Assert.assertTrue(
                dom.getDocumentElement().getAttribute("srsName").startsWith("http://www.opengis.net/def/crs/EPSG/0/"));
    }

    @Test
    public void testEncodeSrsSyntaxIAU() throws Exception {
        Point p = new GeometryFactory().createPoint(new Coordinate(1, 2));
        p.setUserData(CRS.decode("urn:x-ogc:def:crs:IAU::1000"));

        GMLConfiguration gml = new GMLConfiguration();
        Document dom = new Encoder(gml).encodeAsDOM(p, GML.Point);
        assertEquals("urn:x-ogc:def:crs:IAU:1000", dom.getDocumentElement().getAttribute("srsName"));

        gml.setSrsSyntax(SrsSyntax.OGC_URN);
        dom = new Encoder(gml).encodeAsDOM(p, GML.Point);
        assertEquals("urn:ogc:def:crs:IAU::1000", dom.getDocumentElement().getAttribute("srsName"));

        gml.setSrsSyntax(SrsSyntax.OGC_HTTP_URI);
        dom = new Encoder(gml).encodeAsDOM(p, GML.Point);
        assertEquals(
                "http://www.opengis.net/def/crs/IAU/0/1000",
                dom.getDocumentElement().getAttribute("srsName"));
    }

    @Test
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
        Assert.assertEquals("true", count.getAttributes().getNamedItem("xs:nil").getTextContent());
        NodeList dateList = dom.getElementsByTagName("test:date");
        Node date = dateList.item(0);
        Assert.assertEquals("true", date.getAttributes().getNamedItem("xs:nil").getTextContent());

        // now force the XSD prefix
        encoder = new Encoder(configuration);
        encoder.getNamespaces().declarePrefix("xsd", "http://www.w3.org/2001/XMLSchema");
        dom = encoder.encodeAsDOM(feature, TEST.TestFeature);
        countList = dom.getElementsByTagName("test:count");
        count = countList.item(0);
        Assert.assertEquals(
                "true", count.getAttributes().getNamedItem("xsd:nil").getTextContent());
        dateList = dom.getElementsByTagName("test:date");
        date = dateList.item(0);
        Assert.assertEquals("true", date.getAttributes().getNamedItem("xsd:nil").getTextContent());
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

    @Test
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

        assertThat(xml, hasXPath("//test:decimal", equalTo("0.000000015")));
    }

    @Test
    public void testRemoveInvalidXMLChars() throws Exception {
        SimpleFeatureType ft = DataUtilities.createType(
                TEST.TestFeature.getNamespaceURI(), TEST.TestFeature.getLocalPart(), "the_geom:Point,data:String");
        SimpleFeature feature = SimpleFeatureBuilder.build(
                ft, new Object[] {new WKTReader().read("POINT(0 0)"), "One " + (char) 0x7 + " test"}, "123");

        TestConfiguration configuration = new TestConfiguration();
        Encoder encoder = new Encoder(configuration);
        String result = encoder.encodeAsString(feature, TEST.TestFeature);

        assertThat(result, hasXPath("//test:data", equalTo("One  test")));
    }

    @Test
    public void testEncodeFeatureMemberAttributes() throws Exception {
        SimpleFeature feature = GML3MockData.feature();
        Map<Name, Object> attributesMap = new HashMap<>();
        attributesMap.put(new NameImpl("example"), "123");
        feature.getUserData().put(Attributes.class, attributesMap);
        GMLConfiguration configuration = new GMLConfiguration();

        Encoder encoder = new Encoder(configuration);
        Document dom = encoder.encodeAsDOM(feature, GML.featureMember);
        Node featureNode = dom.getDocumentElement().getFirstChild();
        assertTrue(featureNode instanceof Element);
        Element featureElement = (Element) featureNode;
        assertTrue(featureElement.hasAttribute("example"));
        assertFalse(dom.getDocumentElement().hasAttribute("example"));
    }
}
