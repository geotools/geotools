/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.csw;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xsd.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and attributes in the
 * http://purl.org/dc/elements/1.1/ schema.
 *
 * @generated
 */
public final class DC extends XSD {

    /** singleton instance */
    private static final DC instance = new DC();

    /** Returns the singleton instance. */
    public static final DC getInstance() {
        return instance;
    }

    /** private constructor */
    private DC() {}

    protected void addDependencies(Set dependencies) {
        // TODO: add dependencies here
    }

    /** Returns 'http://purl.org/dc/elements/1.1/'. */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns the location of 'rec-dcmes.xsd.'. */
    public String getSchemaLocation() {
        return getClass().getResource("rec-dcmes.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://purl.org/dc/elements/1.1/";

    /* Type Definitions */
    /** @generated */
    public static final QName elementContainer =
            new QName("http://purl.org/dc/elements/1.1/", "elementContainer");
    /** @generated */
    public static final QName SimpleLiteral =
            new QName("http://purl.org/dc/elements/1.1/", "SimpleLiteral");

    /* Elements */
    /** @generated */
    public static final QName contributor =
            new QName("http://purl.org/dc/elements/1.1/", "contributor");
    /** @generated */
    public static final QName coverage = new QName("http://purl.org/dc/elements/1.1/", "coverage");
    /** @generated */
    public static final QName creator = new QName("http://purl.org/dc/elements/1.1/", "creator");
    /** @generated */
    public static final QName date = new QName("http://purl.org/dc/elements/1.1/", "date");
    /** @generated */
    public static final QName DCelement =
            new QName("http://purl.org/dc/elements/1.1/", "DC-element");
    /** @generated */
    public static final QName description =
            new QName("http://purl.org/dc/elements/1.1/", "description");
    /** @generated */
    public static final QName format = new QName("http://purl.org/dc/elements/1.1/", "format");
    /** @generated */
    public static final QName identifier =
            new QName("http://purl.org/dc/elements/1.1/", "identifier");
    /** @generated */
    public static final QName language = new QName("http://purl.org/dc/elements/1.1/", "language");
    /** @generated */
    public static final QName publisher =
            new QName("http://purl.org/dc/elements/1.1/", "publisher");
    /** @generated */
    public static final QName relation = new QName("http://purl.org/dc/elements/1.1/", "relation");
    /** @generated */
    public static final QName rights = new QName("http://purl.org/dc/elements/1.1/", "rights");
    /** @generated */
    public static final QName source = new QName("http://purl.org/dc/elements/1.1/", "source");
    /** @generated */
    public static final QName subject = new QName("http://purl.org/dc/elements/1.1/", "subject");
    /** @generated */
    public static final QName title = new QName("http://purl.org/dc/elements/1.1/", "title");
    /** @generated */
    public static final QName type = new QName("http://purl.org/dc/elements/1.1/", "type");

    /* Attributes */

}
