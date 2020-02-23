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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.parse.MBFilter;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.measure.Units;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import org.geotools.text.Text;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Displacement;
import org.opengis.style.SemanticType;

/**
 * A filled circle.
 *
 * <p>MBLayer wrapper around a {@link JSONObject} representation of a "circle" type layer. All
 * methods act as accessors on provided JSON layer, no other state is maintained. This allows
 * modifications to be made cleanly with out chance of side-effect.
 *
 * <ul>
 *   <li>get methods: access the json directly
 *   <li>query methods: provide logic / transforms to GeoTools classes as required.
 * </ul>
 *
 * @author Reggie Beckwith (Boundless)
 */
public class CircleMBLayer extends MBLayer {

    private JSONObject paint;

    private static String TYPE = "circle";

    public CircleMBLayer(JSONObject json) {
        super(json, new MBObjectParser(CircleMBLayer.class));

        paint = paint();
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.POINT;
    }

    /**
     * (Optional) Circle radius in pixels. Defaults to 5.
     *
     * @return The circle radius
     */
    public Number getCircleRadius() throws MBFormatException {
        return parse.optional(Double.class, paint, "circle-radius", 5.0);
    }

    /**
     * Access circle-radius as literal or function expression, defaults to 5
     *
     * @return The circle radius as literal or function expression
     */
    public Expression circleRadius() throws MBFormatException {
        return parse.percentage(paint, "circle-radius", 5);
    }

    /**
     * (Optional) The fill color of the circle. Defaults to #000000.
     *
     * @return The fill color of the circle
     */
    public Color getCircleColor() throws MBFormatException {
        return parse.optional(Color.class, paint, "circle-color", Color.BLACK);
    }

    /**
     * Access circle-color as literal or function expression, defaults to black.
     *
     * @return The circle color as literal or function expression
     */
    public Expression circleColor() throws MBFormatException {
        return parse.color(paint, "circle-color", Color.BLACK);
    }

    /**
     * (Optional) Amount to blur the circle. 1 blurs the circle such that only the centerpoint is
     * full opacity. Defaults to 0.
     *
     * @return The amount to blur the circle.
     */
    public Number getCircleBlur() throws MBFormatException {
        return parse.optional(Double.class, paint, "circle-blur", 0.0);
    }

    /**
     * Access circle-blur as literal or function expression, defaults to 0
     *
     * @return The amount to blur the circle, as literal or function expression
     */
    public Expression circleBlur() throws MBFormatException {
        return parse.percentage(paint, "circle-blur", 0);
    }

    /**
     * (Optional) The opacity at which the circle will be drawn. Defaults to 1.
     *
     * @return The opacity at which the circle will be drawn.
     */
    public Number getCircleOpacity() throws MBFormatException {
        return parse.optional(Double.class, paint, "circle-opacity", 1.0);
    }

    /**
     * Access circle-opacity, defaults to 1.
     *
     * @return The opacity at which the circle will be drawn as literal or function expression.
     */
    public Expression circleOpacity() throws MBFormatException {
        return parse.percentage(paint, "circle-opacity", 1);
    }

    /**
     * (Optional) The geometry's offset. Values are [x, y] where negatives indicate left and up,
     * respectively. Units in pixels. Defaults to 0, 0.
     *
     * @return x and y offset in pixels.
     */
    public int[] getCircleTranslate() throws MBFormatException {
        return parse.array(paint, "circle-translate", new int[] {0, 0});
    }

