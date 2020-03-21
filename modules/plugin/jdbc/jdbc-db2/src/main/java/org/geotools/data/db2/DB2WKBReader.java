/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2012, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.data.db2;

import java.io.IOException;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ByteOrderDataInStream;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.InStream;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBConstants;

/**
 * @author Christian Mueller
 *     <p>Version of JTS WKB Reader adjusted for DB2
 * @see WKBReader for JTS Java Doc
 */
public class DB2WKBReader {

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param hex a string containing hex digits
     */
    public static byte[] hexToBytes(String hex) {
        int byteLen = hex.length() / 2;
        byte[] bytes = new byte[byteLen];

        for (int i = 0; i < hex.length() / 2; i++) {
            int i2 = 2 * i;
            if (i2 + 1 > hex.length())
                throw new IllegalArgumentException("Hex string has odd length");

            int nib1 = hexToInt(hex.charAt(i2));
            int nib0 = hexToInt(hex.charAt(i2 + 1));
            byte b = (byte) ((nib1 << 4) + (byte) nib0);
            bytes[i] = b;
        }
        return bytes;
    }

    private static int hexToInt(char hex) {
        int nib = Character.digit(hex, 16);
        if (nib < 0) throw new IllegalArgumentException("Invalid hex digit: '" + hex + "'");
        return nib;
    }

    private static final String INVALID_GEOM_TYPE_MSG = "Invalid geometry type encountered in ";

    private GeometryFactory factory;
    private PrecisionModel precisionModel;
    // default dimension - will be set on read
    private int inputDimension = 2;
    private int SRID = 0;
    private ByteOrderDataInStream dis = new ByteOrderDataInStream();
    private double[] ordValues;

    public DB2WKBReader() {
        this(new GeometryFactory());
    }

    public DB2WKBReader(GeometryFactory geometryFactory) {
        this.factory = geometryFactory;
        precisionModel = factory.getPrecisionModel();
    }

    /**
     * Reads a single {@link Geometry} from a byte array.
     *
     * @param bytes the byte array to read from
     * @return the geometry read
     * @throws ParseException if a parse exception occurs
     */
    public Geometry read(byte[] bytes) throws ParseException {
        // possibly reuse the ByteArrayInStream?
        // don't throw IOExceptions, since we are not doing any I/O
        try {
            return read(new ByteArrayInStream(bytes));
        } catch (IOException ex) {
            throw new RuntimeException("Unexpected IOException caught: " + ex.getMessage());
        }
    }

    /**
     * Reads a {@link Geometry} from an {@link InStream).
     *
     * @param is the stream to read from
     * @return the Geometry read
     */
    public Geometry read(InStream is) throws IOException, ParseException {
        dis.setInStream(is);
        Geometry g = readGeometry();
        setSRID(g);
        return g;
    }

    private Geometry readGeometry() throws IOException, ParseException {
        // determine byte order
        byte byteOrder = dis.readByte();
        // default is big endian
        if (byteOrder == WKBConstants.wkbNDR) dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);

        int geometryType = dis.readInt();

        if (DB2WKBConstants.zTypes.contains(geometryType)) {
            inputDimension = 3;
            if (geometryType == DB2WKBConstants.wkbPointZ)
                geometryType = DB2WKBConstants.wkbPoint2D;
            if (geometryType == DB2WKBConstants.wkbOGCPointZ)
                geometryType = DB2WKBConstants.wkbPoint2D;

            if (geometryType == DB2WKBConstants.wkbLineStringZ)
                geometryType = DB2WKBConstants.wkbLineString2D;
            if (geometryType == DB2WKBConstants.wkbOGCLineStringZ)
                geometryType = DB2WKBConstants.wkbLineString2D;

            if (geometryType == DB2WKBConstants.wkbPolygonZ)
                geometryType = DB2WKBConstants.wkbPolygon2D;
            if (geometryType == DB2WKBConstants.wkbOGCPolygonZ)
                geometryType = DB2WKBConstants.wkbPolygon2D;

            if (geometryType == DB2WKBConstants.wkbMultiPointZ)
                geometryType = DB2WKBConstants.wkbMultiPoint2D;
            if (geometryType == DB2WKBConstants.wkbOGCMultiPointZ)
                geometryType = DB2WKBConstants.wkbMultiPoint2D;

            if (geometryType == DB2WKBConstants.wkbMultiLineStringZ)
                geometryType = DB2WKBConstants.wkbMultiLineString2D;
            if (geometryType == DB2WKBConstants.wkbOGCMultiLineStringZ)
                geometryType = DB2WKBConstants.wkbMultiLineString2D;

            if (geometryType == DB2WKBConstants.wkbMultiPolygonZ)
                geometryType = DB2WKBConstants.wkbMultiPolygon2D;
            if (geometryType == DB2WKBConstants.wkbOGCMultiPolygonZ)
                geometryType = DB2WKBConstants.wkbMultiPolygon2D;

            if (geometryType == DB2WKBConstants.wkbGeomCollectionZ)
                geometryType = DB2WKBConstants.wkbGeomCollection2D;
            if (geometryType == DB2WKBConstants.wkbOGCGeomCollectionZ)
                geometryType = DB2WKBConstants.wkbGeomCollection2D;
        }

