/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2.simple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.bindings.GMLTestSupport;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;
import org.xml.sax.helpers.AttributesImpl;

public class GMLWriterTest extends GMLTestSupport {

    Encoder gtEncoder;
    static final String INDENT_AMOUNT_KEY = "{http://xml.apache.org/xslt}indent-amount";
    protected XpathEngine xpath;

    @Override
    protected void setUp() throws Exception {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        this.gtEncoder = new Encoder(createConfiguration());
        this.xpath = XMLUnit.newXpathEngine();
    }

    public void testGeometryCollectionEncoder() throws Exception {
        GeometryCollectionEncoder gce = new GeometryCollectionEncoder(gtEncoder, "gml");
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "GEOMETRYCOLLECTION (LINESTRING"
                                        + " (180 200, 160 180), POINT (19 19), POINT (20 10))");
        Document doc = encode(gce, geometry);
        // print(doc);
        assertEquals(1, xpath.getMatchingNodes("//gml:LineString", doc).getLength());
        assertEquals(2, xpath.getMatchingNodes("//gml:Point", doc).getLength());
        assertEquals(3, xpath.getMatchingNodes("//gml:coordinates", doc).getLength());
    }

    public void testEncode3DLine() throws Exception {
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml");
        Geometry geometry = new WKTReader2().read("LINESTRING(0 0 50, 120 0 100)");
        Document doc = encode(encoder, geometry);
        // print(doc);
        assertEquals("0,0,50 120,0,100", xpath.evaluate("//gml:coordinates", doc));
    }

    public void testEncode3DLineFromLiteCS() throws Exception {
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml");
        LiteCoordinateSequence cs =
                new LiteCoordinateSequence(new double[] {0, 0, 50, 120, 0, 100}, 3);
        LineString geometry = new GeometryFactory().createLineString(cs);
        Document doc = encode(encoder, geometry);
        // print(doc);
        assertEquals("0,0,50 120,0,100", xpath.evaluate("//gml:coordinates", doc));
    }

    public void testEncode3DPoint() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml");
        Geometry geometry = new WKTReader2().read("POINT(0 0 50)");
        Document doc = encode(encoder, geometry);
        // print(doc);
        assertEquals("0,0,50", xpath.evaluate("//gml:coordinates", doc));
    }

    public void testCoordinatesFormatting() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml");
        Geometry geometry = new WKTReader2().read("POINT(21396814.969 0 50)");
        Document doc = encode(encoder, geometry, 2, true, false);
        assertEquals("21396814.97,0,50", xpath.evaluate("//gml:coordinates", doc));

        doc = encode(encoder, geometry, 4, true, true);
        assertEquals("21396814.9690,0.0000,50.0000", xpath.evaluate("//gml:coordinates", doc));

        doc = encode(encoder, geometry, 4, false, false);
        assertEquals("2.1396814969E7,0,50", xpath.evaluate("//gml:coordinates", doc));
    }

    protected Configuration createConfiguration() {
        return new GMLConfiguration();
    }

    protected Document encode(GeometryEncoder encoder, Geometry geometry) throws Exception {
        return encode(encoder, geometry, 6, false, false);
    }

    protected Document encode(
            GeometryEncoder encoder,
            Geometry geometry,
            int numDecimals,
            boolean forceDecimals,
            boolean padWithZeros)
            throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // create the document serializer
        SAXTransformerFactory txFactory =
                (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        TransformerHandler xmls;
        try {
            xmls = txFactory.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        }
        Properties outputProps = new Properties();
        outputProps.setProperty(INDENT_AMOUNT_KEY, "2");
        xmls.getTransformer().setOutputProperties(outputProps);
        xmls.getTransformer().setOutputProperty(OutputKeys.METHOD, "XML");
        xmls.setResult(new StreamResult(out));

        GMLWriter handler =
                new GMLWriter(
                        xmls,
                        gtEncoder.getNamespaces(),
                        numDecimals,
                        forceDecimals,
                        padWithZeros,
                        "gml");
        handler.startDocument();
        handler.startPrefixMapping("gml", GML.NAMESPACE);
        handler.endPrefixMapping("gml");

        encoder.encode(geometry, new AttributesImpl(), handler);
        handler.endDocument();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DOMResult result = new DOMResult();
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new StreamSource(in), result);
        Document d = (Document) result.getNode();
        return d;
    }
}
