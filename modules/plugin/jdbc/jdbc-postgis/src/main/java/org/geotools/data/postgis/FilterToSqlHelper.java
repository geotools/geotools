/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.postgis.filter.FilterFunction_pgNearest;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.LengthFunction;
import org.geotools.filter.function.DateDifferenceFunction;
import org.geotools.filter.function.FilterFunction_area;
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
import org.geotools.filter.function.InArrayFunction;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.filter.function.math.FilterFunction_abs_2;
import org.geotools.filter.function.math.FilterFunction_abs_3;
import org.geotools.filter.function.math.FilterFunction_abs_4;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.filter.function.math.FilterFunction_floor;
import org.geotools.geometry.jts.JTS;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.MultiValuedFilter;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BBOX3D;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.geometry.BoundingBox3D;

class FilterToSqlHelper {

    protected static final String IO_ERROR = "io problem writing filter";

    private static final Envelope WORLD = new Envelope(-180, 180, -90, 90);

    FilterToSQL delegate;
    Writer out;
    boolean looseBBOXEnabled;
    boolean encodeBBOXFilterAsEnvelope;

    public FilterToSqlHelper(FilterToSQL delegate) {
        this.delegate = delegate;
    }

    public static FilterCapabilities createFilterCapabilities(boolean encodeFunctions) {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // adding the spatial filters support
        caps.addType(BBOX.class);
        caps.addType(BBOX3D.class);
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

        // temporal filters
        caps.addType(After.class);
        caps.addType(Before.class);
        caps.addType(Begins.class);
        caps.addType(BegunBy.class);
        caps.addType(During.class);
        caps.addType(TOverlaps.class);
        caps.addType(Ends.class);
        caps.addType(EndedBy.class);
        caps.addType(TEquals.class);

        // replacement for area function that was in deprecated dialect registerFunction
        caps.addType(FilterFunction_area.class);

        if (encodeFunctions) {
            // add support for string functions
            caps.addType(FilterFunction_strConcat.class);
            caps.addType(FilterFunction_strEndsWith.class);
            caps.addType(FilterFunction_strStartsWith.class);
            caps.addType(FilterFunction_strEqualsIgnoreCase.class);
            caps.addType(FilterFunction_strIndexOf.class);
            caps.addType(FilterFunction_strLength.class);
            caps.addType(LengthFunction.class);
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

            // time related functions
            caps.addType(DateDifferenceFunction.class);

            // n nearest function
            caps.addType(FilterFunction_pgNearest.class);

            // array functions
            caps.addType(InArrayFunction.class);
        }

        // native filter support
        caps.addType(NativeFilter.class);

        return caps;
    }

    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        try {
            if (filter instanceof DistanceBufferOperator) {
                visitDistanceSpatialOperator(
                        (DistanceBufferOperator) filter, property, geometry, swapped, extraData);
            } else {
                visitComparisonSpatialOperator(filter, property, geometry, swapped, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }
        return extraData;
    }

    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {

        try {
            visitBinarySpatialOperator(filter, e1, e2, false, extraData);
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }
        return extraData;
    }

    void visitDistanceSpatialOperator(
            DistanceBufferOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData)
            throws IOException {
        if ((filter instanceof DWithin && !swapped) || (filter instanceof Beyond && swapped)) {
            out.write("ST_DWithin(");
            property.accept(delegate, extraData);
            out.write(",");
            geometry.accept(delegate, extraData);
            out.write(",");
            out.write(toNativeUnits(filter));
            out.write(")");
        }
        if ((filter instanceof DWithin && swapped) || (filter instanceof Beyond && !swapped)) {
            out.write("ST_Distance(");
            property.accept(delegate, extraData);
            out.write(",");
            geometry.accept(delegate, extraData);
            out.write(") > ");
            out.write(toNativeUnits(filter));
        }
    }

    private String toNativeUnits(DistanceBufferOperator operator) {
        double distance;
        if (isCurrentGeography()) {
            // need the value in meters
            if (delegate instanceof PostgisPSFilterToSql) {
                distance = ((PostgisPSFilterToSql) delegate).getDistanceInMeters(operator);
            } else {
                distance = ((PostgisFilterToSQL) delegate).getDistanceInMeters(operator);
            }
        } else {
            // need the value in native units
            if (delegate instanceof PostgisPSFilterToSql) {
                distance = ((PostgisPSFilterToSql) delegate).getDistanceInNativeUnits(operator);
            } else {
                distance = ((PostgisFilterToSQL) delegate).getDistanceInNativeUnits(operator);
            }
        }

        return String.valueOf(distance);
    }

    void visitComparisonSpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData)
            throws IOException {

        // if geography case, sanitize geometry first
        if (isCurrentGeography()) {
            geometry = clipToWorld(geometry);
            if (isWorld(geometry)) {
                // nothing to filter in this case
                out.write(" TRUE ");
                return;
            } else if (isEmpty(geometry)) {
                if (!(filter instanceof Disjoint)) {
                    out.write(" FALSE ");
                } else {
                    out.write(" TRUE ");
                }
                return;
            }
        }

        // special case for 3D bbox, it requires a "weird" encoding, from a postgis mail
        // No, just construct a 3D geometry as your query filter, not a 3d box.
        // select count(*) from "3dfloor" where geom &&& ST_Makeline(ST_MakePoint(0,0,0),
        // ST_MakePoint(1000000,1000000,1));
        if (filter instanceof BBOX3D) {
            property.accept(delegate, extraData);
            out.write(" &&& ");
            BBOX3D bbox = (BBOX3D) filter;
            BoundingBox3D bounds = bbox.getBounds();
            out.write("ST_Makeline(ST_MakePoint(");
            out.write(bounds.getMinX() + "," + bounds.getMinY() + "," + bounds.getMinZ());
            out.write("), ST_MakePoint(");
            out.write(bounds.getMaxX() + "," + bounds.getMaxY() + "," + bounds.getMaxZ());
            out.write("))");
        } else {
            // add && filter if possible
            if (!(filter instanceof Disjoint)) {
                if (encodeBBOXFilterAsEnvelope && !isCurrentGeography()) {
                    out.write("ST_envelope(");
                }
                property.accept(delegate, extraData);
                if (encodeBBOXFilterAsEnvelope && !isCurrentGeography()) {
                    out.write(")");
                }
                out.write(" && ");
                geometry.accept(delegate, extraData);

                // if we're just encoding a bbox in loose mode, we're done
                if (filter instanceof BBOX && looseBBOXEnabled) {
                    return;
                }

                out.write(" AND ");
            }

            visitBinarySpatialOperator(
                    filter, (Expression) property, (Expression) geometry, swapped, extraData);
        }
    }

