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
package org.geotools.xml.handlers.xsi;

import java.net.URI;

import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;


/**
 * RootHandler purpose.
 * 
 * <p>
 * This is intended to bootstrap the schema parsing
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class RootHandler extends XSIElementHandler {
    /** 'root' */
    public final static String LOCALNAME = "root";
    private SchemaHandler schema;
    private URI uri;

    /*
     * should not be called
     */
    private RootHandler() {
        // do nothing
    }

    /**
     * Creates a new RootHandler object.
     *
     * @param uri
     */
    public RootHandler(URI uri) {
        this.uri = uri;
        schema = new SchemaHandler();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((uri == null) ? 1 : uri.hashCode());
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName){
        if (SchemaHandler.LOCALNAME.equalsIgnoreCase(localName)
                && SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            if (schema == null) {
                schema = new SchemaHandler();
            }

            return schema;
        }

        logger.warning("Starting schema with " + localName + " element.");

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes attr) throws SAXException {
        throw new SAXNotSupportedException(
            "Should never have elements at the root level");
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * <p>
     * intended to be called after the parse, this generates a Schema object
     * from the schema which was parsed in.
     * </p>
     *
     *
     * @throws SAXException
     */
    public Schema getSchema() throws SAXException {
        Schema s = schema.compress(uri);

        return s;
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String arg0, String arg1) {
        schema.startPrefixMapping(arg0, arg1);
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return DEFAULT;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }
}
