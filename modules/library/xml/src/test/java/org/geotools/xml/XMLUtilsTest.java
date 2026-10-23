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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
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
        Hints hints = new Hints(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

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

    /** Parsers without JAXP 1.5 support (e.g. Apache Xerces) must still be usable. */
    @Test
    public void saxParserIgnoresUnsupportedAccessExternalSchema() throws Exception {
        SAXParserFactory factory = mock(SAXParserFactory.class);
        SAXParser parser = mock(SAXParser.class);
        when(factory.newSAXParser()).thenReturn(parser);
        doThrow(new SAXNotRecognizedException(XMLConstants.ACCESS_EXTERNAL_SCHEMA))
                .when(parser)
                .setProperty(eq(XMLConstants.ACCESS_EXTERNAL_SCHEMA), any());

        assertSame(parser, XMLUtils.newSAXParser(factory));
    }

    /** XInclude namespace, used to spot an include element that was left in place. */
    private static final String XINCLUDE_NS = "http://www.w3.org/2001/XInclude";

    /** Text of the fragment an include would pull in if it were expanded. */
    private static final String FRAGMENT = "MODULAR_FRAGMENT";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void saxFactoryLeavesXIncludeUnprocessed() throws Exception {
        File document = writeXIncludeDocument();
        Hints hints = new Hints(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

        SAXParserFactory factory = XMLUtils.newSAXParserFactory(hints);
        factory.setNamespaceAware(true);
        assertFalse(factory.isXIncludeAware());

        IncludeCollector handler = new IncludeCollector();
        factory.newSAXParser().parse(document, handler);

        assertEquals(1, handler.includeElements);
        assertFalse(handler.text.toString().contains(FRAGMENT));
    }

    @Test
    public void documentBuilderFactoryLeavesXIncludeUnprocessed() throws Exception {
        File document = writeXIncludeDocument();
        Hints hints = new Hints(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

        DocumentBuilderFactory factory = XMLUtils.newDocumentBuilderFactory(hints);
        factory.setNamespaceAware(true);
        assertFalse(factory.isXIncludeAware());
        Document parsed = XMLUtils.newDocumentBuilder(factory, hints).parse(document);

        assertEquals(1, parsed.getElementsByTagNameNS(XINCLUDE_NS, "include").getLength());
        assertFalse(parsed.getDocumentElement().getTextContent().contains(FRAGMENT));
    }

    /** Writes a document whose only child is an include of a sibling text fragment. */
    private File writeXIncludeDocument() throws Exception {
        File fragment = tempFolder.newFile("fragment.txt");
        Files.writeString(fragment.toPath(), FRAGMENT, StandardCharsets.UTF_8);
        File document = tempFolder.newFile("document.xml");
        String xml =
                "<root xmlns:xi=\"" + XINCLUDE_NS + "\">" + "<xi:include href=\"fragment.txt\" parse=\"text\"/></root>";
        Files.writeString(document.toPath(), xml, StandardCharsets.UTF_8);
        return document;
    }

    /** Counts include elements and collects character data, to see if an include expanded. */
    public static class IncludeCollector extends DefaultHandler {
        public int includeElements = 0;
        public final StringBuilder text = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (XINCLUDE_NS.equals(uri) && "include".equals(localName)) {
                includeElements++;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }
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
