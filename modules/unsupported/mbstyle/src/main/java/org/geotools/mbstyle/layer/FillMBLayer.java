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

import java.awt.*;
import java.util.*;
import java.util.List;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.parse.MBFilter;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.measure.Units;
import org.geotools.styling.*;
import org.geotools.text.Text;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.Displacement;
import org.opengis.style.GraphicFill;
import org.opengis.style.SemanticType;

/**
 * MBLayer wrapper for "Fill" layers.
 *
 * <p>Example of line JSON:
 *
 * <pre>
 *      {   "type": "line",
 *          "source": "http://localhost:8080/geoserver/ne/roads",
 *          "source-layer": "road"
 *          "id": "roads",
 *          "paint": {
 *              "fill-anitalias": true,
 *              "fill-opacity": 1
 *              "fill-color": "#6655ae",
 *              "fill-outline-color": "#000000",
 *              "fill-translate": [0,0],
 *              "fill-translate-anchor": "map",
 *              "fill-pattern": "triangle" // Name of image in sprite to use for drawing image fills. For seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512).
 *          },
 *      },
 * </pre>
 *
 * @author Reggie Beckwith (Boundless)
 */
public class FillMBLayer extends MBLayer {

    private JSONObject paint;

    private static String TYPE = "fill";

    /** Controls the translation reference point. */
    public static enum FillTranslateAnchor {
        /** The fill is translated relative to the map. */
        MAP,
        /** The fill is translated relative to the viewport. */
        VIEWPORT
    }

    public FillMBLayer(JSONObject json) {
        super(json, new MBObjectParser(FillMBLayer.class));

        paint = paint();
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.POLYGON;
    }

    /**
     * (Optional) Whether or not the fill should be antialiased.
     *
     * <p>Defaults to true.
     *
     * @return Whether the fill should be antialiased.
     */
    public Expression getFillAntialias() {
        return parse.bool(paint, "fill-antialias", true);
    }

    /**
     * (Optional) The opacity of the entire fill layer. In contrast to the fill-color, this value
     * will also affect the 1px stroke around the fill, if the stroke is used.
     *
     * <p>Defaults to 1.
     *
     * @return The opacity of the layer.
     */
    public Number getFillOpacity() throws MBFormatException {
        return parse.optional(Double.class, paint, "fill-opacity", 1.0);
    }

    /**
     * Access fill-opacity, defaults to 1.
     *
     * @return The opacity of the layer.
     */
    public Expression fillOpacity() throws MBFormatException {
        return parse.percentage(paint, "fill-opacity", 1);
    }

    /**
     * (Optional). The color of the filled part of this layer. This color can be specified as rgba
     * with an alpha component and the color's opacity will not affect the opacity of the 1px
     * stroke, if it is used.
     *
     * <p>Colors are written as JSON strings in a variety of permitted formats.
     *
     * <p>Defaults to #000000. Disabled by fill-pattern.
     *
     * @return The fill color.
     */
    public Color getFillColor() {
        return parse.convertToColor(parse.optional(String.class, paint, "fill-color", "#000000"));
    }

    /**
     * Access fill-color as literal or function expression, defaults to black.
     *
     * @return The fill color.
     */
    public Expression fillColor() {
        return parse.color(paint, "fill-color", Color.BLACK);
    }

    /**
     * (Optional). Requires fill-antialias = true. The outline color of the fill.
     *
     * <p>Matches the value of fill-color if unspecified. Disabled by fill-pattern.
     *
     * @return The outline color of the fill.
     */
    public Color getFillOutlineColor() {
        if (paint.get("fill-outline-color") != null) {
            return parse.convertToColor(
                    parse.optional(String.class, paint, "fill-outline-color", "#000000"));
        } else {
            return getFillColor();
        }
    }

    /**
     * Access fill-outline-color as literal or function expression, defaults to black.
     *
     * @return The outline color of the fill.
     */
    public Expression fillOutlineColor() {
        if (paint.get("fill-outline-color") != null) {
            return parse.color(paint, "fill-outline-color", Color.BLACK);
        } else {
            return fillColor();
        }
    }

    /**
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up,
     * respectively. Units in pixels. Defaults to 0, 0.
     *
     * @return The geometry's offset
     */
    public int[] getFillTranslate() {
        return parse.array(paint, "fill-translate", new int[] {0, 0});
    }

    /**
     * Access fill-translate as Point.
     *
     * @return The geometry's offset, in pixels.
     */
    public Point fillTranslate() {
        int[] translate = getFillTranslate();
        return new Point(translate[0], translate[1]);
    }

