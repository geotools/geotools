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

import java.awt.Color;

import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;

/**
 * The background color or pattern of the map.
 * <p>
 * MBLayer wrapper around a {@link JSONObject} representation of a "background" type latyer. All
 * methods act as accessors on provided JSON layer, no other state is maintained. This allows
 * modifications to be made cleanly with out chance of side-effect.
 * 
 * <ul>
 * <li>get methods: access the json directly</li>
 * <li>query methods: provide logic / transforms to GeoTools classes as required.</li>
 * </ul>
 */
public class BackgroundMBLayer extends MBLayer {

    private JSONObject paint;

    private JSONObject layout;

    private static String type = "background";

    public BackgroundMBLayer(JSONObject json) {
        super(json, new MBObjectParser(BackgroundMBLayer.class));
        paint = paint();
        layout = layout();
    }

    /**
     * Optional color. Defaults to #000000. Disabled by background-pattern.
     * 
     * The color with which the background will be drawn.
     * 
     */
    public Color getBackgroundColor() {
        return parse
                .convertToColor(parse.optional(String.class, paint, "background-color", "#000000"));
    }

    /**
     * Maps {@link #getBackgroundColor()} to an {@link Expression}.
     * 
     * Optional color. Defaults to #000000. Disabled by background-pattern.
     * 
     * The color with which the background will be drawn.
     * 
     */
    public Expression backgroundColor() {
        return parse.color(paint, "background-color", Color.BLACK);
    }

    /**
     * Optional string. Name of image in sprite to use for drawing an image background. For seamless patterns, image width and height must be a factor
     * of two (2, 4, 8, ..., 512).
     * 
     */
    public String getBackgroundPattern() {
        return parse.optional(String.class, paint, "background-pattern", null);
    }

    /**
     * Maps {@link #getBackgroundPattern()} to an {@link Expression}.
     * 
     * Optional string. Name of image in sprite to use for drawing an image background. For seamless patterns, image width and height must be a factor
     * of two (2, 4, 8, ..., 512).
     * 
     */
    public Expression backgroundPattern() {
        return parse.string(paint, "background-pattern", null);
    }

    /**
     * Optional number. Defaults to 1.
     * 
     * The opacity at which the background will be drawn.
     */
    public Number getBackgroundOpacity() {
        return parse.optional(Number.class, paint, "background-opacity", 1.0);
    }

    /**
     * Maps {@link #getBackgroundOpacity()} to an {@link Expression}.
     * 
     * Optional number. Defaults to 1.
     * 
     * The opacity at which the background will be drawn.
     */
    public Expression backgroundOpacity() {
        return parse.percentage(paint, "background-opacity", 1.0);
    }

    @Override
    public String getType() {
        return type;
    }

}
