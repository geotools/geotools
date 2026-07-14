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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.Callable;

/** Default {@link GranuleImageCache}: a single-JVM, heap-backed pool over a Guava {@link Cache}. Thread-safe. */
public final class GuavaGranuleImageCache implements GranuleImageCache {

    private volatile Cache<ImageCacheKey, BufferedImage> cache;
    private volatile long maximumSizeBytes;
    private volatile long defaultThresholdBytes;

    public GuavaGranuleImageCache(long maximumSizeBytes, long defaultThresholdBytes) {
        reconfigure(maximumSizeBytes, defaultThresholdBytes);
    }

    @Override
    public synchronized void reconfigure(long maximumSizeBytes, long defaultThresholdBytes) {
        this.defaultThresholdBytes = defaultThresholdBytes;
        // Guava fixes the maximum weight at build time, so a size change rebuilds the cache, dropping the current
        // entries and freeing their heap at once; the threshold is a plain field, applied in place.
        if (cache == null || this.maximumSizeBytes != maximumSizeBytes) {
            this.maximumSizeBytes = maximumSizeBytes;
            Weigher<ImageCacheKey, BufferedImage> weigher = (key, image) -> imageBytes(image);
            this.cache = CacheBuilder.newBuilder()
                    .maximumWeight(maximumSizeBytes)
                    .weigher(weigher)
                    .build();
        }
    }

    private static int imageBytes(BufferedImage image) {
        // getPixelSize is total bits across all bands, so it already accounts for the band count
        int bytesPerPixel = (image.getColorModel().getPixelSize() + 7) / 8;
        long bytes = (long) image.getWidth() * image.getHeight() * bytesPerPixel;
        return (int) Math.min(bytes, Integer.MAX_VALUE);
    }

    @Override
    public boolean isEnabled() {
        return maximumSizeBytes > 0;
    }

    @Override
    public long size() {
        cache.cleanUp();
        return cache.size();
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public void invalidateGranule(URL granuleUrl) {
        cache.asMap().keySet().removeIf(key -> key.sameGranule(granuleUrl));
    }

    @Override
    public void invalidateMosaic(String mosaicId) {
        cache.asMap().keySet().removeIf(key -> key.sameMosaic(mosaicId));
    }

    @Override
    public long getMaximumSizeBytes() {
        return maximumSizeBytes;
    }

    @Override
    public long getDefaultThresholdBytes() {
        return defaultThresholdBytes;
    }

    @Override
    public BufferedImage get(ImageCacheKey key) {
        return cache.getIfPresent(key);
    }

    @Override
    public BufferedImage getOrLoad(ImageCacheKey key, Callable<BufferedImage> loader) throws Exception {
        try {
            return cache.get(key, loader);
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof MissingRasterException) {
                return null;
            }
            throw e;
        }
    }
}
