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
import java.util.Arrays;
import java.util.List;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.SemanticType;

/**
 * The background color or pattern of the map.
 *
 * <p>MBLayer wrapper around a {@link JSONObject} representation of a "background" type latyer. All
 * methods act as accessors on provided JSON layer, no other state is maintained. This allows
 * modifications to be made cleanly with out chance of side-effect.
 *
 * <ul>
 *   <li>get methods: access the json directly
 *   <li>query methods: provide logic / transforms to GeoTools classes as required.
 * </ul>
 */
public class BackgroundMBLayer extends MBLayer {

    private JSONObject paint;

    private static String TYPE = "background";

    public BackgroundMBLayer(JSONObject json) {
        super(json, new MBObjectParser(BackgroundMBLayer.class));
        paint = paint();
    }

    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.POLYGON;
    }

    /**
     * Optional color. Defaults to #000000. Disabled by background-pattern.
     *
     * @return The color with which the background will be drawn.
     */
    public Color getBackgroundColor() {
        return parse.convertToColor(
                parse.optional(String.class, paint, "background-color", "#000000"));
    }

    /**
     * Maps {@link #getBackgroundColor()} to an {@link Expression}.
     *
     * <p>Optional color. Defaults to #000000. Disabled by background-pattern.
     *
     * @return The color with which the background will be drawn.
     */
    public Expression backgroundColor() {
        return parse.color(paint, "background-color", Color.BLACK);
    }

    /**
     * Optional string. Name of image in sprite to use for drawing an image background. For seamless
     * patterns, image width and height must be a factor of two (2, 4, 8, ..., 512).
     *
     * @return Name of image in sprite to use for drawing an image background, or null if not
     *     defined.
     */
    public String getBackgroundPattern() {
        return parse.optional(String.class, paint, "background-pattern", null);
    }

    /** @return True if the layer has a background-pattern explicitly provided. */
    public boolean hasBackgroundPattern() {
        return parse.isPropertyDefined(paint, "background-pattern");
    }

    /**
     * Maps {@link #getBackgroundPattern()} to an {@link Expression}.
     *
     * <p>Optional string. Name of image in sprite to use for drawing an image background. For
     * seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512).
     *
     * @return Name of image in sprite to use for drawing an image background, or null if not
     *     defined.
     */
    public Expression backgroundPattern() {
        return parse.string(paint, "background-pattern", null);
    }

    /**
     * Optional number. Defaults to 1.
     *
     * @return The opacity at which the background will be drawn.
     */
    public Number getBackgroundOpacity() {
        return parse.optional(Number.class, paint, "background-opacity", 1.0);
    }

    /**
     * Maps {@link #getBackgroundOpacity()} to an {@link Expression}.
     *
     * <p>Optional number. Defaults to 1.
     *
     * @retur The opacity at which the background will be drawn.
     */
    public Expression backgroundOpacity() {
        return parse.percentage(paint, "background-opacity", 1.0);
    }

    /**
     * Transform {@link BackgroundMBLayer} to GeoTools FeatureTypeStyle.
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
        throw new UnsupportedOperationException(
                "This layer is not meant to be transformed, but converted in a Style background fill");
    }

    public Fill getFill(MBStyle styleContext) {
        return getFill(
                styleContext, new MBStyleTransformer(new MBObjectParser(BackgroundMBLayer.class)));
    }

    private Fill getFill(MBStyle styleContext, MBStyleTransformer transformer) {
        if (hasBackgroundPattern()) {
            ExternalGraphic eg =
                    transformer.createExternalGraphicForSprite(backgroundPattern(), styleContext);
            GraphicFill gf =
                    sf.graphicFill(Arrays.asList(eg), backgroundOpacity(), null, null, null, null);
            return sf.fill(gf, backgroundColor(), backgroundOpacity());
        } else {
            return sf.fill(null, backgroundColor(), backgroundOpacity());
        }
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
