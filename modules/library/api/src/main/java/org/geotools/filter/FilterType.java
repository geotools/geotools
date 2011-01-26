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
 * The FilterType interface lists all the possible type of filter.
 * <p>
 * Example:<pre><code>
 * BEFORE: filter.getFilterType() == FilterType.GEOMETRY_CONTAINS
 * QUICK:  Filters.getFilterType( filter ) == FilterType.GEOMETRY_CONTAINS
 * AFTER: filter instanceof Contains
 * </code></pre>
 *
 * @author aaime
 * @source $URL$
 * @deprecated please use instance of check against geoapi class.
 */
public interface FilterType {
    /* ***********************************************************************
     * This is a listing of all possible filter types, grouped by types that
     * are implemented by a single filter (ie. all logic types are implemented
     * by FilterLogic).
     * **********************************************************************/

    /* Types implemented by FilterLogic */
    /** Defines a logical 'OR' filter. */
    public static final short LOGIC_OR = 1;

    /** Defines a logical 'AND' filter. */
    public static final short LOGIC_AND = 2;

    /** Defines a logical 'NOT' filter. */
    public static final short LOGIC_NOT = 3;

    /* Types implemented by FilterGeometry */
    /** Defines a geometric bounding box filter. */
    public static final short GEOMETRY_BBOX = 4;

    /** Defines a geometric 'EQUALS' operator. */
    public static final short GEOMETRY_EQUALS = 5;

    /** Defines a geometric 'DISJOINT' operator. */
    public static final short GEOMETRY_DISJOINT = 6;

    /** Defines a geometric 'INTERSECTS' operator. */
    public static final short GEOMETRY_INTERSECTS = 7;

    /** Defines a geometric 'TOUCHES' operator. */
    public static final short GEOMETRY_TOUCHES = 8;

    /** Defines a geometric 'CROSSES' operator. */
    public static final short GEOMETRY_CROSSES = 9;

    /** Defines a geometric 'WITHIN' operator. */
    public static final short GEOMETRY_WITHIN = 10;

    /** Defines a geometric 'CONTAINS' operator. */
    public static final short GEOMETRY_CONTAINS = 11;

    /** Defines a geometric 'OVERLAPS' operator. */
    public static final short GEOMETRY_OVERLAPS = 12;

    /** Defines a geometric 'BEYOND' operator. */
    public static final short GEOMETRY_BEYOND = 13;

    /** Defines a geometric 'DWITHIN' operator. */
    public static final short GEOMETRY_DWITHIN = 24;

    /* Types implemented by FilterCompare */
    /** Defines a comparative equals filter (may be a math filter). */
    public static final short COMPARE_EQUALS = 14;

    /** Defines a comparative less than filter (is a math filter). */
    public static final short COMPARE_LESS_THAN = 15;

    /** Defines a comparative greater than filter (is a math filter). */
    public static final short COMPARE_GREATER_THAN = 16;

    /** Defines a comparative less than/equals filter (is a math filter). */
    public static final short COMPARE_LESS_THAN_EQUAL = 17;

    /** Defines a comparative greater than/equals filter (is a math filter). */
    public static final short COMPARE_GREATER_THAN_EQUAL = 18;

    /** Defines a comparative not equals filter (may be a math filter). */
    public static final short COMPARE_NOT_EQUALS = 23;

    /**
     * Defines a between filter, which is implemented by FilterBetween. Note
     * that this filter is defined as a math filter.
     */
    public static final short BETWEEN = 19;

    /** Defines a null filter, which is implemented by FilterNull. */
    public static final short NULL = 21;

    /** Defines a like filter, which is implemented by FilterLike. */
    public static final short LIKE = 20;

    /** Defines a fid filter, which is implemented by FidFilterImpl. */
    public static final short FID = 22;

    /** Defines an empty filter, with static implementation Filter.INCLUDE */
    public static final short NONE = 12345;

    /** Defines a sieve filter, with static implementation Filter.EXCLUDE */
    public static final short ALL = -12345;
}
