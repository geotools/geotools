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
package org.geotools.data.h2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
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

/** @source $URL$ */
public class H2FilterToSQL extends FilterToSQL {
    private static final Logger LOGGER = Logging.getLogger(H2FilterToSQL.class);

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
        caps.addType(DWithin.class);
        caps.addType(Beyond.class);

        // native filter support
        caps.addType(NativeFilter.class);

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
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        return visitBinarySpatialOperator(
                filter, (Expression) property, (Expression) geometry, swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        return visitBinarySpatialOperator(filter, e1, e2, false, extraData);
    }

    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            Expression e1,
            Expression e2,
            boolean swapped,
            Object extraData) {

        double distance = 0;
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
                distance = getDistanceInNativeUnits((DistanceBufferOperator) filter);
                out.write(Double.toString(distance));
            } else if (filter instanceof BBOX) {
                // TODO: make a loose bounding box parameter
                out.write("ST_Intersects(");
                e1.accept(this, extraData);
                out.write(",");
                e2.accept(this, extraData);
                out.write(")");
            } else {

                if (filter instanceof Contains) {
                    out.write("ST_Contains(");
                } else if (filter instanceof Crosses) {
                    out.write("ST_Crosses(");
                } else if (filter instanceof Disjoint) {
                    out.write("ST_Disjoint(");
                } else if (filter instanceof Equals) {
                    out.write("ST_Equals(");
                } else if (filter instanceof Intersects) {
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

            Expression geometry = e1 instanceof Literal ? e1 : e2 instanceof Literal ? e2 : null;
            if (geometry != null && !(filter instanceof Disjoint) && !(filter instanceof Beyond)) {
                String spatialIndex =
                        (String) currentGeometry.getUserData().get(H2Dialect.H2_SPATIAL_INDEX);
                if (spatialIndex != null) {
                    // property map the column type
                    if (primaryKey.getColumns().size() == 1
                            && Number.class.isAssignableFrom(
                                    primaryKey.getColumns().get(0).getType())) {

                        Envelope e = geometry.evaluate(null, Envelope.class);

                        if (filter instanceof DWithin) {
                            e.expandBy(distance);
                        }

                        out.write(" AND ");
                        out.write("\"" + primaryKey.getColumns().get(0).getName() + "\" ");
                        out.write("IN (");
                        out.write("SELECT CAST(HATBOX_JOIN_ID AS INT)");
                        out.write(" FROM HATBOX_MBR_INTERSECTS_ENV(");
                        if (databaseSchema != null) {
                            out.write("'" + databaseSchema + "', ");
                        } else {
                            out.write("'PUBLIC', ");
                        }
                        out.write("'" + featureType.getTypeName() + "', ");
                        out.write(
                                e.getMinX()
                                        + ", "
                                        + e.getMaxX()
                                        + ", "
                                        + e.getMinY()
                                        + ", "
                                        + e.getMaxY());
                        out.write(")");
                        out.write(")");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return extraData;
    }

    @Override
    protected void writeLiteral(Object literal) throws IOException {
        if (literal instanceof Date) {
            out.write("PARSEDATETIME(");
            if (literal instanceof java.sql.Date) {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
                out.write("'" + DATE_FORMAT.format(literal) + "', 'yyyy-MM-dd'");
            } else {
                SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
                out.write("'" + DATETIME_FORMAT.format(literal) + "', 'yyyy-MM-dd HH:mm:ss.SSSZ'");
            }
            out.write(")");
        } else {
            super.writeLiteral(literal);
        }
    }
}
