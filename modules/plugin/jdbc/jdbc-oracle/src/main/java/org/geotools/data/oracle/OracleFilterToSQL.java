/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
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
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.data.oracle.filter.FilterFunction_sdonn;
import org.geotools.data.oracle.sdo.SDOSqlDumper;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.FilterFunction_area;
import org.geotools.filter.function.JsonArrayContainsFunction;
import org.geotools.filter.function.JsonPointerFunction;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.jdbc.EscapeSql;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Oracle specific filter encoder.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 * @author Davide Savazzi, GeoSolutions
 */
public class OracleFilterToSQL extends PreparedFilterToSQL {

    /** Contains filter type to SDO_RELATE mask type mappings */
    private static final Map<Class<?>, String> SDO_RELATE_MASK_MAP = Map.of(
            Contains.class, "contains",
            Crosses.class, "overlapbdydisjoint",
            Equals.class, "equal",
            Overlaps.class, "overlapbdyintersect",
            Touches.class, "touch",
            Within.class, "inside",
            Disjoint.class, "disjoint",
            BBOX.class, "anyinteract",
            Intersects.class, "anyinteract");

    /** The whole world in WGS84 */
    private static final Envelope WORLD = new Envelope(-179.99, 179.99, -89.99, 89.99);

    /**
     * If we have to turn <code>a op b</code> into <code>b op2 a</code>, what's the op2 that returns the same result?
     */
    private static final Map<String, String> INVERSE_OPERATOR_MAP = Map.of(

            // asymmetric operators, op2 = !op
            "contains", "inside",
            "inside", "contains",
            // symmetric operators, op2 = op
            "overlapbdydisjoint", "overlapbdydisjoint",
            "overlapbdyintersect", "overlapbdyintersect",
            "touch", "touch",
            "equal", "equal",
            "anyinteract", "anyinteract",
            "disjoint", "disjoint");

    private static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("\\d+");

    /** Whether BBOX should be encoded as just a primary filter or primary+secondary */
    protected boolean looseBBOXEnabled;

    public OracleFilterToSQL(PreparedStatementSQLDialect dialect) {
        super(dialect);
        setSqlNameEscape("\"");
    }

    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
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

        caps.addType(FilterFunction_sdonn.class);
        caps.addType(JsonArrayContainsFunction.class);
        caps.addType(JsonPointerFunction.class);

        // replaces the OracleDialect.registerFunction support
        caps.addType(FilterFunction_area.class);

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

