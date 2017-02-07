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
import java.awt.Point;

import org.geotools.filter.function.RecodeFunction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Stroke;

/**
 * MBLayer wrapper for "line" layers.
 * <p>
 * Example of line JSON:
 * 
 * <pre>
 *      {   "type": "line",
 *          "source": "http://localhost:8080/geoserver/ne/roads",
 *          "source-layer": "road"
 *          "id": "roads",
 *          "paint": {
 *              "line-color": "#6655ae",
 *              "line-width": 2,
 *              "line-opacity": 1
 *          },
 *      },
 * </pre>
 * 
 * @author Reggie Beckwith (Boundless)
 *
 */
public class LineMBLayer extends MBLayer {
    private JSONObject layout;
    private JSONObject paint;

    private static String TYPE = "line";

    
    public LineMBLayer(JSONObject json) {
        super(json);
        paint = super.getPaint();
        layout = super.getLayout();
    }

    /**
     * The display of line endings.
     */
    public enum LineCap {
        /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
        BUTT,
        /**
         * A cap with a rounded end which is drawn beyond the endpoint of the line at a
         * radius of one-half of the line's width and centered on the endpoint of the line.
         */
        ROUND,
        /**
         * A cap with a squared-off end which is drawn beyond the endpoint of the line at a
         * distance of one-half of the line's width.
         * 
         */
        SQUARE
    }
    /**
     * Display of line endings.
     * <p>
     * Supports piecewise constant functions.</p>
     * 
     * @return One of butt, round, square, optional defaults to butt.
     */
    public LineCap getLineCap() {
        // TODO: fuction case 
        
        String lineCap = parse.get(layout, "line-cap", "butt");
        
        switch (lineCap.toLowerCase()) {
        case "round":
            return LineCap.ROUND;
        case "square":
            return LineCap.SQUARE;
        case "butt":
            return LineCap.BUTT; // default
        default:
            throw new MBFormatException(
                    "Provided line-cap \"" + lineCap + "\" invalid - expected round, square, butt");
        }
    }
    /**
     * Maps {@link #getLineCap()} to {@link Stroke#getLineCap()} values of "butt", "round", and "square" Literals.
     * <p>
     * Since piecewise constant functions is supported a {@link RecodeFunction} may be generated.
     * @return Expression for {@link Stroke#getLineCap()} use.
     */
    public Expression lineCap(){
        // TODO: convert getLineCap to an Expression (function or literal)
        return ff.literal( getLineCap().toString().toLowerCase());
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
        Object value = paint.get("line-join");
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
        if (paint.get("line-miter-limit") != null) {
            return (Number) paint.get("line-miter-limit");
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
        if (paint.get("line-round-limit") != null) {
            return (Number) paint.get("line-round-limit");
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
        if( paint.containsKey("line-opacity")){
            Object lineOpacity = paint.get("line-opacity");
            if( lineOpacity == null ){
                return 1;
            }
            else if (lineOpacity instanceof JSONObject){
                throw new UnsupportedOperationException("Functions not supported yet");
            }
            else if (lineOpacity instanceof Number){
                return (Number) lineOpacity;
            }
            else if(lineOpacity instanceof String){
                return Double.parseDouble((String)lineOpacity);
            }
            else {
                throw new IllegalArgumentException(
                        "json contents invalid, line-opacity value limited to Number, String, or JSONObject (function)"
                        + lineOpacity.getClass().getSimpleName());
            }
        } else {
            return 1;
        }
    }
    
    /**
     * The opacity at which the line will be drawn.
     * 
     * Defaults to 1.
     * @return opacity for line (literal or function), defaults to 1.
     * 
     */
    public Expression lineOpacity(){
        return parse.number(paint, "line-opacity", 1 );
    }

    /**
     * (Optional) The color with which the line will be drawn.
     * 
     * Defaults to {@link Color#BLACK}, disabled by line-pattern.
     * 
     * @return color to draw the line, optional defaults to black.
     */
    public Expression getLineColor() {
        if( paint.containsKey("line-pattern")){
            return null; // disabled
        }
        return parse.color(paint,"line-color", Color.BLACK);
    }

    /**
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
     * 
     * Units in pixels. Defaults to 0,0.
     * 
     */
    public Point getLineTranslate() {
        if (paint.get("line-translate") != null) {
            JSONArray array = (JSONArray) paint.get("line-translate");
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
        Object value = paint.get("line-translate-anchor");
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
        if (paint.get("line-width") != null) {
            return (Number) paint.get("line-width");
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
        if (paint.get("line-gap-width") != null) {
            return (Number) paint.get("line-gap-width");
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
        if (paint.get("line-offset") != null) {
            return (Number) paint.get("line-offset");
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
        if (paint.get("line-blur") != null) {
            return (Number) paint.get("line-blur");
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
        if (paint.get("line-dasharray") != null) {
            return (Number) paint.get("line-dasharray");
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
        if (paint.get("line-pattern") != null) {
            return (String) paint.get("line-pattern");
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return TYPE;
    }

}
