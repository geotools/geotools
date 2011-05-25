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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.xml.sax.SAXException;


/**
 *
 *
 * @source $URL$
 */
public class SLDParserTest extends TestCase{

    public void testRemoteSLD() throws ParserConfigurationException, SAXException, URISyntaxException, IOException{
        URL example = new URL("http://schemas.opengis.net/sld/1.0.20/example-sld.xml");
        

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(new URI(example.toString()),null);
        XMLSAXHandler.setLogLevel(Level.FINEST);
        XSISAXHandler.setLogLevel(Level.FINEST);
        XMLElementHandler.setLogLevel(Level.FINEST);
        XSIElementHandler.setLogLevel(Level.FINEST);

        // fails
//        parser.parse(example.openStream(), xmlContentHandler);
//
//        Object doc = xmlContentHandler.getDocument();
//        assertNotNull("Document missing", doc);
    }
}
