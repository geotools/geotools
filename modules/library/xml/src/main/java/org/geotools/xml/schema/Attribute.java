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
 * An instance of this interface should represent an Attribute from an XML schema.
 *
 * @author dzwiers www.refractions.net
 */
public interface Attribute {
    /** Represent a mask used to determine the life of the attribute in an instance document. */
    public static final int OPTIONAL = 0;

    /** Represent a mask used to determine the life of the attribute in an instance document. */
    public static final int PROHIBITED = 1;

    /** Represent a mask used to determine the life of the attribute in an instance document. */
    public static final int REQUIRED = 2;

    /**
     * This is intended to imitate the default value option provided in the declaration of an XML
     * Schema attribute.
     *
     * @return Default Value as a String or Null
     */
    public String getDefault();

    /**
     * This is intended to imitate the fixed value option provided in the declaration of an XML
     * Schema attribute.
     *
     * @return Fixed Value as a String or Null
     */
    public String getFixed();

    /**
     * Intended to represent the form of an XML attribute. This method should return True when the
     * attribute is "qualified".
     */
    public boolean isForm();

    /** The Schema ID for this attribute definition. */
    public String getId();

    /** The name of the attribute specified if one was provided in the attribute declaration */
    public String getName();

    /** DOCUMENT ME! */
    public URI getNamespace();

    /**
     * Attributes only have three options for the number or occurences: none, once, optionally once.
     * This method returns the mask which represents the use of this attribute.
     */
    public int getUse();

    /** Provides a reference to the simpleType which defines the data type of this attribute. */
    public SimpleType getSimpleType();
}
