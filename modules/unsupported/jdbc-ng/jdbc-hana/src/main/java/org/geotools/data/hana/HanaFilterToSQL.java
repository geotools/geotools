/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import org.geotools.data.hana.wkb.HanaWKBWriter;
import org.geotools.data.hana.wkb.HanaWKBWriterException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.function.FilterFunction_strEndsWith;
import org.geotools.filter.function.FilterFunction_strEqualsIgnoreCase;
import org.geotools.filter.function.FilterFunction_strIndexOf;
import org.geotools.filter.function.FilterFunction_strLength;
import org.geotools.filter.function.FilterFunction_strStartsWith;
import org.geotools.filter.function.FilterFunction_strSubstring;
import org.geotools.filter.function.FilterFunction_strSubstringStart;
import org.geotools.filter.function.FilterFunction_strToLowerCase;
import org.geotools.filter.function.FilterFunction_strToUpperCase;
import org.geotools.filter.function.FilterFunction_strTrim;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.filter.function.math.FilterFunction_abs_2;
import org.geotools.filter.function.math.FilterFunction_abs_3;
import org.geotools.filter.function.math.FilterFunction_abs_4;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.filter.function.math.FilterFunction_floor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.SQLDialect;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
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
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.BoundingBox3D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;

/**
 * A filter-to-SQL converter for SAP HANA.
 *
 * @author Stefan Uhrig, SAP SE
 */
@SuppressWarnings("deprecation")
public class HanaFilterToSQL extends PreparedFilterToSQL {

    private static final Map<String, Double> UNITS_MAP = new HashMap<String, Double>();

    static {
        UNITS_MAP.put("kilometers", 1000.0);
        UNITS_MAP.put("kilometer", 1000.0);
        UNITS_MAP.put("km", 1000.0);
        UNITS_MAP.put("meters", 1.0);
        UNITS_MAP.put("meter", 1.0);
        UNITS_MAP.put("m", 1.0);
        UNITS_MAP.put("millimeter", 0.001);
        UNITS_MAP.put("mm", 0.001);
        UNITS_MAP.put("statute miles", 1609.344);
        UNITS_MAP.put("miles", 1609.344);
        UNITS_MAP.put("mile", 1609.344);
        UNITS_MAP.put("mi", 1609.344);
        UNITS_MAP.put("nautical miles", 1852.0);
        UNITS_MAP.put("NM", 1852.0);
        UNITS_MAP.put("nm", 1852.0);
        UNITS_MAP.put("feet", 0.3048);
        UNITS_MAP.put("ft", 0.3048);
        UNITS_MAP.put("in", 0.0254);
    };

    public HanaFilterToSQL(PreparedStatementSQLDialect dialect, boolean functionEncodingEnabled) {
        super(dialect);
        this.functionEncodingEnabled = functionEncodingEnabled;
    }

    private boolean functionEncodingEnabled;

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // Spatial capabilities
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

        // Temporal capabilities
        caps.addType(After.class);
        caps.addType(Before.class);
        caps.addType(Begins.class);
        caps.addType(BegunBy.class);
        caps.addType(During.class);
        caps.addType(TOverlaps.class);
        caps.addType(Ends.class);
        caps.addType(EndedBy.class);
        caps.addType(TEquals.class);

