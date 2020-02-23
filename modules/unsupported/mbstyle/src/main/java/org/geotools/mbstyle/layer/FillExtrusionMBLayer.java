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
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.measure.Units;
import org.geotools.styling.*;
import org.geotools.text.Text;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.SemanticType;

/**
 * MBLayer wrapper for "fill extrusion" representing extruded (3D) polygon.
 *
 * <p>Example of line JSON:
 *
 * <pre>
 * {    'id': 'room-extrusion',
 *      'type': 'fill-extrusion',
 *      'source': {
 *          'type': 'geojson',
 *          'data': 'https://www.mapbox.com/mapbox-gl-js/assets/data/indoor-3d-map.geojson'
 *      },
 *      'paint': {
 *          'fill-extrusion-color': { 'property': 'color', 'type': 'identity'},
 *          'fill-extrusion-height': { 'property': 'height','type': 'identity'},
 *          'fill-extrusion-base': { 'property': 'base_height','type': 'identity'},
 *          'fill-extrusion-opacity': 0.5
 *      }
 *  }
 * </pre>
 *
 * Responsible for accessing wrapped json as expressions (for use in transformer).
 *
 * @author jody
 */
public class FillExtrusionMBLayer extends MBLayer {

    private JSONObject paint;

    private static String TYPE = "fill-extrusion";

    public enum TranslateAnchor {
        /** Translation relative to the map. */
        MAP,

        /** Translation relative to the viewport. */
        VIEWPORT
    }

    public FillExtrusionMBLayer(JSONObject json) {
        super(json, new MBObjectParser(FillExtrusionMBLayer.class));

        paint = paint();
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.POLYGON;
    }
    /**
     * (Optional) Defaults to 1.
     *
     * <p>The opacity of the entire fill extrusion layer. This is rendered on a per-layer, not
     * per-feature, basis, and data-driven styling is not available.
     *
     * @return The opacity of the fill extrusion layer.
     */
    public Number getFillExtrusionOpacity() throws MBFormatException {
        return parse.optional(Double.class, paint, "fill-extrusion-opacity", 1.0);
    }

    /**
     * Access fill-extrusion-opacity as literal or function expression
     *
     * @return The opacity of the fill extrusion layer.
     */
    public Expression fillExtrusionOpacity() throws MBFormatException {
        return parse.percentage(paint, "fill-extrusion-opacity", 1.0);
    }

    /**
     * (Optional). Defaults to #000000. Disabled by fill-extrusion-pattern.
     *
     * <p>The base color of the extruded fill. The extrusion's surfaces will be shaded differently
     * based on this color in combination with the root light settings.
     *
     * <p>If this color is specified as rgba with an alpha component, the alpha component will be
     * ignored; use fill-extrusion-opacity to set layer opacity.
     *
     * @return The color of the extruded fill.
     */
    public Color getFillExtrusionColor() throws MBFormatException {
        return parse.optional(Color.class, paint, "fill-extrusion-color", Color.BLACK);
    }

    /**
     * Access fill-extrusion-color as literal or function expression, defaults to black.
     *
     * @return The color of the extruded fill.
     */
    public Expression fillExtrusionColor() throws MBFormatException {
        return parse.color(paint, "fill-extrusion-color", Color.BLACK);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0,0.
     *
     * <p>The geometry's offset. Values are [x, y] where negatives indicate left and up (on the flat
     * plane), respectively.
     *
     * @return The geometry's offset, in pixels.
     */
    public int[] getFillExtrusionTranslate() throws MBFormatException {
        return parse.array(paint, "fill-extrusion-translate", new int[] {0, 0});
    }

    /**
     * Access fill-extrusion-translate as Point
     *
     * @return The geometry's offset, in pixels.
     */
    public Point fillExtrusionTranslate() {
        int[] translate = getFillExtrusionTranslate();
        return new Point(translate[0], translate[1]);
    }