        // only allocate ordValues buffer if necessary
        if (ordValues == null || ordValues.length < inputDimension)
            ordValues = new double[inputDimension];

        switch (geometryType) {
            case DB2WKBConstants.wkbPoint2D:
                return readPoint();
            case DB2WKBConstants.wkbLineString2D:
                return readLineString();
            case DB2WKBConstants.wkbPolygon2D:
                return readPolygon();
            case DB2WKBConstants.wkbMultiPoint2D:
                return readMultiPoint();
            case DB2WKBConstants.wkbMultiLineString2D:
                return readMultiLineString();
            case DB2WKBConstants.wkbMultiPolygon2D:
                return readMultiPolygon();
            case DB2WKBConstants.wkbGeomCollection2D:
                return readGeometryCollection();
        }
        throw new ParseException("Unknown WKB type " + geometryType);
        // return null;
    }

    /**
     * Sets the SRID, if it was specified in the WKB
     *
     * @param g the geometry to update
     * @return the geometry with an updated SRID value, if required
     */
    private Geometry setSRID(Geometry g) {
        if (SRID != 0) g.setSRID(SRID);
        return g;
    }

    private Point readPoint() throws IOException {
        CoordinateSequence pts = readCoordinateSequence(1);
        return factory.createPoint(pts);
    }

    private LineString readLineString() throws IOException {
        int size = dis.readInt();
        CoordinateSequence pts = readCoordinateSequence(size);
        return factory.createLineString(pts);
    }

    private LinearRing readLinearRing() throws IOException {
        int size = dis.readInt();
        CoordinateSequence pts = readCoordinateSequence(size);
        return factory.createLinearRing(pts);
    }

    private Polygon readPolygon() throws IOException {
        int numRings = dis.readInt();
        LinearRing[] holes = null;
        if (numRings > 1) holes = new LinearRing[numRings - 1];

        LinearRing shell = readLinearRing();
        for (int i = 0; i < numRings - 1; i++) {
            holes[i] = readLinearRing();
        }
        return factory.createPolygon(shell, holes);
    }

    private MultiPoint readMultiPoint() throws IOException, ParseException {
        int numGeom = dis.readInt();
        Point[] geoms = new Point[numGeom];
        for (int i = 0; i < numGeom; i++) {
            Geometry g = readGeometry();
            if (!(g instanceof Point))
                throw new ParseException(INVALID_GEOM_TYPE_MSG + "MultiPoint");
            geoms[i] = (Point) g;
        }
        return factory.createMultiPoint(geoms);
    }

    private MultiLineString readMultiLineString() throws IOException, ParseException {
        int numGeom = dis.readInt();
        LineString[] geoms = new LineString[numGeom];
        for (int i = 0; i < numGeom; i++) {
            Geometry g = readGeometry();
            if (!(g instanceof LineString))
                throw new ParseException(INVALID_GEOM_TYPE_MSG + "MultiLineString");
            geoms[i] = (LineString) g;
        }
        return factory.createMultiLineString(geoms);
    }

    private MultiPolygon readMultiPolygon() throws IOException, ParseException {
        int numGeom = dis.readInt();
        Polygon[] geoms = new Polygon[numGeom];
        for (int i = 0; i < numGeom; i++) {
            Geometry g = readGeometry();
            if (!(g instanceof Polygon))
                throw new ParseException(INVALID_GEOM_TYPE_MSG + "MultiPolygon");
            geoms[i] = (Polygon) g;
        }
        return factory.createMultiPolygon(geoms);
    }

    private GeometryCollection readGeometryCollection() throws IOException, ParseException {
        int numGeom = dis.readInt();
        Geometry[] geoms = new Geometry[numGeom];
        for (int i = 0; i < numGeom; i++) {
            geoms[i] = readGeometry();
        }
        return factory.createGeometryCollection(geoms);
    }

    private CoordinateSequence readCoordinateSequence(int size) throws IOException {
        CoordinateSequence seq =
                JTS.createCS(factory.getCoordinateSequenceFactory(), size, inputDimension);
        int targetDim = seq.getDimension();
        if (targetDim > inputDimension) targetDim = inputDimension;
        for (int i = 0; i < size; i++) {
            readCoordinate();
            for (int j = 0; j < targetDim; j++) {
                seq.setOrdinate(i, j, ordValues[j]);
            }
        }
        return seq;
    }

    /**
     * Reads a coordinate value with the specified dimensionality. Makes the X and Y ordinates
     * precise according to the precision model in use.
     */
    private void readCoordinate() throws IOException {
        for (int i = 0; i < inputDimension; i++) {
            if (i <= 1) {
                ordValues[i] = precisionModel.makePrecise(dis.readDouble());
            } else {
                ordValues[i] = dis.readDouble();
            }
        }
    }
}
