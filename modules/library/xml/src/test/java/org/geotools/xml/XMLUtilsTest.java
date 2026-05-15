/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.geotools.TestData;
import org.geotools.util.NullEntityResolver;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLUtilsTest {
    public static Logger LOGGER = Logging.getLogger(XMLUtils.class);

    @Test
    public void transform() throws Exception {
        // stage lates.xml and lakes.xsd
        File gml = TestData.copy(this, "xml/fme/lakes/lakes.xml");
        File xsd = TestData.copy(this, "xml/fme/lakes/lakes.xsd");
        assertTrue("gml", gml.exists());
        assertTrue("xsd", xsd.exists());
        URI uri = gml.toURI();
        Hints hints = new Hints();
        hints.put(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

        // parsing InputSource
        InputSource gmlSource = new InputSource(uri.toString());

        DocumentBuilderFactory documentBuilderFactory = XMLUtils.newDocumentBuilderFactory(hints);
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = XMLUtils.newDocumentBuilder(documentBuilderFactory, hints);
        Document document = documentBuilder.parse(gmlSource);
        assertNotNull(document);

        TransformerFactory txFactory = XMLUtils.newTransformerFactory(hints);
        txFactory.setAttribute("indent-number", 3);

        // Transformer
        Transformer tx = XMLUtils.newTransformer(txFactory, hints);
        tx.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter writer = new StringWriter();
        tx.transform(XMLUtils.source(document), new StreamResult(writer));

        String text = writer.getBuffer().toString();
        assertNotNull(text);

        SAXParserFactory sxFactory = XMLUtils.newSAXParserFactory(hints);
        sxFactory.setNamespaceAware(true);
        sxFactory.setValidating(true);

        // SAXTransformer
        SAXParser saxParser = sxFactory.newSAXParser();
        FeatureMembers handler = new FeatureMembers();

        InputSource internal = new InputSource(new StringReader(text));
        internal.setPublicId("lakes");
        saxParser.parse(internal, handler);

        assertEquals(100, handler.count);
    }

    public static class FeatureMembers extends DefaultHandler {
        public int count = 0;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if (localName.equals("featureMember")) {
                count++;
            }
            super.startElement(uri, localName, qName, attributes);
        }
    }
}
