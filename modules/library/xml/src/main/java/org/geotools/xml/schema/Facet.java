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

/** @author dzwiers */
public interface Facet {

    public static int ENUMERATION = 1;

    public static int FRACTIONDIGITS = 2;

    public static int LENGTH = 3;

    public static int MAXEXCLUSIVE = 4;

    public static int MAXINCLUSIVE = 5;

    public static int MAXLENGTH = 6;

    public static int MINEXCLUSIVE = 7;

    public static int MININCLUSIVE = 8;

    public static int MINLENGTH = 9;

    public static int PATTERN = 10;

    public static int TOTALDIGITS = 11;

    public static int WHITESPACE = 12;

    /** The Facet Type -- selected from one of the above constant values */
    public int getFacetType();

    /** The facet's constraint */
    public String getValue();
}
