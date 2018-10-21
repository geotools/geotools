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

import static junit.framework.TestCase.assertNotNull;

import java.io.File;
import java.net.URI;
import java.util.logging.Level;
import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestCase;
import org.geotools.TestData;
import org.geotools.xml.schema.Schema;
import org.xml.sax.SAXException;

/**
 * DOCUMENT ME! @
 *
 * @author dzwiers www.refractions.net
 */
public class XMLParserTest extends TestCase {

    public void testNestedFeature() throws Throwable {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "/wfsgetfeature.xml";
        File f = TestData.copy(this, path);
        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(null);
        XMLSAXHandler.setLogLevel(Level.FINEST);
        XSISAXHandler.setLogLevel(Level.FINEST);
        XMLElementHandler.setLogLevel(Level.FINEST);
        XSIElementHandler.setLogLevel(Level.FINEST);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        assertNotNull("Document missing", doc);
    }

    public void testMail() {
        try {
            String path = "xml/mails.xml";

            File f = TestData.copy(this, path);
            TestData.copy(this, "xml/mails.xsd");
            URI u = f.toURI();

            Object doc = DocumentFactory.getInstance(u, null, Level.WARNING);

            assertNotNull("Document missing", doc);
            // System.out.println(doc);
        } catch (SAXException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail(e.toString());
        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail(e.toString());
        }
    }

    public void testMailWrite() {

        try {
            String path = "xml/mails.xml";

            File f = TestData.copy(this, path);
            TestData.copy(this, "xml/mails.xsd");

            Object doc = DocumentFactory.getInstance(f.toURI(), null, Level.WARNING);
            assertNotNull("Document missing", doc);

            Schema s = SchemaFactory.getInstance(new URI("http://mails/refractions/net"));

            path = "mails_out.xml";
            f = TestData.temp(this, path);
            if (f.exists()) f.delete();
            f.createNewFile();

            DocumentWriter.writeDocument(doc, s, f, null);

            doc = DocumentFactory.getInstance(f.toURI(), null, Level.WARNING);
            assertNotNull("New Document missing", doc);

            assertTrue("file was not created +f", f.exists());
            // System.out.println(f);
        } catch (SAXException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail(e.toString());
        } catch (Throwable e) {
            assertTrue(e instanceof OperationNotSupportedException);
            //            e.printStackTrace();
            //            fail(e.toString()); Operation not supported yet
        }
    }
}
