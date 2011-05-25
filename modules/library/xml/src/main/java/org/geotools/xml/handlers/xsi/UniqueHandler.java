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

import java.util.LinkedList;
import java.util.List;

import org.geotools.xml.XSIElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * UniqueHandler purpose.
 * 
 * <p>
 * represents a unique element. This class is not currently used except as a
 * placeholder.
 * </p>
 * TODO used this class semantically
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class UniqueHandler extends XSIElementHandler {
    /** 'unique' */
    public final static String LOCALNAME = "unique";
    private String id;
    private String xpath;
    private SelectorHandler selector;
    private List fields;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((xpath == null)
        ? 1 : xpath.hashCode()) * ((fields == null) ? 1 : fields.hashCode());
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
            // annotation
            // field
            if (FieldHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (fields == null) {
                    fields = new LinkedList();
                }

                FieldHandler fh = new FieldHandler();
                fields.add(fh);

                return fh;
            }

            // selector
            if (SelectorHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SelectorHandler sth = new SelectorHandler();

                if (selector == null) {
                    selector = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child.");
                }

                return sth;
            }
        }

        return null;
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

        xpath = atts.getValue("", "xpath");

        if (xpath == null) {
            xpath = atts.getValue(namespaceURI, "xpath");
        }
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
     * @return the id attribute value
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the xpath attribute value
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return List of FieldHandlers representing sub-elements
     */
    public List getFields() {
        return fields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the selector sub-element
     */
    public SelectorHandler getSelector() {
        return selector;
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
