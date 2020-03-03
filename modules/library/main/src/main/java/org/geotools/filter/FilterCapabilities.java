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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Represents the Filter capabilities that are supported by a SQLEncoder
 *
 * <p>Each SQLEncoder class should have one static FilterCapabilities, representing the filter
 * encoding operations that it can successfully perform.
 *
 * <p>This class is used as one big mask to detect filters that cannot be performed
 *
 * @author Chris Holmes, TOPP TODO: check if possible to deprecate @ deprecated use {@link
 *     org.opengis.filter.capability.FilterCapabilities}.
 */
public class FilterCapabilities {
    /** Mask for no operation */
    public static final long NO_OP = 0;

    /** Mask for Filter.INCLUDE */
    public static final long NONE = 0x01 << 30;

    /** Mask for Filter.EXCLUDE */
    public static final long ALL = 0x01 << 31;

    // spatial masks
    /** Spatial Mask for bbox operation */
    public static final long SPATIAL_BBOX = 0x01;
    /** Spatial Mask for equals operation */
    public static final long SPATIAL_EQUALS = 0x01 << 1;
    /** Spatial Mask for disjoint operation */
    public static final long SPATIAL_DISJOINT = 0x01 << 2;
    /** Spatial Mask for intersect operation */
    public static final long SPATIAL_INTERSECT = 0x01 << 3;
    /** Spatial Mask for touches operation */
    public static final long SPATIAL_TOUCHES = 0x01 << 4;
    /** Spatial Mask for crosses operation */
    public static final long SPATIAL_CROSSES = 0x01 << 5;
    /** Spatial Mask for within operation */
    public static final long SPATIAL_WITHIN = 0x01 << 6;
    /** Spatial Mask for contains operation */
    public static final long SPATIAL_CONTAINS = 0x01 << 7;
    /** Spatial Mask for overlaps operation */
    public static final long SPATIAL_OVERLAPS = 0x01 << 8;
    /** Spatial Mask for beyond operation */
    public static final long SPATIAL_BEYOND = 0x01 << 9;
    /** Spatial Mask for dwithin operation */
    public static final long SPATIAL_DWITHIN = 0x01 << 10;

    // scalar masks
    /** Scalar Mask for like operation */
    public static final long LIKE = 0x01 << 11;
    /** Scalar Mask for between opelongion */
    public static final long BETWEEN = 0x01 << 12;
    /** Scalar Mask for null check operation */
    public static final long NULL_CHECK = 0x01 << 13;
    /** Scalar Mask for simple arithmetic operations */
    public static final long SIMPLE_ARITHMETIC = 0x01 << 14;
    /** Scalar Mask for function operations */
    public static final long FUNCTIONS = 0x01 << 15;

    // masks for different comparison filters
    public static final long COMPARE_EQUALS = 0x01 << 16;

    public static final long COMPARE_GREATER_THAN = 0x01 << 17;

    public static final long COMPARE_GREATER_THAN_EQUAL = 0x01 << 18;

    public static final long COMPARE_LESS_THAN = 0x01 << 19;

    public static final long COMPARE_LESS_THAN_EQUAL = 0x01 << 20;

    public static final long COMPARE_NOT_EQUALS = 0x01 << 21;

    public static final long FID = 0x01 << 22;

    // masks for different logic filters
    public static final long LOGIC_AND = 0x01 << 23;

    public static final long LOGIC_NOT = 0x01 << 24;

    public static final long LOGIC_OR = 0x01 << 25;

    /** Scalar Mask for logical operation */
    public static final long LOGICAL = (LOGIC_AND | LOGIC_OR | LOGIC_NOT);
    /** Scalar Mask for simple comparison operations */
    public static final long SIMPLE_COMPARISONS =
            COMPARE_EQUALS
                    | COMPARE_GREATER_THAN
                    | COMPARE_GREATER_THAN_EQUAL
                    | COMPARE_LESS_THAN
                    | COMPARE_LESS_THAN_EQUAL
                    | COMPARE_NOT_EQUALS;

    public static final FilterCapabilities SIMPLE_COMPARISONS_OPENGIS;

    public static final FilterCapabilities LOGICAL_OPENGIS;

    static final Map /*<int,Class>*/ intTypeToOpenGisTypeMap = new HashMap();

