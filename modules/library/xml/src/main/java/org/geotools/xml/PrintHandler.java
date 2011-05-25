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

import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;


/**
 * PrintHandler accepts SAXish events and generated output.
 *
 * @author dzwiers
 *
 * @source $URL$
 */
public interface PrintHandler {
    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     *
     * @throws IOException
     */
    public void startElement(URI namespaceURI, String localName,
        Attributes attributes) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     *
     * @throws IOException
     */
    public void element(URI namespaceURI, String localName,
        Attributes attributes) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @throws IOException
     */
    public void endElement(URI namespaceURI, String localName)
        throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     * @param arg2 DOCUMENT ME!
     *
     * @throws IOException
     */
    public void characters(char[] arg0, int arg1, int arg2)
        throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws IOException
     */
    public void characters(String s) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     * @param arg2 DOCUMENT ME!
     *
     * @throws IOException
     */
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
        throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @throws IOException
     */
    public void startDocument() throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @throws IOException
     */
    public void endDocument() throws IOException;

    /**
     * Returns the default Schema for the document being printed
     * 
     * @return Schema
     */
    public Schema getDocumentSchema();

    /**
     * Tries to find an appropriate Element so represent the value. 
     * 
     * @param value The Object being attempted to write
     * @return Element The element instance found, or null if not found.
     */
    public Element findElement(Object value);

    public Element findElement(String name);

    public Object getHint(Object key);
}
