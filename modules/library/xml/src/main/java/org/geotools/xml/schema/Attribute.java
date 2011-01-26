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
 * An instance of this interface should represent an Attribute from an XML
 * schema.
 * </p>
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public interface Attribute {
    /**
     * Represent a mask used to determine the life of the attribute in an
     * instance document.
     */
    public static final int OPTIONAL = 0;

    /**
     * Represent a mask used to determine the life of the attribute in an
     * instance document.
     */
    public static final int PROHIBITED = 1;

    /**
     * Represent a mask used to determine the life of the attribute in an
     * instance document.
     */
    public static final int REQUIRED = 2;

    /**
     * <p>
     * This is intended to imitate the default value option provided in the
     * declaration of an XML Schema attribute.
     * </p>
     *
     * @return Default Value as a String or Null
     */
    public String getDefault();

    /**
     * <p>
     * This is intended to imitate the fixed value option provided in the
     * declaration of an XML Schema attribute.
     * </p>
     *
     * @return Fixed Value as a String or Null
     */
    public String getFixed();

    /**
     * <p>
     * Intended to represent the form of an XML attribute. This method should
     * return True when the attribute is "qualified".
     * </p>
     *
     */
    public boolean isForm();

    /**
     * <p>
     * The Schema ID for this attribute definition.
     * </p>
     *
     */
    public String getId();

    /**
     * <p>
     * The name of the attribute specified if one was provided in the attribute
     * declaration
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
     * Attributes only have three options for the number or occurences: none,
     * once, optionally once. This method returns the mask which represents
     * the use of this attribute.
     * </p>
     *
     */
    public int getUse();

    /**
     * <p>
     * Provides a reference to the simpleType which defines the data type of
     * this attribute.
     * </p>
     *
     */
    public SimpleType getSimpleType();
}
