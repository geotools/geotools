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

import java.util.logging.Logger;
import org.geotools.api.filter.Filter;

/**
 * Implements Filter interface, with constants and default behaviors for methods.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public abstract class AbstractFilter extends FilterAbstract implements FilterType, Filter {

    /** The logger for the default core module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(AbstractFilter.class);

    protected AbstractFilter() {}

    /**
     * This method checks if the object is an instance of {@link Feature} and if so, calls through to
     * {@link Filter#evaluate(Feature)}. This is done to maintain backwards compatability with previous version of
     * Filter api which depended on Feature. If the object is not an instance of feature the super implementation is
     * called.
     */
    /*
    public boolean evaluate(Object object) {
    	if (object instanceof Feature  || object == null ) {
    		return evaluate((Feature)object);
    	}

    	return false;
    }*/

    /* todo: replace public abstract void accept(FilterVisitor visitor); */
    /* ************************************************************************
     * Following static methods check for certain aggregate types, based on
     * (above) declared types.  Note that these aggregate types do not
     * necessarily map directly to the sub-classes of FilterDefault.  In most,
     * but not all, cases, a single class implements an aggregate type.
     * However, there are aggregate types that are implemented by multiple
     * classes (ie. the Math type is implemented by two seperate classes).
     * ***********************************************************************/

    /**
     * Checks to see if passed type is logic.
     *
     * @param filterType Type of filter for check.
     * @return Whether or not this is a logic filter type.
     */
    public static boolean isLogicFilter(short filterType) {
        LOGGER.entering("AbstractFilter", "isLogicFilter", Short.valueOf(filterType));

        return filterType == LOGIC_OR || filterType == LOGIC_AND || filterType == LOGIC_NOT;
    }

    /**
     * Checks to see if passed type is math.
     *
     * @param filterType Type of filter for check.
     * @return Whether or not this is a math filter type.
     */
    protected static boolean isMathFilter(int filterType) {
        return filterType == COMPARE_LESS_THAN
                || filterType == COMPARE_GREATER_THAN
                || filterType == COMPARE_LESS_THAN_EQUAL
                || filterType == COMPARE_GREATER_THAN_EQUAL;
    }

    /**
     * Checks to see if passed type is compare.
     *
     * @param filterType Type of filter for check.
     * @return Whether or not this is a compare filter type.
     */
    protected static boolean isCompareFilter(int filterType) {
        return isMathFilter(filterType)
                || filterType == COMPARE_EQUALS
                || filterType == BETWEEN
                || filterType == COMPARE_NOT_EQUALS;
    }

    /**
     * Checks to see if passed type is geometry.
     *
     * @param filterType Type of filter for check.
     * @return Whether or not this is a geometry filter type.
     */
    protected static boolean isGeometryFilter(short filterType) {
        return filterType == GEOMETRY_BBOX
                || filterType == GEOMETRY_EQUALS
                || filterType == GEOMETRY_DISJOINT
                || filterType == GEOMETRY_TOUCHES
                || filterType == GEOMETRY_INTERSECTS
                || filterType == GEOMETRY_CROSSES
                || filterType == GEOMETRY_WITHIN
                || filterType == GEOMETRY_CONTAINS
                || filterType == GEOMETRY_OVERLAPS
                || filterType == GEOMETRY_DWITHIN
                || filterType == GEOMETRY_BEYOND;
    }

    /**
     * Checks to see if passed type is geometry distance type.
     *
     * @param filterType Type of filter for check.
     * @return Whether or not this is a geometry filter type.
     */
    protected static boolean isGeometryDistanceFilter(short filterType) {
        return filterType == GEOMETRY_DWITHIN || filterType == GEOMETRY_BEYOND;
    }

    /**
     * Checks to see if passed type is logic.
     *
     * @param filterType Type of filter for check.
     * @return Whether or not this is a logic filter type.
     */
    public static boolean isSimpleFilter(short filterType) {
        return isCompareFilter(filterType)
                || isGeometryFilter(filterType)
                || filterType == NULL
                || filterType == FID
                || filterType == LIKE;
    }
}
