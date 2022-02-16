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
package org.geotools.mbstyle.layer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.parse.MBFilter;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.measure.Units;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.GraphicFill;
import org.opengis.style.SemanticType;
import org.opengis.style.Symbolizer;

/**
 * MBLayer wrapper for "line" layers.
 *
 * <p>Example of line JSON:
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
 */
public class LineMBLayer extends MBLayer {
    private final JSONObject layout;

    private final JSONObject paint;

    private static final String TYPE = "line";

    public LineMBLayer(JSONObject json) {
        super(json, new MBObjectParser(LineMBLayer.class));
        paint = super.getPaint();
        layout = super.getLayout();
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.LINE;
    }

    /** The display of line endings. */
    public enum LineCap {
        /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
        BUTT,
        /**
         * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of
         * one-half of the line's width and centered on the endpoint of the line.
         */
        ROUND,
        /**
         * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance
         * of one-half of the line's width.
         */
        SQUARE
    }

    /**
     * Display of line endings.
     *
     * <p>Supports piecewise constant functions.
     *
     * @return One of butt, round, square, optional defaults to butt.
     */
    public LineCap getLineCap() {
        return parse.getEnum(layout, "line-cap", LineCap.class, LineCap.BUTT);
    }

    /**
     * Maps {@link #getLineCap()} to {@link Stroke#getLineCap()} values of "butt", "round", and
     * "square" Literals. Defaults to butt.
     *
     * <p>Since piecewise constant functions is supported a {@link RecodeFunction} may be generated.
     *
     * @return Expression for {@link Stroke#getLineCap()} use.
     */
    public Expression lineCap() {
        return parse.enumToExpression(layout, "line-cap", LineCap.class, LineCap.BUTT);
    }

    /**
     * (Optional) The display of lines when joining.
     *
     * <p>Bevel - A join with a squared-off end which is drawn beyond the endpoint of the line at a
     * distance of one-half of the line's width.
     *
     * <p>Round - A join with a rounded end which is drawn beyond the endpoint of the line at a
     * radius of one-half of the line's width and centered on the endpoint of the line.
     *
     * <p>Miter - A join with a sharp, angled corner which is drawn with the outer sides beyond the
     * endpoint of the path until they meet.
     */
    public enum LineJoin {
        BEVEL,
        ROUND,
        MITER
    }

    /**
     * Optional enum. One of bevel, round, miter. Defaults to miter. The display of lines when
     * joining.
     *
     * @return The line join
     */
    public LineJoin getLineJoin() {
        return parse.getEnum(layout, "line-join", LineJoin.class, LineJoin.MITER);
    }

    /**
     * Maps {@link #getLineJoin()} to {@link Stroke#getLineJoin()} values of "mitre", "round", and
     * "bevel" Literals. Defaults to "mitre".
     *
     * <p>Since piecewise constant functions is supported a {@link RecodeFunction} may be generated.
     *
     * @return Expression for {@link Stroke#getLineJoin()} use.
     */
    public Expression lineJoin() {
        return parse.enumToExpression(layout, "line-join", LineJoin.class, LineJoin.MITER);
    }

    /**
     * (Optional) Used to automatically convert miter joins to bevel joins for sharp angles.
     *
     * <p>Defaults to 2. Requires line-join = miter.
     *
     * @return The threshold at which miter joins are converted to bevel joins.
     */
    public Number getLineMiterLimit() {
        return parse.optional(Number.class, layout, "line-miter-limit", 2);
    }

    /**
     * Maps {@link #getLineMiterLimit()} to an {@link Expression}. (Optional) Used to automatically
     * convert miter joins to bevel joins for sharp angles.
     *
     * <p>Defaults to 2. Requires line-join = miter.
     *
     * @return Expression for {@link #getLineMiterLimit()}
     */
    public Expression lineMiterLimit() {
        return parse.number(layout, "line-miter-limit", 2);
    }

