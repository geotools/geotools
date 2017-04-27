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

import org.geotools.factory.Hints;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBFunction;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.styling.Displacement;
import org.geotools.util.ColorConverterFactory;
import org.geotools.util.Converter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.style.SemanticType;
import org.opengis.style.Stroke;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        super(json, new MBObjectParser(LineMBLayer.class));
        paint = super.getPaint();
        layout = super.getLayout();
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.LINE;
    }
    
    /**
     * The display of line endings.
     */
    public static enum LineCap {
        /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
        BUTT,
        /**
         * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half of the line's width and centered on the
         * endpoint of the line.
         */
        ROUND,
        /**
         * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of one-half of the line's width.
         * 
         */
        SQUARE
    }

    /**
     * Display of line endings.
     * <p>
     * Supports piecewise constant functions.
     * </p>
     * 
     * @return One of butt, round, square, optional defaults to butt.
     */
    public LineCap getLineCap() {
        return parse.toEnum(layout, "line-cap", LineCap.class, LineCap.BUTT);
    }

    /**
     * Maps {@link #getLineCap()} to {@link Stroke#getLineCap()} values of "butt", "round", and "square" Literals. Defaults to butt.
     * <p>
     * Since piecewise constant functions is supported a {@link RecodeFunction} may be generated.
     * 
     * @return Expression for {@link Stroke#getLineCap()} use.
     */
    public Expression lineCap() {

        Object obj = layout.get("line-cap");

        if (obj == null) {
            return ff.literal("butt");
        } else if (obj instanceof String) {
            return ff.literal(
                    parse.toEnum(layout, "line-cap", LineCap.class, LineCap.BUTT).name().toLowerCase());
        } else if (obj instanceof Number) {
            throw new UnsupportedOperationException("\"line-cap\": Number not supported");
        } else if (obj instanceof Boolean) {
            throw new UnsupportedOperationException("\"line-cap\": Boolean not supported");
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction(parse, (JSONObject) obj);
            return function.enumeration(LineCap.class );
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException("\"line-cap\": JSONArray not supported");
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid: \"line-cap\" value limited to String or JSONObject but was "
                            + obj.getClass().getSimpleName());
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
    public static enum LineJoin {
        BEVEL, ROUND, MITER
    }

    /**
     * Optional enum. One of bevel, round, miter. Defaults to miter. The display of lines when joining.
     */
    public LineJoin getLineJoin() {
        return parse.toEnum(layout, "line-join", LineJoin.class, LineJoin.MITER);
    }

    /**
     * Maps {@link #getLineJoin()} to {@link Stroke#getLineJoin()} values of "mitre", "round", and "bevel" Literals. Defaults to "mitre".
     * <p>
     * Since piecewise constant functions is supported a {@link RecodeFunction} may be generated.
     * 
     * @return Expression for {@link Stroke#getLineJoin()()} use.
     */
    public Expression lineJoin() {
        Object obj = layout.get("line-join");

        if (obj == null) {
            return ff.literal("mitre");
        } else if (obj instanceof String) {
            LineJoin lj = parse.toEnum(layout, "line-join", LineJoin.class, LineJoin.MITER);
            if (lj.equals(LineJoin.MITER)) {
                // miter (MapBox) -> mitre (GeoTools)
                return ff.literal("mitre");
            } else {
                return ff.literal(lj.name().toLowerCase());
            }
        } else if (obj instanceof Number) {
            throw new UnsupportedOperationException("\"line-join\": Number not supported");
        } else if (obj instanceof Boolean) {
            throw new UnsupportedOperationException("\"line-join\": Boolean not supported");
        } else if (obj instanceof JSONObject) {
            MBFunction function = new MBFunction(parse, (JSONObject) obj);
            return function.enumeration(LineJoin.class);
        } else if (obj instanceof JSONArray) {
            throw new MBFormatException("\"line-join\": JSONArray not supported");
        } else {
            throw new IllegalArgumentException(
                    "json contents invalid: \"line-join\" value limited to String or JSONObject but was "
                            + obj.getClass().getSimpleName());
        }
    }

    /**
     * (Optional) Used to automatically convert miter joins to bevel joins for sharp angles.
     * 
     * Defaults to 2. Requires line-join = miter.
     * 
     */
    public Number getLineMiterLimit() {
        return parse.optional(Number.class, layout, "line-miter-limit", 2);
    }

    /**
     * Maps {@link #getLineMiterLimit()} to an {@link Expression}. (Optional) Used to automatically convert miter joins to bevel joins for sharp
     * angles.
     * 
     * Defaults to 2. Requires line-join = miter.
     * 
     * @return Expression for {@link #getLineMiterLimit()}
     */
    public Expression lineMiterLimit() {
        return parse.number(layout, "line-miter-limit", 2);
    }

    /**
     * (Optional) Used to automatically convert miter joins to bevel joins for sharp angles.
     * 
     * Defaults to 1.05. Requires line-join = round.
     * 
     */
    public Number getLineRoundLimit() {
        return parse.optional(Number.class, layout, "line-round-limit", 1.05);
    }

    /**
     * Maps {@link #getLineRoundLimit()} to an {@link Expression}.
     * 
     * (Optional) Used to automatically convert miter joins to bevel joins for sharp angles.
     * 
     * Defaults to 1.05. Requires line-join = round.
     * 
     */
    public Expression lineRoundLimit() {
        return parse.number(layout, "line-round-limit", 1.05);
    }

    /**
     * (Optional) The opacity at which the line will be drawn.
     * 
     * Defaults to 1.
     * 
     */
    public Number getLineOpacity() {
        return parse.optional(Number.class, paint, "line-opacity", 1);
    }

    /**
     * 
     * Maps {@link #getLineOpacity()} to an {@link Expression}.
     * 
     * (Optional) The opacity at which the line will be drawn.
     * 
     * Defaults to 1.
     * 
     * @return opacity for line (literal or function), defaults to 1.
     * 
     */
    public Expression lineOpacity() {
        return parse.number(paint, "line-opacity", 1);
    }

    /**
     * 
     * (Optional) The color with which the line will be drawn.
     * 
     * Defaults to {@link Color#BLACK}, disabled by line-pattern.
     * 
     * @return color to draw the line, optional defaults to black.
     */
    public Color getLineColor() {
        if (paint.containsKey("line-pattern")) {
            return null; // disabled
        }
        return parse.convertToColor(parse.optional(String.class, paint, "line-color", "#000000"));
    }

    /**
     * 
     * Maps {@link #getLineColor()} to an {@link Expression}.
     * 
     * (Optional) The color with which the line will be drawn.
     * 
     * Defaults to {@link Color#BLACK}, disabled by line-pattern.
     * 
     * @return color to draw the line, optional defaults to black.
     */
    public Expression lineColor() {
        if (paint.containsKey("line-pattern")) {
            return null; // disabled
        }
        return parse.color(paint, "line-color", Color.BLACK);
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
            if (array.size() != 2) {
                throw new MBFormatException("line-translate must be an array of length 2");
            }
            Number x = (Number) array.get(0);
            Number y = (Number) array.get(1);
            return new Point(x.intValue(), y.intValue());
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * Maps {@link #getLineTranslate()} to a {@link Displacement}.
     * 
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
     * 
     * Units in pixels. Defaults to 0,0.
     * 
     */
    public Displacement toDisplacement() {
        Object defn = paint.get("line-translate");
        if (defn == null) {
            return null;
        } else if (defn instanceof JSONArray) {
            JSONArray array = (JSONArray) defn;
            return sf.displacement(parse.number(array, 0, 0), parse.number(array, 1, 0));
        } else if (defn instanceof JSONObject) {
            throw new MBFormatException("\"line-translate\": Functions not supported yet.");
        } else {
            throw new MBFormatException("\"line-translate\": Expected array or function, but was "
                    + defn.getClass().getSimpleName());
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
        return parse.toEnum(paint, "line-translate-anchor", LineTranslateAnchor.class, LineTranslateAnchor.MAP);
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
     * 
     * Convert {@link #getLineWidth()} to an Expression.
     * 
     * (Optional) Stroke thickness. Units in pixels. Defaults to 1.
     */
    public Expression lineWidth() {
        return parse.number(paint, "line-width", 1);
    }

    /**
     * (Optional) Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Number getLineGapWidth() {
        return parse.optional(Number.class, paint, "line-gap-width", 0);
    }

    /**
     * Converts {@link #getLineGapWidth()} to an Expression.
     * 
     * (Optional) Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Expression lineGapWidth() {
        return parse.number(paint, "line-gap-width", 0);
    }

    /**
     * (Optional) The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a
     * negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Number getLineOffset() {
        return parse.optional(Number.class, paint, "line-offset", 0);
    }

    /**
     * Converts {@link #getLineOffset()} to an Expression.
     * 
     * (Optional) The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a
     * negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Expression lineOffset() {
        return parse.number(paint, "line-offset", 0);
    }

    /**
     * (Optional) Blur applied to the line, in pixels.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Number getLineBlur() {
        return parse.optional(Number.class, paint, "line-blur", 0);
    }

    /**
     * Converts {@link #getLineBlur()} to an Expression.
     * 
     * (Optional) Blur applied to the line, in pixels.
     * 
     * Units in pixels. Defaults to 0.
     * 
     */
    public Expression lineBlur() {
        return parse.number(paint, "line-blur", 0);
    }

    /**
     * (Optional) Specifies the lengths of the alternating dashes and gaps that form the dash pattern. The lengths are later scaled by the line width.
     * To convert a dash length to pixels, multiply the length by the current line width.
     * 
     * Units in line widths. Disabled by line-pattern.
     * 
     */
    public List<Double> getLineDasharray() {
        List<Double> ret = new ArrayList<>();
        if (paint.get("line-dasharray") != null
                && paint.get("line-dasharray") instanceof JSONArray) {
            JSONArray a = (JSONArray) paint.get("line-dasharray");
            for (Object o : a) {
                Number n = (Number) o;
                ret.add(n.doubleValue());
            }
            return ret;
        } else {
            return null;
        }
    }

    /**
     * Converts {@link #getLineDasharray()} to a List of Expressions
     * 
     * (Optional) Specifies the lengths of the alternating dashes and gaps that form the dash pattern. The lengths are later scaled by the line width.
     * To convert a dash length to pixels, multiply the length by the current line width.
     * 
     * Units in line widths. Disabled by line-pattern.
     * 
     */
    public List<Expression> lineDasharray() {
        Object defn = paint.get("line-dasharray");

        if (defn == null) {
            return null;
        } else if (defn instanceof JSONArray) {
            JSONArray array = (JSONArray) defn;
            List<Expression> expressionList = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                expressionList.add(parse.number(array, i, 0));
            }
            return expressionList;
        } else if (defn instanceof JSONObject) {
            throw new MBFormatException("\"line-dasharray\": Functions not supported yet.");
        } else {
            throw new MBFormatException("\"line-dasharray\": Expected array or function, but was "
                    + defn.getClass().getSimpleName());
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
        return parse.optional(String.class, paint, "line-pattern", null);
    }

    /**
     * 
     * Converts {@link #getLinePattern()} to an Expression.
     * 
     * (Optional) Name of image in sprite to use for drawing image lines. For seamless patterns, image width must be a factor of two (2, 4, 8, ...,
     * 512).
     * 
     * Units in line widths. Disabled by line-pattern.
     * 
     */
    public Expression linePattern() {
        return parse.string(paint, "line-pattern", null);
    }
    
    /**
     * 
     * @return True if the layer has a line-pattern explicitly provided.
     */
    public boolean hasLinePattern() {
        return parse.isPropertyDefined(paint, "line-pattern");
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return TYPE;
    }

}
