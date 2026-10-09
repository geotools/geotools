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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GranuleRemovalPolicy;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.image.test.ImageAssert;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * End-to-end coverage of the injected {@link GranuleImageCache} enabled through
 * {@link ImageMosaicFormat#CACHE_GRANULES}.
 */
public class GranuleImageCacheTest {

    private static final long KB = 1024;
    private static final long MB = 1024 * 1024;

    @Test
    public void testCachePopulatedWhenEnabled() throws Exception {
        GranuleImageCache cache = defaultCache();
        withReader(reader(cache), r -> {
            read(r, true, -1).dispose(true);
            assertTrue("cache should hold at least one granule", cache.size() > 0);
        });
    }

    @Test
    public void testCacheUntouchedWhenParamOff() throws Exception {
        GranuleImageCache cache = defaultCache();
        withReader(reader(cache), r -> {
            read(r, false, -1).dispose(true);
            assertEquals(0, cache.size());
        });
    }

    @Test
    public void testDisabledPoolDeniesCaching() throws Exception {
        // pool present but with a 0 budget: caching must be off even with the param on
        GranuleImageCache cache = new GuavaGranuleImageCache(0, 0);
        assertFalse(cache.isEnabled());
        withReader(reader(cache), r -> {
            read(r, true, -1).dispose(true);
            assertEquals(0, cache.size());
        });
    }

    @Test
    public void testNoHintReadsWithoutCaching() throws Exception {
        withReader(reader(null), r -> {
            // param on, but no pool injected: must still read fine
            read(r, true, -1).dispose(true);
        });
    }

    @Test
    public void testThresholdOverrideExcludesGranules() throws Exception {
        GranuleImageCache cache = defaultCache();
        withReader(reader(cache), r -> {
            // 1 KB per-granule ceiling is smaller than any decoded tile -> nothing cached
            read(r, true, 1).dispose(true);
            assertEquals(0, cache.size());
        });
    }

    @Test
    public void testGranuleTooLargeForPoolDefaultNotCached() throws Exception {
        // pool default of 1 KB is smaller than any decoded tile, and the read keeps the default (-1) -> nothing cached
        GranuleImageCache cache = new GuavaGranuleImageCache(64 * MB, KB);
        withReader(reader(cache), r -> {
            read(r, true, -1).dispose(true);
            assertEquals(0, cache.size());
        });
    }

    @Test
    public void testDirectReadPopulatesCacheAndMatches() throws Exception {
        // ReadType.DIRECT_READ yields a BufferedImage straight away: readCached must cache it without materializing,
        // and still match an uncached direct read
        GridCoverage2D uncached = withReader(reader(null), r -> {
            return readDirect(r, false);
        });

        GranuleImageCache cache = defaultCache();
        ImageMosaicReader reader = reader(cache);
        try {
            GridCoverage2D cached = readDirect(reader, true);
            assertTrue("direct-read granules should populate the cache", cache.size() > 0);
            ImageAssert.assertEquals(uncached.getRenderedImage(), cached.getRenderedImage(), 0);
            cached.dispose(true);
        } finally {
            reader.dispose();
            uncached.dispose(true);
        }
    }

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testCacheHitSkipsFileOpen() throws Exception {
        File dir = tempFolder.newFolder("rgb_openskip");
        FileUtils.copyDirectory(TestData.file(this, "rgb"), dir);

        GranuleImageCache cache = defaultCache();
        withReader(new ImageMosaicFormat().getReader(dir, new Hints(Hints.GRANULE_IMAGE_CACHE, cache)), r -> {
            GridCoverage2D warm = read(r, true, -1);
            assertTrue(cache.size() > 0);

            // delete every granule file: a read that still opened files would now drop those granules and change
            // the output, so an identical result proves the second read was served entirely from the cache
            File[] granules = dir.listFiles((d, n) -> n.endsWith(".png"));
            assertTrue(granules != null && granules.length > 0);
            for (File f : granules) {
                assertTrue("could not delete " + f, f.delete());
            }

            GridCoverage2D served = read(r, true, -1);
            ImageAssert.assertEquals(warm.getRenderedImage(), served.getRenderedImage(), 0);
            warm.dispose(true);
            served.dispose(true);
        });
    }

    @Test
    public void testReharvestInvalidatesGranule() throws Exception {
        File dir = tempFolder.newFolder("rgb_reharvest");
        FileUtils.copyDirectory(TestData.file(this, "rgb"), dir);

        GranuleImageCache cache = defaultCache();
        withReader(new ImageMosaicFormat().getReader(dir, new Hints(Hints.GRANULE_IMAGE_CACHE, cache)), r -> {
            read(r, true, -1).dispose(true);
            long populated = cache.size();
            assertTrue("expected several granules cached", populated > 1);

            // re-harvesting a granule signals its file may have changed: its cached image must be dropped
            r.harvest(null, new File(dir, "global_mosaic_0.png"), null);
            assertEquals(populated - 1, cache.size());
        });
    }

    @Test
    public void testDeleteGranuleInvalidatesCache() throws Exception {
        File dir = tempFolder.newFolder("rgb_delete");
        FileUtils.copyDirectory(TestData.file(this, "rgb"), dir);

        GranuleImageCache cache = defaultCache();
        withReader(new ImageMosaicFormat().getReader(dir, new Hints(Hints.GRANULE_IMAGE_CACHE, cache)), r -> {
            read(r, true, -1).dispose(true);
            long populated = cache.size();
            assertTrue(populated > 1);

            // removal with GranuleRemovalPolicy.ALL deletes the file, so its cached image must be dropped too
            GranuleStore store = (GranuleStore) r.getGranules(r.getGridCoverageNames()[0], false);
            Hints purge = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.ALL);
            store.removeGranules(ECQL.toFilter("location = 'global_mosaic_0.png'"), purge);
            assertEquals(populated - 1, cache.size());
        });
    }

    @Test
    public void testInvalidateAllClearsCache() throws Exception {
        GranuleImageCache cache = defaultCache();
        withReader(reader(cache), r -> {
            read(r, true, -1).dispose(true);
            assertTrue(cache.size() > 0);
            cache.invalidateAll();
            assertEquals(0, cache.size());
        });
    }

    @Test
    public void testInvalidateGranuleDropsOnlyThatGranule() throws Exception {
        GranuleImageCache cache = defaultCache();
        URL granuleA = new URL("file:/mosaic/a.tif");
        URL granuleB = new URL("file:/mosaic/b.tif");
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        cache.getOrLoad(new ImageCacheKey("m", granuleA, 0, null), () -> dummy);
        cache.getOrLoad(new ImageCacheKey("m", granuleA, 1, null), () -> dummy);
        cache.getOrLoad(new ImageCacheKey("m", granuleB, 0, null), () -> dummy);
        assertEquals(3, cache.size());

        cache.invalidateGranule(granuleA);
        assertEquals("only granule A's entries should be dropped", 1, cache.size());

        cache.invalidateGranule(new URL("file:/mosaic/unknown.tif"));
        assertEquals("an unrelated URL must not drop any entry", 1, cache.size());

        assertTrue(cache.size() > 0);
    }

    @Test
    public void testReconfigureThresholdKeepsEntries() throws Exception {
        GranuleImageCache cache = defaultCache();
        cache.getOrLoad(
                new ImageCacheKey("m", new URL("file:/g.tif"), 0, null),
                () -> new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR));
        assertEquals(1, cache.size());

        // threshold-only change: same budget, entries survive
        cache.reconfigure(64 * MB, 5 * MB);
        assertEquals(1, cache.size());
        assertEquals(5 * MB, cache.getDefaultThresholdBytes());
    }

    @Test
    public void testReconfigureResizeDropsEntries() throws Exception {
        GranuleImageCache cache = defaultCache();
        cache.getOrLoad(
                new ImageCacheKey("m", new URL("file:/g.tif"), 0, null),
                () -> new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR));
        assertEquals(1, cache.size());

        // size change rebuilds the backing cache, dropping current entries and freeing their heap
        cache.reconfigure(32 * MB, 10 * MB);
        assertEquals(0, cache.size());
        assertEquals(32 * MB, cache.getMaximumSizeBytes());
    }

    @Test
    public void testInvalidateMosaicDropsOnlyThatMosaic() throws Exception {
        GranuleImageCache cache = defaultCache();
        URL granule = new URL("file:/data/g.tif");
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        cache.getOrLoad(new ImageCacheKey("mosaicA", granule, 0, null), () -> dummy);
        cache.getOrLoad(new ImageCacheKey("mosaicA", granule, 1, null), () -> dummy);
        cache.getOrLoad(new ImageCacheKey("mosaicB", granule, 0, null), () -> dummy);
        assertEquals(3, cache.size());

        cache.invalidateMosaic("mosaicA");
        assertEquals("only mosaicA's entries should be dropped", 1, cache.size());
    }

    @Test
    public void testReaderDisposeDropsItsCache() throws Exception {
        GranuleImageCache cache = defaultCache();
        ImageMosaicReader reader = reader(cache);
        read(reader, true, -1).dispose(true);
        assertTrue(cache.size() > 0);

        // disposing the reader (e.g. on a store reload, remove or reset) must free its cached granules
        reader.dispose();
        assertEquals(0, cache.size());
    }

    @Test
    public void testCachedReadMatchesUncached() throws Exception {
        GridCoverage2D uncached = withReader(reader(null), r -> {
            return read(r, false, -1);
        });

        GranuleImageCache cache = defaultCache();
        ImageMosaicReader reader = reader(cache);
        try {
            RenderedImage expected = uncached.getRenderedImage();
            // first read populates the cache, second is served from it: both must match the uncached output
            GridCoverage2D miss = read(reader, true, -1);
            ImageAssert.assertEquals(expected, miss.getRenderedImage(), 0);
            GridCoverage2D hit = read(reader, true, -1);
            ImageAssert.assertEquals(expected, hit.getRenderedImage(), 0);
            assertTrue(cache.size() > 0);
            miss.dispose(true);
            hit.dispose(true);
        } finally {
            reader.dispose();
            uncached.dispose(true);
        }
    }

    /** A 64 MB pool with a 10 MB per-granule default, big enough that the test granules are always cached. */
    private static GranuleImageCache defaultCache() {
        return new GuavaGranuleImageCache(64 * MB, 10 * MB);
    }

    @FunctionalInterface
    private interface ReaderTask {
        void accept(ImageMosaicReader reader) throws Exception;
    }

    /** Runs the task against the reader and disposes the reader afterwards, even on failure. */
    private static void withReader(ImageMosaicReader reader, ReaderTask task) throws Exception {
        try {
            task.accept(reader);
        } finally {
            reader.dispose();
        }
    }

    @FunctionalInterface
    private interface ReaderFunction<R> {
        R apply(ImageMosaicReader reader) throws Exception;
    }

    /**
     * Returns the task result, disposing the reader afterwards even on failure; the result must not hold the reader.
     */
    private static <R> R withReader(ImageMosaicReader reader, ReaderFunction<R> task) throws Exception {
        try {
            return task.apply(reader);
        } finally {
            reader.dispose();
        }
    }

    private ImageMosaicReader reader(GranuleImageCache cache) throws Exception {
        URL rgb = TestData.url(this, "rgb");
        Hints hints = cache == null ? null : new Hints(Hints.GRANULE_IMAGE_CACHE, cache);
        return new ImageMosaicFormat().getReader(rgb, hints);
    }

    private GridCoverage2D read(ImageMosaicReader reader, boolean cacheGranules, int thresholdKB) throws Exception {
        ParameterValue<Boolean> cache = ImageMosaicFormat.CACHE_GRANULES.createValue();
        cache.setValue(cacheGranules);
        ParameterValue<Integer> threshold = ImageMosaicFormat.GRANULE_CACHE_THRESHOLD_KB.createValue();
        threshold.setValue(thresholdKB);
        return reader.read(new GeneralParameterValue[] {cache, threshold});
    }

    private GridCoverage2D readDirect(ImageMosaicReader reader, boolean cacheGranules) throws Exception {
        ParameterValue<Boolean> cache = ImageMosaicFormat.CACHE_GRANULES.createValue();
        cache.setValue(cacheGranules);
        ParameterValue<Boolean> useImageN = AbstractGridFormat.USE_IMAGEN_IMAGEREAD.createValue();
        useImageN.setValue(false); // ReadType.DIRECT_READ
        return reader.read(new GeneralParameterValue[] {cache, useImageN});
    }
}
