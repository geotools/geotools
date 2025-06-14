/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.geotools.api.filter.NativeFilter;
import org.geotools.api.filter.expression.Expression;
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
import org.geotools.api.geometry.BoundingBox;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.LengthFunction;
import org.geotools.filter.function.DateDifferenceFunction;
import org.geotools.filter.function.FilterFunction_area;
import org.geotools.filter.function.FilterFunction_buffer;
import org.geotools.filter.function.FilterFunction_equalTo;
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
import org.geotools.jdbc.SQLDialect;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;

/**
 * Base FilterToSQL class for DuckDB with spatial support. Handles conversion of GeoTools filters to DuckDB SQL spatial
 * functions.
 */
public class DuckDBFilterToSQL extends FilterToSQL {

    static FilterCapabilities createFilterCapabilities(FilterCapabilities caps, boolean encodeFunctions) {
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // adding the spatial filters support
        caps.addType(BBOX.class);
        // caps.addType(BBOX3D.class);
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
        // caps.addType(JsonArrayContainsFunction.class);

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

            // array functions
            caps.addType(InArrayFunction.class);

            // compare functions
            caps.addType(FilterFunction_equalTo.class);

            // one geometry function (to support testing, but otherwise fully functional)
            caps.addType(FilterFunction_buffer.class);
        }
        // native filter support
        caps.addType(NativeFilter.class);

        return caps;
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = super.createFilterCapabilities();
        return createFilterCapabilities(caps, false); // TODO enable functions
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        Geometry g = (Geometry) evaluateLiteral(expression, Geometry.class);
        visitLiteralGeometry(g);
    }

    protected void visitLiteralGeometry(Geometry g) throws IOException {
        if (g instanceof LinearRing) {
            // DuckDB doesn't support LinearRing type
            g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
        }
        write("ST_GeomFromText('%s')", g.toText());
    }

    protected void visitLiteralBoundingBox(BoundingBox bounds) throws IOException {
        visitLiteralGeometry(JTS.toGeometry(bounds));
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {

        return visitBinarySpatialOperator(filter, property, geometry, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression leftExp, Expression rightExpt, Object extraData) {

        if (filter instanceof DistanceBufferOperator) {
            return visitDistanceBufferOperator((DistanceBufferOperator) filter, leftExp, rightExpt, extraData);
        }
        if (filter instanceof BBOX) {
            return visitBBOX((BBOX) filter, leftExp, rightExpt, extraData);
        }
        String stFunction;
        if (filter instanceof Contains) {
            stFunction = "ST_Contains";
        } else if (filter instanceof Crosses) {
            stFunction = "ST_Crosses";
        } else if (filter instanceof Disjoint) {
            stFunction = "ST_Disjoint";
        } else if (filter instanceof Equals) {
            stFunction = "ST_Equals";
        } else if (filter instanceof Intersects) {
            stFunction = "ST_Intersects";
        } else if (filter instanceof Overlaps) {
            stFunction = "ST_Overlaps";
        } else if (filter instanceof Touches) {
            stFunction = "ST_Touches";
        } else if (filter instanceof Within) {
            stFunction = "ST_Within";
        } else {
            throw new IllegalArgumentException("Unknown operator: " + filter);
        }

        write("%s(", stFunction);
        leftExp.accept(this, extraData);
        write(", ");
        rightExpt.accept(this, extraData);
        write(")");
        return extraData;
    }

    protected Object visitBBOX(BBOX filter, Expression leftExp, Expression rightExpt, Object extraData) {
        write("ST_Intersects(");
        leftExp.accept(this, extraData);
        write(",");
        rightExpt.accept(this, extraData);
        write(")");
        return extraData;
    }

    protected Object visitDistanceBufferOperator(
            DistanceBufferOperator filter, Expression leftExp, Expression rightExpt, Object extraData) {

        double distance = getDistanceInNativeUnits(filter);
        String dFunc;
        if (filter instanceof DWithin) {
            dFunc = "ST_DWithin";
        } else if (filter instanceof Beyond) {
            dFunc = "ST_Beyond";
        } else {
            throw new IllegalArgumentException("Unknown distance operator");
        }
        write("%s(", dFunc);
        leftExp.accept(this, extraData);
        write(", ");
        rightExpt.accept(this, extraData);
        write(", %f)", distance);
        return extraData;
    }

    @SuppressWarnings("AnnotateFormatMethod")
    protected void write(String fmt, Object... args) {
        try {
            out.write(String.format(fmt, args));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