    /**
     * (Optional) Used to automatically convert round joins to bevel joins for sharp angles.
     *
     * <p>Defaults to 1.05. Requires line-join = round.
     *
     * @return The threshold at which round joins are converted to bevel joins.
     */
    public Number getLineRoundLimit() {
        return parse.optional(Number.class, layout, "line-round-limit", 1.05);
    }

    /**
     * Maps {@link #getLineRoundLimit()} to an {@link Expression}.
     *
     * <p>(Optional) Used to automatically convert round joins to bevel joins for sharp angles.
     *
     * <p>Defaults to 1.05. Requires line-join = round.
     *
     * @return Expression providing threshold at which round joins are converted to bevel joins.
     */
    public Expression lineRoundLimit() {
        return parse.number(layout, "line-round-limit", 1.05);
    }

    /**
     * (Optional) The opacity at which the line will be drawn.
     *
     * <p>Defaults to 1.
     *
     * @return The line opacity
     */
    public Number getLineOpacity() {
        return parse.optional(Number.class, paint, "line-opacity", 1);
    }

    /**
     * Maps {@link #getLineOpacity()} to an {@link Expression}.
     *
     * <p>(Optional) The opacity at which the line will be drawn.
     *
     * <p>Defaults to 1.
     *
     * @return opacity for line (literal or function), defaults to 1.
     */
    public Expression lineOpacity() {
        return parse.number(paint, "line-opacity", 1);
    }

    /**
     * (Optional) The color with which the line will be drawn.
     *
     * <p>Defaults to {@link Color#BLACK}, disabled by line-pattern.
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
     * Maps {@link #getLineColor()} to an {@link Expression}.
     *
     * <p>(Optional) The color with which the line will be drawn.
     *
     * <p>Defaults to {@link Color#BLACK}, disabled by line-pattern.
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
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up,
     * respectively.
     *
     * <p>Units in pixels. Defaults to 0,0.
     *
     * @return The geometry's offset.
     */
    public int[] getLineTranslate() {
        return parse.array(paint, "line-translate", new int[] {0, 0});
    }

