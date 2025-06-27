/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Arrays;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.CoordinateSequences;
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
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.InStream;
import org.locationtech.jts.io.ParseException;

/**
 * Reads <a href="https://github.com/TWKB/Specification/blob/master/twkb.md">Tiny Well-known Binary</a> (TWKB)into a JTS
 * geometry.
 *
 * <p>This class is designed to support reuse of a single instance to read multiple geometries. This * class is not
 * thread-safe; each thread should create its own instance.
 *
 * @author James Hughes
 * @author Andrea Aime
 */
public class TWKBReader {

    static final int twkbPoint = 1;
    static final int twkbLineString = 2;
    static final int twkbPolygon = 3;
    static final int twkbMultiPoint = 4;
    static final int twkbMultiLineString = 5;
    static final int twkbMultiPolygon = 6;
    static final int twkbGeometryCollection = 7;

    protected CoordinateSequenceFactory csFactory;
    private GeometryFactory factory;
    TWKBMetadata metadata = new TWKBMetadata();
    VarintDataInStream dis = new VarintDataInStream();

    public TWKBReader(GeometryFactory geometryFactory) {
        this.factory = geometryFactory;
        this.csFactory = factory.getCoordinateSequenceFactory();
    }

    public Geometry read(byte[] bytes) throws ParseException, IOException {
        return read(new ByteArrayInStream(bytes));
    }

    public Geometry read(InStream is) throws IOException, ParseException {
        dis.setInStream(is);

        return readGeometry();
    }

    private Geometry readGeometry() throws IOException, ParseException {
        TWKBMetadata metadata = readMetadata();
        int geometryType = metadata.getType();
        switch (geometryType) {
            case twkbPoint:
                return readPoint(metadata);
            case twkbLineString:
                return readLineString(metadata);
            case twkbPolygon:
                return readPolygon(metadata);
            case twkbMultiPoint:
                return readMultiPoint(metadata);
            case twkbMultiLineString:
                return readMultiLineString(metadata);
            case twkbMultiPolygon:
                return readMultiPolygon(metadata);
            case twkbGeometryCollection:
                return readGeometryCollection(metadata);
            default:
                throw new ParseException("Unknown TWKB type " + geometryType);
        }
    }

    private Point readPoint(TWKBMetadata metadata) throws IOException {
        if (!metadata.isEmpty()) {
            CoordinateSequence cs = readCoordinateSequence(1, metadata);
            return factory.createPoint(cs);
        } else {
            return factory.createPoint();
        }
    }

    private LineString readLineString(TWKBMetadata metadata) throws IOException {
        if (!metadata.isEmpty()) {
            int size = dis.readUnsignedInt();
            CoordinateSequence cs = readCoordinateSequence(size, metadata);
            return factory.createLineString(cs);
        } else {
            return factory.createLineString();
        }
    }

    private Polygon readPolygon(TWKBMetadata metadata) throws IOException {
        if (!metadata.isEmpty()) {
            int numRings = dis.readUnsignedInt();
            LinearRing[] holes = null;
            if (numRings > 1) holes = new LinearRing[numRings - 1];

            LinearRing shell = readLinearRing(metadata);
            for (int i = 0; i < numRings - 1; i++) {
                holes[i] = readLinearRing(metadata);
            }
            return factory.createPolygon(shell, holes);
        } else {
            return factory.createPolygon();
        }
    }

    private MultiPoint readMultiPoint(TWKBMetadata metadata) throws IOException {
        if (!metadata.isEmpty()) {
            int numGeom = dis.readUnsignedInt();
            Point[] geoms = new Point[numGeom];
            for (int i = 0; i < numGeom; i++) {
                geoms[i] = readPoint(metadata);
            }
            return factory.createMultiPoint(geoms);
        } else {
            return factory.createMultiPoint();
        }
    }

    private MultiLineString readMultiLineString(TWKBMetadata metadata) throws IOException, ParseException {
        if (!metadata.isEmpty()) {
            int numGeom = dis.readUnsignedInt();
            LineString[] geoms = new LineString[numGeom];
            for (int i = 0; i < numGeom; i++) {
                geoms[i] = readLineString(metadata);
            }
            return factory.createMultiLineString(geoms);
        } else {
            return factory.createMultiLineString();
        }
    }

    private MultiPolygon readMultiPolygon(TWKBMetadata metadata) throws IOException, ParseException {
        if (!metadata.isEmpty()) {
            int numGeom = dis.readUnsignedInt();
            Polygon[] geoms = new Polygon[numGeom];
            for (int i = 0; i < numGeom; i++) {
                geoms[i] = readPolygon(metadata);
            }
            return factory.createMultiPolygon(geoms);
        } else {
            return factory.createMultiPolygon();
        }
    }

    private GeometryCollection readGeometryCollection(TWKBMetadata metadata) throws IOException, ParseException {
        if (!metadata.isEmpty()) {
            int numGeom = dis.readUnsignedInt();
            Geometry[] geoms = new Geometry[numGeom];
            for (int i = 0; i < numGeom; i++) {
                // from the spec, "for each geometry there will be a complete TWKB geometry,
                // readable using the rules set out above", so need to reset the ordinates
                if (i > 0) Arrays.fill(metadata.valueArray, 0);
                geoms[i] = readGeometry();
            }
            return factory.createGeometryCollection(geoms);
        } else {
            return factory.createGeometryCollection();
        }
    }

    private LinearRing readLinearRing(TWKBMetadata metadata) throws IOException {
        int size = dis.readUnsignedInt(); // .readInt();
        CoordinateSequence pts = readCoordinateSequenceRing(size, metadata);
        return factory.createLinearRing(pts);
    }

