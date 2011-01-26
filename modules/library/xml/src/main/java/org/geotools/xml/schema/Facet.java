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

/**
 * <p>
 * DOCUMENT ME!
 * </p>
 *
 * @author dzwiers
 * @source $URL$
 */
public interface Facet {
    /** DOCUMENT ME! */
    public static int ENUMERATION = 1;

    /** DOCUMENT ME! */
    public static int FRACTIONDIGITS = 2;

    /** DOCUMENT ME! */
    public static int LENGTH = 3;

    /** DOCUMENT ME! */
    public static int MAXEXCLUSIVE = 4;

    /** DOCUMENT ME! */
    public static int MAXINCLUSIVE = 5;

    /** DOCUMENT ME! */
    public static int MAXLENGTH = 6;

    /** DOCUMENT ME! */
    public static int MINEXCLUSIVE = 7;

    /** DOCUMENT ME! */
    public static int MININCLUSIVE = 8;

    /** DOCUMENT ME! */
    public static int MINLENGTH = 9;

    /** DOCUMENT ME! */
    public static int PATTERN = 10;

    /** DOCUMENT ME! */
    public static int TOTALDIGITS = 11;

    /** DOCUMENT ME! */
    public static int WHITESPACE = 12;

    /**
     * The Facet Type -- selected from one of the above constant values
     *
     */
    public int getFacetType();

    /**
     * The facet's constraint
     *
     */
    public String getValue();
}
