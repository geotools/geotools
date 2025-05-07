/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;

/**
 * Encodes a filter into a SQL WHERE statement using Informix-appropriate syntax.
 *
 * @author George Dewar, Land Information New Zealand
 * @author Ines Falcao, Land Information New Zealand
 */
public class InformixFilterToSQL extends FilterToSQL {
    @Override
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = super.createFilterCapabilities();
        caps.addType(BBOX.class);
        caps.addType(Contains.class);
        caps.addType(Crosses.class);
        caps.addType(Disjoint.class);
        caps.addType(Equals.class);
        caps.addType(Intersects.class);
        caps.addType(Overlaps.class);
        caps.addType(Touches.class);
        caps.addType(Within.class);
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
        out.write("ST_GeomFromText('" + g.toText() + "', " + currentSRID + ")");
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) {

        return visitBinarySpatialOperatorEnhanced(filter, property, geometry, swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        return visitBinarySpatialOperatorEnhanced(filter, e1, e2, false, extraData);
    }

    protected Object visitBinarySpatialOperatorEnhanced(
            BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped, Object extraData) {

        try {

            if (filter instanceof DistanceBufferOperator) {
                out.write("ST_Distance(");
                e1.accept(this, extraData);
                out.write(", ");
                e2.accept(this, extraData);
                out.write(")");

                if (filter instanceof DWithin) {
                    out.write("<");
                } else if (filter instanceof Beyond) {
                    out.write(">");
                } else {
                    throw new RuntimeException("Unknown distance operator");
                }
                out.write(Double.toString(((DistanceBufferOperator) filter).getDistance()));
            } else {
                if (filter instanceof Contains) {
                    out.write("ST_Contains(");
                } else if (filter instanceof Crosses) {
                    out.write("ST_Crosses(");
                } else if (filter instanceof Disjoint) {
                    out.write("ST_Disjoint(");
                } else if (filter instanceof Equals) {
                    out.write("ST_Equals(");
                } else if (filter instanceof Intersects || filter instanceof BBOX) {
                    out.write("ST_Intersects(");
                } else if (filter instanceof Overlaps) {
                    out.write("ST_Overlaps(");
                } else if (filter instanceof Touches) {
                    out.write("ST_Touches(");
                } else if (filter instanceof Within) {
                    out.write("ST_Within(");
                } else {
                    throw new RuntimeException("Unknown operator: " + filter);
                }

                if (swapped) {
                    e2.accept(this, extraData);
                    out.write(", ");
                    e1.accept(this, extraData);
                } else {
                    e1.accept(this, extraData);
                    out.write(", ");
                    e2.accept(this, extraData);
                }

                out.write(")");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return extraData;
    }

    @Override
    protected void writeLiteral(Object literal) throws IOException {
        LOGGER.fine("Writing literal of type "
                + (literal != null ? literal.getClass().getName() : null));
        if (literal instanceof java.sql.Time) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            out.write("'" + format.format(literal) + "'");
        } else if (literal instanceof java.util.Date) { // includes java.sql.Timestamp
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out.write("to_date('" + format.format(literal) + "', '%Y-%m-%d %H:%M:%S')");
        } else {
            super.writeLiteral(literal);
        }
    }
}