    static {
        SIMPLE_COMPARISONS_OPENGIS = new FilterCapabilities();
        SIMPLE_COMPARISONS_OPENGIS.addType(PropertyIsEqualTo.class);
        SIMPLE_COMPARISONS_OPENGIS.addType(PropertyIsGreaterThan.class);
        SIMPLE_COMPARISONS_OPENGIS.addType(PropertyIsGreaterThanOrEqualTo.class);
        SIMPLE_COMPARISONS_OPENGIS.addType(PropertyIsLessThanOrEqualTo.class);
        SIMPLE_COMPARISONS_OPENGIS.addType(PropertyIsLessThan.class);
        SIMPLE_COMPARISONS_OPENGIS.addType(PropertyIsNotEqualTo.class);

        LOGICAL_OPENGIS = new FilterCapabilities();
        LOGICAL_OPENGIS.addType(And.class);
        LOGICAL_OPENGIS.addType(Not.class);
        LOGICAL_OPENGIS.addType(Or.class);

        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_BBOX), BBOX.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_EQUALS), Equals.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_DISJOINT), Disjoint.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_INTERSECT), Intersects.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_TOUCHES), Touches.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_CROSSES), Crosses.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_WITHIN), Within.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_CONTAINS), Contains.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_OVERLAPS), Overlaps.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_BEYOND), Beyond.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(SPATIAL_DWITHIN), DWithin.class);
        intTypeToOpenGisTypeMap.put(
                Long.valueOf(SIMPLE_ARITHMETIC),
                new Class[] {Add.class, Subtract.class, Multiply.class, Divide.class});
        intTypeToOpenGisTypeMap.put(Long.valueOf(COMPARE_EQUALS), PropertyIsEqualTo.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(COMPARE_NOT_EQUALS), PropertyIsNotEqualTo.class);
        intTypeToOpenGisTypeMap.put(
                Long.valueOf(COMPARE_GREATER_THAN), PropertyIsGreaterThan.class);
        intTypeToOpenGisTypeMap.put(
                Long.valueOf(COMPARE_GREATER_THAN_EQUAL), PropertyIsGreaterThanOrEqualTo.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(COMPARE_LESS_THAN), PropertyIsLessThan.class);
        intTypeToOpenGisTypeMap.put(
                Long.valueOf(COMPARE_LESS_THAN_EQUAL), PropertyIsLessThanOrEqualTo.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(NULL_CHECK), PropertyIsNull.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(LIKE), PropertyIsLike.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(BETWEEN), PropertyIsBetween.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(LOGIC_AND), And.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(LOGIC_OR), Or.class);
        intTypeToOpenGisTypeMap.put(Long.valueOf(LOGIC_NOT), Not.class);
    }

    private long ops = NO_OP;

    private Set<Class> functions = new HashSet<>();

    public FilterCapabilities(long filterCapabilitiesType) {
        addType(filterCapabilitiesType);
    }

    public FilterCapabilities() {
        this(NO_OP);
    }

    public FilterCapabilities(Class type) {
        addType(type);
    }

    /**
     * Adds a new support type to capabilities.
     *
     * @param type The one of the masks enumerated in this class
     */
    public void addType(long type) {
        // avoid infinite recursion with addType(class)
        if ((ops & type) != 0) return;

        ops = ops | type;
        // the type can be a mask signifying multiple filter types, we have to add them all
        for (Iterator it = intTypeToOpenGisTypeMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            long key = ((Long) entry.getKey()).longValue();
            if ((key & type) != 0) {
                Object filter = entry.getValue();
                if (filter != null) {
                    if (filter instanceof Class[]) {
                        Class[] filters = (Class[]) filter;
                        for (int i = 0; i < filters.length; i++) {
                            addType(filters[i], false);
                        }
                    } else {
                        addType((Class) filter, false);
                    }
                }
            }
        }
    }

    /**
     * Adds a new support type to capabilities. For 2.2 only function expression support is added
     * this way. As of geotools 2.3 this will be the supported way of adding to Filtercapabilities.
     *
     * @param type the Class that indicates the new support.
     */
    public void addType(Class type) {
        if (org.opengis.filter.Filter.class.isAssignableFrom(type)
                || org.opengis.filter.expression.Expression.class.isAssignableFrom(type)) {
            addType(FUNCTIONS);
            functions.add(type);
        }
    }

    /**
     * Adds a new support type to capabilities. For 2.2 only function expression support is added
     * this way. As of geotools 2.3 this will be the supported way of adding to Filtercapabilities.
     *
     * @param type the Class that indicates the new support.
     */
    public void addType(Class type, boolean addFunctionType) {
        if (org.opengis.filter.Filter.class.isAssignableFrom(type)
                || org.opengis.filter.expression.Expression.class.isAssignableFrom(type)) {
            if (addFunctionType) addType(FUNCTIONS);
            functions.add(type);
        }
    }

    /**
     * Add all the capabilities in the provided FilterCapabilities to this capabilities.
     *
     * @param capabilities capabilities to add.
     */
    public void addAll(FilterCapabilities capabilities) {
        addType(capabilities.ops);
        functions.addAll(capabilities.functions);
    }

    /**
     * Returns the mask that is equivalent to the FilterType constant.
     *
     * @param type a constant from {@link FilterType}
     * @return the mask that is equivalent to the FilterType constant.
     */
    public FilterCapabilities convertFilterTypeToMask(short type) {
        if (type == FilterType.ALL) return FilterNameTypeMapping.NO_OP_CAPS;
        if (type == FilterType.NONE) return FilterNameTypeMapping.ALL_CAPS;
        Object object =
                FilterNameTypeMapping.filterTypeToFilterCapabilitiesMap.get(Short.valueOf(type));
        return (FilterCapabilities) object;
    }

    /**
     * Determines if the filter passed in is supported.
     *
     * @param filter The Filter to be tested.
     * @return true if supported, false otherwise.
     */
    public boolean supports(org.opengis.filter.Filter filter) {
        for (Iterator ifunc = functions.iterator(); ifunc.hasNext(); ) {
            if (((Class) ifunc.next()).isAssignableFrom(filter.getClass())) return true;
        }

        if (functions.contains(filter.getClass())) return true;

        short filterType = getFilterType(filter);
        if (filterType == 0) {
            // unknown type
            return false;
        }
        return supports(filterType);
    }

    /**
     * Determines if the filter and all its sub filters are supported. Is most important for logic
     * filters, as they are the only ones with subFilters. Null filters should not be used here, if
     * nothing should be filtered than Filter.INCLUDE can be used. Embedded nulls can be a
     * particular source of problems, buried in logic filters.
     *
     * @param filter the filter to be tested.
     * @return true if all sub filters are supported, false otherwise.
     * @throws IllegalArgumentException If a null filter is passed in. As this function is recursive
     *     a null in a logic filter will also cause an error.
     */
    public boolean fullySupports(org.opengis.filter.Filter filter) {
        boolean supports = true;

        if (filter == null) {
            throw new IllegalArgumentException(
                    "Null filters can not be " + "unpacked, did you mean " + "Filter.INCLUDE?");
        }

        if (filter instanceof BinaryLogicOperator) {
            BinaryLogicOperator lf = (BinaryLogicOperator) filter;
            for (Filter testFilter : lf.getChildren()) {
                if (!(this.fullySupports(testFilter))) {
                    supports = false;
                    break;
                }
            }
        } else if (filter instanceof Not) {
            Not lf = (Not) filter;
            if (!(this.fullySupports(lf.getFilter()))) {
                supports = false;
            }
        } else {
            supports = this.supports(filter);
        }
        return supports;
    }

    private boolean supports(long type) {
        return (ops & type) == type;
    }

    public boolean supports(FilterCapabilities type) {
        return (ops & type.ops) == type.ops && functions.containsAll(type.functions);
    }

    public boolean supports(Class type) {
        if (functions.contains(type)) {
            return true;
        }
        for (Class functionClass : functions) {
            if (functionClass.isAssignableFrom(type)) {
                return true;
            }
        }

        return false;
    }

    public long getScalarOps() {
        return ops
                & (SIMPLE_ARITHMETIC
                        | SIMPLE_COMPARISONS
                        | FID
                        | FUNCTIONS
                        | LIKE
                        | LOGICAL
                        | NULL_CHECK
                        | BETWEEN);
    }

    public long getSpatialOps() {
        return ops
                & (SPATIAL_BBOX
                        | SPATIAL_BEYOND
                        | SPATIAL_CONTAINS
                        | SPATIAL_CROSSES
                        | SPATIAL_DISJOINT
                        | SPATIAL_DWITHIN
                        | SPATIAL_EQUALS
                        | SPATIAL_INTERSECT
                        | SPATIAL_OVERLAPS
                        | SPATIAL_TOUCHES
                        | SPATIAL_WITHIN);
    }
    /**
     * Translates a String into an object that represents the operation
     *
     * @param name String, operation name
     * @return one of the {@link FilterCapabilities} constants
     */
    public static FilterCapabilities findOperation(String name) {
        return FilterNameTypeMapping.findOperation(name);
    }
    /**
     * Translates a String into an object that represents function expression
     *
     * @param name String, expression name
     * @return one of the {@link FilterCapabilities} constants
     */
    public static FilterCapabilities findFunction(String name) {
        return FilterNameTypeMapping.findFunction(name);
    }

    /**
     * Convert filter to a constant for use in switch statements. This is an alternative to
     * performing instanceof checks.
     *
     * <p>This utility method for those upgrading to a newer version of GeoTools, instance of checks
     * are preferred as they will take into account new kinds of filters (example temporal filters
     * added for Filter 2.0 specification). Example:
     *
     * <pre>
     * <code>
     * BEFORE: filter.getFilterType() == FilterType.GEOMETRY_CONTAINS
     * QUICK:  Filters.getFilterType( filter ) == FilterType.GEOMETRY_CONTAINS
     * AFTER: filter instanceof Contains
     * </code>
     * </pre>
     */
    private static short getFilterType(org.opengis.filter.Filter filter) {
        if (filter == null) return 0;
        if (filter == org.opengis.filter.Filter.EXCLUDE) return FilterType.ALL;
        if (filter == org.opengis.filter.Filter.INCLUDE) return FilterType.NONE;
        if (filter instanceof PropertyIsBetween) return FilterType.BETWEEN;
        if (filter instanceof PropertyIsEqualTo) return FilterType.COMPARE_EQUALS;
        if (filter instanceof PropertyIsGreaterThan) return FilterType.COMPARE_GREATER_THAN;
        if (filter instanceof PropertyIsGreaterThanOrEqualTo)
            return FilterType.COMPARE_GREATER_THAN_EQUAL;
        if (filter instanceof PropertyIsLessThan) return FilterType.COMPARE_LESS_THAN;
        if (filter instanceof PropertyIsLessThanOrEqualTo)
            return FilterType.COMPARE_LESS_THAN_EQUAL;
        if (filter instanceof PropertyIsNotEqualTo) return FilterType.COMPARE_NOT_EQUALS;
        if (filter instanceof Id) return FilterType.FID;
        if (filter instanceof BBOX) return FilterType.GEOMETRY_BBOX;
        if (filter instanceof Beyond) return FilterType.GEOMETRY_BEYOND;
        if (filter instanceof Contains) return FilterType.GEOMETRY_CONTAINS;
        if (filter instanceof Crosses) return FilterType.GEOMETRY_CROSSES;
        if (filter instanceof Disjoint) return FilterType.GEOMETRY_DISJOINT;
        if (filter instanceof DWithin) return FilterType.GEOMETRY_DWITHIN;
        if (filter instanceof Equals) return FilterType.GEOMETRY_EQUALS;
        if (filter instanceof Intersects) return FilterType.GEOMETRY_INTERSECTS;
        if (filter instanceof Overlaps) return FilterType.GEOMETRY_OVERLAPS;
        if (filter instanceof Touches) return FilterType.GEOMETRY_TOUCHES;
        if (filter instanceof Within) return FilterType.GEOMETRY_WITHIN;
        if (filter instanceof PropertyIsLike) return FilterType.LIKE;
        if (filter instanceof And) return FilterType.LOGIC_AND;
        if (filter instanceof Not) return FilterType.LOGIC_NOT;
        if (filter instanceof Or) return FilterType.LOGIC_OR;
        if (filter instanceof PropertyIsNull) return FilterType.NULL;

        if (filter instanceof Filter) {
            return 0;
        }
        return 0;
    }
}
