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
 *    
 */
package org.geotools.mbstyle;

import java.awt.Point;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

public class MBFillLayer extends MBLayer {

    private JSONObject paintJson;

    private static String type = "fill";

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /**
     * Controls the translation reference point.
     * 
     * Map: The fill is translated relative to the map.
     * 
     * Viewport: The fill is translated relative to the viewport.
     *
     */
    public enum FillTranslateAnchor {
        MAP, VIEWPORT
    }

    public MBFillLayer(JSONObject json) {
        super(json);

        if (json.get("paint") != null) {
            paintJson = (JSONObject) json.get("paint");
        } else {
            paintJson = new JSONObject();
        }

    }

    /**
     * (Optional) Whether or not the fill should be antialiased.
     * 
     * Defaults to true.
     * 
     */
    public Expression getFillAntialias() {        
        return MBObjectParser.bool(paintJson, "fill-antialias", true);
    }

    /**
     * (Optional) The opacity of the entire fill layer. In contrast to the fill-color, this value will also affect the 1px stroke around the fill, if
     * the stroke is used.
     * 
     * Defaults to 1.
     * @throws MBFormatException 
     * 
     */
    public Expression getFillOpacity() throws MBFormatException {
        return MBObjectParser.percentage( paintJson, "fill-opacity", 1 );
    }

    /**
     * (Optional). The color of the filled part of this layer. This color can be specified as rgba with an alpha component and the color's opacity
     * will not affect the opacity of the 1px stroke, if it is used.
     * 
     * Colors are written as JSON strings in a variety of permitted formats.
     * 
     * Defaults to #000000. Disabled by fill-pattern.
     */
    public Expression getFillColor() {      
        return MBObjectParser.color(paintJson, "fill-color", "#000000");
    }

    /**
     * (Optional). Requires fill-antialias = true. The outline color of the fill.
     * 
     * Matches the value of fill-color if unspecified. Disabled by fill-pattern.
     */
    public Expression getFillOutlineColor() {
        if (paintJson.get("fill-outline-color") != null) {
            return MBObjectParser.color(paintJson, "fill-outline-color", "#000000");
        } else {
            return getFillColor();
        }
    }

    /**
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively. Units in pixels. Defaults to 0, 0.
     */
    public Point getFillTranslate() {
        if (paintJson.get("fill-translate") != null) {
            JSONArray array = (JSONArray) paintJson.get("fill-translate");
            Number x = (Number) array.get(0);
            Number y = (Number) array.get(1);
            return new Point(x.intValue(), y.intValue());
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * (Optional) Controls the translation reference point.
     * 
     * {@link FillTranslateAnchor#MAP}: The fill is translated relative to the map.
     * 
     * {@link FillTranslateAnchor#VIEWPORT}: The fill is translated relative to the viewport.
     * 
     * Defaults to {@link FillTranslateAnchor#MAP}. Requires fill-translate.
     * 
     */
    public FillTranslateAnchor getFillTranslateAnchor() {
        Object value = paintJson.get("fill-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return FillTranslateAnchor.VIEWPORT;
        } else {
            return FillTranslateAnchor.MAP;
        }
    }

    /**
     * (Optional) Name of image in a sprite to use for drawing image fills. For seamless patterns, image width and height must be a factor of two (2,
     * 4, 8, ..., 512).
     */
    public Expression getFillPattern() {
        return MBObjectParser.color(paintJson, "fill-pattern", null);
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return type;
    }

}
