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
 * represents a complex content element
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @source $URL$
 */
public class ComplexContentHandler extends XSIElementHandler {
    /** 'complexContent' */
    public static final String LOCALNAME = "complexContent";
    private String id;
    private String mixed;
    private Object child;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((mixed == null)
        ? 2 : mixed.hashCode());
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts){
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        mixed = atts.getValue("", "mixed");

        if (mixed == null) {
            mixed = atts.getValue(namespaceURI, "mixed");
        }
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
                ExtensionHandler sth = new ExtensionHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // restriction
            if (RestrictionHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                RestrictionHandler sth = new RestrictionHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }
        }

        return null;
    }

    /**
     * <p>
     * getter for the complexContent's child
     * </p>
     *
     */
    public Object getChild() {
        return child;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
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
