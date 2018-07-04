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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.parse.MBFilter;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.measure.Units;
import org.geotools.styling.*;
import org.geotools.text.Text;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Rule;
import org.opengis.style.SemanticType;

public class RasterMBLayer extends MBLayer {

    private JSONObject paintJson;

    private static String TYPE = "raster";

    public RasterMBLayer(JSONObject json) {
        super(json, new MBObjectParser(RasterMBLayer.class));

        if (json.get("paint") != null) {
            paintJson = (JSONObject) json.get("paint");
        } else {
            paintJson = new JSONObject();
        }
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.RASTER;
    }

    /**
     * (Optional) The opacity (Number) at which the image will be drawn.
     *
     * <p>Defaults to 1. Range: [0, 1]
     *
     * @return The opacity of this raster layer.
     */
    public Expression opacity() {
        return parse.percentage(paintJson, "raster-opacity", 1);
    }

    /**
     * (Optional) The opacity (Number) at which the image will be drawn.
     *
     * <p>Defaults to 1. Range: [0, 1]
     *
     * @return The opacity of this raster layer.
     */
    public Number getOpacity() {
        return parse.optional(Number.class, paintJson, "raster-opacity", 1);
    }

    /**
     * (Optional) Rotates hues around the color wheel.
     *
     * <p>Number. Units in degrees. Defaults to 0.
     *
     * @return The angle to rotate the hue of the raster image by.
     */
    public Expression hueRotate() {
        return parse.number(paintJson, "raster-hue-rotate", 0);
    }

    /**
     * (Optional) Rotates hues around the color wheel.
     *
     * <p>Number. Units in degrees. Defaults to 0.
     *
     * @return The angle to rotate the hue of the raster image by.
     */
    public Number getHueRotate() {
        return parse.optional(Number.class, paintJson, "raster-hue-rotate", 0);
    }

    /**
     * (Optional) Scale the brightness of the image. The value is the minimum brightness.
     *
     * <p>Number. Defaults to 0. Range: [0, 1]
     *
     * @return The minimum magnitude of the brightness.
     */
    public Expression brightnessMin() {
        return parse.number(paintJson, "raster-brightness-min", 0);
    }

    /**
     * (Optional) Scale the brightness of the image. The value is the minimum brightness.
     *
     * <p>Number. Defaults to 0. Range: [0, 1]
     *
     * @return The minimum magnitude of the brightness.
     */
    public Number getBrightnessMin() {
        return parse.optional(Number.class, paintJson, "raster-brightness-min", 0);
    }

    /**
     * (Optional) Scale the brightness of the image. The value is the maximum brightness.
     *
     * <p>Number. Defaults to 1. Range: [0, 1]
     *
     * @return The maximum magnitude of the brightness.
     */
    public Expression brightnessMax() {
        return parse.number(paintJson, "raster-brightness-max", 1);
    }

    /**
     * (Optional) Scale the brightness of the image. The value is the maximum brightness.
     *
     * <p>Number. Defaults to 1. Range: [0, 1]
     *
     * @return The maximum magnitude of the brightness.
     */
    public Number getBrightnessMax() {
        return parse.optional(Number.class, paintJson, "raster-brightness-max", 1);
    }

    /**
     * (Optional) Increase or reduce the saturation of the image.
     *
     * <p>Number. Defaults to 0. Range: [-1, 1]
     *
     * @return The change in saturation
     */
    public Expression saturation() {
        return parse.number(paintJson, "raster-saturation", 0);
    }

    /**
     * (Optional) Increase or reduce the saturation of the image.
     *
     * <p>Number. Defaults to 0. Range: [-1, 1]
     *
     * @return The change in saturation
     */
    public Number getSaturation() {
        return parse.optional(Number.class, paintJson, "raster-saturation", 0);
    }

    /**
     * (Optional) Increase or reduce the contrast of the image.
     *
     * <p>Number. Defaults to 0. Range: [-1, 1]
     *
     * @return The change in contrast
     */
    public Expression contrast() {
        return parse.number(paintJson, "raster-contrast", 0);
    }

    /**
     * (Optional) Increase or reduce the contrast of the image.
     *
     * <p>Number. Defaults to 0. Range: [-1, 1]
     *
     * @return The change in contrast
     */
    public Number getContrast() {
        return parse.optional(Number.class, paintJson, "raster-contrast", 0);
    }

    /**
     * (Optional) Fade duration when a new tile is added.
     *
     * <p>Number. Units in milliseconds. Defaults to 300.
     *
     * @return The duration of the fade when a new tile is added.
     */
    public Expression fadeDuration() {
        return parse.number(paintJson, "raster-fade-duration", 300);
    }

    /**
     * (Optional) Fade duration when a new tile is added.
     *
     * <p>Number. Units in milliseconds. Defaults to 300.
     *
     * @return The duration of the fade when a new tile is added.
     */
    public Number getFadeDuration() {
        return parse.optional(Number.class, paintJson, "raster-fade-duration", 300);
    }

    /**
     * Transform {@link RasterMBLayer} to GeoTools FeatureTypeStyle.
     *
     * <p>Notes:
     *
     * <ul>
     *   <li>Assumes 3-band RGB
     * </ul>
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things
     *     like resolving sprite and glyph names to full urls.
     * @return FeatureTypeStyle
     */
    public List<FeatureTypeStyle> transformInternal(MBStyle styleContext) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NONE);

        // Use of builder is easier for code examples; but fills in SLD defaults
        // Currently only applies the opacity.
        RasterSymbolizer symbolizer =
                sf.rasterSymbolizer(
                        getId(),
                        null,
                        sf.description(Text.text("raster"), null),
                        Units.PIXEL,
                        opacity(),
                        null,
                        null,
                        null,
                        ce,
                        null,
                        null);

        List<Rule> rules = new ArrayList<>();
        MBFilter filter = getFilter();
        org.geotools.styling.Rule rule =
                sf.rule(
                        getId(),
                        null,
                        null,
                        0.0,
                        Double.MAX_VALUE,
                        Arrays.asList(symbolizer),
                        filter.filter());
        rules.add(rule);
        rule.setLegendGraphic(new Graphic[0]);

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
    public String getType() {
        return TYPE;
    }
}
