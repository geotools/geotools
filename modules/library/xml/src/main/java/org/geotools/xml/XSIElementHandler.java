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

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * This abstract class is intended to act as both a definition of what a generic handler is, and a default handler.
 *
 * <p>When extending this class, one should as a minimum replace the start/end Element methods.
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public abstract class XSIElementHandler implements Serializable {
    /** the logger -- should be used for debugging (assuming there are bugs LOL) */
    public static final Logger logger = org.geotools.util.logging.Logging.getLogger(XSIElementHandler.class);

    private static Level level = Level.WARNING;

    /** Type constants */
    public static final int DEFAULT = 0; // for those cases where type is not

    /** Type constants */
    public static final int UNION = 1;

    /** Type constants */
    public static final int LIST = 2;

    /** Type constants */
    public static final int RESTRICTION = 4;

    /** Type constants */
    public static final int EXTENSION = 64;

    /** Type constants */
    public static final int SIMPLETYPE = 8;

    /** Type constants */
    public static final int SEQUENCE = 16;

    /** Type constants */
    public static final int FACET = 32;

    /** Creates a new XSIElementHandler object. Intended to limit creation to the sub-packages */
    protected XSIElementHandler() {
        logger.setLevel(level);
    }

    /**
     * Returns one of the Specified types ... intended for use by the child packages only
     *
     * @return int (DEFAULT?)
     */
    public abstract int getHandlerType();

    /**
     * In most cases this class should not be called within this framework as we do not intend to parse + store all the
     * information required to recreate the exact Schema document being parsed. As a result, information such as
     * annotations are ignored. When used, they method may be called multiple times for one element. This means the
     * implementor should keep this in mind when overriding this method.
     */
    public void characters(String text) throws SAXException {
        throw new SAXNotSupportedException("Should overide this method.");
    }

    /** handles SAX end Element events. this is an opportunity to complete some post-processing */
    public abstract void endElement(String namespaceURI, String localName) throws SAXException;

    /** handles SAX start Element events. This is an opportunity to complete some pre-processing. */
    public abstract void startElement(String namespaceURI, String localName, Attributes attr) throws SAXException;

    /**
     * This method will be used to create the XSI document. Validation and in-fix processing is expected to exist within
     * this method, along with data logging for post-processing. This method will directly affect the stack being used
     * to complete the parse.
     *
     * @return XSIElementHandler, or null
     */
    public abstract XSIElementHandler getHandler(String namespaceURI, String localName) throws SAXException;

    /**
     * Returns the LocalName for this element (ie this declaration in the Schema ... so ComplexType or Sequence ...)
     *
     * @return String (not-null)
     */
    public abstract String getLocalName();

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj instanceof XSIElementHandler) {
            XSIElementHandler ob = (XSIElementHandler) obj;

            if (getLocalName() != null) {
                return getLocalName().equals(ob.getLocalName());
            }

            return null == ob.getLocalName();
        }

        return false;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public abstract int hashCode();

    /** Sets the logging level for all XSIElementHandlers */
    public static void setLogLevel(Level l) {
        level = l;
        logger.setLevel(l);
    }
}