        return caps;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        FilterFunction_sdonn sdoNnQuery = getSDO_NN_Query(filter);
        if (sdoNnQuery != null) {
            return visit(sdoNnQuery, extraData);
        } else {
            return super.visit(filter, extraData);
        }
    }

    private FilterFunction_sdonn getSDO_NN_Query(PropertyIsEqualTo filter) {
        Expression expr1 = filter.getExpression1();
        Expression expr2 = filter.getExpression2();

        if (expr2 instanceof FilterFunction_sdonn) {
            // switch position
            Expression tmp = expr1;
            expr1 = expr2;
            expr2 = tmp;
        }

        if (expr1 instanceof FilterFunction_sdonn) {
            if (!(expr2 instanceof Literal)) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of SDO_NN Oracle function: it can be compared only to a Boolean \"true\" value");
            }

            Boolean nearest = (Boolean) evaluateLiteral((Literal) expr2, Boolean.class);
            if (nearest == null || !nearest.booleanValue()) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of SDO_NN Oracle function: it can be compared only to a Boolean \"true\" value");
            }

            return (FilterFunction_sdonn) expr1;
        } else {
            return null;
        }
    }

    @Override
    public Object visit(Function function, Object extraData) {
        if (function instanceof FilterFunction_sdonn) {
            throw new UnsupportedOperationException(
                    "Unsupported usage of SDO_NN Oracle function: must be used in a Equals Filter");
        } else if (function instanceof FilterFunction_area) {
            try {
                Expression s1 = getParameter(function, 0, true);
                out.write("SDO_GEOM.SDO_AREA(");
                s1.accept(this, String.class);
                out.write(",0.05)");
                return extraData;
            } catch (IOException ioe) {
                throw new RuntimeException(IO_ERROR, ioe);
            }
        } else if (function instanceof JsonArrayContainsFunction) {
            try {
                out.write(jsonExists(function));
                return extraData;
            } catch (IOException ioe) {
                throw new RuntimeException(IO_ERROR, ioe);
            }
        } else if (function instanceof JsonPointerFunction) {
            try {
                out.write(encodeJsonPointer(function, extraData));
                return extraData;
            } catch (IOException ioe) {
                throw new RuntimeException(IO_ERROR, ioe);
            }
        }

        return super.visit(function, extraData);
    }

    private String encodeJsonPointer(Function jsonPointer, Object extraData) {
        Expression json = getParameter(jsonPointer, 0, true);
        Expression pointer = getParameter(jsonPointer, 1, true);
        if (json instanceof PropertyName && pointer instanceof Literal) {
            String strPointer = ((Literal) pointer).getValue().toString();
            String escapedLiteral = EscapeSql.escapeLiteral(strPointer, true, true);
            List<String> pointerEl = Stream.of(escapedLiteral.split("/"))
                    .filter(p -> !p.equals(""))
                    .collect(Collectors.toList());

            StringBuilder sb = new StringBuilder("$");
            for (String property : pointerEl) {
                if (ARRAY_INDEX_PATTERN.matcher(property).matches()) {
                    sb.append("[" + property + "]");
                } else {
                    sb.append("." + property);
                }
            }
            return String.format("JSON_VALUE(%s, '%s')", json, sb);
        }
        return null;
    }

    private String getPrimaryKeyColumnsAsCommaSeparatedList(List<PrimaryKeyColumn> pkColumns) {
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

    private Object visit(FilterFunction_sdonn sdoNnQuery, Object extraData) {
        Expression geometryExp = getParameter(sdoNnQuery, 0, true);
        Expression sdoNumResExp = getParameter(sdoNnQuery, 1, true);
        Expression cqlLiteralExp = getParameter(sdoNnQuery, 2, false);
        Expression sdoBatchSizeExp = getParameter(sdoNnQuery, 3, false);

        try {
            List<PrimaryKeyColumn> pkColumns = getPrimaryKey().getColumns();
            if (pkColumns == null || pkColumns.isEmpty()) {
                throw new UnsupportedOperationException(
                        "Unsupported usage of SDO_NN Oracle function: table with no primary key");
            }

            String pkColumnsAsString = getPrimaryKeyColumnsAsCommaSeparatedList(pkColumns);

            StringBuffer sb = new StringBuffer();
            sb.append(" (")
                    .append(pkColumnsAsString)
                    .append(")")
                    .append(" in (select ")
                    .append(pkColumnsAsString)
                    .append(" from ");

            if (getDatabaseSchema() != null) {
                dialect.encodeSchemaName(getDatabaseSchema(), sb);
                sb.append(".");
            }

            dialect.encodeTableName(getPrimaryKey().getTableName(), sb);

            sb.append(" where SDO_NN(");

            // geometry column name
            dialect.encodeColumnName(null, featureType.getGeometryDescriptor().getLocalName(), sb);
            sb.append(",");

            // reference geometry
            Geometry geomValue = (Geometry) evaluateLiteral((Literal) geometryExp, Geometry.class);
            sb.append("?");
            literalValues.add(clipToWorldFeatureTypeGeometry(geomValue));
            descriptors.add(null);
            literalTypes.add(Geometry.class);
            SRIDs.add(getFeatureTypeGeometrySRID());
            dimensions.add(getFeatureTypeGeometryDimension());

            int sdo_num_res = getIntFromLiteral((Literal) sdoNumResExp);
            if (sdoBatchSizeExp != null) {
                // if sdo_batch_size is specified, sdo_num_res keyword is ignored
                int sdo_batch_size = getIntFromLiteral((Literal) sdoBatchSizeExp);
                sb.append(",'sdo_batch_size=" + sdo_batch_size + "'");
            } else if (cqlLiteralExp == null) {
                sb.append(",'sdo_num_res=" + sdo_num_res + "'");
            }

            sb.append(") = 'TRUE' ");

            if (cqlLiteralExp != null) {
                try {
                    sb.append("AND ");

                    // flush
                    out.write(sb.toString());
                    sb.setLength(0);

                    Filter cqlExp = CQL.toFilter((String) evaluateLiteral((Literal) cqlLiteralExp, String.class));
                    cqlExp.accept(this, extraData);
                } catch (CQLException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            // if sdo_batch_size is specified, sdo_num_res keyword is ignored by SDO_NN function
            // if cqlExp is specified, we can't use sdo_num_res
            if (sdoBatchSizeExp != null || cqlLiteralExp != null) {
                sb.append(" AND ROWNUM <= " + sdo_num_res);
            }

            sb.append(")");

            out.write(sb.toString());

        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }

        return extraData;
    }

    private int getIntFromLiteral(Literal literal) {
        return ((Number) evaluateLiteral(literal, Number.class)).intValue();
    }

    private Geometry clipToWorldFeatureTypeGeometry(Geometry geom) {
        // Oracle cannot deal with filters using geometries that span beyond the whole world
        if (isFeatureTypeGeometryGeodetic() && !WORLD.contains(geom.getEnvelopeInternal())) {
            Geometry result = geom.intersection(JTS.toGeometry(WORLD));
            if (result != null && !result.isEmpty()) {
                if (result instanceof GeometryCollection) {
                    result = distillSameTypeGeometries((GeometryCollection) result, geom);
                }
                return result;
            }
        }
        return geom;
    }

    private Integer getFeatureTypeGeometrySRID() {
        return (Integer) featureType.getGeometryDescriptor().getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
    }

    private Integer getFeatureTypeGeometryDimension() {
        GeometryDescriptor descriptor = featureType.getGeometryDescriptor();
        return (Integer) descriptor.getUserData().get(Hints.COORDINATE_DIMENSION);
    }

    private boolean isFeatureTypeGeometryGeodetic() {
        Boolean geodetic =
                (Boolean) featureType.getGeometryDescriptor().getUserData().get(OracleDialect.GEODETIC);
        return geodetic != null && geodetic;
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {
        return visitBinarySpatialOperator(filter, property, (Expression) geometry, swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        return visitBinarySpatialOperator(filter, e1, e2, false, extraData);
    }

    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped, Object extraData) {

        try {
            e1 = clipToWorld(filter, e1);
            e2 = clipToWorld(filter, e2);

            if (filter instanceof Beyond || filter instanceof DWithin) doSDODistance(filter, e1, e2, extraData);
            else if (filter instanceof BBOX && looseBBOXEnabled) {
                doSDOFilter(filter, e1, e2, extraData);
            } else doSDORelate(filter, e1, e2, swapped, extraData);
        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    Expression clipToWorld(BinarySpatialOperator filter, Expression e) {
        if (e instanceof Literal) {
            Geometry eval = e.evaluate(filter, Geometry.class);
            // Oracle cannot deal with filters using geometries that span beyond the whole world
            // in case the
            if (dialect != null && isCurrentGeometryGeodetic() && !WORLD.contains(eval.getEnvelopeInternal())) {
                Geometry result = eval.intersection(JTS.toGeometry(WORLD));

                if (result != null && !result.isEmpty()) {
                    if (result instanceof GeometryCollection) {
                        result = distillSameTypeGeometries((GeometryCollection) result, eval);
                    }
                    FilterFactory ff = CommonFactoryFinder.getFilterFactory();
                    e = ff.literal(result);
                }
            }
        }
        return e;
    }

    /** Returns true if the current geometry has the geodetic marker raised */
    boolean isCurrentGeometryGeodetic() {
        if (currentGeometry != null) {
            Boolean geodetic = (Boolean) currentGeometry.getUserData().get(OracleDialect.GEODETIC);
            return geodetic != null && geodetic;
        }
        return false;
    }

    protected Geometry distillSameTypeGeometries(GeometryCollection coll, Geometry original) {
        if (original instanceof Polygon || original instanceof MultiPolygon) {
            List<Polygon> polys = new ArrayList<>();
            accumulateGeometries(polys, coll, Polygon.class);
            return original.getFactory().createMultiPolygon(polys.toArray(new Polygon[polys.size()]));
        } else if (original instanceof LineString || original instanceof MultiLineString) {
            List<LineString> ls = new ArrayList<>();
            accumulateGeometries(ls, coll, LineString.class);
            return original.getFactory().createMultiLineString(ls.toArray(new LineString[ls.size()]));
        } else if (original instanceof Point || original instanceof MultiPoint) {
            List<Point> points = new ArrayList<>();
            accumulateGeometries(points, coll, Point.class);
            return original.getFactory().createMultiPoint(points.toArray(new Point[points.size()]));
        } else {
            return original;
        }
    }

    protected <T> void accumulateGeometries(List<T> collection, Geometry g, Class<? extends T> target) {
        if (target.isInstance(g)) {
            collection.add(target.cast(g));
        } else if (g instanceof GeometryCollection) {
            GeometryCollection coll = (GeometryCollection) g;
            for (int i = 0; i < coll.getNumGeometries(); i++) {
                accumulateGeometries(collection, coll.getGeometryN(i), target);
            }
        }
    }

    protected void doSDOFilter(Filter filter, Expression e1, Expression e2, Object extraData) throws IOException {
        out.write("SDO_FILTER(");
        e1.accept(this, extraData);
        out.write(", ");
        e2.accept(this, extraData);
        // for backwards compatibility with Oracle 9 we add the mask and querytypes params
        out.write(", 'mask=anyinteract querytype=WINDOW') = 'TRUE' ");
    }

    /** Encodes an SDO relate */
    protected void doSDORelate(Filter filter, Expression e1, Expression e2, boolean swapped, Object extraData)
            throws IOException {
        // grab the operating mask
        String mask = null;
        for (Class<?> filterClass : SDO_RELATE_MASK_MAP.keySet()) {
            if (filterClass.isAssignableFrom(filter.getClass())) mask = SDO_RELATE_MASK_MAP.get(filterClass);
        }
        if (mask == null)
            throw new IllegalArgumentException("Cannot encode filter " + filter.getClass() + " into a SDO_RELATE");
        if (swapped) mask = INVERSE_OPERATOR_MAP.get(mask);

        // ok, ready to write out the SDO_RELATE
        out.write("SDO_RELATE(");
        e1.accept(this, extraData);
        out.write(", ");
        e2.accept(this, extraData);
        // for disjoint we ask for no interaction, anyinteract == false
        if (filter instanceof Disjoint) {
            out.write(", 'mask=ANYINTERACT querytype=WINDOW') <> 'TRUE' ");
        } else {
            out.write(", 'mask=" + mask + " querytype=WINDOW') = 'TRUE' ");
        }
    }

    protected void doSDODistance(BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData)
            throws IOException {
        double distance = ((DistanceBufferOperator) filter).getDistance();
        String unit = getSDOUnitFromOGCUnit(((DistanceBufferOperator) filter).getDistanceUnits());

        String within = filter instanceof DWithin ? "TRUE" : "FALSE";

        out.write("SDO_WITHIN_DISTANCE(");
        e1.accept(this, extraData);
        out.write(",");
        e2.accept(this, extraData);

        // encode the unit verbatim when available
        if (unit != null && !"".equals(unit.trim())) {
            unit = escapeLiteral(unit);
            out.write(",'distance=" + distance + " unit=" + unit + "') = '" + within + "' ");
        } else out.write(",'distance=" + distance + "') = '" + within + "' ");
    }

    /**
     * The mapping between OGC filter units and Oracle Units. The full list of Oracle Units can be obtained by issuing
     * "select * from MDSYS.SDO_DIST_UNITS WHERE SDO_UNIT IS NOT NULL order by SDO_UNIT;"
     */
    private static final Map<String, String> UNITS_MAP = Map.of(
            "metre", "m",
            "meters", "m",
            "kilometers", "km",
            "mi", "Mile",
            "miles", "Mile",
            "NM", "naut_mile",
            "feet", "foot",
            "ft", "foot",
            "in", "inch");

    private static String getSDOUnitFromOGCUnit(String ogcUnit) {
        Object sdoUnit = ogcUnit != null ? UNITS_MAP.get(ogcUnit) : null;
        return sdoUnit != null ? sdoUnit.toString() : ogcUnit;
    }

    @Override
    public String escapeName(String name) {
        String sqlNameEscape = getSqlNameEscape();
        if (OracleDialect.reservedWords.contains(name.toUpperCase()) || !sqlNameEscape.isEmpty()) {
            if (sqlNameEscape.isEmpty()) sqlNameEscape = "\"";
            StringBuilder sb = new StringBuilder();
            sb.append(sqlNameEscape);
            int offset = 0;
            int escapeOffset;
            while ((escapeOffset = name.indexOf(sqlNameEscape, offset)) != -1) {
                sb.append(name.substring(offset, escapeOffset));
                sb.append(sqlNameEscape);
                sb.append(sqlNameEscape);
                offset = escapeOffset + sqlNameEscape.length();
            }
            sb.append(name.substring(offset));
            sb.append(sqlNameEscape);
            return sb.toString();
        } else {
            return name;
        }
    }

    @Override
    protected void encodeBinaryComparisonOperator(
            BinaryComparisonOperator filter,
            Object extraData,
            Expression left,
            Expression right,
            Class leftContext,
            Class rightContext) {
        if (left instanceof JsonArrayContainsFunction) {
            try {
                writeBinaryExpressionMember(left, leftContext);
            } catch (java.io.IOException ioe) {
                throw new RuntimeException(IO_ERROR, ioe);
            }
        } else super.encodeBinaryComparisonOperator(filter, extraData, left, right, leftContext, rightContext);
    }

    public String jsonExists(Function function) {
        PropertyName columnName = (PropertyName) getParameter(function, 0, true);
        Literal jsonPath = (Literal) getParameter(function, 1, true);
        Expression expected = getParameter(function, 2, true);

        String[] pointers = jsonPath.getValue().toString().split("/");
        if (pointers.length > 0) {
            String strJsonPath = escapeLiteral(String.join(".", pointers));
            String strExpected = escapeLiteral(expected.evaluate(null, String.class));
            return String.format("json_exists(%s, '$%s?(@ == \"%s\")')", columnName, strJsonPath, strExpected);
        } else {
            throw new IllegalArgumentException("Cannot encode filter Invalid pointer " + jsonPath.getValue());
        }
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);

        if (geom instanceof LinearRing) {
            geom = geom.getFactory().createLineString(((LinearRing) geom).getCoordinateSequence());
        }
        geom = clipToWorldFeatureTypeGeometry(geom);
        String sdoGeom = SDOSqlDumper.toSDOGeom(geom, getFeatureTypeGeometrySRID());
        out.write(sdoGeom);
    }
}
