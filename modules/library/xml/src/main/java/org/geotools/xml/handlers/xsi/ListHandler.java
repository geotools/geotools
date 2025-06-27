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
 * ListHandler purpose.
 *
 * <p>represents a 'list' element
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class ListHandler extends XSIElementHandler {
    /** 'list' */
    public static final String LOCALNAME = "list";

    private String id;
    private String itemType;
    private SimpleTypeHandler simpleType;

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode() * (id == null ? 1 : id.hashCode()) * (itemType == null ? 1 : itemType.hashCode());
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SimpleTypeHandler sth = new SimpleTypeHandler();

                if (simpleType == null) {
                    simpleType = sth;
                } else {
                    throw new SAXNotRecognizedException(
                            getLocalName() + " may only have one '" + AllHandler.LOCALNAME + "' declaration.");
                }

                return sth;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, Attributes atts) {
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        itemType = atts.getValue("", "itemType");

        if (itemType == null) {
            itemType = atts.getValue(namespaceURI, "itemType");
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /** returns the itemType attribute */
    public String getItemType() {
        return itemType;
    }

    /** returns the nested simpleType if one exists */
    public SimpleTypeHandler getSimpleType() {
        return simpleType;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    @Override
    public int getHandlerType() {
        return LIST;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    @Override
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }
}