    /**
     * Access circle-translate
     *
     * @return x and y offset in pixels as Point
     */
    public Point circleTranslate() throws MBFormatException {
        int[] circleTranslate = getCircleTranslate();
        return new Point(circleTranslate[0], circleTranslate[1]);
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
     */
    public Displacement circleTranslateDisplacement() {
        return parse.displacement(
                paint, "circle-translate", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    /**
     * Controls the translation reference point.
     *
     * <p>Map: The circle is translated relative to the map. Viewport: The circle is translated
     * relative to the viewport.
     */
    public enum CircleTranslateAnchor {
        MAP,
        VIEWPORT
    }

    /**
     * (Optional) Controls the translation reference point.
     *
     * <p>{@link CircleTranslateAnchor#MAP}: The circle is translated relative to the map. {@link
     * CircleTranslateAnchor#VIEWPORT}: The circle is translated relative to the viewport.
     *
     * <p>Defaults to {@link CircleTranslateAnchor#MAP}. Requires circle-translate.
     *
     * @return The translation reference point.
     */
    public CircleTranslateAnchor getCircleTranslateAnchor() {
        Object value = paint.get("circle-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return CircleTranslateAnchor.VIEWPORT;
        } else {
            return CircleTranslateAnchor.MAP;
        }
    }

    /**
     * Controls the translation reference point.
     *
     * <p>Map: The circle is translated relative to the map. Viewport: The circle is translated
     * relative to the viewport.
     */
    public enum CirclePitchScale {
        MAP,
        VIEWPORT
    }

    /**
     * (Optional) Controls the scaling behavior of the circle when the map is pitched.
     *
     * <p>{@link CirclePitchScale#MAP}: Circles are scaled according to their apparent distance to
     * the camera. {@link CirclePitchScale#VIEWPORT}: Circles are not scaled.
     *
     * <p>Defaults to {@link CirclePitchScale#MAP}.
     *
     * @return The circle scaling behavior.
     */
    public CirclePitchScale getCirclePitchScale() {
        Object value = paint.get("circle-pitch-scale");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return CirclePitchScale.VIEWPORT;
        } else {
            return CirclePitchScale.MAP;
        }
    }

    /**
     * (Optional) The width of the circle's stroke. Strokes are placed outside of the circle-radius.
     *
     * <p>Units in pixels. Defaults to 0.
     *
     * @return The circle stroke width.
     */
    public Number getCircleStrokeWidth() throws MBFormatException {
        return parse.optional(Double.class, paint, "circle-stroke-width", 0.0);
    }

    /**
     * Access circle-stroke-width, defaults to 0.
     *
     * @return The circle stroke width.
     */
    public Expression circleStrokeWidth() throws MBFormatException {
        return parse.percentage(paint, "circle-stroke-width", 0);
    }

    /**
     * (Optional) The stroke color of the circle.
     *
     * <p>Defaults to #000000.
     *
     * @return The color of the circle stroke.
     */
    public Color getCircleStrokeColor() throws MBFormatException {
        return parse.optional(Color.class, paint, "circle-stroke-color", Color.BLACK);
    }

    /**
     * Access circle-stroke-color as literal or function expression, defaults to black.
     *
     * @return The color of the circle stroke.
     */
    public Expression circleStrokeColor() throws MBFormatException {
        return parse.color(paint, "circle-stroke-color", Color.BLACK);
    }

    /**
     * (Optional) The opacity of the circle's stroke.
     *
     * <p>Defaults to 1.
     *
     * @return Number representing the stroke opacity.
     */
    public Number getCircleStrokeOpacity() throws MBFormatException {
        return parse.optional(Double.class, paint, "circle-stroke-opacity", 1.0);
    }

    /**
     * Access circle-stroke-opacity, defaults to 1.
     *
     * @return Number representing the stroke opacity.
     */
    public Expression circleStrokeOpacity() throws MBFormatException {
        return parse.percentage(paint, "circle-stroke-opacity", 1);
    }

    /**
     * Transform {@link CircleMBLayer} to GeoTools FeatureTypeStyle.
     *
     * <p>Notes:
     *
     * <ul>
     * </ul>
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things
     *     like resolving sprite and glyph names to full urls.
     * @return FeatureTypeStyle
     */
    public List<FeatureTypeStyle> transformInternal(MBStyle styleContext) {
        // default linecap because StrokeImpl.getOpacity has a bug. If lineCap == null, it returns a
        // default opacity.
        Stroke s =
                sf.stroke(
                        circleStrokeColor(),
                        circleStrokeOpacity(),
                        circleStrokeWidth(),
                        null,
                        Stroke.DEFAULT.getLineCap(),
                        null,
                        null);
        Fill f = sf.fill(null, circleColor(), circleOpacity());
        Mark m = sf.mark(ff.literal("circle"), f, s);

        Graphic gr =
                sf.graphic(
                        Arrays.asList(m),
                        null,
                        ff.multiply(ff.literal(2), circleRadius()),
                        null,
                        null,
                        circleTranslateDisplacement());
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(m);

        PointSymbolizer ps =
                sf.pointSymbolizer(
                        getId(),
                        ff.property((String) null),
                        sf.description(
                                Text.text("MBStyle " + getId()),
                                Text.text("Generated for " + getSourceLayer())),
                        Units.PIXEL,
                        gr);

        MBFilter filter = getFilter();

        List<org.opengis.style.Rule> rules = new ArrayList<>();
        Rule rule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.POSITIVE_INFINITY,
                        Arrays.asList(ps),
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
