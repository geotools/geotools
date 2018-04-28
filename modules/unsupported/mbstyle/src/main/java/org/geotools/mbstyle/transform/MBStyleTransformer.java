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
 */
package org.geotools.mbstyle.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.layer.SymbolMBLayer.TextAnchor;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.sprite.SpriteGraphicFactory;
import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.styling.*;
import org.geotools.util.logging.Logging;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Responsible for traverse {@link MBStyle} and generating {@link StyledLayerDescriptor}.
 *
 * @author Jody Garnett (Jody Garnett)
 */
public class MBStyleTransformer {

    private final FilterFactory2 ff;

    private final StyleFactory sf;

    private final StyleBuilder sb;

    private final List<String> defaultFonts;

    protected static Pattern mapboxTokenPattern = Pattern.compile("\\{(.*?)\\}");

    private static final Logger LOGGER = Logging.getLogger(MBStyleTransformer.class);

    public MBStyleTransformer(MBObjectParser parse) {
        defaultFonts = new ArrayList<>();
        defaultFonts.add("Open Sans Regular");
        defaultFonts.add("Arial Unicode MS Regular");
        ff = parse.getFilterFactory();
        sf = parse.getStyleFactory();
        sb = new StyleBuilder();
    }

    /**
     * Takes the name of an icon, and an {@link MBStyle} as a context, and returns an External
     * Graphic referencing the full URL of the image for consumption by the {@link
     * SpriteGraphicFactory}. (The format of the image will be {@link SpriteGraphicFactory#FORMAT}).
     *
     * @see {@link SpriteGraphicFactory} for more information.
     * @param iconName The name of the icon inside the spritesheet.
     * @param iconSize The size (scale multiplier) to apply to the icon. (Nullable).
     * @param styleContext The style context in which to resolve the icon name to the full sprite
     *     URL (for consumption by the {@link SpriteGraphicFactory}).
     * @return An external graphic with the full URL of the mage for the {@link
     *     SpriteGraphicFactory}.
     */
    public ExternalGraphic createExternalGraphicForSprite(
            Expression iconName, Expression iconSize, MBStyle styleContext) {
        String spriteUrl;
        String iconNameCql = ECQL.toCQL(ff.function("strURLEncode", iconName));

        String iconSizeCql = null;
        if (iconSize != null) {
            iconSizeCql = ECQL.toCQL(ff.function("strURLEncode", iconSize));
        }

        /*
         * Note: The provided iconName {@link Expression} will be embedded in the {@link ExternalGraphic}'s URL as a CQL string, in order to support
         * Mapbox functions. The {@link SLDStyleFactory} will transform it back into a proper {@link Expression} before sending it to the {@link
         * SpriteGraphicFactory}.
         */

        if (styleContext != null && styleContext.getSprite() != null) {
            String spriteBase = styleContext.getSprite().trim();

            String fragment;
            if (iconSizeCql != null) {
                fragment = "icon=${" + iconNameCql + "}&size=${" + iconSizeCql + "}";
            } else {
                fragment = "icon=${" + iconNameCql + "}";
            }

            spriteUrl = spriteBase + "#" + fragment;
        } else {
            spriteUrl = iconNameCql;
        }

        return sf.createExternalGraphic(spriteUrl, SpriteGraphicFactory.FORMAT);
    }

    /**
     * Takes the name of an icon, and an {@link MBStyle} as a context, and returns an External
     * Graphic referencing the full URL of the image for consumption by the {@link
     * SpriteGraphicFactory}. (The format of the image will be {@link SpriteGraphicFactory#FORMAT}).
     *
     * @see {@link SpriteGraphicFactory} for more information.
     * @param iconName The name of the icon inside the spritesheet.
     * @param styleContext The style context in which to resolve the icon name to the full sprite
     *     URL (for consumption by the {@link SpriteGraphicFactory}).
     * @return An external graphic with the full URL of the mage for the {@link
     *     SpriteGraphicFactory}.
     */
    public ExternalGraphic createExternalGraphicForSprite(
            Expression iconName, MBStyle styleContext) {
        return createExternalGraphicForSprite(iconName, ff.literal("1"), styleContext);
    }

