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

import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;

public class RasterMBLayer extends MBLayer {

    private JSONObject paintJson;

    private static String type = "raster";

    public RasterMBLayer(JSONObject json) {
        super(json);

        if (json.get("paint") != null) {
            paintJson = (JSONObject) json.get("paint");
        } else {
            paintJson = new JSONObject();
        }
    }

    /**
     * (Optional) The opacity (Number) at which the image will be drawn.
     * 
     * Defaults to 1. Range: [0, 1]
     * 
     */
    public Expression getOpacity() {
        return parse.percentage(paintJson, "raster-opacity", 1);
    }

    /**
     * (Optional) Rotates hues around the color wheel.
     * 
     * Number. Units in degrees. Defaults to 0.
     */
    public Expression getHueRotate() {
        return parse.number(paintJson, "raster-hue-rotate", 0);
    }

    /**
     * (Optional) Increase or reduce the brightness of the image. The value is the minimum brightness.
     * 
     * Number. Defaults to 0. Range: [0, 1]
     */
    public Expression getBrightnessMin() {
        return parse.number(paintJson, "raster-brightness-min", 0);
    }

    /**
     * (Optional) Increase or reduce the brightness of the image. The value is the maximum brightness.
     * 
     * Number. Defaults to 1. Range: [0, 1]
     */
    public Expression getBrightnessMax() {
        return parse.number(paintJson, "raster-brightness-max", 1);
    }

    /**
     * (Optional) Increase or reduce the saturation of the image.
     * 
     * Number. Defaults to 0. Range: [-1, 1]
     */
    public Expression getSaturation() {
        return parse.number(paintJson, "raster-saturation", 0);
    }

    /**
     * (Optional) Increase or reduce the contrast of the image.
     * 
     * Number. Defaults to 0. Range: [-1, 1]
     */
    public Expression getContrast() {
        return parse.number(paintJson, "raster-contrast", 0);
    }

    /**
     * (Optional) Fade duration when a new tile is added.
     * 
     * Number. Units in milliseconds. Defaults to 300.
     */
    public Expression getFadeDuration() {
        return parse.number(paintJson, "raster-fade-duration", 300);
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return type;
    }

}
