/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gml3.simple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.xml.sax.helpers.AttributesImpl;

public abstract class GeometryEncoderTestSupport extends GML3TestSupport {

    static final String INDENT_AMOUNT_KEY = "{http://xml.apache.org/xslt}indent-amount";

    protected Encoder gtEncoder;

    protected XpathEngine xpath;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.gtEncoder = new Encoder(createConfiguration());
        this.xpath = XMLUnit.newXpathEngine();
    }

    protected Document encode(GeometryEncoder encoder, Geometry geometry) throws Exception {
        return encode(encoder, geometry, null);
    }

    protected Document encode(GeometryEncoder encoder, Geometry geometry, String gmlId)
            throws Exception {
        return encode(encoder, geometry, true, gmlId, 6, false, false);
    }

    protected Document encode(
            GeometryEncoder encoder, Geometry geometry, boolean encodeMeasures, String gmlId)
            throws Exception {
        return encode(encoder, geometry, encodeMeasures, gmlId, 6, false, false);
    }

    protected Document encode(
            GeometryEncoder encoder,
            Geometry geometry,
            boolean encodeMeasures,
            String gmlId,
            int numDecimals,
            boolean decimalEncoding,
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
        xmls.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
        xmls.setResult(new StreamResult(out));

        GMLWriter handler =
                new GMLWriter(
                        xmls,
                        gtEncoder.getNamespaces(),
                        numDecimals,
                        decimalEncoding,
                        padWithZeros,
                        "gml",
                        encodeMeasures);
        handler.startDocument();
        handler.startPrefixMapping("gml", GML.NAMESPACE);
        handler.endPrefixMapping("gml");

        encoder.encode(geometry, new AttributesImpl(), handler, gmlId);
        handler.endDocument();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DOMResult result = new DOMResult();
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new StreamSource(in), result);
        Document d = (Document) result.getNode();
        return d;
    }
}