    /**
     * Processes the filter-translate into a Displacement.
     *
     * <p>This should handle both literals and function stops:
     *
     * <pre>
     * filter-translate: [0,0]
     * filter-translate: { property: "building-height", "stops": [[0,[0,0]],[5,[1,2]]] }
     * filter-translate: [ 0, { property: "building-height", "TYPE":"exponential","stops": [[0,0],[30, 5]] }
     * </pre>
     *
     * @return The geometry displacement
     */
    public Displacement fillTranslateDisplacement() {
        return parse.displacement(
                paint, "fill-translate", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    /**
     * (Optional) Controls the translation reference point.
     *
     * <ul>
     *   <li>{@link FillTranslateAnchor#MAP}: The fill is translated relative to the map.
     *   <li>{@link FillTranslateAnchor#VIEWPORT}: The fill is translated relative to the viewport.
     * </ul>
     *
     * Requires fill-translate.
     *
     * @return One of 'map','viewport', defaults to 'map'.
     */
    public FillTranslateAnchor getFillTranslateAnchor() {
        Object value = paint.get("fill-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return FillTranslateAnchor.VIEWPORT;
        } else {
            return FillTranslateAnchor.MAP;
        }
    }

    /**
     * (Optional) Name of image in a sprite to use for drawing image fills. For seamless patterns,
     * image width and height must be a factor of two (2, 4, 8, ..., 512).
     *
     * @return name of the sprite for the fill pattern, or null if not defined.
     */
    public Expression fillPattern() {
        return parse.string(paint, "fill-pattern", null);
    }

    /** @return True if the layer has a fill-pattern explicitly provided. */
    public boolean hasFillPattern() {
        return parse.isPropertyDefined(paint, "fill-pattern");
    }

    /**
     * Transform MBFillLayer to GeoTools FeatureTypeStyle.
     *
     * <p>Notes:
     *
     * <ul>
     *   <li>stroke-width is assumed to be 1 (not specified by MapBox style)
     * </ul>
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things
     *     like resolving sprite and glyph names to full urls.
     * @return FeatureTypeStyle
     */
    public List<FeatureTypeStyle> transformInternal(MBStyle styleContext) {
        MBStyleTransformer transformer = new MBStyleTransformer(parse);
        PolygonSymbolizer symbolizer;
        // use factory to avoid defaults values
        org.geotools.styling.Stroke stroke =
                sf.stroke(
                        fillOutlineColor(),
                        fillOpacity(),
                        ff.literal(1),
                        ff.literal("miter"),
                        ff.literal("butt"),
                        null,
                        null);

        // from fill pattern or fill color
        Fill fill;
        if (hasFillPattern()) {

            // If the fill-pattern is a literal string (not a function), then
            // we need to support Mapbox {token} replacement.
            Expression fillPatternExpr = fillPattern();
            if (fillPatternExpr instanceof Literal) {
                String text = fillPatternExpr.evaluate(null, String.class);
                if (text.trim().isEmpty()) {
                    fillPatternExpr = ff.literal(" ");
                } else {
                    fillPatternExpr = transformer.cqlExpressionFromTokens(text);
                }
            }

            ExternalGraphic eg =
                    transformer.createExternalGraphicForSprite(fillPatternExpr, styleContext);
            GraphicFill gf =
                    sf.graphicFill(
                            Arrays.asList(eg),
                            fillOpacity(),
                            null,
                            null,
                            null,
                            fillTranslateDisplacement());
            stroke.setOpacity(ff.literal(0));
            fill = sf.fill(gf, null, null);
        } else {
            fill = sf.fill(null, fillColor(), fillOpacity());
        }

        symbolizer =
                sf.polygonSymbolizer(
                        getId(),
                        ff.property((String) null),
                        sf.description(Text.text("fill"), null),
                        Units.PIXEL,
                        stroke,
                        fill,
                        fillTranslateDisplacement(),
                        ff.literal(0));

        MBFilter filter = getFilter();

        Rule rule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.POSITIVE_INFINITY,
                        Arrays.asList(symbolizer),
                        filter.filter());

        return Collections.singletonList(
                sf.featureTypeStyle(
                        getId(),
                        sf.description(
                                Text.text("MBStyle " + getId()),
                                Text.text("Generated for " + getSourceLayer())),
                        null, // (unused)
                        Collections.emptySet(),
                        filter.semanticTypeIdentifiers(),
                        Arrays.asList(rule)));
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
