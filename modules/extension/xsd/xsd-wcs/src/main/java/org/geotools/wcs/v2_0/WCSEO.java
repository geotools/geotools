/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs.v2_0;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xsd.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and attributes in the
 * http://www.opengis.net/wcseo/1.0 schema.
 *
 * @generated
 */
public final class WCSEO extends XSD {

    /** singleton instance */
    private static final WCSEO instance = new WCSEO();

    /** Returns the singleton instance. */
    public static final WCSEO getInstance() {
        return instance;
    }

    /** private constructor */
    private WCSEO() {}

    protected void addDependencies(Set dependencies) {
        dependencies.add(WCS.getInstance());
    }

    /** Returns 'http://www.opengis.net/wcs/2.0'. */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns the location of 'wcsAll.xsd.'. */
    public String getSchemaLocation() {
        return getClass().getResource("wcseo/v1_0/wcsEODescribeEOCoverageSet.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wcseo/1.0";

    /** @generated */
    public static final QName DescribeEOCoverageSetType =
            new QName(NAMESPACE, "DescribeEOCoverageSetType");

    /** @generated */
    public static final QName Sections = new QName(NAMESPACE, "Sections");

    /** @generated */
    public static final QName Section = new QName(NAMESPACE, "Section");
}
