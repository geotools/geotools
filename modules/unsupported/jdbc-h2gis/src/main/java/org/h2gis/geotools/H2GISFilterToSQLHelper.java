/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.h2gis.geotools;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.DistanceBufferOperator;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.function.FilterFunction_strEndsWith;
import org.geotools.filter.function.FilterFunction_strEqualsIgnoreCase;
import org.geotools.filter.function.FilterFunction_strIndexOf;
import org.geotools.filter.function.FilterFunction_strLength;
import org.geotools.filter.function.FilterFunction_strReplace;
import org.geotools.filter.function.FilterFunction_strStartsWith;
import org.geotools.filter.function.FilterFunction_strSubstring;
import org.geotools.filter.function.FilterFunction_strSubstringStart;
import org.geotools.filter.function.FilterFunction_strToLowerCase;
import org.geotools.filter.function.FilterFunction_strToUpperCase;
import org.geotools.filter.function.FilterFunction_strTrim;
import org.geotools.filter.function.FilterFunction_strTrim2;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.filter.function.math.FilterFunction_abs_2;
import org.geotools.filter.function.math.FilterFunction_abs_3;
import org.geotools.filter.function.math.FilterFunction_abs_4;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.filter.function.math.FilterFunction_exp;
import org.geotools.filter.function.math.FilterFunction_floor;
import org.geotools.jdbc.SQLDialect;

/**
 * jdbc-h2gis is an extension to connect H2GIS a spatial library that brings spatial support to the H2 Java database.
 *
 * <p>H2GIS filter wrapper for H2GIS database.
 *
 * @author Erwan Bocher
 */
public class H2GISFilterToSQLHelper {

    FilterToSQL filterToSQL;
    Writer out;

    /** @param filterToSQL */
    public H2GISFilterToSQLHelper(FilterToSQL filterToSQL) {
        this.filterToSQL = filterToSQL;
    }

    /**
     * @param encodeFunctions
     * @return
     */
    public static FilterCapabilities createFilterCapabilities(boolean encodeFunctions, FilterCapabilities superCaps) {
        FilterCapabilities caps = superCaps;
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);
        // adding the spatial filters support
        caps.addType(BBOX.class);
        caps.addType(Contains.class);
        caps.addType(Crosses.class);
        caps.addType(Disjoint.class);
        caps.addType(Equals.class);
        caps.addType(Intersects.class);
        caps.addType(Overlaps.class);
        caps.addType(Touches.class);
        caps.addType(Within.class);
        caps.addType(DWithin.class);
        caps.addType(Beyond.class);

