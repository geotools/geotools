/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import static org.geotools.geometry.jts.WKBReader.COORDINATE_BATCH_SIZE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.InputStreamInStream;
import org.locationtech.jts.io.Ordinate;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBWriter;

/** Reads WKB into {@link LiteCoordinateSequence}, the path used by the renderer. */
public class WKBReaderLiteTest {

    // ordinates with all mantissa bits in use, so a byte order or rounding mistake shows
    private static final double[][] XYZM = {
        {-119191.40812345678, 276083.76712345678, 12.345678901234567, 0.1},
        {162129.08112345678, -300404.80412345678, -7.0000000000000001, 1e-300},
        {162129.08112345678, 276083.76712345678, Math.PI, Double.MAX_VALUE},
        {-119191.40812345678, 276083.76712345678, 12.345678901234567, 0.1}
    };

    private static final int BATCH = COORDINATE_BATCH_SIZE;
    private static final EnumSet<Ordinate> XY = EnumSet.of(Ordinate.X, Ordinate.Y);

    private static final List<EnumSet<Ordinate>> ORDINATE_SETS = List.of(
            XY,
            EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.Z),
            EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.M),
            EnumSet.allOf(Ordinate.class));
    private static final GeometryFactory LITE = new GeometryFactory(new LiteCoordinateSequenceFactory());

    @Test
    public void testReadXY() throws Exception {
        assertReadsExactly(XY);
    }

    @Test
    public void testReadXYZ() throws Exception {
        assertReadsExactly(EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.Z));
    }

    @Test
    public void testReadXYM() throws Exception {
        assertReadsExactly(EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.M));
    }

    @Test
    public void testReadXYZM() throws Exception {
        assertReadsExactly(EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.Z, Ordinate.M));
    }

    @Test
    public void testFloatingSinglePrecisionRoundsOrdinates() throws Exception {
        GeometryFactory single = new GeometryFactory(
                new PrecisionModel(PrecisionModel.FLOATING_SINGLE), 0, new LiteCoordinateSequenceFactory());
        byte[] wkb = xyPolygon();

        Polygon polygon = (Polygon) new WKBReader(single).read(wkb);

        CoordinateSequence seq = polygon.getExteriorRing().getCoordinateSequence();
        assertEquals((float) XYZM[0][0], seq.getX(0), 0d);
        assertEquals((float) XYZM[0][1], seq.getY(0), 0d);
    }

    @Test
    public void testTruncatedInput() throws Exception {
        byte[] wkb = xyPolygon();
        byte[] truncated = Arrays.copyOf(wkb, wkb.length - 3);

        ParseException e = assertThrows(ParseException.class, () -> new WKBReader(LITE).read(truncated));
        assertEquals("Attempt to read past end of input", e.getMessage());
    }

    @Test
    public void testReadBatchBoundaries() throws Exception {
        for (EnumSet<Ordinate> ordinates : ORDINATE_SETS) {
            for (int order : new int[] {ByteOrderValues.LITTLE_ENDIAN, ByteOrderValues.BIG_ENDIAN}) {
                assertReadsBatchSizes(ordinates, order);
            }
        }
    }

    private static void assertReadsBatchSizes(EnumSet<Ordinate> ordinates, int order) throws Exception {
        WKBReader reader = new WKBReader(LITE);
        for (int size : new int[] {2, BATCH - 1, BATCH, BATCH + 1, 2 * BATCH, 2 * BATCH + 1, 4, 0}) {
            LineString source = line(size, ordinates);
            LineString read = (LineString) reader.read(write(source, ordinates, order));
            assertEquals(size, read.getNumPoints());
            assertSequence(source.getCoordinateSequence(), read.getCoordinateSequence());
        }
    }

    @Test
    public void testBatchReadsAreBounded() throws Exception {
        int batchBytes = BATCH * 2 * Double.BYTES;
        assertEquals(List.of(batchBytes, batchBytes, 2 * Double.BYTES), coordinateReadSizes(line(2 * BATCH + 1, XY)));
    }

    @Test
    public void testSmallSequenceUsesSmallBuffer() throws Exception {
        assertEquals(List.of(4 * 2 * Double.BYTES), coordinateReadSizes(line(4, XY)));
    }

    /** Reads the line and returns the sizes of the reads larger than an int, that is the coordinate reads. */
    private static List<Integer> coordinateReadSizes(LineString source) throws Exception {
        ByteArrayInStream bytes = new ByteArrayInStream(write(source, XY, ByteOrderValues.LITTLE_ENDIAN));
        List<Integer> sizes = new ArrayList<>();
        LineString read = (LineString) new WKBReader(LITE).read(buffer -> {
            if (buffer.length > Integer.BYTES) sizes.add(buffer.length);
            return bytes.read(buffer);
        });
        assertSequence(source.getCoordinateSequence(), read.getCoordinateSequence());
        return sizes;
    }

    @Test
    public void testReadShortChunks() throws Exception {
        assertReadsShortChunks(line(2 * BATCH + 1, XY), XY);
    }

    @Test
    public void testReadPointShortChunks() throws Exception {
        assertReadsShortChunks(point(XY), XY);
    }

    private static void assertReadsShortChunks(Geometry source, EnumSet<Ordinate> ordinates) throws Exception {
        byte[] wkb = write(source, ordinates, ByteOrderValues.BIG_ENDIAN);
        try (ByteArrayInputStream stream = new ByteArrayInputStream(wkb) {
            @Override
            public synchronized int read(byte[] buffer, int offset, int length) {
                return super.read(buffer, offset, Math.min(length, 7));
            }
        }) {
            Geometry read = new WKBReader(LITE).read(new InputStreamInStream(stream));
            assertSequence(sequence(source), sequence(read));
        }
    }

    @Test
    public void testTruncatedBatchAndRemainder() throws Exception {
        byte[] wkb = write(line(BATCH + 1, XY), XY, ByteOrderValues.LITTLE_ENDIAN);
        for (int length : new int[] {9, 9 + BATCH * 2 * Double.BYTES - 1, wkb.length - 1}) {
            byte[] truncated = Arrays.copyOf(wkb, length);
            ParseException e = assertThrows(
                    ParseException.class, () -> new WKBReader(LITE).read(new ByteArrayInStream(truncated)));
            assertEquals("Attempt to read past end of input", e.getMessage());
        }
    }

    @Test
    public void testZeroLengthCoordinateRead() throws Exception {
        byte[] wkb = write(line(BATCH + 1, XY), XY, ByteOrderValues.LITTLE_ENDIAN);
        ByteArrayInStream bytes = new ByteArrayInStream(wkb);
        ParseException thrown = assertThrows(ParseException.class, () -> new WKBReader(LITE)
                .read(buffer -> buffer.length > Integer.BYTES ? 0 : bytes.read(buffer)));
        assertEquals("Attempt to read past end of input", thrown.getMessage());
    }

    @Test
    public void testReadMixedCollection() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(ByteBuffer.allocate(9)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put((byte) 1)
                .putInt(7)
                .putInt(6)
                .array());
        Geometry[] expected = {
            line(2 * BATCH + 1, EnumSet.allOf(Ordinate.class)),
            line(4, XY),
            point(XY),
            line(2 * BATCH + 1, EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.M)),
            line(0, XY),
            line(BATCH, XY)
        };
        List<EnumSet<Ordinate>> flags =
                List.of(EnumSet.allOf(Ordinate.class), XY, XY, EnumSet.of(Ordinate.X, Ordinate.Y, Ordinate.M), XY, XY);
        for (int i = 0; i < expected.length; i++) {
            bytes.write(write(
                    expected[i],
                    flags.get(i),
                    i % 2 == 0 ? ByteOrderValues.BIG_ENDIAN : ByteOrderValues.LITTLE_ENDIAN));
        }
        GeometryCollection read = (GeometryCollection) new WKBReader(LITE).read(bytes.toByteArray());
        assertEquals(expected.length, read.getNumGeometries());
        for (int i = 0; i < expected.length; i++) {
            assertSequence(sequence(expected[i]), sequence(read.getGeometryN(i)));
        }
    }

    @Test
    public void testReadPoints() throws Exception {
        WKBReader reader = new WKBReader(LITE);
        for (EnumSet<Ordinate> ordinates : ORDINATE_SETS) {
            Point source = point(ordinates);
            for (int order : new int[] {ByteOrderValues.LITTLE_ENDIAN, ByteOrderValues.BIG_ENDIAN}) {
                Point read = (Point) reader.read(write(source, ordinates, order));
                assertSequence(source.getCoordinateSequence(), read.getCoordinateSequence());
            }
        }
    }

    private static CoordinateSequence sequence(Geometry geometry) {
        return geometry instanceof Point point
                ? point.getCoordinateSequence()
                : ((LineString) geometry).getCoordinateSequence();
    }

    private static Point point(EnumSet<Ordinate> ordinates) {
        LiteCoordinateSequence seq =
                new LiteCoordinateSequence(1, ordinates.size(), ordinates.contains(Ordinate.M) ? 1 : 0);
        System.arraycopy(expected(ordinates), 0, seq.getArray(), 0, ordinates.size());
        return LITE.createPoint(seq);
    }

    private static LineString line(int size, EnumSet<Ordinate> ordinates) {
        LiteCoordinateSequence seq =
                new LiteCoordinateSequence(size, ordinates.size(), ordinates.contains(Ordinate.M) ? 1 : 0);
        for (int i = 0; i < seq.getArray().length; i++) {
            seq.getArray()[i] = (i % 2 == 0 ? 1 : -1) * (i + 0.123456789012345);
        }
        return LITE.createLineString(seq);
    }

    private static void assertSequence(CoordinateSequence expected, CoordinateSequence actual) {
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.getDimension(), actual.getDimension());
        assertEquals(expected.getMeasures(), actual.getMeasures());
        assertThat(actual, instanceOf(LiteCoordinateSequence.class));
        assertArrayEquals(
                ((LiteCoordinateSequence) expected).getArray(), ((LiteCoordinateSequence) actual).getArray(), 0d);
    }

    /** Reads a two polygon multipolygon in both byte orders, and checks every ordinate bit by bit. */
    private static void assertReadsExactly(EnumSet<Ordinate> ordinates) throws Exception {
        int dimension = ordinates.size();
        GeometryFactory gf = new GeometryFactory();
        for (int byteOrder : new int[] {ByteOrderValues.LITTLE_ENDIAN, ByteOrderValues.BIG_ENDIAN}) {
            MultiPolygon written = gf.createMultiPolygon(
                    new Polygon[] {gf.createPolygon(ring(ordinates)), gf.createPolygon(ring(ordinates))});
            byte[] wkb = write(written, ordinates, byteOrder);

            MultiPolygon read = (MultiPolygon) new WKBReader(LITE).read(wkb);

            assertEquals(2, read.getNumGeometries());
            for (int i = 0; i < 2; i++) {
                CoordinateSequence seq =
                        ((Polygon) read.getGeometryN(i)).getExteriorRing().getCoordinateSequence();
                assertThat(seq, instanceOf(LiteCoordinateSequence.class));
                assertEquals(dimension, seq.getDimension());
                assertEquals(ordinates.contains(Ordinate.M) ? 1 : 0, seq.getMeasures());
                assertArrayEquals(expected(ordinates), ((LiteCoordinateSequence) seq).getArray(), 0d);
            }
        }
    }

    private static double[] expected(EnumSet<Ordinate> ordinates) {
        int dimension = ordinates.size();
        double[] result = new double[XYZM.length * dimension];
        for (int i = 0; i < XYZM.length; i++) {
            int next = i * dimension;
            result[next++] = XYZM[i][0];
            result[next++] = XYZM[i][1];
            if (ordinates.contains(Ordinate.Z)) result[next++] = XYZM[i][2];
            if (ordinates.contains(Ordinate.M)) result[next] = XYZM[i][3];
        }
        return result;
    }

    /** Builds a ring with exactly the given ordinates: the writer takes the third one as Z or M by position. */
    private static CoordinateSequence ring(EnumSet<Ordinate> ordinates) {
        int measures = ordinates.contains(Ordinate.M) ? 1 : 0;
        LiteCoordinateSequence seq = new LiteCoordinateSequence(XYZM.length, ordinates.size(), measures);
        double[] values = expected(ordinates);
        System.arraycopy(values, 0, seq.getArray(), 0, values.length);
        return seq;
    }

    private static byte[] xyPolygon() {
        return write(new GeometryFactory().createPolygon(ring(XY)), XY, ByteOrderValues.LITTLE_ENDIAN);
    }

    private static byte[] write(Geometry g, EnumSet<Ordinate> ordinates, int byteOrder) {
        WKBWriter writer = new WKBWriter(ordinates.size(), byteOrder);
        writer.setOutputOrdinates(ordinates);
        return writer.write(g);
    }
}
