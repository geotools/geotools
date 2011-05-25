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
package org.geotools.xlink;

import javax.xml.namespace.QName;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.w3.org/1999/xlink schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class XLINK extends XSD {
    /**
     * singleton instance
     */
    private static XLINK instance = new XLINK();

    /** @generated */
    public static final String NAMESPACE = "http://www.w3.org/1999/xlink";

    /* Type Definitions */

    /* Elements */

    /* Attributes */

    /** @generated */
    public static final QName ACTUATE = new QName("http://www.w3.org/1999/xlink", "actuate");

    /** @generated */
    public static final QName ARCROLE = new QName("http://www.w3.org/1999/xlink", "arcrole");

    /** @generated */
    public static final QName FROM = new QName("http://www.w3.org/1999/xlink", "from");

    /** @generated */
    public static final QName HREF = new QName("http://www.w3.org/1999/xlink", "href");

    /** @generated */
    public static final QName LABEL = new QName("http://www.w3.org/1999/xlink", "label");

    /** @generated */
    public static final QName ROLE = new QName("http://www.w3.org/1999/xlink", "role");

    /** @generated */
    public static final QName SHOW = new QName("http://www.w3.org/1999/xlink", "show");

    /** @generated */
    public static final QName TITLE = new QName("http://www.w3.org/1999/xlink", "title");

    /** @generated */
    public static final QName TO = new QName("http://www.w3.org/1999/xlink", "to");

    /**
     * Private constructor.
     */
    private XLINK() {
    }

    /**
     * The single instance.
     */
    public static XLINK getInstance() {
        return instance;
    }

    /**
     * Returns 'http://www.w3.org/1999/xlink'
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns location of 'xlinks.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("xlinks.xsd").toString();
    }
}
