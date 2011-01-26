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
package org.geotools.renderer.lite;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A wrapper that forwards any request to the default JAXP xml reader.
 * <p>
 * By default Batik wants Xerces, but we want to avoid the dependency since a
 * SAX2 parser is already included in the jre.
 * <p>
 * This class is needed because Batik wants the name of a class that implements
 * XMLReader and has a public default constructor, and default jre parsers do
 * not have it.
 * 
 * @author wolf
 * @since 2.2.1
 *
 * @source $URL$
 */
public class BatikXMLReader implements XMLReader {
    XMLReader reader;

    public BatikXMLReader() throws ParserConfigurationException, SAXException {
        reader = XMLReaderFactory.createXMLReader();
    }

    public ContentHandler getContentHandler() {
        return reader.getContentHandler();
    }

    public DTDHandler getDTDHandler() {
        return reader.getDTDHandler();
    }

    public EntityResolver getEntityResolver() {
        return reader.getEntityResolver();
    }

    public ErrorHandler getErrorHandler() {
        return reader.getErrorHandler();
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        return reader.getFeature(name);
    }

    public Object getProperty(String name) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        return reader.getProperty(name);
    }

    public void parse(InputSource input) throws IOException, SAXException {
        reader.parse(input);
    }

    public void parse(String systemId) throws IOException, SAXException {
        reader.parse(systemId);
    }

    public void setContentHandler(ContentHandler handler) {
        reader.setContentHandler(handler);
    }

    public void setDTDHandler(DTDHandler handler) {
        reader.setDTDHandler(handler);
    }

    public void setEntityResolver(EntityResolver resolver) {
        reader.setEntityResolver(resolver);
    }

    public void setErrorHandler(ErrorHandler handler) {
        reader.setErrorHandler(handler);
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        reader.setFeature(name, value);
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException,
            SAXNotSupportedException {
        reader.setProperty(name, value);
    }
}
