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

import java.util.ArrayList;
import java.util.List;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Wrapper around a {@link JSONObject} holding a tiled Mapbox source. Tiled sources (vector and
 * raster) must specify their details in terms of the TileJSON specification.
 *
 * @see {@link MBSource}
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources</a>
 */
public abstract class TileMBSource extends MBSource {

    public TileMBSource(JSONObject json) {
        this(json, null);
    }

    public TileMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Optional) A URL to a TileJSON resource. Supported protocols are http:, https:, and
     * mapbox://<mapid>.
     *
     * @return A String for the URL.
     */
    public String getUrl() {
        return parser.optional(String.class, json, "url", null);
    }

    /**
     * (Optional) An array of one or more tile source URLs, as in the TileJSON spec.
     *
     * @return A list for the tile source URLs; empty list by default.
     */
    public List<String> getTiles() {
        JSONArray tilesArray = parser.getJSONArray(json, "tiles", new JSONArray());
        List<String> tilesList = new ArrayList<>();
        for (Object o : tilesArray) {
            tilesList.add((String) o);
        }
        return tilesList;
    }

    /**
     * (Optional) Defaults to 0. Minimum zoom level for which tiles are available, as in the
     * TileJSON spec.
     *
     * @return Number for the min zoom, defaulting to 0
     */
    public Number getMinZoom() {
        return parser.optional(Number.class, json, "minzoom", 0);
    }

    /**
     * (Optional) Defaults to 22. Maximum zoom level for which tiles are available, as in the
     * TileJSON spec. Data from tiles at the maxzoom are used when displaying the map at higher zoom
     * levels.
     *
     * @return Number for the max zoom, defaulting to 22
     */
    public Number getMaxZoom() {
        return parser.optional(Number.class, json, "maxzoom", 22);
    }
}
