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
 * Wrapper around a {@link JSONObject} containing a Mapbox canvas source.
 *
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-canvas">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-canvas</a>
 */
public class CanvasMBSource extends MediaMBSource {

    public CanvasMBSource(JSONObject json) {
        this(json, null);
    }

    public CanvasMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Required) HTML ID of the canvas from which to read pixels.
     *
     * @return String value for the HTML ID
     */
    public String getCanvas() {
        return parser.get(json, "canvas");
    }

    /**
     * (Optional) Defaults to true. Whether the canvas source is animated. If the canvas is static,
     * animate should be set to false to improve performance.
     *
     * @return Boolean for whether the source is animated, defaulting to true.
     */
    public Boolean getAnimate() {
        return parser.optional(Boolean.class, json, "animate", true);
    }

    @Override
    public String getType() {
        return "canvas";
    }
}
