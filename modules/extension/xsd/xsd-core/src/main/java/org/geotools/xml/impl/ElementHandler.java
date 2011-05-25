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
package org.geotools.xml.impl;

import org.eclipse.xsd.XSDElementDeclaration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.namespace.QName;


/**
 * Classes implementing this interface serve has handlers for elements in an
 * instance document as it is parsed. The element handler interface is a subset of the {@link
 * org.xml.sax.ContentHandler} interface.
 *
 * <p>The methods <code>startElement, characters, and endElement</code> are called in
 * sequence as they are for normal sax content handlers.
 * </p>
 *
 * <p>
 * An element handler corresponds to a specific element in a schema. A handler
 * must return a child handler for each valid child element of its corresponding
 * element.
 * </p>
 *
 * @see org.xml.sax.ContentHandler
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public interface ElementHandler extends Handler {
    /**
     * Callback on leading edge of an element.
     *
     * @param qName The qualified name of the element being handled.
     * @param attributes The attributes of hte elmenent being handled.
     *
     * @throws SAXException Any xml errors that occur.
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    void startElement(QName qName, Attributes attributes)
        throws SAXException;

    /**
     * Callback when characters of an element are encountered.
     *
     * @param ch Array containing characters.
     * @param start The starting index of the characters.
     * @param length The number of characters.
     *
     * @throws SAXException Any xml errors.
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    void characters(char[] ch, int start, int length) throws SAXException;

    /**
     * Callback on trailing edge of element.
     *
     * @param qName The qualified name of the element being handled.
     *
     * @throws SAXException Any xml errors.
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    void endElement(QName qName) throws SAXException;

    /**
     * @return The declaration of hte element being handled.
     */
    XSDElementDeclaration getElementDeclaration();
}