    /**
     * (Optional) One of map, viewport. Defaults to map. Requires fill-extrusion-translate.
     *
     * <p>Controls the translation reference point.
     *
     * <p>{@link TranslateAnchor#MAP}: The fill extrusion is translated relative to the map.
     *
     * <p>{@link TranslateAnchor#VIEWPORT}: The fill extrusion is translated relative to the
     * viewport.
     *
     * <p>Defaults to {@link TranslateAnchor#MAP}.
     *
     * @return The translation reference point
     */
    public TranslateAnchor getFillExtrusionTranslateAnchor() {
        Object value = paint.get("fill-extrusion-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return TranslateAnchor.VIEWPORT;
        } else {
            return TranslateAnchor.MAP;
        }
    }

    /**
     * (Optional) Name of image in sprite to use for drawing images on extruded fills. For seamless
     * patterns, image width and height must be a factor of two (2, 4, 8, ..., 512).
     *
     * @return The name of the image sprite, or null if not defined.
     */
    public Expression getFillExtrusionPattern() throws MBFormatException {
        return parse.string(paint, "fill-extrusion-pattern", null);
    }

    /**
     * (Optional) Units in meters. Defaults to 0. The height with which to extrude this layer.
     *
     * @return The height with which to extrude this layer.
     */
    public Number getFillExtrusionHeight() throws MBFormatException {
        return parse.optional(Double.class, paint, "fill-extrusion-height", 0.0);
    }

    /**
     * Access fill-extrusion-height as literal or function expression
     *
     * @return The height with which to extrude this layer.
     */
    public Expression fillExtrusionHeight() throws MBFormatException {
        return parse.percentage(paint, "fill-extrusion-height", 0.0);
    }

    /**
     * (Optional) Units in meters. Defaults to 0. Requires fill-extrusion-height.
     *
     * <p>The height with which to extrude the base of this layer. Must be less than or equal to
     * fill-extrusion-height.
     *
     * @return The height with which to extrude the base of this layer
     */
    public Number getFillExtrusionBase() throws MBFormatException {
        return parse.optional(Double.class, paint, "fill-extrusion-base", 0.0);
    }

    /**
     * Access fill-extrusion-base as literal or function expression
     *
     * @return The height with which to extrude the base of this layer
     */
    public Expression fillExtrusionBase() throws MBFormatException {
        return parse.percentage(paint, "fill-extrusion-base", 0.0);
    }

