/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import java.util.logging.Logger;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Converts a JTS geometry into the equivalent MDSYS.SDO_GEOMETRY SQL syntax. Useful for non
 * prepared statement based dialects and for debugging purposes
 */
public class SDOSqlDumper {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SDOSqlDumper.class);

    /**
     * Converts JTS Geometry to a String version of a SDO Geometry. This should move to a utility
     * class, as we now have more than one class using this (which is why it changed to public
     * static)
     *
     * <p>TODO: Multi eometries
     *
     * @param geometry The JTS Geometry to convert.
     * @return A String representation of the SDO Geometry.
     */
    public static String toSDOGeom(Geometry geometry, int srid) {
        if (Point.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((Point) geometry, srid);
        } else if (LineString.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((LineString) geometry, srid);
        } else if (Polygon.class.isAssignableFrom(geometry.getClass())) {
            if (geometry.equalsTopo(geometry.getEnvelope())) {
                return toSDOGeom(geometry.getEnvelopeInternal(), srid);
            } else {
                return toSDOGeom((Polygon) geometry, srid);
            }
        } else if (MultiLineString.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((MultiLineString) geometry, srid);
        } else if (MultiPolygon.class.isAssignableFrom(geometry.getClass())) {
            return toSDOGeom((MultiPolygon) geometry, srid);
        } else {
            LOGGER.warning(
                    "Got a literal geometry that I can't handle: " + geometry.getClass().getName());

            return "";
        }
    }

    /** TODO: Encode more then 1 */
    private static String toSDOGeom(MultiLineString line, int srid) {
        if (line.getNumGeometries() == 1) {
            return toSDOGeom(line.getGeometryN(0), srid);
        }
        throw new UnsupportedOperationException("Cannot encode MultiLineString (yet)");
    }
    /** TODO: Encode more then 1 */
    private static String toSDOGeom(MultiPolygon polygon, int srid) {
        if (polygon.getNumGeometries() == 1) {
            return toSDOGeom(polygon.getGeometryN(0), srid);
        }
        throw new UnsupportedOperationException("Cannot encode MultiPolygon (yet)");
    }
    /**
     * Converts a LineString Geometry in an SDO SQL geometry construction statement.
     *
     * <p>2D geometries is assumed. If higher dimensional geometries are used the query will be
     * encoded as a 2D geometry.
     *
     * @param line The line to encode.
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(LineString line, int srid) {
        if (SDO.D(line) > 2) {
            LOGGER.warning(
                    ""
                            + SDO.D(line)
                            + " dimensioned geometry provided."
                            + " This encoder only supports 2D geometries. The query will be constructed as"
                            + " a 2D query.");
        }

        StringBuffer buffer = new StringBuffer("MDSYS.SDO_GEOMETRY(");

        buffer.append(SDO.D(line));
        buffer.append("002,");

        if (srid > 0) {
            LOGGER.fine("Using layer SRID: " + srid);
            buffer.append(srid);
        } else {
            LOGGER.fine("Using NULL SRID: ");
            buffer.append("NULL");
        }

        buffer.append(",NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),");
        buffer.append("MDSYS.SDO_ORDINATE_ARRAY(");

        Coordinate[] coordinates = line.getCoordinates();

        for (int i = 0; i < coordinates.length; i++) {
            buffer.append(coordinates[i].x);
            buffer.append(",");
            buffer.append(coordinates[i].y);

            if (i != (coordinates.length - 1)) {
                buffer.append(",");
            }
        }

        buffer.append("))");

        return buffer.toString();
    }

    /**
     * Converts a Point Geometry in an SDO SQL geometry construction statement.
     *
     * <p>2D geometries is assumed. If higher dimensional geometries are used the query will be
     * encoded as a 2D geometry.
     *
     * @param point The point to encode.
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(Point point, int srid) {
        if (SDO.D(point) > 2) {
            LOGGER.warning(
                    ""
                            + SDO.D(point)
                            + " dimensioned geometry provided."
                            + " This encoder only supports 2D geometries. The query will be constructed as"
                            + " a 2D query.");
        }

        StringBuffer buffer = new StringBuffer("MDSYS.SDO_GEOMETRY(");

        buffer.append(SDO.D(point));
        buffer.append("001,");

        if (srid > 0) {
            LOGGER.fine("Using layer SRID: " + srid);
            buffer.append(srid);
        } else {
            LOGGER.fine("Using NULL SRID: ");
            buffer.append("NULL");
        }

        buffer.append(",MDSYS.SDO_POINT_TYPE(");
        buffer.append(point.getX());
        buffer.append(",");
        buffer.append(point.getY());
        buffer.append(",NULL),NULL,NULL)");

        return buffer.toString();
    }

    /**
     * Converts a Polygon Geometry in an SDO SQL geometry construction statement.
     *
     * <p>2D geometries is assumed. If higher dimensional geometries are used the query will be
     * encoded as a 2D geometry.
     *
     * @param polygon The polygon to encode.
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(Polygon polygon, int srid) {
        StringBuffer buffer = new StringBuffer();

        if (SDO.D(polygon) > 2) {
            LOGGER.warning(
                    ""
                            + SDO.D(polygon)
                            + " dimensioned geometry provided."
                            + " This encoder only supports 2D geometries. The query will be constructed as"
                            + " a 2D query.");
        }

        if (polygon.getExteriorRing() != null) {
            buffer.append("MDSYS.SDO_GEOMETRY(");
            buffer.append(SDO.D(polygon));
            buffer.append("003,");

            if (srid > 0) {
                LOGGER.fine("Using layer SRID: " + srid);
                buffer.append(srid);
            } else {
                LOGGER.fine("Using NULL SRID: ");
                buffer.append("NULL");
            }

            buffer.append(",NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),");
            buffer.append("MDSYS.SDO_ORDINATE_ARRAY(");

            CoordinateSequenceFactory fact = polygon.getFactory().getCoordinateSequenceFactory();
            CoordinateSequence exterior = polygon.getExteriorRing().getCoordinateSequence();
            CoordinateSequence coordSeq = SDO.counterClockWise(fact, exterior);

            for (int i = 0, size = coordSeq.size(); i < size; i++) {
                Coordinate cur = coordSeq.getCoordinate(i);
                buffer.append(cur.x);
                buffer.append(",");
                buffer.append(cur.y);

                if (i != (size - 1)) {
                    buffer.append(",");
                }
            }

            /* This could be expensive if coordSeq implementation is not an
               an array.  Leaving in for now as I can't test, and this is
               more likely to work right.
                Coordinate[] coordinates = coordSeq.toCoordinateArray();
                for (int i = 0; i < coordinates.length; i++) {
                    buffer.append(coordinates[i].x);
                    buffer.append(",");
                    buffer.append(coordinates[i].y);

                    if (i != (coordinates.length - 1)) {
                        buffer.append(",");
                    }
            }*/

            buffer.append("))");
        } else {
            LOGGER.warning(
                    "No Exterior ring on polygon.  "
                            + "This encode only supports Polygons with exterior rings.");
        }

        if (polygon.getNumInteriorRing() > 0) {
            LOGGER.warning(
                    "Polygon contains Interior Rings. "
                            + "These rings will not be included in the query.");
        }

        return buffer.toString();
    }

    /**
     * Converts an Envelope in an SDO SQL geometry construction statement.
     *
     * @param envelope The envelope to encode.
     * @return An SDO SQL geometry object construction statement
     */
    private static String toSDOGeom(Envelope envelope, int srid) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MDSYS.SDO_GEOMETRY(");
        buffer.append("2003,");

        if (srid > 0) {
            LOGGER.fine("Using layer SRID: " + srid);
            buffer.append(srid);
        } else {
            LOGGER.fine("Using NULL SRID: ");
            buffer.append("NULL");
        }

        buffer.append(",NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),");
        buffer.append("MDSYS.SDO_ORDINATE_ARRAY(");
        buffer.append(envelope.getMinX());
        buffer.append(",");
        buffer.append(envelope.getMinY());
        buffer.append(",");
        buffer.append(envelope.getMaxX());
        buffer.append(",");
        buffer.append(envelope.getMaxY());
        buffer.append("))");

        return buffer.toString();
    }
}
