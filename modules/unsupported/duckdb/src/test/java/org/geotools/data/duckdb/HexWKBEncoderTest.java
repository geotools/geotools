/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;

public class HexWKBEncoderTest {

    @Test
    public void testLinearRingIsEncodedAsLineString() throws Exception {
        LinearRing ring = (LinearRing) new WKTReader().read("LINEARRING (0 0, 1 0, 1 1, 0 0)");

        StringBuilder encoded = new StringBuilder();
        HexWKBEncoder.encode(ring, encoded);

        Geometry decoded = new WKBReader().read(HexFormat.of().parseHex(encoded));
        assertTrue(decoded instanceof LineString);
    }

    @Test
    public void testConcurrentEncodingProducesStableHexOutput() throws Exception {
        Point point = (Point) new WKTReader().read("POINT (1 2)");
        Geometry polygon = new WKTReader().read("POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0))");

        String expectedPoint = encodeWithFreshWriter(point);
        String expectedPolygon = encodeWithFreshWriter(polygon);

        int threads = 8;
        int iterationsPerThread = 2_000;
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            Callable<Void> pointTask = () -> {
                start.await(5, TimeUnit.SECONDS);
                for (int i = 0; i < iterationsPerThread; i++) {
                    StringBuilder out = new StringBuilder();
                    HexWKBEncoder.encode(point, out);
                    assertEquals(expectedPoint, out.toString());
                }
                return null;
            };
            Callable<Void> polygonTask = () -> {
                start.await(5, TimeUnit.SECONDS);
                for (int i = 0; i < iterationsPerThread; i++) {
                    StringBuilder out = new StringBuilder();
                    HexWKBEncoder.encode(polygon, out);
                    assertEquals(expectedPolygon, out.toString());
                }
                return null;
            };

            List<Future<Void>> futures = new ArrayList<>(threads);
            for (int i = 0; i < threads; i++) {
                futures.add(executor.submit(i % 2 == 0 ? pointTask : polygonTask));
            }
            start.countDown();
            for (Future<Void> future : futures) {
                future.get(30, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }
    }

    private static String encodeWithFreshWriter(Geometry geometry) throws IOException {
        WKBWriter writer = new WKBWriter();
        StringBuilder out = new StringBuilder();
        writer.write(geometry, (buffer, length) -> HexFormat.of().formatHex(out, buffer, 0, length));
        return out.toString();
    }
}
