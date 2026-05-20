/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Big dataset tests ... more than you want for regular testing @
 *
 * @author dzwiers www.refractions.net
 */
public class GMLParser2Test {
    @Test
    public void testBlank() {
        // blank test ... lets it sit in the repository
    }

    @Test
    public void testFMEPostalFeatures() throws SAXException, IOException {
        try {
            SAXParserFactory parserFactory = XMLUtils.newSAXParserFactory();
            parserFactory.setNamespaceAware(true);
            parserFactory.setValidating(false);

            SAXParser parser = XMLUtils.newSAXParser(parserFactory);

            String path = "city/dj.xml";
            File f = TestData.file(this, path);
            URI u = f.toURI();

            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
            XMLSAXHandler.setLogLevel(Level.WARNING);
            XSISAXHandler.setLogLevel(Level.WARNING);
            XMLElementHandler.setLogLevel(Level.WARNING);
            XSIElementHandler.setLogLevel(Level.WARNING);

            parser.parse(f, xmlContentHandler);

            Object doc = xmlContentHandler.getDocument();
            Assert.assertNotNull("Document missing", doc);
            // System.out.println(doc);
        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }
}
