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
 * <p>
 * Instances of this interface are intended to represent XML Schema Elements.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 * @source $URL$
 */
public interface Element extends ElementGrouping {
    /**
     * <p>
     * Returns True when the instance of this XML Schema Element is abstract,
     * false otherwise
     * </p>
     *
     */
    public boolean isAbstract();

    /**
     * @see Schema#getBlockDefault()
     */
    public int getBlock();

    /**
     * <p>
     * This returns the default value for the Element as a String
     * </p>
     *
     */
    public String getDefault();

    /**
     * @see Schema#getFinalDefault()
     */
    public int getFinal();

    /**
     * <p>
     * This returns the fixed value for the Element as a String
     * </p>
     *
     */
    public String getFixed();

    /**
     * @see Schema#isElementFormDefault()
     */
    public boolean isForm();

    /**
     * <p>
     * The Schema ID for this element definition.
     * </p>
     *
     */
    public String getId();

    /**
     * @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs()
     */
    public int getMaxOccurs();

    /**
     * @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs()
     */
    public int getMinOccurs();

    /**
     * <p>
     * Returns the element declaration's name in the Schema document, and
     * element name in the instance document.
     * </p>
     *
     */
    public String getName();

    /**
     * DOCUMENT ME!
     *
     */
    public URI getNamespace();

    /**
     * <p>
     * Returns true when the element is nillable, false otherwise
     * </p>
     *
     */
    public boolean isNillable();

    /**
     * <p>
     * This returns a reference to an element representing this element's
     * substitution group. This is of particular importance when resolving an
     * instance document's value.
     * </p>
     *
     */
    public Element getSubstitutionGroup();

    /**
     * <p>
     * Returns the declared type for this Element in the given Schema.
     * </p>
     *
     *
     * @see Type
     */
    public Type getType(); // simpleType or complexType
}
