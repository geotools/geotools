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
 * KeyHandler purpose.
 *
 * <p>represents a 'key' element. This class is not currently used asside from as a placeholder. TODO use this class
 * semantically
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @version $Id$
 */
public class KeyHandler extends XSIElementHandler {
    /** 'key' */
    public static final String LOCALNAME = "key";

    private String id;
    private String name;
    private SelectorHandler selector;
    private List<FieldHandler> fields;

    /** @see java.lang.Object#hashCode() */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        return LOCALNAME.hashCode() * (id == null ? 1 : id.hashCode()) * (name == null ? 1 : name.hashCode());
    }

    /** @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String) */
    @Override
    public XSIElementHandler getHandler(String namespaceURI, String localName) throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // field
            if (FieldHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (fields == null) {
                    fields = new LinkedList<>();
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
                    throw new SAXNotRecognizedException(LOCALNAME + " may only have one child.");
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

        name = atts.getValue("", "name");

        if (name == null) {
            name = atts.getValue(namespaceURI, "name");
        }
    }

    /** @see org.geotools.xml.XSIElementHandler#getLocalName() */
    @Override
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * getFields purpose.
     *
     * <p>Returns a list of fields child declarations
     */
    public List getFields() {
        return fields;
    }

    /**
     * getId purpose.
     *
     * <p>returns the id attribute
     */
    public String getId() {
        return id;
    }

    /**
     * getName purpose.
     *
     * <p>returns the name attribute
     */
    public String getName() {
        return name;
    }

    /**
     * getSelector purpose.
     *
     * <p>returns the child selector element
     */
    public SelectorHandler getSelector() {
        return selector;
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
