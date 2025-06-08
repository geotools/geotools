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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.MessageFormat;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * A parser for the well-known-binary created by HANA.
 *
 * <p>The JTS parser cannot be used to parse 3- or 4-dimensional geometries because of different type code conventions.
 * HANA offsets type codes by multiples of 1000 (as described in the relevant OGC and SQL/MM standard) while JTS expects
 * specific bits to be set.
 *
 * @author Stefan Uhrig, SAP SE
 */
public class HanaWKBParser {

    private static final byte XDR = 0;

    private static final byte NDR = 1;

    private static final int TYPE_MASK = 0xFFFFF;

    private static final int XYZM_MODE_DIV = 1000;

    private static final int XYZM_MODE_XY = 0;

    private static final int XYZM_MODE_XYZ = 1;

    private static final int XYZM_MODE_XYM = 2;

    private static final int XYZM_MODE_XYZM = 3;

    public HanaWKBParser(GeometryFactory factory) {
        this.factory = factory;
    }

    private GeometryFactory factory;

    private ByteBuffer data;

    private XyzmMode xyzmMode;

    private int dimension;

    private int jtsDimension;

    public Geometry parse(byte[] wkb) throws HanaWKBParserException {
        data = ByteBuffer.wrap(wkb);
        try {
            readAndSetByteOrder();
            int typeCode = data.getInt();

            GeometryType type = getGeometryType(typeCode);
            xyzmMode = getXyzmMode(typeCode);
            dimension = xyzmMode.getCoordinatesPerPoint();
            jtsDimension = xyzmMode.hasM() ? dimension - 1 : dimension;

            Geometry geometry = parseGeometryOfType(type);
            if (data.hasRemaining()) {
                throw new HanaWKBParserException("There is unparsed WKB data left");
            }
            return geometry;
        } catch (BufferUnderflowException e) {
            throw new HanaWKBParserException("WKB is too short", e);
        }
    }

    private Geometry parseGeometryOfType(GeometryType type) throws HanaWKBParserException {
        switch (type) {
            case POINT:
                return parsePoint();
            case LINESTRING:
                return parseLineString();
            case POLYGON:
                return parsePolygon();
            case MULTIPOINT:
                return parseMultiPoint();
            case MULTILINESTRING:
                return parseMultiLineString();
            case MULTIPOLYGON:
                return parseMultiPolygon();
            case GEOMETRYCOLLECTION:
                return parseGeometryCollection();
            case CIRCULARSTRING:
                throw new HanaWKBParserException("Circular strings are not supported by JTS");
        }
        throw new AssertionError();
    }

    private Point parsePoint() {
        Coordinate coord;
        double x = data.getDouble();
        double y = data.getDouble();
        if (xyzmMode.hasZ()) {
            double z = data.getDouble();
            coord = new Coordinate(x, y, z);
        } else {
            coord = new Coordinate(x, y);
        }
        if (xyzmMode.hasM()) {
            data.getDouble();
        }
        return factory.createPoint(coord);
    }

    private LineString parseLineString() {
        CoordinateSequence cs = readCoordinateSequence();
        return factory.createLineString(cs);
    }

    private Polygon parsePolygon() {
        int numRings = data.getInt();
        if (numRings == 0) {
            return factory.createPolygon((LinearRing) null);
        }
        LinearRing shell = parseLinearRing();
        if (numRings == 1) {
            return factory.createPolygon(shell);
        }
        LinearRing[] holes = new LinearRing[numRings - 1];
        for (int i = 1; i < numRings; ++i) {
            holes[i - 1] = parseLinearRing();
        }
        return factory.createPolygon(shell, holes);
    }

    private MultiPoint parseMultiPoint() throws HanaWKBParserException {
        int numPoints = data.getInt();
        Point[] points = new Point[numPoints];
        for (int i = 0; i < numPoints; ++i) {
            points[i] = (Point) parseSubGeometry(GeometryType.MULTIPOINT);
        }
        return factory.createMultiPoint(points);
    }

    private MultiLineString parseMultiLineString() throws HanaWKBParserException {
        int numLineStrings = data.getInt();
        LineString[] lineStrings = new LineString[numLineStrings];
        for (int i = 0; i < numLineStrings; ++i) {
            lineStrings[i] = (LineString) parseSubGeometry(GeometryType.MULTILINESTRING);
        }
        return factory.createMultiLineString(lineStrings);
    }

