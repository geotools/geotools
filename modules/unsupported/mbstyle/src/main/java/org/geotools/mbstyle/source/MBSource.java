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

import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;

/**
 * Wrapper around a {@link JSONObject} containing the "sources" in a Mapbox style. Mapbox sources
 * supply data to be shown on the map. The type of source is specified by the "type" property, and
 * must be one of vector, raster, geojson, image, video, canvas.
 *
 * <p>"Layers refer to a source and give it a visual representation. This makes it possible to style
 * the same source in different ways, like differentiating between types of roads in a highways
 * layer."
 *
 * <p>Internally we use a wtms end-point to refer to the data source:
 *
 * <pre>
 * "us-states": {
 *    "type": "vector",
 *    "url": "https://localhost:8080/geoserver/gwc/service/wmts#us:states"
 * }
 * </pre>
 *
 * This is based on the following request for a single file::
 *
 * <pre>http://localhost:8080/geoserver/gwc/service/wmts?
 *   REQUEST=GetTile&SERVICE=WMTS&VERSION=1.0.0&
 *   LAYER=dfs:ne_110m_coastline&STYLE=&TILEMATRIX=EPSG:4326:2&TILEMATRIXSET=EPSG:4326&
 *   FORMAT=application/x-protobuf;type=mapbox-vector
 *   &TILECOL=3&TILEROW=1</pre>
 *
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources</a>
 */
public abstract class MBSource {

    protected JSONObject json;
    protected MBObjectParser parser;

    public MBSource(JSONObject json) {
        this(json, null);
    }

    public MBSource(JSONObject json, MBObjectParser parser) {
        this.json = json != null ? json : new JSONObject();
        this.parser = parser != null ? parser : new MBObjectParser(MBSource.class);
    }

    public static MBSource create(JSONObject json, MBObjectParser parser) {
        if (!json.containsKey("type") || !(json.get("type") instanceof String)) {
            throw new MBFormatException(
                    "Mapbox source \"type\" is required and must be one of: vector, raster, geojson, image, video, or canvas.");
        }

        String type = ((String) json.get("type")).toLowerCase().trim();

        if ("vector".equalsIgnoreCase(type)) {
            return new VectorMBSource(json, parser);
        }
        if ("raster".equalsIgnoreCase(type)) {
            return new RasterMBSource(json, parser);
        }
        if ("geojson".equalsIgnoreCase(type)) {
            return new GeoJsonMBSource(json, parser);
        }
        if ("image".equalsIgnoreCase(type)) {
            return new ImageMBSource(json, parser);
        }
        if ("video".equalsIgnoreCase(type)) {
            return new VideoMBSource(json, parser);
        }
        if ("canvas".equalsIgnoreCase(type)) {
            return new CanvasMBSource(json, parser);
        }
        throw new MBFormatException(
                "Mapbox source \"type\" is required and must be one of: vector, raster, geojson, image, video, or canvas.");
    }

    /** Must be one of "vector", "raster", "geojson", "image", "video", "canvas" */
    public abstract String getType();
}
