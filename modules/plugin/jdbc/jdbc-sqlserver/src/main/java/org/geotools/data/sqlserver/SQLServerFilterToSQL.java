/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sqlserver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.geotools.jdbc.SQLDialect;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;

public class SQLServerFilterToSQL extends FilterToSQL {

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = super.createFilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);
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
        return caps;
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        Geometry g = (Geometry) evaluateLiteral(expression, Geometry.class);
        if (g instanceof LinearRing) {
            // WKT does not support linear rings
            g = g.getFactory().createLineString(((LinearRing) g).getCoordinateSequence());
        }
        out.write("geometry::STGeomFromText('" + g.toText() + "', " + currentSRID + ")");
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
            // if the filter is not disjoint, and it with a BBOX filter
            if (!(filter instanceof Disjoint) && !(filter instanceof DistanceBufferOperator)) {
                e1.accept(this, extraData);
                out.write(".Filter(");
                e2.accept(this, extraData);
                out.write(") = 1");

                if (!(filter instanceof BBOX)) {
                    out.write(" AND ");
                }
            }

            if (filter instanceof BBOX) {
                // nothing to do. already encoded above
                return extraData;
            }

            if (filter instanceof DistanceBufferOperator) {
                e1.accept(this, extraData);
                out.write(".STDistance(");
                e2.accept(this, extraData);
                out.write(")");

                if (filter instanceof DWithin) {
                    out.write("<");
                } else if (filter instanceof Beyond) {
                    out.write(">");
                } else {
                    throw new RuntimeException("Unknown distance operator.");
                }

                out.write(Double.toString(((DistanceBufferOperator) filter).getDistance()));
            } else {

                if (swapped) {
                    e2.accept(this, extraData);
                } else {
                    e1.accept(this, extraData);
                }

                if (filter instanceof Contains) {
                    out.write(".STContains(");
                } else if (filter instanceof Crosses) {
                    out.write(".STCrosses(");
                } else if (filter instanceof Disjoint) {
                    out.write(".STDisjoint(");
                } else if (filter instanceof Equals) {
                    out.write(".STEquals(");
                } else if (filter instanceof Intersects) {
                    out.write(".STIntersects(");
                } else if (filter instanceof Overlaps) {
                    out.write(".STOverlaps(");
                } else if (filter instanceof Touches) {
                    out.write(".STTouches(");
                } else if (filter instanceof Within) {
                    out.write(".STWithin(");
                } else {
                    throw new RuntimeException("Unknown operator: " + filter);
                }

                if (swapped) {
                    e1.accept(this, extraData);
                } else {
                    e2.accept(this, extraData);
                }

                out.write(") = 1");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return extraData;
    }

    @Override
    protected void writeLiteral(Object literal) throws IOException {
        if (literal instanceof Date) {
            SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            out.write("'" + DATETIME_FORMAT.format(literal) + "'");
        } else if (literal instanceof Boolean) {
            out.write(String.valueOf((Boolean) literal ? 1 : 0));
        } else {
            super.writeLiteral(literal);
        }
    }
}