    /**
     * Maps {@link #getLineTranslate()} to a {@link Displacement}.
     *
     * <p>(Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up,
     * respectively.
     *
     * <p>Units in pixels. Defaults to 0,0.
     *
     * @return The geometry's offset, as a Displacement.
     */
    public Displacement lineTranslateDisplacement() {
        return parse.displacement(
                paint, "line-translate", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    /**
     * Controls the translation reference point.
     *
     * <p>Map: The fill is translated relative to the map.
     *
     * <p>Viewport: The fill is translated relative to the viewport.
     */
    public enum LineTranslateAnchor {
        MAP,
        VIEWPORT
    }

    /**
     * (Optional) Controls the translation reference point.
     *
     * <p>{@link LineTranslateAnchor#MAP}: The fill is translated relative to the map.
     *
     * <p>{@link LineTranslateAnchor#VIEWPORT}: The fill is translated relative to the viewport.
     *
     * <p>Defaults to {@link LineTranslateAnchor#MAP}. Requires fill-translate.
     *
     * @return The translation reference point.
     */
    public LineTranslateAnchor getLineTranslateAnchor() {
        return parse.getEnum(
                paint, "line-translate-anchor", LineTranslateAnchor.class, LineTranslateAnchor.MAP);
    }

    /**
     * Wraps {@link #getLineTranslateAnchor()} in a GeoTools expression. Returns an expression that
     * evaluates to "map" or "viewport".
     *
     * @return Expression providing translation reference point.
     */
    public Expression lineTranslateAnchor() {
        return parse.enumToExpression(
                paint, "line-translate-anchor", LineTranslateAnchor.class, LineTranslateAnchor.MAP);
    }

    /**
     * (Optional) Stroke thickness.
     *
     * <p>Units in pixels. Defaults to 1.
     *
     * @return The stroke thickness.
     */
    public Number getLineWidth() {
        if (paint.get("line-width") != null) {
            return (Number) paint.get("line-width");
        } else {
            return 1;
        }
    }

    /**
     * Convert {@link #getLineWidth()} to an Expression.
     *
     * <p>(Optional) Stroke thickness. Units in pixels. Defaults to 1.
     *
     * @return The stroke thickness.
     */
    public Expression lineWidth() {
        return parse.number(paint, "line-width", 1);
    }

    /**
     * (Optional) Draws a line casing outside of a line's actual path. Value indicates the width of
     * the inner gap.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The inner gap between the sides of the line casing
     */
    public Number getLineGapWidth() {
        return parse.optional(Number.class, paint, "line-gap-width", 0);
    }

    /**
     * Converts {@link #getLineGapWidth()} to an Expression.
     *
     * <p>(Optional) Draws a line casing outside of a line's actual path. Value indicates the width
     * of the inner gap.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The inner gap between the sides of the line casing
     */
    public Expression lineGapWidth() {
        return parse.number(paint, "line-gap-width", 0);
    }

    /**
     * (Optional) The line's offset. For linear features, a positive value offsets the line to the
     * right, relative to the direction of the line, and a negative value to the left. For polygon
     * features, a positive value results in an inset, and a negative value results in an outset.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The line's offset.
     */
    public Number getLineOffset() {
        return parse.optional(Number.class, paint, "line-offset", 0);
    }

    /**
     * Converts {@link #getLineOffset()} to an Expression.
     *
     * <p>(Optional) The line's offset. For linear features, a positive value offsets the line to
     * the right, relative to the direction of the line, and a negative value to the left. For
     * polygon features, a positive value results in an inset, and a negative value results in an
     * outset.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The line's offset.
     */
    public Expression lineOffset() {
        return parse.number(paint, "line-offset", 0);
    }

    /**
     * (Optional) Blur applied to the line, in pixels.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The line blur.
     */
    public Number getLineBlur() {
        return parse.optional(Number.class, paint, "line-blur", 0);
    }

    /**
     * Converts {@link #getLineBlur()} to an Expression.
     *
     * <p>(Optional) Blur applied to the line, in pixels.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The line blur.
     */
    public Expression lineBlur() {
        return parse.number(paint, "line-blur", 0);
    }

    /**
     * (Optional) Specifies the lengths of the alternating dashes and gaps that form the dash
     * pattern. The lengths are later scaled by the line width. To convert a dash length to pixels,
     * multiply the length by the current line width.
     *
     * <p>Units in line widths. Disabled by line-pattern.
     *
     * @return A list of dash and gap lengths defining the pattern for a dashed line.
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
     * <p>(Optional) Specifies the lengths of the alternating dashes and gaps that form the dash
     * pattern. The lengths are later scaled by the line width. To convert a dash length to pixels,
     * multiply the length by the current line width.
     *
     * <p>Units in line widths. Disabled by line-pattern.
     *
     * @return A list of dash and gap lengths defining the pattern for a dashed line.
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
            throw new MBFormatException(
                    "\"line-dasharray\": Expected array or function, but was "
                            + defn.getClass().getSimpleName());
        }
    }

    /**
     * (Optional) Name of image in sprite to use for drawing image lines. For seamless patterns,
     * image width must be a factor of two (2, 4, 8, ..., 512).
     *
     * <p>Units in line widths. Disabled by line-pattern.
     *
     * @return The name of the sprite to use for the line pattern.
     */
    public String getLinePattern() {
        return parse.optional(String.class, paint, "line-pattern", null);
    }

    /**
     * Converts {@link #getLinePattern()} to an Expression.
     *
     * <p>(Optional) Name of image in sprite to use for drawing image lines. For seamless patterns,
     * image width must be a factor of two (2, 4, 8, ..., 512).
     *
     * <p>Units in line widths. Disabled by line-pattern.
     *
     * @return Expression providing name of the sprite to use for the line pattern.
     */
    public Expression linePattern() {
        return parse.string(paint, "line-pattern", null);
    }

    /** @return True if the layer has a line-pattern explicitly provided. */
    public boolean hasLinePattern() {
        return parse.isDefined(paint, "line-pattern");
    }

    /** @return True if the layer has a line-gap-width explicitly provided. */
    public boolean hasLineGapWidth() {
        return parse.isDefined(paint, "line-gap-width");
    }

    /**
     * Transform {@link LineMBLayer} to GeoTools FeatureTypeStyle.
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things
     *     like resolving sprite and glyph names to full urls.
     * @return FeatureTypeStyle
     */
    @Override
    public List<FeatureTypeStyle> transformInternal(MBStyle styleContext) {
        MBStyleTransformer transformer = new MBStyleTransformer(parse);
        List<Symbolizer> symbolizers = new ArrayList<>();
        org.geotools.styling.Stroke stroke =
                sf.stroke(
                        lineColor(),
                        lineOpacity(),
                        lineWidth(),
                        lineJoin(),
                        lineCap(),
                        null,
                        null); // last "offset" is really "dash offset"

        stroke.setDashArray(scaleByWidth(lineDasharray(), lineWidth()));
        LineSymbolizer ls =
                sf.lineSymbolizer(
                        getId(),
                        null,
                        sf.description(Text.text("line"), null),
                        Units.PIXEL,
                        stroke,
                        lineOffset());

        if (hasLinePattern()) {
            ExternalGraphic eg =
                    transformer.createExternalGraphicForSprite(linePattern(), styleContext);
            GraphicFill fill =
                    sf.graphicFill(Arrays.asList(eg), lineOpacity(), null, null, null, null);
            stroke.setGraphicFill(fill);
        }

        if (hasLineGapWidth()) {
            Expression topOffset =
                    ff.add(
                            lineOffset(),
                            ff.divide(ff.add(lineGapWidth(), lineWidth()), ff.literal(2)));
            Expression bottomOffset =
                    ff.subtract(
                            lineOffset(),
                            ff.divide(ff.add(lineGapWidth(), lineWidth()), ff.literal(2)));

            ls =
                    sf.lineSymbolizer(
                            getId(),
                            null,
                            sf.description(Text.text("line"), null),
                            Units.PIXEL,
                            stroke,
                            topOffset);
            LineSymbolizer bottomLine =
                    sf.lineSymbolizer(
                            getId(),
                            null,
                            sf.description(Text.text("line"), null),
                            Units.PIXEL,
                            stroke,
                            bottomOffset);
            symbolizers.add(bottomLine);
        }
        symbolizers.add(ls);

        MBFilter filter = getFilter();
        List<org.opengis.style.Rule> rules = new ArrayList<>();

        Rule rule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.POSITIVE_INFINITY,
                        symbolizers,
                        filter.filter());
        rules.add(rule);

        return Collections.singletonList(
                sf.featureTypeStyle(
                        getId(),
                        sf.description(
                                Text.text("MBStyle " + getId()),
                                Text.text("Generated for " + getSourceLayer())),
                        null,
                        Collections.emptySet(),
                        filter.semanticTypeIdentifiers(),
                        rules));
    }

    private List<Expression> scaleByWidth(List<Expression> dasharray, Expression lineWidth) {
        if (dasharray == null) {
            return null;
        }

        // no need for scaling if the width happens to be exactly 1
        if (lineWidth instanceof Literal
                && Double.valueOf(1).equals(lineWidth.evaluate(null, Double.class))) {
            return dasharray;
        }

        return dasharray.stream()
                .map(dash -> ff.multiply(dash, lineWidth))
                .collect(Collectors.toList());
    }

    /**
     * Rendering type of this layer.
     *
     * @return {@link #TYPE}
     */
    @Override
    public String getType() {
        return TYPE;
    }
}
