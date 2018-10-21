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
package org.geotools.xml.schema;

import java.net.URI;

/**
 * Instances of this interface are intended to represent XML Schema Elements.
 *
 * @author dzwiers www.refractions.net
 */
public interface Element extends ElementGrouping {
    /** Returns True when the instance of this XML Schema Element is abstract, false otherwise */
    public boolean isAbstract();

    /** @see Schema#getBlockDefault() */
    public int getBlock();

    /** This returns the default value for the Element as a String */
    public String getDefault();

    /** @see Schema#getFinalDefault() */
    public int getFinal();

    /** This returns the fixed value for the Element as a String */
    public String getFixed();

    /** @see Schema#isElementFormDefault() */
    public boolean isForm();

    /** The Schema ID for this element definition. */
    public String getId();

    /** @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs() */
    public int getMaxOccurs();

    /** @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs() */
    public int getMinOccurs();

    /**
     * Returns the element declaration's name in the Schema document, and element name in the
     * instance document.
     */
    public String getName();

    /** DOCUMENT ME! */
    public URI getNamespace();

    /** Returns true when the element is nillable, false otherwise */
    public boolean isNillable();

    /**
     * This returns a reference to an element representing this element's substitution group. This
     * is of particular importance when resolving an instance document's value.
     */
    public Element getSubstitutionGroup();

    /**
     * Returns the declared type for this Element in the given Schema.
     *
     * @see Type
     */
    public Type getType(); // simpleType or complexType
}
