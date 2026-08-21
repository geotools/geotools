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
package org.geotools.renderer.crs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.util.logging.Logging;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

/** Checks that the valid area cache keys on the projection parameters, and only on those. */
public class GeosValidAreaCacheTest {

    private static final Logger LOGGER = Logging.getLogger(GeosValidAreaCacheTest.class);

    private static final double GEOSTATIONARY_HEIGHT = 35785831;

    /** Warm cache lookups performed by {@link #benchmarkWarmLookup()}. */
    private static final int BENCHMARK_LOOKUPS = 200_000;

    private final GeosValidAreaCache cache = new GeosValidAreaCache();

    /**
     * Two factories have separate canonicalization pools, so they return distinct but equal CRS instances: those must
     * share the cache entry, or the valid area gets recomputed per instance.
     */
    @Test
    public void testEqualCrsShareEntry() throws Exception {
        String wkt = wkt(0, GEOSTATIONARY_HEIGHT);
        CoordinateReferenceSystem first = new ReferencingObjectFactory().createFromWKT(wkt);
        CoordinateReferenceSystem second = new ReferencingObjectFactory().createFromWKT(wkt);
        assertNotSame(first, second);
        assertEquals(first, second);

        Geometry area = cache.getValidAreaInGeosCrs(first);
        assertSame(area, cache.getValidAreaInGeosCrs(second));
    }

    /** The disc follows the satellite, so the central meridian has to be part of the key. */
    @Test
    public void testCentralMeridianNotShared() throws Exception {
        CoordinateReferenceSystem greenwichCrs = geos(0, GEOSTATIONARY_HEIGHT);
        CoordinateReferenceSystem shiftedCrs = geos(60, GEOSTATIONARY_HEIGHT);
        Geometry greenwich = cache.getValidAreaInGeosCrs(greenwichCrs);
        Geometry shifted = cache.getValidAreaInGeosCrs(shiftedCrs);

        // the repeat lookups matter: assertNotSame alone would also pass on a cache that never
        // caches and rebuilds the valid area on every call
        assertNotSame(greenwich, shifted);
        assertSame(greenwich, cache.getValidAreaInGeosCrs(greenwichCrs));
        assertSame(shifted, cache.getValidAreaInGeosCrs(shiftedCrs));

        assertEquals(0, greenwich.getCentroid().getX(), 0.5);
        assertEquals(60, shifted.getCentroid().getX(), 0.5);
        assertEquals(81.3, greenwich.getEnvelopeInternal().getMaxX(), 0.5);
        assertEquals(141.3, shifted.getEnvelopeInternal().getMaxX(), 0.5);
    }

    /** A lower satellite sees a smaller disc, so the height has to be part of the key too. */
    @Test
    public void testSatelliteHeightNotShared() throws Exception {
        CoordinateReferenceSystem highCrs = geos(0, GEOSTATIONARY_HEIGHT);
        CoordinateReferenceSystem lowCrs = geos(0, 5000);
        Geometry high = cache.getValidAreaInGeosCrs(highCrs);
        Geometry low = cache.getValidAreaInGeosCrs(lowCrs);

        assertNotSame(high, low);
        assertSame(high, cache.getValidAreaInGeosCrs(highCrs));
        assertSame(low, cache.getValidAreaInGeosCrs(lowCrs));

        assertEquals(81.3, high.getEnvelopeInternal().getMaxX(), 0.5);
        assertEquals(2.3, low.getEnvelopeInternal().getMaxX(), 0.1);
    }

    /**
     * Measures the per lookup cost of the cache key on an already populated entry, which is what the rendering path
     * pays on every request. Not an assertion, and not part of the build: run it from the IDE against this and the
     * previous key strategy, and compare the printed times.
     *
     * <p>Uses a single CRS instance, the common case: readers and map contexts hand out the same canonicalized instance
     * for the life of a request.
     *
     * <p>On a 12 core Ryzen 9 AI: 9309 ms with the normalized WKT key, 26 ms with the current CRS key, that is 46.5 us
     * against 0.13 us per lookup. Absolute numbers depend on the machine, the ratio is the point.
     */
    @Ignore("manual benchmark, run it from the IDE")
    @Test
    public void benchmarkWarmLookup() throws Exception {
        CoordinateReferenceSystem geos = geos(0, GEOSTATIONARY_HEIGHT);
        timeLookups(geos); // warmup, also populates the entry
        long millis = TimeUnit.NANOSECONDS.toMillis(timeLookups(geos));
        LOGGER.info(BENCHMARK_LOOKUPS + " warm lookups: " + millis + " ms");
    }

    /** Wall clock nanos for {@link #BENCHMARK_LOOKUPS} lookups of an already cached valid area. */
    private long timeLookups(CoordinateReferenceSystem geos) throws Exception {
        long start = System.nanoTime();
        Geometry last = null;
        for (int i = 0; i < BENCHMARK_LOOKUPS; i++) {
            last = cache.getValidAreaInGeosCrs(geos);
        }
        assertNotNull(last); // so the loop cannot be optimized away
        return System.nanoTime() - start;
    }

    private static CoordinateReferenceSystem geos(double centralMeridian, double satelliteHeight) throws Exception {
        return CRS.parseWKT(wkt(centralMeridian, satelliteHeight));
    }

    private static String wkt(double centralMeridian, double satelliteHeight) {
        return "PROJCS[\"GEOS\", GEOGCS[\"WGS 84\", DATUM[\"WGS84\", "
                + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]], "
                + "PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\", 0.017453292519943295]], "
                + "PROJECTION[\"GEOS\"], "
                + "PARAMETER[\"central_meridian\", "
                + centralMeridian
                + "], PARAMETER[\"satellite_height\", "
                + satelliteHeight
                + "], UNIT[\"m\", 1.0]]";
    }
}
