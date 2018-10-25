/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.geom;

import java.io.IOException;
import java.io.InputStream;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Envelope;
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
import org.locationtech.jts.io.ByteOrderDataInStream;
import org.locationtech.jts.io.InStream;
import org.locationtech.jts.io.InputStreamInStream;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

/**
 * Translates a GeoPackage geometry BLOB to a vividsolutions Geometry.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPkgGeomReader {

    static final GeometryFactory DEFAULT_GEOM_FACTORY = new GeometryFactory();

    protected InStream input;

    protected GeometryHeader header = null;

    protected Geometry geometry = null;

    private GeometryFactory factory = DEFAULT_GEOM_FACTORY;

    private Hints hints;
    private Number simplificationDistance;
    private Class geometryType;

    public GeoPkgGeomReader(InStream input) {
        this.input = input;
    }

    public GeoPkgGeomReader(InputStream input) throws IOException {
        this.input = new InputStreamInStream(input);
    }

    public GeoPkgGeomReader(byte[] bytes) {
        this.input = new ByteArrayInStream(bytes);
    }

    public GeometryHeader getHeader() throws IOException {
        if (header == null) {
            header = readHeader();
        }
        return header;
    }

    public Geometry get() throws IOException {
        if (header == null) {
            header = readHeader();
        }

        if (geometry == null) {
            Envelope envelope = header.getEnvelope();
            if (simplificationDistance != null
                    && geometryType != null
                    && header.getFlags().getEnvelopeIndicator() != EnvelopeType.NONE
                    && envelope.getWidth() < simplificationDistance.doubleValue()
                    && envelope.getHeight() < simplificationDistance.doubleValue()) {
                Geometry simplified =
                        getSimplifiedShape(
                                geometryType,
                                envelope.getMinX(),
                                envelope.getMinY(),
                                envelope.getMaxX(),
                                envelope.getMaxY());
                if (simplified != null) {
                    geometry = simplified;
                }
            }

            if (geometry == null) {
                geometry = read();
            }
        }
        return geometry;
    }

    public Geometry getSimplifiedShape(
            Class type, double minX, double minY, double maxX, double maxY) {
        CoordinateSequenceFactory csf = factory.getCoordinateSequenceFactory();
        if (Point.class.equals(type)) {
            CoordinateSequence cs = JTS.createCS(csf, 1, 2);
            cs.setOrdinate(0, 0, (minX + maxX) / 2);
            cs.setOrdinate(0, 1, (minY + maxY) / 2);
            return factory.createPoint(cs);
        } else if (MultiPoint.class.equals(type)) {
            Point p = (Point) getSimplifiedShape(Point.class, minX, minY, maxX, maxY);
            return factory.createMultiPoint(new Point[] {p});
        } else if (LineString.class.equals(type) || LinearRing.class.equals(type)) {
            CoordinateSequence cs = JTS.createCS(csf, 2, 2);
            cs.setOrdinate(0, 0, minX);
            cs.setOrdinate(0, 1, minY);
            cs.setOrdinate(1, 0, maxX);
            cs.setOrdinate(1, 1, maxY);
            return factory.createLineString(cs);
        } else if (MultiLineString.class.equals(type)) {
            LineString ls =
                    (LineString) getSimplifiedShape(LineString.class, minX, minY, maxX, maxY);
            return factory.createMultiLineString(new LineString[] {ls});
        } else if (Polygon.class.equals(type)) {
            CoordinateSequence cs = JTS.createCS(csf, 5, 2);
            cs.setOrdinate(0, 0, minX);
            cs.setOrdinate(0, 1, minY);
            cs.setOrdinate(1, 0, minX);
            cs.setOrdinate(1, 1, maxY);
            cs.setOrdinate(2, 0, maxX);
            cs.setOrdinate(2, 1, maxY);
            cs.setOrdinate(3, 0, maxX);
            cs.setOrdinate(3, 1, minY);
            cs.setOrdinate(4, 0, minX);
            cs.setOrdinate(4, 1, minY);
            LinearRing ring = factory.createLinearRing(cs);
            return factory.createPolygon(ring, null);
        } else if (MultiPolygon.class.equals(type) || GeometryCollection.class.equals(type)) {
            Polygon polygon = (Polygon) getSimplifiedShape(Polygon.class, minX, minY, maxX, maxY);
            return factory.createMultiPolygon(new Polygon[] {polygon});
        } else {
            // don't really know what to do with this case, guessing a type might break expectations
            return null;
        }
    }

    public Envelope getEnvelope() throws IOException {
        if (getHeader().getFlags().getEnvelopeIndicator() == EnvelopeType.NONE) {
            return get().getEnvelopeInternal();
        } else {
            return getHeader().getEnvelope();
        }
    }

    protected Geometry read() throws IOException { // header must be read!
        // read the geometry
        try {
            WKBReader wkbReader = new WKBReader(factory);
            Geometry g = wkbReader.read(input);
            g.setSRID(header.getSrid());
            return g;
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    /*
    * OptimizedGeoPackageBinary {
    * byte[3] magic = 0x47504230; // 'GPB'
    * byte flags;                 // see flags layout below
    * unit32 srid;
    * double[] envelope;          // see flags envelope contents indicator code below
    * WKBGeometry geometry;       // per OGC 06-103r4 clause 8
    *
    *
    * flags layout:
    *   bit     7       6       5       4       3       2       1       0
    *   use     -       -       X       Y       E       E       E       B

    *   use:
    *   X: GeoPackageBinary type (0: StandardGeoPackageBinary, 1: ExtendedGeoPackageBinary)
    *   Y: 0: non-empty geometry, 1: empty geometry
    *
    *   E: envelope contents indicator code (3-bit unsigned integer)
    *     value |                    description                               | envelope length (bytes)
    *       0   | no envelope (space saving slower indexing option)            |      0
    *       1   | envelope is [minx, maxx, miny, maxy]                         |      32
    *       2   | envelope is [minx, maxx, miny, maxy, minz, maxz]             |      48
    *       3   | envelope is [minx, maxx, miny, maxy, minm, maxm]             |      48
    *       4   | envelope is [minx, maxx, miny, maxy, minz, maxz, minm, maxm] |      64
    *   B: byte order for header values (1-bit Boolean)
    *       0 = Big Endian   (most significant bit first)
    *       1 = Little Endian (least significant bit first)
    */
    protected GeometryHeader readHeader() throws IOException {
        GeometryHeader h = new GeometryHeader();

        // read first 4 bytes
        // TODO: something with the magic number
        byte[] buf = new byte[4];
        input.read(buf);

        // next byte flags
        h.setFlags(new GeometryHeaderFlags((byte) buf[3]));

        // set endianess
        ByteOrderDataInStream din = new ByteOrderDataInStream(input);
        din.setOrder(h.getFlags().getEndianess());

        // read the srid
        h.setSrid(din.readInt());

        // read the envelope
        if (h.getFlags().getEnvelopeIndicator() != EnvelopeType.NONE) {
            double x1 = din.readDouble();
            double x2 = din.readDouble();
            double y1 = din.readDouble();
            double y2 = din.readDouble();

            if (h.getFlags().getEnvelopeIndicator().getValue() > 1) {
                // 2 = minz,maxz; 3 = minm,maxm - we ignore these for now
                din.readDouble();
                din.readDouble();
            }

            if (h.getFlags().getEnvelopeIndicator().getValue() > 3) {
                // 4 = minz,maxz,minm,maxm - we ignore these for now
                din.readDouble();
                din.readDouble();
            }

            h.setEnvelope(new Envelope(x1, x2, y1, y2));
        }
        return h;
    }

    /** @return the factory */
    public GeometryFactory getFactory() {
        return factory;
    }

    /** @param factory the factory to set */
    public void setFactory(GeometryFactory factory) {
        if (factory != null) {
            this.factory = factory;
        }
    }

    public void setHints(Hints hints) {
        if (hints != null) {
            this.simplificationDistance = (Number) hints.get(Hints.GEOMETRY_DISTANCE);
        }
        this.hints = hints;
    }

    public void setGeometryType(Class geometryType) {
        this.geometryType = geometryType;
    }
}
