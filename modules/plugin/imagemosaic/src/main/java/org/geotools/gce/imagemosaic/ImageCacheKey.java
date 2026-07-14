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

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Identifies a whole-granule read of a single source image index in the shared {@link GranuleImageCache}. Carries the
 * owning mosaic id so the pool can drop all of a mosaic's entries at once when its reader is disposed (store
 * reload/remove/reset), keeping heap in step with the mosaic's lifecycle.
 */
class ImageCacheKey {

    private static final Logger LOGGER = Logging.getLogger(ImageCacheKey.class);

    private final String mosaicId;

    // external string form, not a URL: URL#hashCode/#equals can do a blocking DNS lookup, and this key is hashed
    // and compared on every cache access, including for remote (COG) granules
    private final String granuleUrlSpec;

    private final int imageIndex;
    private final int[] bands;

    ImageCacheKey(String mosaicId, URL granuleUrl, int imageIndex, int[] bands) {
        this.mosaicId = mosaicId;
        this.granuleUrlSpec = granuleUrl.toExternalForm();
        this.imageIndex = imageIndex;
        this.bands = bands;
    }

    /**
     * Whether a whole-granule read of this size is small enough to cache: its estimated decoded bytes must not exceed
     * the threshold (the per-request {@code thresholdOverrideKB} when positive, otherwise the pool default).
     */
    static boolean isCacheable(
            GranuleImageCache cache,
            int thresholdOverrideKB,
            Rectangle rasterDimensions,
            int[] bands,
            SampleModel sampleModel) {
        int numBands = bands != null ? bands.length : sampleModel.getNumBands();
        int bytesPerSample = DataBuffer.getDataTypeSize(sampleModel.getDataType()) / 8;
        long estimatedBytes = (long) rasterDimensions.width * rasterDimensions.height * numBands * bytesPerSample;
        long thresholdBytes = thresholdOverrideKB > 0 ? thresholdOverrideKB * 1024L : cache.getDefaultThresholdBytes();
        boolean cacheable = estimatedBytes <= thresholdBytes;
        if (!cacheable) {
            LOGGER.fine(() -> "Granule not cached, estimated " + estimatedBytes + " bytes exceeds cache threshold of "
                    + thresholdBytes + " bytes");
        }
        return cacheable;
    }

    /** Returns whether this key refers to the given granule, without the DNS cost of {@link URL#equals(Object)}. */
    boolean sameGranule(URL granuleUrl) {
        return granuleUrl != null && granuleUrlSpec.equals(granuleUrl.toExternalForm());
    }

    /** Returns whether this key belongs to the given mosaic. */
    boolean sameMosaic(String mosaicId) {
        return Objects.equals(this.mosaicId, mosaicId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageCacheKey other)) return false;
        return imageIndex == other.imageIndex
                && Objects.equals(mosaicId, other.mosaicId)
                && Objects.equals(granuleUrlSpec, other.granuleUrlSpec)
                && Arrays.equals(bands, other.bands);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mosaicId, granuleUrlSpec, imageIndex);
        return 31 * result + Arrays.hashCode(bands);
    }

    @Override
    public String toString() {
        return "ImageCacheKey{mosaicId=" + mosaicId + ", granuleUrl=" + granuleUrlSpec + ", imageIndex=" + imageIndex
                + ", bands=" + Arrays.toString(bands) + '}';
    }
}
