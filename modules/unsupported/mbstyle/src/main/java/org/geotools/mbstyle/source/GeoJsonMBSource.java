/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.source;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * Wrapper around a {@link JSONObject} holding a Mapbox GeoJSON source. Data must be provided via a
 * "data" property, whose value can be a URL or inline GeoJSON.
 *
 * @see {@link MBSource}
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-geojson">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-geojson</a>
 */
public class GeoJsonMBSource extends MBSource {

    public GeoJsonMBSource(JSONObject json) {
        this(json, null);
    }

    public GeoJsonMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /** (Optional) A URL to a GeoJSON file, or inline GeoJSON. */
    public Object getData() {
        return json.get("data");
    }

    /**
     * (Optional) Defaults to 18. Maximum zoom level at which to create vector tiles (higher means
     * greater detail at high zoom levels).
     *
     * @return Number for the max zoom, defaulting to 18.
     */
    public Number getMaxZoom() {
        return parser.optional(Number.class, json, "maxzoom", 18);
    }

    /**
     * (Optional) Defaults to 128. Size of the tile buffer on each side. A value of 0 produces no
     * buffer. A value of 512 produces a buffer as wide as the tile itself. Larger values produce
     * fewer rendering artifacts near tile edges and slower performance.
     *
     * @return Number for the size of the tile buffer
     */
    public Number getBuffer() {
        return parser.optional(Number.class, json, "buffer", 128);
    }

    /**
     * (Optional) Defaults to 0.375. Douglas-Peucker simplification tolerance (higher means simpler
     * geometries and faster performance).
     *
     * @return Number for the simplification tolerance, defaulting to 0.375
     */
    public Number getTolerance() {
        return parser.optional(Number.class, json, "tolerance", 0.375);
    }

    /**
     * (Optional) Defaults to false. If the data is a collection of point features, setting this to
     * true clusters the points by radius into groups.
     *
     * @return Boolean, whether to cluster, defaulting to false
     */
    public Boolean getCluster() {
        return parser.optional(Boolean.class, json, "cluster", false);
    }

    /**
     * (Optional) Defaults to 50.
     *
     * <p>Radius of each cluster if clustering is enabled. A value of 512 indicates a radius equal
     * to the width of a tile.
     *
     * @return Number for the cluster radius, defaulting to 50
     */
    public Number getClusterRadius() {
        return parser.optional(Number.class, json, "clusterRadius", 50);
    }

    /**
     * (Optional) Max zoom on which to cluster points if clustering is enabled. Defaults to one zoom
     * less than maxzoom (so that last zoom features are not clustered).
     *
     * @return Number for the cluster max zoom
     */
    public Number getClusterMaxZoom() {
        return parser.optional(Number.class, json, "clusterMaxZoom", getMaxZoom().intValue() - 1);
    }

    @Override
    public String getType() {
        return "geojson";
    }
}
