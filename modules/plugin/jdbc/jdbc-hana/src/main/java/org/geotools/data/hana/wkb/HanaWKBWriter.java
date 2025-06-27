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
package org.geotools.data.hana.wkb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.MessageFormat;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * A well-known binary writer.
 *
 * <p>The JTS WKB writer cannot be used as it rejects empty points.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaWKBWriter {

    public static byte[] write(Geometry geometry, int dimension) throws HanaWKBWriterException {
        if (geometry == null) {
            return null;
        }
        int size = computeSize(geometry, dimension);
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        write(geometry, dimension, buffer);
        return buffer.array();
    }

    private static final int HEADER_SIZE = 5;

    private static final int COUNT_SIZE = 4;

    private static final int COORD_SIZE = 8;

    private static int computeSize(Geometry geometry, int dimension) throws HanaWKBWriterException {
        if (geometry instanceof Point) {
            return computeSize((Point) geometry, dimension);
        } else if (geometry instanceof LineString) {
            return computeSize((LineString) geometry, dimension);
        } else if (geometry instanceof Polygon) {
            return computeSize((Polygon) geometry, dimension);
        } else if (geometry instanceof MultiPoint) {
            return computeSize((MultiPoint) geometry, dimension);
        } else if (geometry instanceof MultiLineString) {
            return computeSize((MultiLineString) geometry, dimension);
        } else if (geometry instanceof MultiPolygon) {
            return computeSize((MultiPolygon) geometry, dimension);
        } else if (geometry instanceof GeometryCollection) {
            return computeSize((GeometryCollection) geometry, dimension);
        } else {
            throw new HanaWKBWriterException(
                    MessageFormat.format("Unsupported geometry type {0}", geometry.getGeometryType()));
        }
    }

    private static int computeSize(Point point, int dimension) {
        return HEADER_SIZE + dimension * COORD_SIZE;
    }

    private static int computeSize(LineString lineString, int dimension) {
        return HEADER_SIZE + COUNT_SIZE + lineString.getNumPoints() * dimension * COORD_SIZE;
    }

    private static int computeSize(Polygon polygon, int dimension) {
        int size = HEADER_SIZE + COUNT_SIZE;
        LineString shell = polygon.getExteriorRing();
        if (shell == null || shell.getNumPoints() == 0) {
            return size;
        }
        int pointSize = dimension * COORD_SIZE;
        size += COUNT_SIZE + shell.getNumPoints() * pointSize;
        int numHoles = polygon.getNumInteriorRing();
        for (int i = 0; i < numHoles; ++i) {
            size += COUNT_SIZE + polygon.getInteriorRingN(i).getNumPoints() * pointSize;
        }
        return size;
    }

    private static int computeSize(MultiPoint multiPoint, int dimension) {
        return HEADER_SIZE + COUNT_SIZE + multiPoint.getNumPoints() * (HEADER_SIZE + dimension * COORD_SIZE);
    }

    private static int computeSize(MultiLineString multiLineString, int dimension) {
        int size = HEADER_SIZE + COUNT_SIZE;
        for (int i = 0; i < multiLineString.getNumGeometries(); ++i) {
            size += computeSize((LineString) multiLineString.getGeometryN(i), dimension);
        }
        return size;
    }

    private static int computeSize(MultiPolygon multiPolygon, int dimension) {
        int size = HEADER_SIZE + COUNT_SIZE;
        for (int i = 0; i < multiPolygon.getNumGeometries(); ++i) {
            size += computeSize((Polygon) multiPolygon.getGeometryN(i), dimension);
        }
        return size;
    }

    private static int computeSize(GeometryCollection geometryCollection, int dimension) throws HanaWKBWriterException {
        int size = HEADER_SIZE + COUNT_SIZE;
        for (int i = 0; i < geometryCollection.getNumGeometries(); ++i) {
            size += computeSize(geometryCollection.getGeometryN(i), dimension);
        }
        return size;
    }

    private static final byte NDR = 1;

    private static final int DIM_OFFSET = 1000;

    private static void write(Geometry geometry, int dimension, ByteBuffer buffer) throws HanaWKBWriterException {
        if (geometry instanceof Point) {
            write((Point) geometry, dimension, buffer);
        } else if (geometry instanceof LineString) {
            write((LineString) geometry, dimension, buffer);
        } else if (geometry instanceof Polygon) {
            write((Polygon) geometry, dimension, buffer);
        } else if (geometry instanceof MultiPoint) {
            write((MultiPoint) geometry, dimension, buffer);
        } else if (geometry instanceof MultiLineString) {
            write((MultiLineString) geometry, dimension, buffer);
        } else if (geometry instanceof MultiPolygon) {
            write((MultiPolygon) geometry, dimension, buffer);
        } else if (geometry instanceof GeometryCollection) {
            write((GeometryCollection) geometry, dimension, buffer);
        } else {
            throw new HanaWKBWriterException(
                    MessageFormat.format("Unsupported geometry type {0}", geometry.getGeometryType()));
        }
    }

    private static void write(Point point, int dimension, ByteBuffer buffer) {
        writeHeader(GeometryType.POINT, dimension, buffer);
        Coordinate coord = point.getCoordinate();
        if (coord != null) {
            buffer.putDouble(coord.x);
            buffer.putDouble(coord.y);
            if (dimension >= 3) {
                buffer.putDouble(coord.getZ());
            }
        } else {
            for (int i = 0; i < dimension; ++i) {
                buffer.putDouble(Double.NaN);
            }
        }
    }

    private static void write(LineString lineString, int dimension, ByteBuffer buffer) {
        writeHeader(GeometryType.LINESTRING, dimension, buffer);
        write(lineString.getCoordinateSequence(), dimension, buffer);
    }

    private static void write(Polygon polygon, int dimension, ByteBuffer buffer) {
        writeHeader(GeometryType.POLYGON, dimension, buffer);
        LineString shell = polygon.getExteriorRing();
        if (shell == null || shell.getNumPoints() == 0) {
            buffer.putInt(0);
            return;
        }
        int numHoles = polygon.getNumInteriorRing();
        buffer.putInt(1 + numHoles);

        write(shell.getCoordinateSequence(), dimension, buffer);
        for (int i = 0; i < numHoles; ++i) {
            LineString hole = polygon.getInteriorRingN(i);
            write(hole.getCoordinateSequence(), dimension, buffer);
        }
    }

    private static void write(MultiPoint multiPoint, int dimension, ByteBuffer buffer) {
        writeHeader(GeometryType.MULTIPOINT, dimension, buffer);
        int numPoints = multiPoint.getNumPoints();
        buffer.putInt(numPoints);
        for (int i = 0; i < numPoints; ++i) {
            write((Point) multiPoint.getGeometryN(i), dimension, buffer);
        }
    }

    private static void write(MultiLineString multiLineString, int dimension, ByteBuffer buffer) {
        writeHeader(GeometryType.MULTILINESTRING, dimension, buffer);
        int numLineStrings = multiLineString.getNumGeometries();
        buffer.putInt(numLineStrings);
        for (int i = 0; i < numLineStrings; ++i) {
            write((LineString) multiLineString.getGeometryN(i), dimension, buffer);
        }
    }

    private static void write(MultiPolygon multiPolygon, int dimension, ByteBuffer buffer) {
        writeHeader(GeometryType.MULTIPOLYGON, dimension, buffer);
        int numPolygons = multiPolygon.getNumGeometries();
        buffer.putInt(numPolygons);
        for (int i = 0; i < numPolygons; ++i) {
            write((Polygon) multiPolygon.getGeometryN(i), dimension, buffer);
        }
    }

    private static void write(GeometryCollection geometryCollection, int dimension, ByteBuffer buffer)
            throws HanaWKBWriterException {
        writeHeader(GeometryType.GEOMETRYCOLLECTION, dimension, buffer);
        int numGeometries = geometryCollection.getNumGeometries();
        buffer.putInt(numGeometries);
        for (int i = 0; i < numGeometries; ++i) {
            write(geometryCollection.getGeometryN(i), dimension, buffer);
        }
    }

    private static void write(CoordinateSequence cs, int dimension, ByteBuffer buffer) {
        int numPoints = cs.size();
        buffer.putInt(numPoints);
        for (int i = 0; i < numPoints; ++i) {
            for (int j = 0; j < dimension; ++j) {
                buffer.putDouble(cs.getOrdinate(i, j));
            }
        }
    }

    private static void writeHeader(GeometryType geometryType, int dimension, ByteBuffer buffer) {
        buffer.put(NDR);
        int typeCode = geometryType.getTypeCode();
        if (dimension == 3) {
            typeCode += DIM_OFFSET;
        }
        buffer.putInt(typeCode);
    }
}