    /**
     * Transform {@link FillExtrusionMBLayer} to GeoTools FeatureTypeStyle.
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things
     *     like resolving sprite and glyph names to full urls.
     */
    public List<FeatureTypeStyle> transformInternal(MBStyle styleContext) {
        List<FeatureTypeStyle> fillExtrusion = new ArrayList<>();
        MBStyleTransformer transformer = new MBStyleTransformer(parse);

        // from fill pattern or fill color
        Fill fill;

        //        DisplacementImpl displacement = new DisplacementImpl();
        //
        //        displacement.setDisplacementX(getFillExtrusionBase().doubleValue());
        //        displacement.setDisplacementY(getFillExtrusionHeight().doubleValue());

        if (getFillExtrusionPattern() != null) {
            // Fill graphic (with external graphics)
            ExternalGraphic eg =
                    transformer.createExternalGraphicForSprite(
                            getFillExtrusionPattern(), styleContext);
            GraphicFill gf =
                    sf.graphicFill(
                            Arrays.asList(eg), fillExtrusionOpacity(), null, null, null, null);
            fill = sf.fill(gf, null, null);
        } else {
            fill = sf.fill(null, fillExtrusionColor(), fillExtrusionOpacity());
        }

        // Create 3 symbolizers one each for shadow, sides, and roof.
        PolygonSymbolizer shadowSymbolizer = sf.createPolygonSymbolizer();
        PolygonSymbolizer sidesSymbolizer = sf.createPolygonSymbolizer();
        PolygonSymbolizer roofSymbolizer = sf.createPolygonSymbolizer();

        shadowSymbolizer.setName("shadow");
        shadowSymbolizer.setGeometry(
                ff.function(
                        "offset",
                        ff.property((String) null),
                        ff.literal(0.005),
                        ff.literal(-0.005)));
        shadowSymbolizer.setDescription(sf.description(Text.text("fill"), null));
        shadowSymbolizer.setUnitOfMeasure(Units.PIXEL);
        shadowSymbolizer.setStroke(null);
        shadowSymbolizer.setFill(fill);
        shadowSymbolizer.setDisplacement(null);
        shadowSymbolizer.setPerpendicularOffset(ff.literal(0));

        sidesSymbolizer.setName("sides");
        sidesSymbolizer.setGeometry(
                ff.function(
                        "isometric",
                        ff.property((String) null),
                        ff.literal(fillExtrusionHeight())));
        sidesSymbolizer.setDescription(sf.description(Text.text("fill"), null));
        sidesSymbolizer.setUnitOfMeasure(Units.PIXEL);
        sidesSymbolizer.setStroke(null);
        sidesSymbolizer.setFill(fill);
        sidesSymbolizer.setDisplacement(null);
        sidesSymbolizer.setPerpendicularOffset(ff.literal(0));

        roofSymbolizer.setName("roof");
        roofSymbolizer.setGeometry(
                ff.function(
                        "offset",
                        ff.property((String) null),
                        ff.literal(fillExtrusionBase()),
                        ff.literal(fillExtrusionHeight())));
        roofSymbolizer.setDescription(sf.description(Text.text("fill"), null));
        roofSymbolizer.setUnitOfMeasure(Units.PIXEL);
        roofSymbolizer.setStroke(null);
        roofSymbolizer.setFill(fill);
        roofSymbolizer.setDisplacement(null);
        roofSymbolizer.setPerpendicularOffset(ff.literal(0));

        //        PolygonSymbolizer shadowSymbolizer = sf.polygonSymbolizer("shadow",
        //                ff.function("offset", ff.property("the_geom"), ff.literal(0.005),
        // ff.literal(-0.005)),
        //                sf.description(Text.text("fill"),null),
        //                Units.PIXEL,
        //                null,
        //                fill,
        //                null,
        //                ff.literal(0));

        //        PolygonSymbolizer sidesSymbolizer = sf.polygonSymbolizer("sides",
        //                ff.function("isometric", ff.property("the_geom"),
        // ff.literal(fillExtrusionHeight())),
        //                sf.description(Text.text("fill"),null),
        //                Units.PIXEL,
        //                null,
        //                fill,
        //                null,
        //                ff.literal(0));

        //        PolygonSymbolizer roofSymbolizer = sf.polygonSymbolizer("shadow",
        //                ff.function("offset", ff.property("the_geom"),
        // ff.literal(fillExtrusionBase()), ff.literal(fillExtrusionHeight())),
        //                sf.description(Text.text("fill"),null),
        //                Units.PIXEL,
        //                null,
        //                fill,
        //                null,
        //                ff.literal(0));

        MBFilter filter = getFilter();

        // Each symbolizer needs a rule.
        Rule shadowRule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.POSITIVE_INFINITY,
                        Arrays.asList(shadowSymbolizer),
                        filter.filter());

        Rule sidesRule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.POSITIVE_INFINITY,
                        Arrays.asList(sidesSymbolizer),
                        filter.filter());

        Rule roofRule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.POSITIVE_INFINITY,
                        Arrays.asList(roofSymbolizer),
                        filter.filter());

        // Finally we create the FeatureTypeStyles for the extrusion.
        FeatureTypeStyle shadow =
                sf.featureTypeStyle(
                        getId(),
                        sf.description(
                                Text.text("MBStyle " + getId()),
                                Text.text("Generated for " + getSourceLayer())),
                        null, // (unused)
                        Collections.emptySet(),
                        filter.semanticTypeIdentifiers(),
                        Arrays.asList(shadowRule));

        FeatureTypeStyle sides =
                sf.featureTypeStyle(
                        getId(),
                        sf.description(
                                Text.text("MBStyle " + getId()),
                                Text.text("Generated for " + getSourceLayer())),
                        null, // (unused)
                        Collections.emptySet(),
                        filter.semanticTypeIdentifiers(),
                        Arrays.asList(sidesRule));

        FeatureTypeStyle roof =
                sf.featureTypeStyle(
                        getId(),
                        sf.description(
                                Text.text("MBStyle " + getId()),
                                Text.text("Generated for " + getSourceLayer())),
                        null, // (unused)
                        Collections.emptySet(),
                        filter.semanticTypeIdentifiers(),
                        Arrays.asList(roofRule));

        fillExtrusion.add(shadow);
        fillExtrusion.add(sides);
        fillExtrusion.add(roof);

        return fillExtrusion;
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
