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
package org.geotools.xml.handlers;

import java.net.URI;
import java.util.Map;
import org.geotools.xml.XMLElementHandler;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * Represents the start of an XML document ... serves up elements wrapped in handlers for a specified schema.
 *
 * @author dzwiers www.refractions.net
 */
public class DocumentHandler extends XMLElementHandler {
    /** Supplied {@link Schema} for parsing and validation */
    public static final String DEFAULT_NAMESPACE_HINT_KEY =
            "org.geotools.xml.handlers.DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY";

    private XMLElementHandler xeh = null;
    private ElementHandlerFactory ehf;

    /**
     * Creates a new DocumentHandler object.
     *
     * @param ehf ElementHandlerFactory
     */
    public DocumentHandler(ElementHandlerFactory ehf) {
        this.ehf = ehf;
    }

    /** @see org.geotools.xml.XMLElementHandler#getElement() */
    @Override
    public Element getElement() {
        return null;
    }

    /** @see org.geotools.xml.XMLElementHandler#endElement(java.lang.String, java.lang.String) */
    @Override
    public void endElement(URI namespaceURI, String localName, Map<String, Object> hints) {
        // do nothing
    }

    /** @see org.geotools.xml.XMLElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XMLElementHandler getHandler(URI namespaceURI, String localName, Map<String, Object> hints)
            throws SAXException {
        if (xeh != null) {
            throw new SAXNotRecognizedException("XML Documents may only have one top-level element");
        }
        if (hints != null && hints.containsKey(DEFAULT_NAMESPACE_HINT_KEY)) {
            Object t = hints.get(DEFAULT_NAMESPACE_HINT_KEY);
            if (t instanceof Schema) ehf.startPrefixMapping("", (Schema) t);
            else ehf.startPrefixMapping("", t.toString());
        }
        xeh = ehf.createElementHandler(namespaceURI, localName);

        return xeh;
    }

    /**
     * @see org.geotools.xml.XMLElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(URI namespaceURI, String localName, Attributes attr) {
        // do nothing
    }

    /** @see org.geotools.xml.XMLElementHandler#getValue() */
    @Override
    public Object getValue() throws SAXException {
        return xeh == null ? null : xeh.getValue();
    }

    /** @see org.geotools.xml.XMLElementHandler#getName() */
    @Override
    public String getName() {
        return "";
    }
}
