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
package org.geotools.filter;


/**
 * The ExpressionType interface lists all the possible type of filter. Should
 * be replaced by a type safe enum when we move to Java 1.5
 *
 * @author wolf
 *
 * @source $URL$
 */
public interface ExpressionType {
    /* This is a listing of all possible expression types, grouped by
       expressions that are implemented by a single expression class
       (ie. all math types are implemented by ExpressionMath). */

    /* Types implemented by ExpressionLiteral */
    /** Defines a literal expression with an undeclared type. */
    public static final short LITERAL_UNDECLARED = 115;

    /** Defines a literal expression with a declared double type. */
    public static final short LITERAL_DOUBLE = 101;

    /** Defines a literal expression with a declared integer type. */
    public static final short LITERAL_INTEGER = 102;

    /** Defines a literal expression with a declared string type. */
    public static final short LITERAL_STRING = 103;

    /** Defines a literal expression with a declared geometry type. */
    public static final short LITERAL_GEOMETRY = 104;

    /**
     *  Defines a literal expression with a declared long type.
     *  @since 2.4
     **/
    public static final short LITERAL_LONG = 100;

    /* Types implemented by ExpressionMath. */
    /** Defines a math expression for adding. */
    public static final short MATH_ADD = 105;

    /** Defines a math expression for subtracting. */
    public static final short MATH_SUBTRACT = 106;

    /** Defines a math expression for multiplying. */
    public static final short MATH_MULTIPLY = 107;

    /** Defines a math expression for dividing. */
    public static final short MATH_DIVIDE = 108;

    /* Types implemented by ExpressionAttribute. */
    /** Defines an attribute expression with a declared double type. */
    public static final short ATTRIBUTE_DOUBLE = 109;

    /** Defines an attribute expression with a declared integer type. */
    public static final short ATTRIBUTE_INTEGER = 110;

    /** Defines an attribute expression with a declared string type. */
    public static final short ATTRIBUTE_STRING = 111;

    /** Defines an attribute expression with a declared string type. */
    public static final short ATTRIBUTE_GEOMETRY = 112;

    /** Defines an attribute expression with a declared string type. */
    public static final short ATTRIBUTE_UNDECLARED = 100;

    /** Defines an attribute expression with a declared string type. */
    public static final short ATTRIBUTE = 113;

    /** Defines a function expression */
    public static final short FUNCTION = 114;
}