    void visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            Expression e1,
            Expression e2,
            boolean swapped,
            Object extraData)
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
            if (swapped) out.write("ST_Contains");
            else out.write("ST_Within");
        } else if (filter instanceof Contains) {
            if (swapped) out.write("ST_Within");
            else out.write("ST_Contains");
        } else if (filter instanceof Overlaps) {
            out.write("ST_Overlaps");
        } else if (filter instanceof Touches) {
            out.write("ST_Touches");
        } else {
            throw new RuntimeException("Unsupported filter type " + filter.getClass());
        }
        out.write("(");

        e1.accept(delegate, extraData);
        out.write(", ");
        e2.accept(delegate, extraData);

        out.write(closingParenthesis);
    }

    boolean isCurrentGeography() {
        AttributeDescriptor geom = null;
        if (delegate instanceof PostgisPSFilterToSql) {
            geom = ((PostgisPSFilterToSql) delegate).getCurrentGeometry();
        } else if (delegate instanceof PostgisFilterToSQL) {
            geom = ((PostgisFilterToSQL) delegate).getCurrentGeometry();
        }

        return geom != null
                && "geography".equals(geom.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));
    }

    private Literal clipToWorld(Literal geometry) {
        if (geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            if (g != null) {
                Envelope env = g.getEnvelopeInternal();
                // first, limit to world
                if (!WORLD.contains(env)) {
                    g = sanitizePolygons(g.intersection(JTS.toGeometry(WORLD)));
                }

                // second, postgis will always use the shortest distance between two
                // points, if an arc is longer than 180 degrees the opposite will
                // be used instead, so we have to slice the geometry in parts
                env = g.getEnvelopeInternal();
                if (Math.sqrt(env.getWidth() * env.getWidth() + env.getHeight() * env.getHeight())
                        >= 180) {
                    // slice in 90x90 degrees quadrants, none of them has a diagonal longer than 180
                    final List<Polygon> polygons = new ArrayList<Polygon>();
                    for (double lon = Math.floor(env.getMinX()); lon < env.getMaxX(); lon += 90) {
                        for (double lat = Math.floor(env.getMinY());
                                lat < env.getMaxY();
                                lat += 90) {
                            Geometry quadrant =
                                    JTS.toGeometry(new Envelope(lon, lon + 90, lat, lat + 90));
                            Geometry cut = sanitizePolygons(g.intersection(quadrant));
                            if (!cut.isEmpty()) {
                                if (cut instanceof Polygon) {
                                    polygons.add((Polygon) cut);
                                } else {
                                    for (int i = 0; i < cut.getNumGeometries(); i++) {
                                        polygons.add((Polygon) cut.getGeometryN(i));
                                    }
                                }
                            }
                        }
                    }
                    g = toPolygon(g.getFactory(), polygons);
                }

                geometry = CommonFactoryFinder.getFilterFactory(null).literal(g);
            }
        }

        return geometry;
    }

    /**
     * Given a geometry that might contain heterogeneous components extracts only the polygonal ones
     */
    private Geometry sanitizePolygons(Geometry geometry) {
        // already sane?
        if (geometry == null || geometry instanceof Polygon || geometry instanceof MultiPolygon) {
            return geometry;
        }

        // filter out only polygonal parts
        final List<Polygon> polygons = new ArrayList<Polygon>();
        geometry.apply(
                new GeometryComponentFilter() {

                    public void filter(Geometry geom) {
                        if (geom instanceof Polygon) {
                            polygons.add((Polygon) geom);
                        }
                    }
                });

        // turn filtered selection into a geometry
        return toPolygon(geometry.getFactory(), polygons);
    }

    private Geometry toPolygon(GeometryFactory gf, final List<Polygon> polygons) {
        if (polygons.size() == 0) {
            return gf.createGeometryCollection(null);
        } else if (polygons.size() == 1) {
            return polygons.get(0);
        } else {
            return gf.createMultiPolygon(
                    (Polygon[]) polygons.toArray(new Polygon[polygons.size()]));
        }
    }

    /** Returns true if the geometry covers the entire world */
    private boolean isWorld(Literal geometry) {
        if (geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            if (g != null) {
                return JTS.toGeometry(WORLD).equalsTopo(g.union());
            }
        }
        return false;
    }

    /** Returns true if the geometry is fully empty */
    private boolean isEmpty(Literal geometry) {
        if (geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            return g == null || g.isEmpty();
        }
        return false;
    }

    /** Maps a function to its native db equivalent */
    public String getFunctionName(Function function) {
        if (function instanceof FilterFunction_strLength || function instanceof LengthFunction) {
            return "char_length";
        } else if (function instanceof FilterFunction_strToLowerCase) {
            return "lower";
        } else if (function instanceof FilterFunction_strToUpperCase) {
            return "upper";
        } else if (function instanceof FilterFunction_abs
                || function instanceof FilterFunction_abs_2
                || function instanceof FilterFunction_abs_3
                || function instanceof FilterFunction_abs_4) {
            return "abs";
        }
        return function.getName();
    }

    /**
     * Performs custom visits for functions that cannot be encoded as <code>
     * functionName(p1, p2, ... pN).</code>
     */
    public boolean visitFunction(Function function, Object extraData) throws IOException {
        if (function instanceof DateDifferenceFunction) {
            Expression d1 = getParameter(function, 0, true);
            Expression d2 = getParameter(function, 1, true);
            // extract epoch returns seconds, DateDifference is defined in ms instead
            out.write("(extract(epoch from ");
            d1.accept(delegate, java.util.Date.class);
            out.write(" - ");
            d2.accept(delegate, java.util.Date.class);
            out.write(") * 1000)");
        } else if (function instanceof FilterFunction_area) {
            Expression s1 = getParameter(function, 0, true);
            out.write("ST_Area(");
            s1.accept(delegate, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strConcat) {
            Expression s1 = getParameter(function, 0, true);
            Expression s2 = getParameter(function, 1, true);
            out.write("(");
            s1.accept(delegate, String.class);
            out.write(" || ");
            s2.accept(delegate, String.class);
            out.write(")");
        } else if (function instanceof FilterFunction_strEndsWith) {
            Expression str = getParameter(function, 0, true);
            Expression end = getParameter(function, 1, true);

            out.write("(");
            str.accept(delegate, String.class);
            out.write(" LIKE ");
            if (end instanceof Literal) {
                out.write("'%" + end.evaluate(null, String.class) + "'");
            } else {
                out.write("('%' || ");
                end.accept(delegate, String.class);
                out.write(")");
            }
            out.write(")");
        } else if (function instanceof FilterFunction_strStartsWith) {
            Expression str = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);

            out.write("(");
            str.accept(delegate, String.class);
            out.write(" LIKE ");
            if (start instanceof Literal) {
                out.write("'" + start.evaluate(null, String.class) + "%'");
            } else {
                out.write("(");
                start.accept(delegate, String.class);
                out.write(" || '%')");
            }
            out.write(")");
        } else if (function instanceof FilterFunction_strEqualsIgnoreCase) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);

            out.write("(lower(");
            first.accept(delegate, String.class);
            out.write(") = lower(");
            second.accept(delegate, String.class);
            out.write("::text))");
        } else if (function instanceof FilterFunction_strIndexOf) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);

            // would be a simple call, but strIndexOf returns zero based indices
            out.write("(strpos(");
            first.accept(delegate, String.class);
            out.write(", ");
            second.accept(delegate, String.class);
            out.write(") - 1)");
        } else if (function instanceof FilterFunction_strSubstring) {
            Expression string = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);
            Expression end = getParameter(function, 2, true);

            // postgres does sub(string, start, count)... count instead of end, and 1 based indices
            out.write("substr(");
            string.accept(delegate, String.class);
            out.write(", ");
            start.accept(delegate, Integer.class);
            out.write(" + 1, (");
            end.accept(delegate, Integer.class);
            out.write(" - ");
            start.accept(delegate, Integer.class);
            out.write("))");
        } else if (function instanceof FilterFunction_strSubstringStart) {
            Expression string = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);

            // postgres does sub(string, start, count)... count instead of end, and 1 based indices
            out.write("substr(");
            string.accept(delegate, String.class);
            out.write(", ");
            start.accept(delegate, Integer.class);
            out.write(" + 1)");
        } else if (function instanceof FilterFunction_strTrim) {
            Expression string = getParameter(function, 0, true);

            out.write("trim(both ' ' from ");
            string.accept(delegate, String.class);
            out.write(")");
        } else {
            // function not supported
            return false;
        }

        return true;
    }

    Expression getParameter(Function function, int idx, boolean mandatory) {
        final List<Expression> params = function.getParameters();
        if (params == null || params.size() <= idx) {
            if (mandatory) {
                throw new IllegalArgumentException(
                        "Missing parameter number "
                                + (idx + 1)
                                + "for function "
                                + function.getName()
                                + ", cannot encode in SQL");
            }
        }
        return params.get(idx);
    }

    public String cast(String property, Class target) {
        if (String.class.equals(target)) {
            return property + "::text";
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
        } else if (Double.class.equals(target)) {
            return property + "::float8";
        } else if (Time.class.isAssignableFrom(target)) {
            return property + "::time";
        } else if (Timestamp.class.isAssignableFrom(target)) {
            return property + "::timestamp";
        } else if (Date.class.isAssignableFrom(target)) {
            return property + "::date";
        } else if (java.util.Date.class.isAssignableFrom(target)) {
            return property + "::timesamp";
        } else {
            // dunno how to cast, leave as is
            return property;
        }
    }

    boolean isArray(Expression exp) {
        if (exp instanceof Literal) {
            Object value = exp.evaluate(null);
            return value != null && value.getClass().isArray();
        }
        return false;
    }

    boolean isNull(Expression exp) {
        return (exp instanceof Literal && (exp.evaluate(null) == null))
                || exp instanceof NilExpression;
    }

    boolean isArray(Class clazz) {
        return clazz != null && clazz.isArray();
    }

    void visitArrayComparison(
            BinaryComparisonOperator filter,
            Expression left,
            Expression right,
            Class rightContext,
            Class leftContext,
            String type) {
        String leftCast = "";
        String rightCast = "";
        if (left instanceof PropertyName) {
            rightCast = getArrayTypeCast((PropertyName) left);
        }
        if (right instanceof PropertyName) {
            leftCast = getArrayTypeCast((PropertyName) right);
        }

        try {
            // match any against non array literals? we need custom logic
            MultiValuedFilter.MatchAction matchAction = filter.getMatchAction();
            if ((matchAction == MultiValuedFilter.MatchAction.ANY
                            || matchAction == MultiValuedFilter.MatchAction.ONE)
                    && (!isArray(left) && !isArray(right))) {
                // the only indexable search in this block
                if ("=".equalsIgnoreCase(type) && !isNull(left) && !isNull(right)) {
                    // if using a prepared statement dialect we need the native type info
                    // contained in the AttributeDescriptor to create a SQL array...
                    Object leftArrayContext = getArrayComparisonContext(left, right, leftContext);
                    writeBinaryExpressionMember(left, leftArrayContext);
                    out.write(leftCast);
                    // use the overlap operator to avoid deciding which side is the expression
                    out.write(" && ");
                    Object rightArrayContext = getArrayComparisonContext(right, left, rightContext);
                    writeBinaryExpressionMember(right, rightArrayContext);
                    out.write(rightCast);
                } else {
                    // need to un-nest and apply element by element, this is not indexable
                    if (left instanceof PropertyName) {
                        rightContext = rightContext.getComponentType();
                    }
                    if (right instanceof PropertyName) {
                        leftContext = leftContext.getComponentType();
                    }

                    boolean isPropertyLeft = left instanceof PropertyName;
                    boolean isPropertyRight = right instanceof PropertyName;
                    // un-nesting the array to do element by element comparisons... the
                    // generated "table" has "unnest" as the variable name
                    if (matchAction == MultiValuedFilter.MatchAction.ANY) {
                        out.write("EXISTS ( SELECT * from unnest(");
                    } else {
                        out.write("( SELECT count(*) from unnest(");
                    }
                    if (isPropertyLeft) {
                        left.accept(delegate, null);
                    } else {
                        right.accept(delegate, null);
                    }
                    out.write(") WHERE ");
                    // oh fun, if there are nulls we cannot write the same sql
                    if ((isPropertyLeft && isNull(right))
                            || (isPropertyRight && isNull(left))
                                    && ("=".equalsIgnoreCase(type)
                                            || "!=".equalsIgnoreCase(type))) {
                        if ("=".equalsIgnoreCase(type)) {
                            out.write("unnest is NULL");
                        } else if ("!=".equalsIgnoreCase(type)) {
                            out.write("unnest is NOT NULL");
                        }
                    } else {
                        // no nulls, but we still have to consider the comparison direction
                        if (isPropertyLeft) {
                            out.write("unnest");
                            out.write(" " + type + " ");
                            writeBinaryExpressionMember(right, rightContext);
                        } else {
                            writeBinaryExpressionMember(left, leftContext);
                            out.write(" " + type + " ");
                            out.write("unnest");
                        }
                    }
                    if (matchAction == MultiValuedFilter.MatchAction.ONE) {
                        out.write(") = 1");
                    } else {
                        out.write(")");
                    }
                }
            } else if (matchAction == MultiValuedFilter.MatchAction.ALL
                    || isArray(left)
                    || isArray(right)) {
                // for comparison against array literals we only support match-all style
                // for the user it would be really strange to ask for equality on an array
                // and get a positive match on a partial element overlap (filters build
                // without explicit match action default to "ANY")
                Object leftArrayContext = getArrayComparisonContext(left, right, leftContext);
                writeBinaryExpressionMember(left, leftArrayContext);
                out.write(leftCast);
                out.write(" " + type + " ");
                Object rightArrayContext = getArrayComparisonContext(right, left, rightContext);
                writeBinaryExpressionMember(right, rightArrayContext);
                out.write(rightCast);
            }

        } catch (IOException ioe) {
            throw new RuntimeException("Failed to write out SQL", ioe);
        }
    }

    /**
     * When using prepared statements we need the AttributeDescritor's stored native type name to
     * set array values in the PreparedStatement
     */
    private Object getArrayComparisonContext(
            Expression thisExpression, Expression otherExpression, Class context) {
        if (delegate instanceof PreparedFilterToSQL
                && thisExpression instanceof Literal
                && otherExpression instanceof PropertyName) {
            // grab the info from the other side of the comparison
            AttributeDescriptor ad =
                    otherExpression.evaluate(delegate.getFeatureType(), AttributeDescriptor.class);
            if (ad != null) {
                return ad;
            }
        }
        // all good, no extra info actually needed
        return context;
    }

    private void writeBinaryExpressionMember(Expression exp, Object context) throws IOException {
        if (context != null && exp instanceof BinaryExpression) {
            writeBinaryExpression(exp, context);
        } else {
            exp.accept(delegate, context);
        }
    }

    @SuppressWarnings("PMD.CloseResource") // tmp it a copy of out, that's managed elsewhere
    protected void writeBinaryExpression(Expression e, Object context) throws IOException {
        Writer tmp = out;
        try {
            out = new StringWriter();
            out.write("(");
            e.accept(delegate, null);
            out.write(")");
            if (context instanceof Class) {
                tmp.write(cast(out.toString(), (Class) context));
            } else {
                tmp.write(out.toString());
            }
        } finally {
            out = tmp;
        }
    }

    /** Returns the type cast needed to match this property */
    String getArrayTypeCast(PropertyName pn) {
        AttributeDescriptor at = pn.evaluate(delegate.getFeatureType(), AttributeDescriptor.class);
        if (at != null) {
            Object value = at.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME);
            if (value instanceof String) {
                String typeName = (String) value;
                if (typeName.startsWith("_")) {
                    return "::" + typeName.substring(1) + "[]";
                }
            }
        }

        return "";
    }

    public void visitArrayBetween(PropertyIsBetween filter, Class context, Object extraData) {
        Expression expr = filter.getExpression();
        Expression lowerbounds = filter.getLowerBoundary();
        Expression upperbounds = filter.getUpperBoundary();

        try {
            // we have to un-nest
            // generated "table" has "unnest" as the variable name
            MultiValuedFilter.MatchAction matchAction = filter.getMatchAction();
            if (matchAction == MultiValuedFilter.MatchAction.ANY) {
                out.write("EXISTS ( SELECT * from unnest(");
            } else {
                out.write("( SELECT count(*) from unnest(");
            }
            expr.accept(delegate, null);
            out.write(") WHERE unnest BETWEEN ");
            lowerbounds.accept(delegate, context);
            out.write(" AND ");
            upperbounds.accept(delegate, context);

            if (matchAction == MultiValuedFilter.MatchAction.ONE) {
                out.write(") = 1");
            } else if (matchAction == MultiValuedFilter.MatchAction.ALL) {
                out.write(") = (SELECT COUNT(*) FROM unnest(");
                expr.accept(delegate, null);
                out.write("))");
            } else {
                out.write(")");
            }
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    private String getPrimaryKeyColumnsAsCommaSeparatedList(
            List<PrimaryKeyColumn> pkColumns, SQLDialect dialect) {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (PrimaryKeyColumn c : pkColumns) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            dialect.encodeColumnName(null, c.getName(), sb);
        }
        return sb.toString();
    }

    public Object visit(InArrayFunction filter, Object extraData) {
        Expression candidate = getParameter(filter, 0, true);
        Expression array = getParameter(filter, 1, true);
        Class<?> arrayType = getBaseType(array);
        Class<?> candidateType = getBaseType(candidate);
        String castToArrayType = "";
        if (arrayType != null && (candidateType == null || !candidateType.equals(arrayType))) {
            castToArrayType = cast("", arrayType);
        }
        try {
            candidate.accept(delegate, extraData);
            out.write(castToArrayType);
            out.write("=any(");
            array.accept(delegate, extraData);
            out.write(")");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    private Class<?> getBaseType(Expression expr) {
        Class<?> type = delegate.getExpressionType(expr);
        if (type == null && expr instanceof Literal) {
            Object value = delegate.evaluateLiteral((Literal) expr, Object.class);
            if (value != null) {
                type = value.getClass();
            }
        }
        if (isArray(type)) {
            type = type.getComponentType();
        }
        return type;
    }

    public Object visit(
            FilterFunction_pgNearest filter, Object extraData, NearestHelperContext ctx) {
        SQLDialect pgDialect = ctx.getPgDialect();
        Expression geometryExp = getParameter(filter, 0, true);
        Expression numNearest = getParameter(filter, 1, true);
        try {
            List<PrimaryKeyColumn> pkColumns = delegate.getPrimaryKey().getColumns();
            if (pkColumns == null || pkColumns.size() == 0) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of Postgis Nearest Operator: table with no primary key");
            }

            String pkColumnsAsString =
                    getPrimaryKeyColumnsAsCommaSeparatedList(pkColumns, pgDialect);
            StringBuffer sb = new StringBuffer();
            sb.append(" (")
                    .append(pkColumnsAsString)
                    .append(")")
                    .append(" in (select ")
                    .append(pkColumnsAsString)
                    .append(" from ");
            if (delegate.getDatabaseSchema() != null) {
                pgDialect.encodeSchemaName(delegate.getDatabaseSchema(), sb);
                sb.append(".");
            }
            pgDialect.encodeTableName(delegate.getPrimaryKey().getTableName(), sb);
            sb.append(" order by ");
            // geometry column name
            pgDialect.encodeColumnName(
                    null, delegate.getFeatureType().getGeometryDescriptor().getLocalName(), sb);
            sb.append(" <-> ");
            // reference geometry
            Geometry geomValue =
                    (Geometry) delegate.evaluateLiteral((Literal) geometryExp, Geometry.class);
            ctx.encodeGeometryValue.accept(geomValue, sb);

            // num of features
            sb.append(" limit ");
            int numFeatures = numNearest.evaluate(null, Number.class).intValue();
            sb.append(numFeatures);
            sb.append(")");

            out.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    /** Context data struct for nearest visit method */
    public static class NearestHelperContext {
        private SQLDialect pgDialect;
        private BiConsumer<Geometry, StringBuffer> encodeGeometryValue;

        public NearestHelperContext(
                SQLDialect pgDialect, BiConsumer<Geometry, StringBuffer> encodeGeometryValue) {
            super();
            this.pgDialect = pgDialect;
            this.encodeGeometryValue = encodeGeometryValue;
        }

        public SQLDialect getPgDialect() {
            return pgDialect;
        }

        public void setPgDialect(SQLDialect pgDialect) {
            this.pgDialect = pgDialect;
        }

        public BiConsumer<Geometry, StringBuffer> getEncodeGeometryValue() {
            return encodeGeometryValue;
        }

        public void setEncodeGeometryValue(BiConsumer<Geometry, StringBuffer> encodeGeometryValue) {
            this.encodeGeometryValue = encodeGeometryValue;
        }
    }

    /**
     * Detects and return a InArrayFunction if found, otherwise null
     *
     * @param filter filter to evaluate
     * @return FilterFunction_any if found
     */
    public InArrayFunction getInArray(PropertyIsEqualTo filter) {
        Expression expr1 = filter.getExpression1();
        Expression expr2 = filter.getExpression2();
        if (expr2 instanceof InArrayFunction) {
            return (InArrayFunction) expr2;
        }
        if (expr1 instanceof InArrayFunction) {
            return (InArrayFunction) expr1;
        } else {
            return null;
        }
    }

    /**
     * Detects and return a FilterFunction_pgNearest if found, otherwise null
     *
     * @param filter filter to evaluate
     * @return FilterFunction_pgNearest if found
     */
    public FilterFunction_pgNearest getNearestFilter(PropertyIsEqualTo filter) {
        Expression expr1 = filter.getExpression1();
        Expression expr2 = filter.getExpression2();
        // if expr2 is nearest filter, switch positions
        if (expr2 instanceof FilterFunction_pgNearest) {
            Expression tmp = expr1;
            expr1 = expr2;
            expr2 = tmp;
        }
        if (expr1 instanceof FilterFunction_pgNearest) {
            if (!(expr2 instanceof Literal)) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of Nearest Operator: it can be compared only to a Boolean \"true\" value");
            }
            Boolean nearest = (Boolean) delegate.evaluateLiteral((Literal) expr2, Boolean.class);
            if (nearest == null || !nearest.booleanValue()) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of Nearest Operator: it can be compared only to a Boolean \"true\" value");
            }
            return (FilterFunction_pgNearest) expr1;
        } else {
            return null;
        }
    }

    public Integer getFeatureTypeGeometrySRID() {
        return (Integer)
                delegate.getFeatureType()
                        .getGeometryDescriptor()
                        .getUserData()
                        .get(JDBCDataStore.JDBC_NATIVE_SRID);
    }

    public Integer getFeatureTypeGeometryDimension() {
        GeometryDescriptor descriptor = delegate.getFeatureType().getGeometryDescriptor();
        return (Integer) descriptor.getUserData().get(Hints.COORDINATE_DIMENSION);
    }
}
