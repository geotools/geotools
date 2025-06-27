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

/**
 * ImportHandler purpose.
 *
 * <p>Represents an 'include' element
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class IncludeHandler extends XSIElementHandler {
    /** 'include' */
    public static final String LOCALNAME = "include";

    private static int offset = 0;

    //    private String id;
    private String schemaLocation;
    private int hashCodeOffset = getOffset();

    /*
     * helper for hashCode()
     */
    private static int getOffset() {
        return offset++;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode() * (schemaLocation == null ? 1 : schemaLocation.hashCode()) + hashCodeOffset;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) {
        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String namespaceURI, String localName, Attributes atts) {
        schemaLocation = atts.getValue("", "schemaLocation");

        if (schemaLocation == null) {
            schemaLocation = atts.getValue(namespaceURI, "schemaLocation");
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /** returns the schemaLocation attribute */
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandlerType() */
    @Override
    public int getHandlerType() {
        return DEFAULT;
    }

    /** @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String, java.lang.String) */
    @Override
    public void endElement(String namespaceURI, String localName) {
        // do nothing
    }
}
