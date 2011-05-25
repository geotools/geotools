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

import org.geotools.xml.XSIElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * <p>
 * represtents a simpleContent element
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @source $URL$
 */
public class SimpleContentHandler extends XSIElementHandler {
    /** 'simpleContent' */
    public static final String LOCALNAME = "simpleContent";

    //    private String id;
    private Object child;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((child == null) ? 1 : child.hashCode());
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts){
        // do nothing
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName)
        throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // extension
            if (ExtensionHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ExtensionHandler lh = new ExtensionHandler();

                if (child == null) {
                    child = lh;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one child declaration.");
                }

                return lh;
            }

            // restriction
            if (RestrictionHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                RestrictionHandler lh = new RestrictionHandler();

                if (child == null) {
                    child = lh;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one child declaration.");
                }

                return lh;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the Child element
     */
    public Object getChild() {
        return child;
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
