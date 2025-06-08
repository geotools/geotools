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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.bindings.GMLTestSupport;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;
import org.xml.sax.helpers.AttributesImpl;

public class GMLWriterTest extends GMLTestSupport {

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(
                Namespace("xs", "http://www.w3.org/2001/XMLSchema"),
                Namespace("xsd", "http://www.w3.org/2001/XMLSchema"),
                Namespace("gml", "http://www.opengis.net/gml"),
                Namespace("xlink", "http://www.w3.org/1999/xlink"),
                Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    Encoder gtEncoder;
    static final String INDENT_AMOUNT_KEY = "{http://xml.apache.org/xslt}indent-amount";

    @Override
    @Before
    public void setUp() throws Exception {
        this.gtEncoder = new Encoder(createConfiguration());
    }

    @Test
    public void testGeometryCollectionEncoder() throws Exception {
        GeometryCollectionEncoder gce = new GeometryCollectionEncoder(gtEncoder, "gml");
        GeometryCollection geometry = (GeometryCollection) new WKTReader2()
                .read("GEOMETRYCOLLECTION (LINESTRING" + " (180 200, 160 180), POINT (19 19), POINT (20 10))");
        Document doc = encode(gce, geometry);

        assertThat(doc, hasXPath("count(//gml:LineString)", equalTo("1")));
        assertThat(doc, hasXPath("count(//gml:Point)", equalTo("2")));
        assertThat(doc, hasXPath("count(//gml:coordinates)", equalTo("3")));
    }

    @Test
    public void testEncode3DLine() throws Exception {
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml");
        LineString geometry = (LineString) new WKTReader2().read("LINESTRING(0 0 50, 120 0 100)");
        Document doc = encode(encoder, geometry);

        assertThat(doc, hasXPath("//gml:coordinates", equalTo("0,0,50 120,0,100")));
    }

    @Test
    public void testEncode3DLineFromLiteCS() throws Exception {
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml");
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 0, 50, 120, 0, 100}, 3);
        LineString geometry = new GeometryFactory().createLineString(cs);
        Document doc = encode(encoder, geometry);
        assertThat(doc, hasXPath("//gml:coordinates", equalTo("0,0,50 120,0,100")));
    }

    @Test
    public void testEncode3DPoint() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml");
        Point geometry = (Point) new WKTReader2().read("POINT(0 0 50)");
        Document doc = encode(encoder, geometry);
        assertThat(doc, hasXPath("//gml:coordinates", equalTo("0,0,50")));
    }

    @Test
    public void testCoordinatesFormatting() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml");
        Point geometry = (Point) new WKTReader2().read("POINT(21396814.969 0 50)");
        Document doc = encode(encoder, geometry, 2, true, false);
        assertThat(doc, hasXPath("//gml:coordinates", equalTo("21396814.97,0,50")));

        doc = encode(encoder, geometry, 4, true, true);
        assertThat(doc, hasXPath("//gml:coordinates", equalTo("21396814.9690,0.0000,50.0000")));

        doc = encode(encoder, geometry, 4, false, false);
        assertThat(doc, hasXPath("//gml:coordinates", equalTo("2.1396814969E7,0,50")));
    }

    @Override
    protected Configuration createConfiguration() {
        return new GMLConfiguration();
    }

    protected <T extends Geometry> Document encode(GeometryEncoder<T> encoder, T geometry) throws Exception {
        return encode(encoder, geometry, 6, false, false);
    }

    protected <T extends Geometry> Document encode(
            GeometryEncoder<T> encoder, T geometry, int numDecimals, boolean forceDecimals, boolean padWithZeros)
            throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // create the document serializer
        SAXTransformerFactory txFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

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
                new GMLWriter(xmls, gtEncoder.getNamespaces(), numDecimals, forceDecimals, padWithZeros, "gml");
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
