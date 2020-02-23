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

import java.util.Map;
import javax.naming.OperationNotSupportedException;

/**
 * This interface is intended to represent an XML Schema simple type. This interface extends the
 * generic XML schema type interface to represent datum within a single element.
 *
 * @author dzwiers www.refractions.net
 */
public interface SimpleType extends Type {
    /**
     * Represents a masks used to represent how this simpleType may or may not be changed though the
     * use of inheritance
     */
    public static final int NONE = 0;

    /**
     * Represents a masks used to represent how this simpleType may or may not be changed though the
     * use of inheritance
     */
    public static final int UNION = 1;

    /**
     * Represents a masks used to represent how this simpleType may or may not be changed though the
     * use of inheritance
     */
    public static final int LIST = 2;

    /**
     * Represents a masks used to represent how this simpleType may or may not be changed though the
     * use of inheritance
     */
    public static final int RESTRICTION = 4;

    /**
     * Represents a masks used to represent how this simpleType may or may not be changed though the
     * use of inheritance
     */
    public static final int ALL = 7;

    /**
     * This specifies a mask which represents how this XML Schema SimpleType may be
     * extended/restricted ... through Schema declared derivations.
     */
    public int getFinal();

    /** The Schema ID for this simpleType definition. */
    public String getId();

    /** returns the value as a string */
    public AttributeValue toAttribute(Attribute attribute, Object value, Map hints)
            throws OperationNotSupportedException;

    /** */
    public boolean canCreateAttributes(Attribute attribute, Object value, Map hints);

    /** Returns an int of either List, Union or Restriction */
    public int getChildType();

    /** A simple simpleType when either a List or Restriction ... A set when a Union */
    public SimpleType[] getParents();

    /** The list of facets for this Restriction ... Null if another type (List, Union) */
    public Facet[] getFacets();
}