    /**
     * Given a string of "bottom-right" or "top-left" find the x,y coordinates and create an
     * AnchorPoint
     *
     * @param textAnchor The value of the "text-anchor" property in the mapbox style.
     * @return AnchorPoint
     */
    AnchorPoint getAnchorPoint(String textAnchor) {
        TextAnchor anchor = TextAnchor.parse(textAnchor);
        return sb.createAnchorPoint(anchor.getX(), anchor.getY());
    }

    /**
     * Take a string that may contain Mapbox-style tokens, and convert it to a CQL expression
     * string.
     *
     * <p>E.g., convert "<code>String with {tokens}</code>" to a CQL Expression (String) "<code>
     * String with ${tokens}</code>".
     *
     * <p>See documentation of Mapbox {token} values, linked below.
     *
     * @see <a
     *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-icon-image">Mapbox
     *     Style Spec: {token} values for icon-image</a>
     * @see <a
     *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-text-field">Mapbox
     *     Style Spec: {token} values for text-field</a>
     * @param tokenStr A string with mapbox-style tokens
     * @return A CQL Expression
     */
    public String cqlStringFromTokens(String tokenStr) {
        // Find all {tokens} and turn them into CQL ${expressions}
        Matcher m = mapboxTokenPattern.matcher(tokenStr);
        return m.replaceAll("\\${$1}");
    }

    /**
     * Take a string that may contain Mapbox-style tokens, and convert it to a CQL expression.
     *
     * <p>E.g., convert "<code>String with {tokens}</code>" to a CQL Expression: "<code>
     * String with ${tokens}</code>".
     *
     * <p>See documentation of Mapbox {token} values, linked below.
     *
     * @see <a
     *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-icon-image">Mapbox
     *     Style Spec: {token} values for icon-image</a>
     * @see <a
     *     href="https://www.mapbox.com/mapbox-gl-js/style-spec/#layout-symbol-text-field">Mapbox
     *     Style Spec: {token} values for text-field</a>
     * @param tokenStr A string with mapbox-style tokens
     * @return A CQL Expression
     */
    public Expression cqlExpressionFromTokens(String tokenStr) {
        try {
            return ExpressionExtractor.extractCqlExpressions(cqlStringFromTokens(tokenStr));
        } catch (IllegalArgumentException iae) {
            LOGGER.warning(
                    "Exception converting Mapbox token string to CQL expression. Mapbox token string was: \""
                            + tokenStr
                            + "\". Exception was: "
                            + iae.getMessage());
            return ff.literal(tokenStr);
        }
    }

    /**
     * Utility method for getting a concrete value out of an expression, used by transformer methods
     * when GeoTools is unable to accept an expression.
     *
     * <ul>
     *   <li>If the provided {@link Expression} is a {@link Literal}, evaluates it and returns the
     *       value.
     *   <li>Otherwise, returns the provided fallback value and logs a warning that dynamic styling
     *       is not yet supported for this property.
     * </ul>
     *
     * @param expression The expression
     * @param clazz The type to provide as the context for the expression's evaluation.
     * @param fallback The value to return if the expression is not a literal
     * @param propertyName The name of the property that the expression corresponds to, for logging
     *     purposes.
     * @param layerId The ID of the layer that the expression corresponds to, for logging purposes.
     * @return The evaluated value of the provided {@link Expression}, or the provided fallback
     *     value.
     */
    public static <T> T requireLiteral(
            Expression expression,
            Class<T> clazz,
            T fallback,
            String propertyName,
            String layerId) {
        if (expression instanceof Literal) {
            T value = expression.evaluate(null, clazz);
            if (value != null) {
                return value;
            } else {
                return fallback;
            }
        } else {
            LOGGER.warning(
                    "Mapbox '"
                            + propertyName
                            + "' property: functions not yet supported for this property, falling back to default value."
                            + " (layerId = '"
                            + layerId
                            + "')");
            return fallback;
        }
    }

    /** @return The list of default font names */
    public List<String> getDefaultFonts() {
        return defaultFonts;
    }
}
