/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import java.util.OptionalDouble;

/**
 * Hook to provide a requested scale denominator for
 * {@link VectorTilesFeatureSource#determineZoomLevel(org.geotools.api.data.Query)}, superseding the
 * {@code GEOMETRY_GENERALIZATION} and {@code GEOMETRY_DISTANCE} hints provided by the renderer.
 *
 * <p>Rationale being that {@code StreamingRenderer} computes the generalization distance in a way that's inconsistent
 * with the map scale, results in different values for different tiles on the same map, and often, especially when doing
 * reprojection, the generalization distances result in querying much higher tile levels than it should, leading to
 * traversing too many tiles from the PMTiles or other vector tiles datastore. It also always performs the query in the
 * layer's native CRS.
 *
 * <p><b>Usage:</b>
 *
 * <pre>{@code
 * try {
 *     VectorTilesRequestScale.set(scaleDenominator);
 *     // perform feature source queries...
 * } finally {
 *     VectorTilesRequestScale.clear();
 * }
 * }</pre>
 *
 * <p>When set, the scale denominator is converted to a resolution using the OGC standard pixel size of 0.28mm (0.00028
 * meters): {@code resolution = scaleDenominator * 0.00028}
 *
 * @see VectorTilesFeatureSource#determineZoomLevel(org.geotools.api.data.Query)
 */
public final class VectorTilesRequestScale {

    private static final ThreadLocal<Double> SCALE = new ThreadLocal<>();

    private VectorTilesRequestScale() {}

    /**
     * Sets the scale denominator for the current thread's vector tile requests.
     *
     * <p>The scale denominator represents the ratio between a distance on the map and the corresponding distance on the
     * ground. For example, a scale denominator of 50000 means 1 unit on the map equals 50000 units on the ground.
     *
     * @param requestScale the scale denominator value (e.g., 50000 for 1:50,000 scale)
     */
    public static void set(double requestScale) {
        SCALE.set(requestScale);
    }

    /**
     * Returns the scale denominator set for the current thread, if any.
     *
     * @return an {@link OptionalDouble} containing the scale denominator if set, or an empty optional if not set
     */
    public static OptionalDouble get() {
        Double scale = SCALE.get();
        return scale == null ? OptionalDouble.empty() : OptionalDouble.of(scale.doubleValue());
    }

    /**
     * Clears the scale denominator for the current thread.
     *
     * <p>This method should be called in a {@code finally} block to ensure proper cleanup and prevent memory leaks in
     * thread pool environments.
     */
    public static void clear() {
        SCALE.remove();
    }
}
