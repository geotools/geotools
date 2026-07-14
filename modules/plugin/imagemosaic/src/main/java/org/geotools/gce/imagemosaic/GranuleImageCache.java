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

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * A bounded, shared pool of decoded whole-granule images that {@link ImageMosaicReader}s can reuse across requests,
 * sized by total decoded bytes with LRU eviction. Built and owned by the caller, injected into readers at construction
 * time through {@link org.geotools.util.factory.Hints#GRANULE_IMAGE_CACHE} and enabled via the
 * {@link ImageMosaicFormat#CACHE_GRANULES} read parameter. Implementations must be thread-safe.
 *
 * <p>Entries are keyed by owning mosaic plus granule URL, image index and band selection, so the pool can shed a
 * mosaic's whole footprint at once through {@link #invalidateMosaic(String)} when its reader is disposed (store reload,
 * remove or reset): heap then tracks the mosaic's lifecycle instead of lingering until the LRU bound reclaims it. A
 * stale single granule (an in-place overwrite, a re-harvest, a delete) is dropped with {@link #invalidateGranule(URL)},
 * and {@link #invalidateAll()} clears the pool. The band selection is part of the key because it's typically associated
 * with multispectral/hyperspectral images where there are too many bands to cache the whole set.
 *
 * <p>A cached BufferedImage is shared, as-is, by every request that hits it, so the read path must treat it strictly
 * read-only: no consumer may write into its raster in place, or it would corrupt other requests' and other mosaics'
 * reads.
 */
public interface GranuleImageCache {

    /**
     * Applies new sizing while keeping this instance's identity, so readers holding it through
     * {@link org.geotools.util.factory.Hints#GRANULE_IMAGE_CACHE} see the change with no rebuild on their side. A
     * change in total size may drop the current entries and free their heap at once; the threshold is applied in place.
     */
    void reconfigure(long maximumSizeBytes, long defaultThresholdBytes);

    /** Whether caching is on: a non-positive max size means the pool is present but denies caching. */
    boolean isEnabled();

    /** Number of granule images currently held. */
    long size();

    /** Drops all cached granule images. */
    void invalidateAll();

    /** Drops every cached read of the given granule, whatever the mosaic, image index or band selection. */
    void invalidateGranule(URL granuleUrl);

    /** Drops every entry owned by the given mosaic, called when its reader is disposed to free heap at once. */
    void invalidateMosaic(String mosaicId);

    /** Maximum size of the pool, in bytes. */
    long getMaximumSizeBytes();

    /** Largest decoded granule eligible for caching, in bytes (a read parameter can override it). */
    long getDefaultThresholdBytes();

    /** Returns the cached image for the key, or {@code null} on a miss; never loads. */
    BufferedImage get(ImageCacheKey key);

    /**
     * Returns the cached image for the key, loading it once atomically if absent. Returns {@code null} if the loader
     * signals nothing was read by throwing {@link MissingRasterException}; any other loader failure propagates.
     */
    BufferedImage getOrLoad(ImageCacheKey key, Callable<BufferedImage> loader) throws Exception;

    /**
     * Thrown by a {@link #getOrLoad} loader to signal the read produced nothing; {@link #getOrLoad} maps it to a
     * {@code null} result instead of caching.
     */
    class MissingRasterException extends RuntimeException {}
}
