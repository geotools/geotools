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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Wrapper around a {@link JSONObject} containing a Mapbox media (image or video) source.
 *
 * @see <a
 *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-image">https://www.mapbox.com/mapbox-gl-js/style-spec/#sources-image</a>
 */
public abstract class MediaMBSource extends MBSource {

    public MediaMBSource(JSONObject json) {
        this(json, null);
    }

    public MediaMBSource(JSONObject json, MBObjectParser parser) {
        super(json, parser);
    }

    /**
     * (Required) Array of [longitude, latitude] pairs for the image corners listed in clockwise
     * order: top left, top right, bottom right, bottom left. Example: <br>
     * <code>
     * "coordinates": [
     *   [-80.425, 46.437],
     *   [-71.516, 46.437],
     *   [-71.516, 37.936],
     *   [-80.425, 37.936]
     * ]
     * </code>
     */
    public List<Point2D.Double> getCoordinates() {
        JSONArray arr = parser.getJSONArray(json, "coordinates");
        if (arr.size() != 4) {
            throw new MBFormatException(
                    "image/video/canvas source \"coordinates\" tag requires JSONArray of size 4");
        } else {
            List<Point2D.Double> coords = new ArrayList<>();
            for (Object o : arr) {
                if (o instanceof JSONArray) {
                    coords.add(parsePoint((JSONArray) o));
                } else {
                    throw new MBFormatException(
                            "image/video/canvas source \"coordinates\" values must be JSONArrays");
                }
            }
            return coords;
        }
    }

    private Double parseDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o instanceof String) {
            return Double.valueOf((String) o);
        } else {
            throw new MBFormatException(
                    "image/video/canvas source \"coordinates\" tags must contain Numbers");
        }
    }

    private Point2D.Double parsePoint(JSONArray arr) {
        if (arr == null || arr.size() != 2) {
            throw new MBFormatException(
                    "image/video/canvas source \"coordinates\" tags must each be JSONArray of size 2");
        } else {
            Double x = parseDouble(arr.get(0));
            Double y = parseDouble(arr.get(1));
            return new Point2D.Double(x, y);
        }
    }
}
