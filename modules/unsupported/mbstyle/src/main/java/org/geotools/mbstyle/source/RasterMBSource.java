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
 * Wrapper around a {@link JSONObject} holding a Mapbox raser source. Tiled sources (vector and
 * raster) must specify their details in terms of the TileJSON specification.
 *
 * @see {@link MBSource}
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-raster">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-raster</a>
 */
public class RasterMBSource extends TileMBSource {

    public RasterMBSource(JSONObject json) {
        this(json, null);
    }

    public RasterMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Optional) Units in pixels. Defaults to 512.
     *
     * <p>The minimum visual size to display tiles for this layer. Only configurable for raster
     * layers.
     *
     * @return Number for the tile size, defaulting to 512
     */
    public Number getTileSize() {
        return parser.optional(Number.class, json, "tileSize", 512);
    }

    @Override
    public String getType() {
        return "raster";
    }
}
