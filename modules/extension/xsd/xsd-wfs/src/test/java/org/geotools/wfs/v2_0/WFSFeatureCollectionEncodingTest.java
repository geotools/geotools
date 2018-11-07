/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfs20.Wfs20Factory;
import org.custommonkey.xmlunit.XMLAssert;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.test.XMLTestSupport;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class WFSFeatureCollectionEncodingTest extends TestCase {

    MemoryDataStore store;

    @Override
    protected void setUp() throws Exception {

        store = new MemoryDataStore();

        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        ab.setNamespaceURI(GML.NAMESPACE);
        ab.setName("identifier");
        ab.setBinding(String.class);
        AttributeType identifierType = ab.buildType();
        AttributeDescriptor identifierDescriptor =
                ab.buildDescriptor(new NameImpl(GML.NAMESPACE, "identifier"), identifierType);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setNamespaceURI("http://geotools.org");
        tb.add(identifierDescriptor);
        tb.add("geometry", Point.class);
        tb.add("integer", Integer.class);
        store.createSchema(tb.buildFeatureType());

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(store.getSchema("feature"));
        b.add("id1");
        b.add(new GeometryFactory().createPoint(new Coordinate(0, 0)));
        b.add(0);
        store.addFeature(b.buildFeature("zero"));

        b.add("id2");
        b.add(new GeometryFactory().createPoint(new Coordinate(1, 1)));
        b.add(1);
        store.addFeature(b.buildFeature("one"));

        tb = new SimpleFeatureTypeBuilder();
        tb.setName("other");
        tb.setNamespaceURI("http://geotools.org");
        tb.add("geometry", Point.class);
        tb.add("integer", Integer.class);
        store.createSchema(tb.buildFeatureType());

        b = new SimpleFeatureBuilder(store.getSchema("other"));
        b.add(new GeometryFactory().createPoint(new Coordinate(2, 2)));
        b.add(2);
        store.addFeature(b.buildFeature("two"));

        b.add(new GeometryFactory().createPoint(new Coordinate(3, 3)));
        b.add(3);
        store.addFeature(b.buildFeature("three"));
    }

    public void testEncodeFeatureCollection() throws Exception {
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = store.getFeatureSource("feature").getFeatures();
        fc.getFeature().add(features);

        Encoder e = encoder();
        e.getNamespaces().declarePrefix("geotools", "http://geotools.org");
        e.setIndenting(true);

        Document d = e.encodeAsDOM(fc, WFS.FeatureCollection);
        // XMLTestSupport.print(d);

        NamedNodeMap attributes = d.getDocumentElement().getAttributes();
        assertEquals("unknown", attributes.getNamedItem("numberMatched").getTextContent());
        assertEquals(1, d.getElementsByTagName("wfs:boundedBy").getLength());
        assertEquals(2, d.getElementsByTagName("gml:boundedBy").getLength());
        assertEquals(2, d.getElementsByTagName("wfs:member").getLength());
        assertEquals(2, d.getElementsByTagName("gml:Point").getLength());
        assertEquals(2, d.getElementsByTagName("gml:pos").getLength());
        assertEquals(0, d.getElementsByTagName("gml:coord").getLength());

        // check ids
        assertEquals(
                "zero.geometry",
                d.getElementsByTagName("gml:Point")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("gml:id")
                        .getNodeValue());
        assertEquals(
                "one.geometry",
                d.getElementsByTagName("gml:Point")
                        .item(1)
                        .getAttributes()
                        .getNamedItem("gml:id")
                        .getNodeValue());

        XMLAssert.assertEquals(2, d.getElementsByTagName("geotools:feature").getLength());

        assertNotNull(
                ((Element) d.getElementsByTagName("geotools:feature").item(0))
                        .getAttribute("gml:id"));
    }

    public void testEncodeFeatureCollectionCoordinatesFormatting() throws Exception {
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = store.getFeatureSource("feature").getFeatures();
        fc.getFeature().add(features);

        Encoder e = encoderFormatting();
        e.getNamespaces().declarePrefix("geotools", "http://geotools.org");
        e.setIndenting(true);

        Document d = e.encodeAsDOM(fc, WFS.FeatureCollection);
        // XMLTestSupport.print(d);

        NamedNodeMap attributes = d.getDocumentElement().getAttributes();
        assertEquals("unknown", attributes.getNamedItem("numberMatched").getTextContent());
        assertEquals(1, d.getElementsByTagName("wfs:boundedBy").getLength());
        assertEquals(2, d.getElementsByTagName("gml:boundedBy").getLength());
        assertEquals(2, d.getElementsByTagName("wfs:member").getLength());
        assertEquals(2, d.getElementsByTagName("gml:Point").getLength());
        assertEquals(2, d.getElementsByTagName("gml:pos").getLength());
        assertEquals(0, d.getElementsByTagName("gml:coord").getLength());
        assertEquals("0.0000 0.0000", d.getElementsByTagName("gml:pos").item(0).getTextContent());

        // check ids
        assertEquals(
                "zero.geometry",
                d.getElementsByTagName("gml:Point")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("gml:id")
                        .getNodeValue());
        assertEquals(
                "one.geometry",
                d.getElementsByTagName("gml:Point")
                        .item(1)
                        .getAttributes()
                        .getNamedItem("gml:id")
                        .getNodeValue());

        XMLAssert.assertEquals(2, d.getElementsByTagName("geotools:feature").getLength());

        assertNotNull(
                ((Element) d.getElementsByTagName("geotools:feature").item(0))
                        .getAttribute("gml:id"));
    }

    public void testEncodeNumberMatchedReturned() throws Exception {
        // prepare empty result
        net.opengis.wfs20.FeatureCollectionType fc =
                Wfs20Factory.eINSTANCE.createFeatureCollectionType();
        fc.setNumberReturned(new BigInteger("0"));

        Encoder e = encoder();

        Document d = e.encodeAsDOM(fc, WFS.FeatureCollection);
        NamedNodeMap attributes = d.getDocumentElement().getAttributes();
        assertEquals("unknown", attributes.getNamedItem("numberMatched").getTextContent());
        assertEquals("0", attributes.getNamedItem("numberReturned").getTextContent());

        // try with -1
        e = encoder();
        fc.setNumberMatched(-1);
        d = e.encodeAsDOM(fc, WFS.FeatureCollection);
        attributes = d.getDocumentElement().getAttributes();
        assertEquals("unknown", attributes.getNamedItem("numberMatched").getTextContent());
        assertEquals("0", attributes.getNamedItem("numberReturned").getTextContent());

        // now with a valid value
        e = encoder();
        fc.setNumberMatched(10);
        d = e.encodeAsDOM(fc, WFS.FeatureCollection);
        attributes = d.getDocumentElement().getAttributes();
        assertEquals("10", attributes.getNamedItem("numberMatched").getTextContent());
        assertEquals("0", attributes.getNamedItem("numberReturned").getTextContent());
    }

    public void testEncodeFeatureCollectionWithoutBBOX() throws Exception {
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = store.getFeatureSource("feature").getFeatures();
        fc.getFeature().add(features);
        Configuration wfsConfiguration = new org.geotools.wfs.v2_0.WFSConfiguration();
        wfsConfiguration.getProperties().add(GMLConfiguration.NO_FEATURE_BOUNDS);
        Encoder e = encoder(wfsConfiguration);
        e.getNamespaces().declarePrefix("geotools", "http://geotools.org");
        e.setIndenting(true);

        Document d = e.encodeAsDOM(fc, WFS.FeatureCollection);

        assertEquals(0, d.getElementsByTagName("wfs:boundedBy").getLength());
        assertEquals(0, d.getElementsByTagName("gml:boundedBy").getLength());
        assertEquals(2, d.getElementsByTagName("wfs:member").getLength());
        assertEquals(2, d.getElementsByTagName("gml:Point").getLength());
        assertEquals(2, d.getElementsByTagName("gml:pos").getLength());
        assertEquals(0, d.getElementsByTagName("gml:coord").getLength());

        assertEquals(2, d.getElementsByTagName("geotools:feature").getLength());
        assertNotNull(
                ((Element) d.getElementsByTagName("geotools:feature").item(0))
                        .getAttribute("gml:id"));
    }

    public void testEncodeMultiFeatureCollection() throws Exception {
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();

        fc.getFeature().add(store.getFeatureSource("feature").getFeatures());
        fc.getFeature().add(store.getFeatureSource("other").getFeatures());

        Encoder e = encoder();
        e.getNamespaces().declarePrefix("geotools", "http://geotools.org");
        e.setIndenting(true);

        Document d = e.encodeAsDOM(fc, WFS.FeatureCollection);
        XMLTestSupport.print(d);

        List<Element> members = getChildElementsByTagName(d.getDocumentElement(), "wfs:member");
        assertEquals(2, members.size());

        assertEquals(1, getChildElementsByTagName(members.get(0), "wfs:FeatureCollection").size());

        Element featureCollection =
                getChildElementsByTagName(members.get(0), "wfs:FeatureCollection").get(0);

        assertEquals(2, getChildElementsByTagName(featureCollection, "wfs:member").size());
        assertEquals(2, featureCollection.getElementsByTagName("gml:Point").getLength());
        assertEquals(2, featureCollection.getElementsByTagName("gml:pos").getLength());
        assertEquals(0, featureCollection.getElementsByTagName("gml:coord").getLength());

        assertEquals(1, getChildElementsByTagName(members.get(1), "wfs:FeatureCollection").size());

        featureCollection =
                getChildElementsByTagName(members.get(1), "wfs:FeatureCollection").get(0);

        assertEquals(2, getChildElementsByTagName(featureCollection, "wfs:member").size());
        assertEquals(2, featureCollection.getElementsByTagName("gml:Point").getLength());
        assertEquals(2, featureCollection.getElementsByTagName("gml:pos").getLength());
        assertEquals(0, featureCollection.getElementsByTagName("gml:coord").getLength());
    }

    List<Element> getChildElementsByTagName(Node e, String name) {
        List<Element> elements = new ArrayList();
        for (int i = 0; i < e.getChildNodes().getLength(); i++) {
            Node n = e.getChildNodes().item(i);
            if (n instanceof Element && n.getNodeName().equals(name)) {
                elements.add((Element) n);
            }
        }
        return elements;
    }

    Encoder encoder(Configuration configuration) {
        return new Encoder(configuration);
    }

    Encoder encoder() {
        WFSConfiguration configuration = new WFSConfiguration();
        configuration.getProperties().add(org.geotools.gml2.GMLConfiguration.OPTIMIZED_ENCODING);
        return new Encoder(configuration);
    }

    Encoder encoderFormatting() {
        WFSConfiguration configuration = new WFSConfiguration();
        ((org.geotools.gml3.v3_2.GMLConfiguration)
                        configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class))
                .setNumDecimals(4);
        ((org.geotools.gml3.v3_2.GMLConfiguration)
                        configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class))
                .setForceDecimalEncoding(true);
        ((org.geotools.gml3.v3_2.GMLConfiguration)
                        configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class))
                .setPadWithZeros(true);
        configuration.getProperties().add(org.geotools.gml2.GMLConfiguration.OPTIMIZED_ENCODING);
        return new Encoder(configuration);
    }
}