    private MultiPolygon parseMultiPolygon() throws HanaWKBParserException {
        int numPolygons = data.getInt();
        Polygon[] polygons = new Polygon[numPolygons];
        for (int i = 0; i < numPolygons; ++i) {
            polygons[i] = (Polygon) parseSubGeometry(GeometryType.MULTIPOLYGON);
        }
        return factory.createMultiPolygon(polygons);
    }

    private GeometryCollection parseGeometryCollection() throws HanaWKBParserException {
        int numGeometries = data.getInt();
        Geometry[] geometries = new Geometry[numGeometries];
        for (int i = 0; i < numGeometries; ++i) {
            geometries[i] = parseSubGeometry(GeometryType.GEOMETRYCOLLECTION);
        }
        return factory.createGeometryCollection(geometries);
    }

    private Geometry parseSubGeometry(GeometryType parentType) throws HanaWKBParserException {
        readAndSetByteOrder();
        int typeCode = data.getInt();
        GeometryType type = getGeometryType(typeCode);
        return parseGeometryOfType(type);
    }

    private LinearRing parseLinearRing() {
        CoordinateSequence cs = patchRing(readCoordinateSequence());
        return factory.createLinearRing(cs);
    }

    private CoordinateSequence patchRing(CoordinateSequence cs) {
        if (cs.size() >= 4 || cs.size() == 0) {
            return cs;
        }
        Coordinate[] coords = new Coordinate[4];
        for (int i = 0; i < cs.size(); ++i) {
            coords[i] = cs.getCoordinate(i);
        }
        for (int i = cs.size(); i < 4; ++i) {
            coords[i] = cs.getCoordinate(0);
        }
        CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
        return csf.create(coords);
    }

    private CoordinateSequence readCoordinateSequence() {
        CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
        int numPoints = data.getInt();
        CoordinateSequence cs = csf.create(numPoints, jtsDimension);
        switch (xyzmMode) {
            case XY:
                for (int i = 0; i < numPoints; ++i) {
                    cs.setOrdinate(i, 0, data.getDouble());
                    cs.setOrdinate(i, 1, data.getDouble());
                }
                break;
            case XYZ:
                for (int i = 0; i < numPoints; ++i) {
                    cs.setOrdinate(i, 0, data.getDouble());
                    cs.setOrdinate(i, 1, data.getDouble());
                    cs.setOrdinate(i, 2, data.getDouble());
                }
                break;
            case XYM:
                for (int i = 0; i < numPoints; ++i) {
                    cs.setOrdinate(i, 0, data.getDouble());
                    cs.setOrdinate(i, 1, data.getDouble());
                    data.getDouble();
                }
                break;
            case XYZM:
                for (int i = 0; i < numPoints; ++i) {
                    cs.setOrdinate(i, 0, data.getDouble());
                    cs.setOrdinate(i, 1, data.getDouble());
                    cs.setOrdinate(i, 2, data.getDouble());
                    data.getDouble();
                }
                break;
        }
        return cs;
    }

    private GeometryType getGeometryType(int typeCode) throws HanaWKBParserException {
        int wkbType = typeCode & TYPE_MASK;
        wkbType = wkbType % XYZM_MODE_DIV;
        GeometryType type = GeometryType.getFromCode(wkbType);
        if (type == null) {
            throw new HanaWKBParserException(MessageFormat.format("Unknown WKB type {0}", wkbType));
        }
        return type;
    }

    private XyzmMode getXyzmMode(int typeCode) throws HanaWKBParserException {
        int wkbType = typeCode & TYPE_MASK;
        int xyzmFlag = wkbType / XYZM_MODE_DIV;
        switch (xyzmFlag) {
            case XYZM_MODE_XY:
                return XyzmMode.XY;
            case XYZM_MODE_XYZ:
                return XyzmMode.XYZ;
            case XYZM_MODE_XYM:
                return XyzmMode.XYM;
            case XYZM_MODE_XYZM:
                return XyzmMode.XYZM;
        }
        throw new HanaWKBParserException(MessageFormat.format("Invalid XYZM-mode {0}", xyzmFlag));
    }

    private void readAndSetByteOrder() throws HanaWKBParserException {
        byte order = data.get();
        switch (order) {
            case XDR:
                data.order(ByteOrder.BIG_ENDIAN);
                break;
            case NDR:
                data.order(ByteOrder.LITTLE_ENDIAN);
                break;
            default:
                throw new HanaWKBParserException(MessageFormat.format("Invalid BOM value {0}", order));
        }
    }
}