        if (encodeFunctions) {
            // add support for string functions
            caps.addType(FilterFunction_strConcat.class);
            caps.addType(FilterFunction_strEndsWith.class);
            caps.addType(FilterFunction_strStartsWith.class);
            caps.addType(FilterFunction_strEqualsIgnoreCase.class);
            caps.addType(FilterFunction_strIndexOf.class);
            caps.addType(FilterFunction_strLength.class);
            caps.addType(FilterFunction_strToLowerCase.class);
            caps.addType(FilterFunction_strToUpperCase.class);
            caps.addType(FilterFunction_strReplace.class);
            caps.addType(FilterFunction_strSubstring.class);
            caps.addType(FilterFunction_strSubstringStart.class);
            caps.addType(FilterFunction_strTrim.class);
            caps.addType(FilterFunction_strTrim2.class);
            // add support for math functions
            caps.addType(FilterFunction_abs.class);
            caps.addType(FilterFunction_abs_2.class);
            caps.addType(FilterFunction_abs_3.class);
            caps.addType(FilterFunction_abs_4.class);
            caps.addType(FilterFunction_ceil.class);
            caps.addType(FilterFunction_floor.class);
            caps.addType(FilterFunction_exp.class);
        }
        return caps;
    }

    /**
     * @param filter
     * @param property
     * @param geometry
     * @param swapped
     * @param extraData
     * @return
     */
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {
        try {
            if (filter instanceof DistanceBufferOperator) {
                visitDistanceSpatialOperator((DistanceBufferOperator) filter, property, geometry, swapped, extraData);
            } else {
                visitComparisonSpatialOperator(filter, property, geometry, swapped, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create this spatial filter", e);
        }
        return extraData;
    }

    /**
     * @param filter
     * @param e1
     * @param e2
     * @param extraData
     * @return
     */
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        try {
            visitBinarySpatialOperator(filter, e1, e2, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create this spatial filter", e);
        }
        return extraData;
    }

    /**
     * @param filter
     * @param property
     * @param geometry
     * @param swapped
     * @param extraData
     * @throws IOException
     */
    private void visitDistanceSpatialOperator(
            DistanceBufferOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData)
            throws IOException {
        if (filter instanceof DWithin && !swapped || filter instanceof Beyond && swapped) {
            out.write("ST_DWithin(");
            property.accept(filterToSQL, extraData);
            out.write(",");
            geometry.accept(filterToSQL, extraData);
            out.write(",");
            out.write(String.valueOf(filter.getDistance()));
            out.write(")");
        }
        if (filter instanceof DWithin && swapped || filter instanceof Beyond && !swapped) {
            out.write("ST_Distance(");
            property.accept(filterToSQL, extraData);
            out.write(",");
            geometry.accept(filterToSQL, extraData);
            out.write(") > ");
            out.write(Double.toString(filter.getDistance()));
        }
    }

    /**
     * @param filter
     * @param property
     * @param geometry
     * @param swapped
     * @param extraData
     * @throws IOException
     */
    private void visitComparisonSpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData)
            throws IOException {
        // add && filter if possible
        if (!(filter instanceof Disjoint)) {
            property.accept(filterToSQL, extraData);
            out.write(" && ");
            geometry.accept(filterToSQL, extraData);
            // if we're just encoding a bbox in loose mode, we're done
            if (filter instanceof BBOX) {
                return;
            }
            out.write(" AND ");
        }
        visitBinarySpatialOperator(filter, (Expression) property, (Expression) geometry, swapped, extraData);
    }

    /**
     * @param filter
     * @param e1
     * @param e2
     * @param swapped
     * @param extraData
     * @throws IOException
     */
    private void visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped, Object extraData)
            throws IOException {
        String closingParenthesis = ")";
        if (filter instanceof Equals) {
            out.write("ST_Equals");
        } else if (filter instanceof Disjoint) {
            out.write("NOT (ST_Intersects");
            closingParenthesis += ")";
        } else if (filter instanceof Intersects || filter instanceof BBOX) {
            out.write("ST_Intersects");
        } else if (filter instanceof Crosses) {
            out.write("ST_Crosses");
        } else if (filter instanceof Within) {
            if (swapped) {
                out.write("ST_Contains");
            } else {
                out.write("ST_Within");
            }
        } else if (filter instanceof Contains) {
            if (swapped) {
                out.write("ST_Within");
            } else {
                out.write("ST_Contains");
            }
        } else if (filter instanceof Overlaps) {
            out.write("ST_Overlaps");
        } else if (filter instanceof Touches) {
            out.write("ST_Touches");
        } else {
            throw new RuntimeException("Unsupported filter type " + filter.getClass());
        }
        out.write("(");
        e1.accept(filterToSQL, extraData);
        out.write(", ");
        e2.accept(filterToSQL, extraData);
        out.write(closingParenthesis);
    }

    /**
     * Maps a function to its native db equivalent
     *
     * @param function
     * @return
     */
    public static String getFunctionName(Function function) {
        if (function instanceof FilterFunction_strLength) {
            return "length";
        } else if (function instanceof FilterFunction_strToLowerCase) {
            return "lower";
        } else if (function instanceof FilterFunction_strToUpperCase) {
            return "upper";
        } else if (function instanceof FilterFunction_abs
                || function instanceof FilterFunction_abs_2
                || function instanceof FilterFunction_abs_3
                || function instanceof FilterFunction_abs_4) {
            return "abs";
        } else if (function instanceof FilterFunction_exp) {
            return "exp";
        }
        return function.getName();
    }

    /**
     * Performs custom visits for functions that cannot be encoded as <code>
     * functionName(p1, p2, ... pN).</code>
     *
     * @param function
     * @param extraData
     * @return
     * @throws java.io.IOException
     */
    public boolean visitFunction(Function function, Object extraData) throws IOException {
        if (function instanceof FilterFunction_strConcat) {
            Expression s1 = getParameter(function, 0, true);
            Expression s2 = getParameter(function, 1, true);
            out.write("(");
            s1.accept(filterToSQL, String.class);
            out.write(" || ");
            s2.accept(filterToSQL, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strEndsWith) {
            Expression str = getParameter(function, 0, true);
            Expression end = getParameter(function, 1, true);
            out.write("(");
            str.accept(filterToSQL, String.class);
            out.write(" LIKE ");
            if (end instanceof Literal) {
                out.write("'%" + end.evaluate(null, String.class) + "'");
            } else {
                out.write("('%' || ");
                end.accept(filterToSQL, String.class);
                out.write(")");
            }
            out.write(")");
        } else if (function instanceof FilterFunction_strStartsWith) {
            Expression str = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);
            out.write("(");
            str.accept(filterToSQL, String.class);
            out.write(" LIKE ");
            if (start instanceof Literal) {
                out.write("'" + start.evaluate(null, String.class) + "%'");
            } else {
                out.write("(");
                start.accept(filterToSQL, String.class);
                out.write(" || '%')");
            }
            out.write(")");
        } else if (function instanceof FilterFunction_strEqualsIgnoreCase) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);
            out.write("(lower(");
            first.accept(filterToSQL, String.class);
            out.write(") = lower(");
            second.accept(filterToSQL, String.class);
            out.write("::text))");
        } else if (function instanceof FilterFunction_strIndexOf) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);
            // would be a simple call, but strIndexOf returns zero based indices
            out.write("(POSITION(");
            first.accept(filterToSQL, String.class);
            out.write(", ");
            second.accept(filterToSQL, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strSubstring) {
            Expression string = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);
            Expression end = getParameter(function, 2, true);
            out.write("SUBSTRING(");
            string.accept(filterToSQL, String.class);
            out.write(", ");
            start.accept(filterToSQL, Integer.class);
            out.write(", ");
            end.accept(filterToSQL, Integer.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strSubstringStart) {
            Expression string = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);
            out.write("SUBSTRING(");
            string.accept(filterToSQL, String.class);
            out.write(", ");
            start.accept(filterToSQL, Integer.class);
            out.write(" + 1)");
        } else if (function instanceof FilterFunction_strTrim) {
            Expression string = getParameter(function, 0, true);
            out.write("trim(both ' ' from ");
            string.accept(filterToSQL, String.class);
            out.write(")");
        } else {
            // function not supported
            return false;
        }
        return true;
    }

    /**
     * @param function
     * @param idx
     * @param mandatory
     * @return
     */
    private Expression getParameter(Function function, int idx, boolean mandatory) {
        final List<Expression> params = function.getParameters();
        if (params == null || params.size() <= idx) {
            if (mandatory) {
                throw new IllegalArgumentException("Missing parameter number "
                        + (idx + 1)
                        + "for function "
                        + function.getName()
                        + ", cannot encode in SQL");
            }
        }
        return params.get(idx);
    }

    /**
     * @param property
     * @param target
     * @return
     */
    public String cast(String property, Class<?> target) {
        if (String.class.equals(target)) {
            return property + "::varchar";
        } else if (Short.class.equals(target) || Byte.class.equals(target)) {
            return property + "::smallint";
        } else if (Integer.class.equals(target)) {
            return property + "::integer";
        } else if (Long.class.equals(target)) {
            return property + "::bigint";
        } else if (Float.class.equals(target)) {
            return property + "::real";
        } else if (Double.class.equals(target)) {
            return property + "::float8";
        } else if (BigInteger.class.equals(target)) {
            return property + "::numeric";
        } else if (BigDecimal.class.equals(target)) {
            return property + "::decimal";
        } else if (Time.class.isAssignableFrom(target)) {
            return property + "::time";
        } else if (Timestamp.class.isAssignableFrom(target)) {
            return property + "::timestamp";
        } else if (Date.class.isAssignableFrom(target)) {
            return property + "::date";
        } else if (java.util.Date.class.isAssignableFrom(target)) {
            return property + "::timesamp";
        } else {
            return property;
        }
    }
}
