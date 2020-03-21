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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.geotools.data.Base64;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequences;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.WKBWriter;

/**
 * A helper ransforming a TWKB into a Geometry from a byte[] or {@link ResultSet}, without having to
 * reallocate a InStream over and over for each geometry parsed during a database read. This class
 * is stateful as a consequence, thus not thread safe, use a separate instance per thread.
 */
class TWKBAttributeIO {
    TWKBReader twkbReader;
    ByteArrayInStream inStream = new ByteArrayInStream(new byte[0]);
    GeometryFactory gf;
    boolean base64EncodingEnabled;

    /**
     * A variation of TWKBReader tha optimizes conversion of collapsed point expressed using
     * LiteCoordinateSequence
     */
    static final class LiteTWKBReader extends TWKBReader {

        public LiteTWKBReader(GeometryFactory geometryFactory) {
            super(geometryFactory);
        }

        protected CoordinateSequence readCoordinateSequence(int numPts, TWKBMetadata metadata)
                throws IOException {
            if (!(csFactory instanceof LiteCoordinateSequenceFactory)) {
                return super.readCoordinateSequence(numPts, metadata);
            }

            final int dims = metadata.getDims();
            final LiteCoordinateSequence seq =
                    (LiteCoordinateSequence) csFactory.create(numPts, dims);
            final double[] ordinates = new double[numPts * dims];
            final double[] scales = new double[dims];
            for (int i = 0; i < scales.length; i++) {
                scales[i] = metadata.getScale(i);
            }
            int k = 0;
            for (int i = 0; i < numPts; i++) {
                for (int j = 0; j < dims; j++) {
                    double ordinateDelta = readNextDouble(scales[j]);
                    double value = metadata.valueArray[j] + ordinateDelta;
                    metadata.valueArray[j] = value;
                    ordinates[k++] = value;
                }
            }
            seq.setArray(ordinates);

            return seq;
        }
    }

    public TWKBAttributeIO() {
        this(new GeometryFactory());
    }

    public TWKBAttributeIO(GeometryFactory gf) {
        twkbReader = new LiteTWKBReader(gf);
        this.gf = gf;
    }

    public void setGeometryFactory(GeometryFactory gf) {
        if (gf != this.gf) {
            this.gf = gf;
            twkbReader = new LiteTWKBReader(gf);
        }
    }

    public GeometryFactory getGeometryFactory() {
        return gf;
    }

    public boolean isBase64EncodingEnabled() {
        return base64EncodingEnabled;
    }

    public void setBase64EncodingEnabled(boolean base64EncodingEnabled) {
        this.base64EncodingEnabled = base64EncodingEnabled;
    }

    /**
     * This method will convert a Well Known Binary representation to a JTS Geometry object.
     *
     * @param wkbBytes the TWKB encoded byte array
     * @return a JTS Geometry object that is equivalent to the WTB representation passed in by param
     *     wkb
     * @throws IOException if more than one geometry object was found in the WTB representation, or
     *     if the parser could not parse the WKB representation.
     */
    private Geometry wkb2Geometry(byte[] wkbBytes) throws IOException {
        if (wkbBytes == null) return null;
        try {
            inStream.setBytes(wkbBytes);
            return twkbReader.read(inStream);
        } catch (Exception e) {
            throw new DataSourceException("An exception occurred while parsing TWKB data", e);
        }
    }

    /** @see org.geotools.data.jdbc.attributeio.AttributeIO#read(ResultSet, int) */
    public Object read(ResultSet rs, String columnName) throws IOException {
        try {
            byte bytes[] = rs.getBytes(columnName);
            if (bytes == null) // ie. its a null column -> return a null geometry!
            return null;
            if (base64EncodingEnabled) {
                bytes = Base64.decode(bytes);
            }
            return wkb2Geometry(bytes);
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /** @see org.geotools.data.jdbc.attributeio.AttributeIO#read(ResultSet, int) */
    public Object read(ResultSet rs, int columnIndex, Class<?> binding) throws IOException {
        try {
            byte bytes[] = rs.getBytes(columnIndex);
            if (bytes == null) // ie. its a null column -> return a null geometry!
            return null;

            // the TWKB encoding collapses geometries into points and encodes them as such,
            // causing an inefficient converted to be called later down the road, handle
            // this case in a special way to ensure better performance
            if (base64EncodingEnabled) {
                bytes = Base64.decode(bytes);
            }
            Geometry g = wkb2Geometry(bytes);
            g = adaptToBinding(g, binding);

            return g;
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /** @see org.geotools.data.jdbc.attributeio.AttributeIO#read(ResultSet, int) */
    public Object read(ResultSet rs, String columnName, Class<?> binding) throws IOException {
        try {
            byte bytes[] = rs.getBytes(columnName);
            if (bytes == null) // ie. its a null column -> return a null geometry!
            return null;

            Geometry g = wkb2Geometry(Base64.decode(bytes));
            g = adaptToBinding(g, binding);

            return g;
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /**
     * The TWKB encoding collapses geometries into points and encodes them as such, causing an
     * inefficient conversion to be called later down the road, handle this case in a special way to
     * ensure better performance
     */
    public Geometry adaptToBinding(Geometry g, Class<?> binding) {
        if (g instanceof Point && !binding.isInstance(g)) {
            CoordinateSequence cs = ((Point) g).getCoordinateSequence();
            if (Polygon.class.isAssignableFrom(binding)) {
                g = toPolygon(cs);
            } else if (LineString.class.isAssignableFrom(binding)) {
                g = toLineString(cs);
            } else if (MultiPolygon.class.isAssignableFrom(binding)) {
                Polygon p = toPolygon(cs);
                g = gf.createMultiPolygon(new Polygon[] {p});
            } else if (MultiLineString.class.isAssignableFrom(binding)) {
                LineString ls = toLineString(cs);
                g = gf.createMultiLineString(new LineString[] {ls});
            }
        }
        return g;
    }

    public LineString toLineString(CoordinateSequence cs) {
        CoordinateSequence lineSequence =
                CoordinateSequences.extend(gf.getCoordinateSequenceFactory(), cs, 2);
        return gf.createLineString(lineSequence);
    }

    public Polygon toPolygon(CoordinateSequence cs) {
        CoordinateSequence ringSequence =
                CoordinateSequences.ensureValidRing(gf.getCoordinateSequenceFactory(), cs);
        LinearRing shell = gf.createLinearRing(ringSequence);
        return gf.createPolygon(shell);
    }

    /** @see org.geotools.data.jdbc.attributeio.AttributeIO#write(PreparedStatement, int, Object) */
    public void write(PreparedStatement ps, int position, Object value) throws IOException {
        try {
            if (value == null) {
                ps.setNull(position, Types.OTHER);
            } else {
                ps.setBytes(position, new WKBWriter().write((Geometry) value));
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /** Turns a char that encodes four bits in hexadecimal notation into a byte */
    public static byte getFromChar(char c) {
        if (c <= '9') {
            return (byte) (c - '0');
        } else if (c <= 'F') {
            return (byte) (c - 'A' + 10);
        } else {
            return (byte) (c - 'a' + 10);
        }
    }
}