        if (functionEncodingEnabled) {
            // String capabilities
            caps.addType(FilterFunction_strConcat.class);
            caps.addType(FilterFunction_strEndsWith.class);
            caps.addType(FilterFunction_strStartsWith.class);
            caps.addType(FilterFunction_strEqualsIgnoreCase.class);
            caps.addType(FilterFunction_strIndexOf.class);
            caps.addType(FilterFunction_strLength.class);
            caps.addType(FilterFunction_strToLowerCase.class);
            caps.addType(FilterFunction_strToUpperCase.class);
            caps.addType(FilterFunction_strSubstring.class);
            caps.addType(FilterFunction_strSubstringStart.class);
            caps.addType(FilterFunction_strTrim.class);

            // Math capabilities
            caps.addType(FilterFunction_abs.class);
            caps.addType(FilterFunction_abs_2.class);
            caps.addType(FilterFunction_abs_3.class);
            caps.addType(FilterFunction_abs_4.class);
            caps.addType(FilterFunction_ceil.class);
            caps.addType(FilterFunction_floor.class);
        }
        return caps;
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        if (filter instanceof DistanceBufferOperator) {
            return visitDistanceSpatialOperator(
                    (DistanceBufferOperator) filter, property, geometry, swapped, extraData);
        } else if (filter instanceof BBOX) {
            return visitBBOXSpatialOperator((BBOX) filter, property, geometry, extraData);
        } else {
            return visitBinarySpatialOperator(
                    filter, (Expression) property, (Expression) geometry, swapped, extraData);
        }
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        return visitBinarySpatialOperator(filter, e1, e2, false, extraData);
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);
        visitGeometry(geom);
    }

    private Object visitDistanceSpatialOperator(
            DistanceBufferOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        if (!(filter instanceof DWithin) && !(filter instanceof Beyond)) {
            throw new IllegalArgumentException("Unsupported filter type " + filter.getClass());
        }
        try {
            property.accept(this, extraData);
            out.write(".ST_WithinDistance(");
            geometry.accept(this, extraData);
            out.write(", ");
            String unit = filter.getDistanceUnits();
            if (UNITS_MAP.containsKey(unit)) {
                double distInMeters = filter.getDistance() * UNITS_MAP.get(unit);
                out.write(Double.toString(distInMeters));
                out.write(", 'meter'");
            } else {
                out.write(Double.toString(filter.getDistance()));
                out.write(", '");
                out.write(filter.getDistanceUnits());
                out.write("'");
            }
            out.write(")");
            if ((filter instanceof DWithin) != swapped) {
                out.write(" = 1");
            } else {
                out.write(" = 0");
            }
            return extraData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object visitBBOXSpatialOperator(
            BBOX filter, PropertyName property, Literal geometry, Object extraData) {
        try {
            property.accept(this, extraData);
            out.write(".ST_IntersectsRectPlanar(");
            BoundingBox bbox = clamp(filter.getBounds());
            Coordinate ll = new Coordinate(bbox.getMinX(), bbox.getMinY());
            Coordinate ur = new Coordinate(bbox.getMaxX(), bbox.getMaxY());
            GeometryFactory factory = new GeometryFactory();
            visitGeometry(factory.createPoint(ll));
            out.write(", ");
            visitGeometry(factory.createPoint(ur));
            out.write(") = 1");
            if (filter instanceof BBOX3D) {
                BBOX3D filter3d = (BBOX3D) filter;
                BoundingBox3D bbox3d = filter3d.getBounds();
                double minz = bbox3d.getMinZ();
                double maxz = bbox3d.getMaxZ();
                out.write(" AND ");
                property.accept(this, extraData);
                out.write(".ST_ZMin() <= ");
                out.write(Double.toString(maxz));
                out.write(" AND ");
                property.accept(this, extraData);
                out.write(".ST_ZMax() >= ");
                out.write(Double.toString(minz));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraData;
    }

    private BoundingBox clamp(BoundingBox bounds) {
        // In geographic CRS', HANA will reject any points outside the "normalized" range, which is
        // (in radian) [-PI;PI] for longitude and
        // [-PI/2;PI/2] for latitude. As GeoTools seems to expect that larger bounding boxes should
        // be allowed and should not wrap-around, we clamp
        // the bounding boxes for geographic CRS' here.

        CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
        if (crs == null) {
            return bounds;
        }
        CoordinateReferenceSystem hcrs = CRS.getHorizontalCRS(crs);
        if (!(hcrs instanceof GeographicCRS)) {
            return bounds;
        }

        GeographicCRS gcrs = (GeographicCRS) hcrs;
        @SuppressWarnings("unchecked")
        Unit<Length> u1 = (Unit<Length>) gcrs.getCoordinateSystem().getAxis(0).getUnit();
        Unit<Length> su1 = u1.getSystemUnit();
        UnitConverter uc1 = su1.getConverterTo(u1);
        double minx = uc1.convert(-Math.PI);
        double maxx = uc1.convert(Math.PI);

        @SuppressWarnings("unchecked")
        Unit<Length> u2 = (Unit<Length>) hcrs.getCoordinateSystem().getAxis(1).getUnit();
        Unit<Length> su2 = u2.getSystemUnit();
        UnitConverter uc2 = su2.getConverterTo(u2);
        double miny = uc2.convert(-0.5 * Math.PI);
        double maxy = uc2.convert(0.5 * Math.PI);

        double x1 = Math.min(maxx, Math.max(minx, bounds.getMinX()));
        double y1 = Math.min(maxy, Math.max(miny, bounds.getMinY()));
        double x2 = Math.max(minx, Math.min(maxx, bounds.getMaxX()));
        double y2 = Math.max(miny, Math.min(maxy, bounds.getMaxY()));

        return new ReferencedEnvelope(x1, x2, y1, y2, crs);
    }

    private Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            Expression e1,
            Expression e2,
            boolean swapped,
            Object extraData) {
        try {
            e1.accept(this, extraData);
            out.write('.');
            if (filter instanceof Equals) {
                out.write("ST_Equals");
            } else if (filter instanceof Disjoint) {
                out.write("ST_Disjoint");
            } else if (filter instanceof Intersects) {
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
                throw new IllegalArgumentException("Unsupported filter type " + filter.getClass());
            }
            out.write('(');
            e2.accept(this, extraData);
            out.write(") = 1");
            return extraData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void visitGeometry(Geometry geom) throws IOException {
        if (geom == null) {
            out.write("NULL");
            return;
        }

        int dimension = HanaDimensionFinder.findDimension(geom);
        byte[] wkb;
        try {
            wkb = HanaWKBWriter.write(geom, dimension);
        } catch (HanaWKBWriterException e) {
            throw new IOException(e);
        }

        out.write("ST_GeomFromWKB('");
        out.write(encodeAsHex(wkb));
        if ((currentSRID == null) && (currentGeometry != null)) {
            out.write(", ");
            out.write(HanaUtil.encodeIdentifier(currentGeometry.getLocalName()));
            out.write(".ST_SRID())");
        } else {
            out.write("', " + currentSRID + ")");
        }
    }

    private static final char[] HEXDIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private char[] encodeAsHex(byte[] data) {
        int l = data.length;
        char[] ret = new char[2 * l];
        for (int i = 0, j = 0; i < l; i++) {
            ret[j++] = HEXDIGITS[(0xF0 & data[i]) >>> 4];
            ret[j++] = HEXDIGITS[0x0F & data[i]];
        }
        return ret;
    }

    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {
        encodingFunction = true;
        try {
            boolean encoded = visitFunction(function, extraData);
            if (encoded) {
                return extraData;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            encodingFunction = false;
        }
        return super.visit(function, extraData);
    }

    private boolean visitFunction(Function function, Object extraData) throws IOException {
        if (function instanceof FilterFunction_strConcat) {
            Expression s1 = getParameter(function, 0, true);
            Expression s2 = getParameter(function, 1, true);
            out.write("(");
            s1.accept(this, String.class);
            out.write(" || ");
            s2.accept(this, String.class);
            out.write(")");
            return true;
        }
        if (function instanceof FilterFunction_strEndsWith) {
            Expression str = getParameter(function, 0, true);
            Expression end = getParameter(function, 1, true);
            out.write("CASE WHEN (RIGHT(");
            str.accept(this, String.class);
            out.write(", LENGTH(");
            end.accept(this, String.class);
            out.write(")) = ");
            end.accept(this, String.class);
            out.write(") THEN 1 ELSE 0 END");
            return true;
        }
        if (function instanceof FilterFunction_strStartsWith) {
            Expression str = getParameter(function, 0, true);
            Expression start = getParameter(function, 1, true);
            out.write("CASE WHEN (LEFT(");
            str.accept(this, String.class);
            out.write(", LENGTH(");
            start.accept(this, String.class);
            out.write(")) = ");
            start.accept(this, String.class);
            out.write(") THEN 1 ELSE 0 END");
            return true;
        }
        if (function instanceof FilterFunction_strEqualsIgnoreCase) {
            Expression first = getParameter(function, 0, true);
            Expression second = getParameter(function, 1, true);
            out.write("CASE WHEN (LOWER(");
            first.accept(this, String.class);
            out.write(") = LOWER(");
            second.accept(this, String.class);
            out.write(")) THEN 1 ELSE 0 END");
            return true;
        }
        if (function instanceof FilterFunction_strIndexOf) {
            Expression string = getParameter(function, 0, true);
            Expression substr = getParameter(function, 1, true);
            out.write("(LOCATE(");
            string.accept(this, String.class);
            out.write(", ");
            substr.accept(this, String.class);
            out.write(") - 1)");
            return true;
        }
        if (function instanceof FilterFunction_strLength) {
            Expression string = getParameter(function, 0, true);
            out.write("LENGTH(");
            string.accept(this, String.class);
            out.write(")");
            return true;
        }
        if (function instanceof FilterFunction_strToLowerCase) {
            Expression string = getParameter(function, 0, true);
            out.write("LOWER(");
            string.accept(this, String.class);
            out.write(")");
            return true;
        }
        if (function instanceof FilterFunction_strToUpperCase) {
            Expression string = getParameter(function, 0, true);
            out.write("UPPER(");
            string.accept(this, String.class);
            out.write(")");
            return true;
        }
        if (function instanceof FilterFunction_strSubstring) {
            Expression string = getParameter(function, 0, true);
            Expression begIdx = getParameter(function, 1, true);
            Expression endIdx = getParameter(function, 2, true);
            out.write("SUBSTRING(");
            string.accept(this, String.class);
            out.write(", ");
            begIdx.accept(this, Integer.class);
            out.write(" + 1, (");
            endIdx.accept(this, Integer.class);
            out.write(") - (");
            begIdx.accept(this, Integer.class);
            out.write("))");
            return true;
        }
        if (function instanceof FilterFunction_strSubstringStart) {
            Expression string = getParameter(function, 0, true);
            Expression begIdx = getParameter(function, 1, true);
            out.write("SUBSTRING(");
            string.accept(this, String.class);
            out.write(", ");
            begIdx.accept(this, Integer.class);
            out.write(" + 1)");
            return true;
        }
        if (function instanceof FilterFunction_strTrim) {
            Expression string = getParameter(function, 0, true);
            out.write("TRIM(' ' || CHAR(9) || CHAR(10) || CHAR(13) FROM (");
            string.accept(this, String.class);
            out.write("))");
            return true;
        }
        if (function instanceof FilterFunction_abs
                || function instanceof FilterFunction_abs_2
                || function instanceof FilterFunction_abs_3
                || function instanceof FilterFunction_abs_4) {
            Expression string = getParameter(function, 0, true);
            out.write("CAST (");
            out.write("ABS(");
            string.accept(this, String.class);
            out.write(")");
            out.write(" AS ");
            String dataType = null;
            if (function instanceof FilterFunction_abs) dataType = "SMALLINT";
            if (function instanceof FilterFunction_abs_2) dataType = "INT";
            if (function instanceof FilterFunction_abs_3) dataType = "FLOAT";
            if (function instanceof FilterFunction_abs_4) dataType = "DOUBLE";
            out.write(dataType);
            out.write(")");
            return true;
        }
        if (function instanceof FilterFunction_ceil) {
            Expression number = getParameter(function, 0, true);
            out.write("CEIL(");
            number.accept(this, Number.class);
            out.write(")");
            return true;
        }
        if (function instanceof FilterFunction_floor) {
            Expression number = getParameter(function, 0, true);
            out.write("FLOOR(");
            number.accept(this, Number.class);
            out.write(")");
            return true;
        }
        return false;
    }
}