    private CoordinateSequence readCoordinateSequenceRing(int size, TWKBMetadata metadata) throws IOException {
        CoordinateSequence seq = readCoordinateSequence(size, metadata);
        return CoordinateSequences.ensureValidRing(csFactory, seq);
    }

    private int zigzagDecode(int input) {
        return input >>> 1 ^ -(input & 1);
    }

    private TWKBMetadata readMetadata() throws IOException {
        int geometryTypeAndPrecision = dis.readByte();
        int geometryType = geometryTypeAndPrecision & 0x0F;
        int precision = zigzagDecode((geometryTypeAndPrecision & 0xF0) >>> 4);

        metadata.setType(geometryType);
        metadata.setPrecision(precision);

        byte header = dis.readByte();
        metadata.setHeader(header);

        int dims = 2;
        // according to https://github.com/TWKB/Specification/blob/master/twkb.md it is Z then M
        if (metadata.hasExtendedDims()) {
            int dimensions = dis.readByte();

            if ((dimensions & 0x01) > 0) {
                dims += 1;
                metadata.setHasZ(true);
                metadata.setZprecision((dimensions & 0x1C) >> 2);
            }
            if ((dimensions & 0x02) > 0) {
                dims += 1;
                metadata.setHasM(true);
                metadata.setMprecision((dimensions & 0xE0) >> 5);
            }
        }
        metadata.setDims(dims);

        // TODO: Read optional size?
        if (metadata.hasSize()) {
            metadata.setSize(dis.readSignedInt());
        } else {
            metadata.setSize(1);
        }

        if (metadata.hasBBOX()) {
            CoordinateSequence bbox = csFactory.create(2, dims);
            for (int i = 0; i < dims; i++) {
                double min = readNextDouble(metadata.getScale(0));
                double delta = readNextDouble(metadata.getScale(0));
                bbox.setOrdinate(0, i, min);
                bbox.setOrdinate(1, i, min + delta);
            }
            // System.out.println("BBOX read " + bbox);
            metadata.setEnvelope(bbox);
        }

        return metadata;
    }

    protected CoordinateSequence readCoordinateSequence(int numPts, TWKBMetadata metadata) throws IOException {
        int dims = metadata.getDims();

        // Create CoordinateSequence and read geometry
        CoordinateSequence seq = csFactory.create(numPts, dims);
        final double[] scales = new double[dims];
        for (int i = 0; i < scales.length; i++) {
            scales[i] = metadata.getScale(i);
        }
        for (int i = 0; i < numPts; i++) {
            for (int j = 0; j < dims; j++) {
                double ordinateDelta = readNextDouble(scales[j]);
                double value = metadata.valueArray[j] + ordinateDelta;
                metadata.valueArray[j] = value;
                seq.setOrdinate(i, j, value);
            }
        }

        return seq;
    }

    protected double readNextDouble(double scale) throws IOException {
        long value = dis.readSignedLong();
        return value / scale;
    }

    protected static class TWKBMetadata {
        int zprecision;
        int mprecision;
        byte header;
        int size;
        int dims;
        double[] valueArray;
        int precision;
        double scale = 1;
        double scaleZ = 1;
        double scaleM = 1;

        boolean hasZ, hasM;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getDims() {
            return dims;
        }

        public void setDims(int dims) {
            this.dims = dims;
            if (valueArray == null || valueArray.length != dims) {
                this.valueArray = new double[this.dims];
            } else {
                Arrays.fill(valueArray, 0);
            }
        }

        public byte getHeader() {
            return header;
        }

        public void setHeader(byte header) {
            this.header = header;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        int type;

        public int getPrecision() {
            return precision;
        }

        public double getScale(int i) {
            switch (i) {
                case 0:
                case 1:
                    return scale;
                case 2:
                    if (hasZ) {
                        return scaleZ;
                    } else if (hasM) {
                        return scaleM;
                    } else {
                        throw new IllegalArgumentException("Geometry only has XY dimensions.");
                    }
                case 3:
                    if (hasZ && hasM) {
                        return scaleM;
                    } else {
                        throw new IllegalArgumentException("Mismatch with the number of dimensions.");
                    }
            }

            throw new IllegalArgumentException("Mismatch with the number of dimensions.");
        }

        public void setPrecision(int precision) {
            this.precision = precision;
            this.scale = Math.pow(10, precision);
        }

        public int getZprecision() {
            return zprecision;
        }

        public void setZprecision(int zprecision) {
            this.zprecision = zprecision;
            this.scaleZ = Math.pow(10, zprecision);
        }

        public int getMprecision() {
            return mprecision;
        }

        public void setMprecision(int mprecision) {
            this.mprecision = mprecision;
            this.scaleM = Math.pow(10, mprecision);
        }

        public boolean isHasZ() {
            return hasZ;
        }

        public void setHasZ(boolean hasZ) {
            this.hasZ = hasZ;
        }

        public boolean isHasM() {
            return hasM;
        }

        public void setHasM(boolean hasM) {
            this.hasM = hasM;
        }

        CoordinateSequence envelope;

        public CoordinateSequence getEnvelope() {
            return envelope;
        }

        public void setEnvelope(CoordinateSequence envelope) {
            this.envelope = envelope;
        }

        public TWKBMetadata() {}

        boolean hasBBOX() {
            return (header & 0x01) > 0;
        }

        boolean hasSize() {
            return (header & 0x02) > 0;
        }

        boolean hasIdList() {
            return (header & 0x04) > 0;
        }

        boolean hasExtendedDims() {
            return (header & 0x08) > 0;
        }

        boolean isEmpty() {
            return (header & 0x10) > 0;
        }
    }
}
