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
 * Wrapper around a {@link JSONObject} containing a Mapbox image source.
 *
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-image">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-image</a>
 */
public class ImageMBSource extends MediaMBSource {

    public ImageMBSource(JSONObject json) {
        this(json, null);
    }

    public ImageMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Required) URL that points to an image.
     *
     * @return String for the URL
     */
    public String getUrl() {
        return parser.get(json, "url");
    }

    @Override
    public String getType() {
        return "image";
    }
}
