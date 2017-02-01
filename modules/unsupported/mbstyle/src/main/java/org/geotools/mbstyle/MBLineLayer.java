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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

public abstract class MBLineLayer extends MBLayer {
    private JSONObject paintJson;

    private static String type = "line";

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public MBLineLayer(JSONObject json) {
        super(json);

        if (json.get("paint") != null) {
            paintJson = (JSONObject) json.get("paint");
        } else {
            paintJson = new JSONObject();
        }

    }

    /**
     * 
     * (Optional) The display of line endings.
     * 
     * Butt - A cap with a squared-off end which is drawn to the exact endpoint of the line.
     * 
     * Round - A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half of the line's width and centered on the
     * endpoint of the line.
     * 
     * Square - A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of one-half of the line's width.
     *
     */
    public enum LineCap {
        BUTT, ROUND, SQUARE
    }

    public LineCap getLineCap() {
        Object value = paintJson.get("line-cap");
        if (value != null && "round".equalsIgnoreCase((String) value)) {
            return LineCap.ROUND;
        } else if (value != null && "square".equalsIgnoreCase((String) value)) {
            return LineCap.SQUARE;
        } else {
            return LineCap.BUTT;
        }
    }

    /**
     * (Optional) The display of lines when joining.
     * 
     * Bevel - A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of one-half of the line's width.
     * 
     * Round - A join with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half of the line's width and centered on
     * the endpoint of the line.
     * 
     * Miter - A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of the path until they meet.
     */
    public enum LineJoin {
        BEVEL, ROUND, MITER
    }

    public LineJoin getLineJoin() {
        Object value = paintJson.get("line-join");
        if (value != null && "round".equalsIgnoreCase((String) value)) {
            return LineJoin.ROUND;
        } else if (value != null && "bevel".equalsIgnoreCase((String) value)) {
            return LineJoin.BEVEL;
        } else {
            return LineJoin.MITER;
        }

    }

    /**
     * (Optional) Used to automatically convert miter joins to bevel joins for sharp angles.
     * 
     * Defaults to 2. Requires line-join = miter.
     * 
     */
    public Number getLineMiterLimit() {
        if (paintJson.get("line-miter_limit") != null) {
            return (Number) paintJson.get("line-miter-limit");
        } else {
            return 2;
        }
    }

    /**
     * (Optional) Used to automatically convert miter joins to bevel joins for sharp angles.
     * 
     * Defaults to 1.05. Requires line-join = round.
     * 
     */
    public Number getLineRoundLimit() {
        if (paintJson.get("line-round_limit") != null) {
            return (Number) paintJson.get("line-round-limit");
        } else {
            return 1.05;
        }
    }

    /**
     * (Optional) The opacity at which the line will be drawn.
     * 
     * Defaults to 1.
     * 
     */
    public Number getLineOpacity() {
        if (paintJson.get("line-opacity") != null) {
            return (Number) paintJson.get("line-opacity");
        } else {
            return 1;
        }
    }

    /**
     * (Optional) The color with which the line will be drawn.
     * 
     * Defaults to #000000. Disabled by line-pattern.
     */
    public Expression getLineColor() {
        if (paintJson.get("line-color") != null) {
            String color = (String) paintJson.get("line-color");
            return ff.literal(color);
        } else {
            return ff.literal("#000000");
        }
    }

    /**
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
     * 
     * Units in pixels. Defaults to 0,0.
     * 
     */
    public Point getLineTranslate() {
        if (paintJson.get("line-translate") != null) {
            JSONArray array = (JSONArray) paintJson.get("line-translate");
            Number x = (Number) array.get(0);
            Number y = (Number) array.get(1);
            return new Point(x.intValue(), y.intValue());
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * Controls the translation reference point.
     * 
     * Map: The fill is translated relative to the map.
     * 
     * Viewport: The fill is translated relative to the viewport.
     *
     */
    public enum LineTranslateAnchor {
        MAP, VIEWPORT
    }

    /**
     * (Optional) Controls the translation reference point.
     * 
     * {@link LineTranslateAnchor#MAP}: The fill is translated relative to the map.
     * 
     * {@link LineTranslateAnchor#VIEWPORT}: The fill is translated relative to the viewport.
     * 
     * Defaults to {@link LineTranslateAnchor#MAP}. Requires fill-translate.
     * 
     */
    public LineTranslateAnchor getLineTranslateAnchor() {
        Object value = paintJson.get("line-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return LineTranslateAnchor.VIEWPORT;
        } else {
            return LineTranslateAnchor.MAP;
        }
    }

    /**
     * (Optional) Stroke thickness.
     * 
     * Units in pixels. Defaults to 1.
     * 
     */
    public Number getLineWidth() {
        if (paintJson.get("line-width") != null) {
            return (Number) paintJson.get("line-width");
        } else {
            return 1;
        }
    }

    /**
     * (Optional) Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Number getLineGapWidth() {
        if (paintJson.get("line-gap-width") != null) {
            return (Number) paintJson.get("line-gap-width");
        } else {
            return 0;
        }
    }

    /**
     * (Optional) The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a
     * negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Number getLineOffset() {
        if (paintJson.get("line-offset") != null) {
            return (Number) paintJson.get("line-offset");
        } else {
            return 0;
        }
    }

    /**
     * (Optional) Blur applied to the line, in pixels.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Number getLineBlur() {
        if (paintJson.get("line-blur") != null) {
            return (Number) paintJson.get("line-blur");
        } else {
            return 0;
        }
    }

    /**
     * (Optional) Specifies the lengths of the alternating dashes and gaps that form the dash pattern. The lengths are later scaled by the line width.
     * To convert a dash length to pixels, multiply the length by the current line width.
     * 
     * Units in line widths. Disabled by line-pattern.
     * 
     */
    public Number getLineDasharray() {
        if (paintJson.get("line-dasharray") != null) {
            return (Number) paintJson.get("line-dasharray");
        } else {
            return 0;
        }
    }

    /**
     * (Optional) Name of image in sprite to use for drawing image lines. For seamless patterns, image width must be a factor of two (2, 4, 8, ...,
     * 512).
     * 
     * Units in line widths. Disabled by line-pattern.
     * 
     */
    public String getLinePattern() {
        if (paintJson.get("line-pattern") != null) {
            return (String) paintJson.get("line-pattern");
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return type;
    }

}
