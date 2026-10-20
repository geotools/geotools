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
 *
 */
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;

/**
 * Checks that the attribute readers cached per thread by {@link PostGISDialect} do not keep the last geometry bytes
 * reachable once a read is over.
 */
public class AttributeIOByteRetentionTest {

    static final String WKT_POLYGON = "POLYGON((1 2,3 4,5 6,1 2))";

    /** {@link #WKT_POLYGON} as TWKB, from {@link TWKBReaderTest} */
    static final String TWKB_POLYGON = "a3000104c09a0c80b51880b51880b51880b51880b518ffe930ffe930";

    /** TWKB header with geometry type 15, which does not exist */
    static final byte[] TWKB_BAD_TYPE = {0x0f, 0x00};

    /** Big endian WKB with geometry type 255, which does not exist */
    static final byte[] WKB_BAD_TYPE = {0x00, 0x00, 0x00, 0x00, (byte) 0xff};

    @Test
    public void testTWKBRelease() throws Exception {
        TWKBAttributeIO io = new TWKBAttributeIO();
        io.inStream = new TrackingStream();
        assertEquals(polygon(), io.read(resultSet(WKBReader.hexToBytes(TWKB_POLYGON)), 1, Polygon.class));
        assertReleased(io.inStream);
    }

    @Test
    public void testTWKBReleaseOnError() throws Exception {
        TWKBAttributeIO io = new TWKBAttributeIO();
        io.inStream = new TrackingStream();
        assertThrows(IOException.class, () -> io.read(resultSet(TWKB_BAD_TYPE), 1, Polygon.class));
        assertReleased(io.inStream);
    }

    @Test
    public void testWKBRelease() throws Exception {
        WKBAttributeIO io = plainWKB();
        io.inStream = new TrackingStream();
        assertEquals(polygon(), io.read(resultSet(new WKBWriter().write(polygon())), 1));
        assertReleased(io.inStream);
    }

    @Test
    public void testWKBReleaseOnError() throws Exception {
        WKBAttributeIO io = plainWKB();
        io.inStream = new TrackingStream();
        assertThrows(IOException.class, () -> io.read(resultSet(WKB_BAD_TYPE), 1));
        assertReleased(io.inStream);
    }

    /** Checks the same instance still decodes after successful and failed reads, as it does when cached. */
    @Test
    public void testReuse() throws Exception {
        Geometry expected = polygon();
        byte[] twkbBytes = WKBReader.hexToBytes(TWKB_POLYGON);
        byte[] wkbBytes = new WKBWriter().write(expected);
        TWKBAttributeIO twkb = new TWKBAttributeIO();
        WKBAttributeIO wkb = plainWKB();

        for (int i = 0; i < 2; i++) {
            assertEquals(expected, twkb.read(resultSet(twkbBytes), 1, Polygon.class));
            assertEquals(expected, wkb.read(resultSet(wkbBytes), 1));
            assertThrows(IOException.class, () -> twkb.read(resultSet(TWKB_BAD_TYPE), 1, Polygon.class));
            assertThrows(IOException.class, () -> wkb.read(resultSet(WKB_BAD_TYPE), 1));
        }
        assertNull(twkb.read(resultSet(null), 1, Polygon.class));
        assertNull(wkb.read(resultSet(null), 1));
    }

    /** Disables base64, so the result set bytes are parsed as they are. */
    private static WKBAttributeIO plainWKB() {
        WKBAttributeIO io = new WKBAttributeIO();
        io.setBase64EncodingEnabled(false);
        return io;
    }

    private static Geometry polygon() throws ParseException {
        return new WKTReader().read(WKT_POLYGON);
    }

    /** Returns a result set whose getBytes returns the given array. */
    private static ResultSet resultSet(byte[] bytes) {
        return (ResultSet) Proxy.newProxyInstance(
                AttributeIOByteRetentionTest.class.getClassLoader(), new Class<?>[] {ResultSet.class}, (p, m, a) -> {
                    if ("getBytes".equals(m.getName())) return bytes;
                    throw new UnsupportedOperationException(m.getName());
                });
    }

    /** Checks the stream was last set to an empty array, so it no longer holds the geometry bytes. */
    private static void assertReleased(ByteArrayInStream stream) {
        assertEquals(0, ((TrackingStream) stream).last.length);
    }

    /** Remembers the last array set, which ByteArrayInStream does not expose. */
    static class TrackingStream extends ByteArrayInStream {
        byte[] last;

        TrackingStream() {
            super(new byte[0]);
        }

        @Override
        public void setBytes(byte[] buffer) {
            last = buffer;
            super.setBytes(buffer);
        }
    }
}
