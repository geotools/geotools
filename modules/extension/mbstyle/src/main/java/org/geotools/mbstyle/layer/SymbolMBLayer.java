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
package org.geotools.mbstyle.layer;

import static org.geotools.api.style.TextSymbolizer.GraphicPlacement.INDEPENDENT;
import static org.geotools.renderer.label.LabelCacheItem.GraphicResize.NONE;
import static org.geotools.renderer.label.LabelCacheItem.GraphicResize.PROPORTIONAL;
import static org.geotools.renderer.label.LabelCacheItem.GraphicResize.STRETCH;

import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.logging.Logger;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Halo;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.Mark;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SemanticType;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.TextSymbolizer.GroupPriority;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.expression.MBExpression;
import org.geotools.mbstyle.function.FontAlternativesFunction;
import org.geotools.mbstyle.function.FontAttributesExtractor;
import org.geotools.mbstyle.function.MapBoxFontBaseNameFunction;
import org.geotools.mbstyle.function.MapBoxFontStyleFunction;
import org.geotools.mbstyle.function.MapBoxFontWeightFunction;
import org.geotools.mbstyle.parse.MBFilter;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.mbstyle.sprite.SpriteGraphicFactory;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.measure.Units;
import org.geotools.styling.StyleBuilder;
import org.geotools.text.Text;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A symbol.
 *
 * <p>MBLayer wrapper around a {@link JSONObject} representation of a "symbol" type layer. All methods act as accessors
 * on provided JSON layer, no other state is maintained. This allows modifications to be made cleanly with out chance of
 * side-effect.
 *
 * <ul>
 *   <li>get methods: access the json directly
 *   <li>query methods: provide logic / transforms to GeoTools classes as required.
 * </ul>
 */
public class SymbolMBLayer extends MBLayer {

    private static final Logger LOGGER = Logging.getLogger(SymbolMBLayer.class);

    static final Font TINY_FONT;

    static {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        TINY_FONT = sf.createFont(ff.literal("Sans"), ff.literal("normal"), ff.literal("normal"), ff.literal(1));
    }

    private static final Color DEFAULT_HALO_COLOR = new Color(0, 0, 0, 0);
    private final JSONObject layout;

    private final JSONObject paint;

    private static final String TYPE = "symbol";

    /** Offset of a label with symbol-sort-key 0, the middle of the 1000 wide priority band of its layer */
    private static final int SORT_KEY_MIDDLE = 500;

    /** Largest distance of a sort key offset from {@link #SORT_KEY_MIDDLE}, so it never reaches the band edges */
    private static final int SORT_KEY_HALF_RANGE = 499;

    /** Natural log of 1 + the largest sort key absolute value, larger keys get the end offsets */
    private static final double LOG_MAX_SORT_KEY = Math.log1p(1e15);

    private Integer labelPriority;

    public enum SymbolPlacement {
        /** The label is placed at the point where the geometry is located. */
        POINT,

        /**
         * The label is placed along the line of the geometry. Can only be used on LineString and Polygon geometries.
         */
        LINE
    }

    /** Interpreted differently when applied to different fields. */
    public enum Alignment {
        MAP,
        VIEWPORT,
        AUTO
    }

    public enum IconTextFit {
        /** The icon is displayed at its intrinsic aspect ratio. */
        NONE,

        /** The icon is scaled in the x-dimension to fit the width of the text. */
        WIDTH,

        /** The icon is scaled in the y-dimension to fit the height of the text. */
        HEIGHT,

        /** The icon is scaled in both x- and y-dimensions. */
        BOTH
    }

    public enum Justification {
        /** The text is aligned to the left. */
        LEFT,

        /** The text is centered. */
        CENTER,

        /** The text is aligned to the right. */
        RIGHT
    }
    /** Text justification options. */
    public enum TextAnchor {
        /** The center of the text is placed closest to the anchor. */
        CENTER(0.5, 0.5),

        /** The left side of the text is placed closest to the anchor. */
        LEFT(0.0, 0.5),

        /** The right side of the text is placed closest to the anchor. */
        RIGHT(1.0, 0.5),

        /** The top of the text is placed closest to the anchor. */
        TOP(0.5, 1.0),

        /** The bottom of the text is placed closest to the anchor. */
        BOTTOM(0.5, 0.0),

        /** The top left corner of the text is placed closest to the anchor. */
        TOP_LEFT(0.0, 1.0),

        /** The top right corner of the text is placed closest to the anchor. */
        TOP_RIGHT(1.0, 1.0),

        /** The bottom left corner of the text is placed closest to the anchor. */
        BOTTOM_LEFT(0.0, 0.0),

        /** The bottom right corner of the text is placed closest to the anchor. */
        BOTTOM_RIGHT(1.0, 0.0);

        /** horizontal justification */
        private final double x;

        /** vertical justification */
        private final double y;

        TextAnchor(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Horizontal justification.
         *
         * @return horizontal alignment between 0.0 and 1.0.
         */
        public double getX() {
            return x;
        }

        /**
         * Vertical justification.
         *
         * @return vertical alignment between 0.0 and 1.0.
         */
        public double getY() {
            return y;
        }

        /**
         * Parse provided jsonString as a TextAnchor.
         *
         * <p>One of center, left, right, top, bottom, top-left, top-right, bottom-left, bottom-right. Defaults to
         * center.
         *
         * @param jsonString text anchor definition
         * @return TextAnchor, defaults TextAnchor#CENTER if undefined
         */
        public static TextAnchor parse(String jsonString) {
            if (jsonString == null) {
                return CENTER;
            }
            String name = jsonString.toUpperCase().trim().replace('-', '_');
            try {
                return TextAnchor.valueOf(name);
            } catch (IllegalArgumentException invalid) {
                throw new MBFormatException("Invalid text-alginment '"
                        + jsonString
                        + "' expected one of"
                        + "center, left, right, top, bottom, top-left, top-right, bottom-left, bottom-right");
            }
        }

        /**
         * The json representation of this TextAnchor.
         *
         * @return json representation
         */
        public String json() {
            return name().toLowerCase().replace('_', '-');
        }

        /**
         * Quickly grab y justification for jsonString.
         *
         * @param jsonString text anchor definition
         * @return vertical anchor, defaults to 0.5
         */
        public static double getAnchorY(String jsonString) {
            return TextAnchor.parse(jsonString).getY();
        }
        /**
         * Quickly grab x justification for jsonString.
         *
         * @param jsonString text anchor definition
         * @return horizontal anchor, defaults to 0.5
         */
        public static double getAnchorX(String jsonString) {
            return TextAnchor.parse(jsonString).getX();
        }
    }

    public enum TextTransform {
        /** The text is not altered. */
        NONE,

        /** Forces all letters to be displayed in uppercase. */
        UPPERCASE,

        /** Forces all letters to be displayed in lowercase. */
        LOWERCASE,
    }

    public enum TranslateAnchor {
        /** Translation relative to the map. */
        MAP,

        /** Translation relative to the viewport. */
        VIEWPORT
    }

    /**
     * When any of these strings is provided as the sprite source in an MB style, the style's 'icon-image' will actually
     * be interpreted as the well-known name of a GeoTools {@link Mark} rather than an actual sprite sheet location.
     */
    protected static final Set<String> MARK_SHEET_ALIASES = ImmutableSet.of("geotoolsmarks", "geoservermarks", "");

    /**
     * The default base size (pixels) to use to render GeoTools marks in a MB style. This is needed because MB styles
     * have only a relative "size" property that scales the icon, but no absolute reference size.
     */
    protected static final int MARK_ICON_DEFAULT_SIZE = 32;

    /**
     * Create a "symbol" type layer.
     *
     * @param json JSON symbol definition.
     */
    public SymbolMBLayer(JSONObject json) {
        super(json, new MBObjectParser(SymbolMBLayer.class));
        paint = super.getPaint();
        layout = super.getLayout();
    }

    /** @return The default semantic type. */
    @Override
    protected SemanticType defaultSemanticType() {
        return SemanticType.ANY;
    }

    /**
     * (Optional) One of point, line. Defaults to point.
     *
     * <p>Label placement relative to its geometry.
     *
     * @return SymbolPlacement
     */
    public SymbolPlacement getSymbolPlacement() {
        Object value = layout.get("symbol-placement");
        if (value != null && "line".equalsIgnoreCase((String) value)) {
            return SymbolPlacement.LINE;
        } else {
            return SymbolPlacement.POINT;
        }
    }

    /**
     * (Optional) One of point, line. Defaults to point.
     *
     * <p>Label placement relative to its geometry.
     *
     * @return SymbolPlacement
     */
    public Expression symbolPlacement() {
        return parse.string(layout, "symbol-placement", "point");
    }

    /**
     * (Optional) Units in pixels. Defaults to 250. Requires SymbolPlacement.LINE
     *
     * <p>Distance between two symbol anchors.
     *
     * @return Number representing distance between two symbol anchors
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Number getSymbolSpacing() throws MBFormatException {
        return parse.optional(Number.class, layout, "symbol-spacing", 250);
    }

    /**
     * Access symbol-spacing, defaults to 250.
     *
     * @return Number representing distance between two symbol anchors
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression symbolSpacing() throws MBFormatException {
        return parse.percentage(layout, "symbol-spacing", 250);
    }

    /**
     * (Optional) Defaults to false.
     *
     * <p>If true, the symbols will not cross tile edges to avoid mutual collisions. Recommended in layers that don't
     * have enough padding in the vector tile to prevent collisions, or if it is a point symbol layer placed after a
     * line symbol layer.
     *
     * @return Whether or not the symbols should avoid edges.
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Boolean getSymbolAvoidEdges() throws MBFormatException {
        return parse.getBoolean(layout, "symbol-avoid-edges", false);
    }

    /**
     * Wraps {@link #getSymbolAvoidEdges()} in a GeoTools expression.
     *
     * <p>(Optional) Defaults to false. If true, the symbols will not cross tile edges to avoid mutual collisions.
     * Recommended in layers that don't have enough padding in the vector tile to prevent collisions, or if it is a
     * point symbol layer placed after a line symbol layer.
     *
     * @return Whether or not the symbols should avoid edges.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression symbolAvoidEdges() {
        return parse.bool(layout, "symbol-avoid-edges", false);
    }

    /**
     * (Optional) Defaults to false. Requires icon-image.
     *
     * <p>If true, the icon will be visible even if it collides with other previously drawn symbols.
     *
     * @return Whether or not the symbols should be allowed to overlap other symbols
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Boolean getIconAllowOverlap() throws MBFormatException {
        return parse.getBoolean(layout, "icon-allow-overlap", false);
    }

    /**
     * Wraps {@link #getIconAllowOverlap()} in a GeoTools expression.
     *
     * <p>(Optional) Defaults to false. Requires icon-image.
     *
     * <p>If true, the icon will be visible even if it collides with other previously drawn symbols.
     *
     * @return Whether or not the symbols should be allowed to overlap other symbols
     */
    public Expression iconAllowOverlap() throws MBFormatException {
        return parse.bool(layout, "icon-allow-overlap", false);
    }

    /**
     * (Optional) Defaults to false. Requires icon-image.
     *
     * <p>If true, other symbols can be visible even if they collide with the icon.
     *
     * @return Whether or not other symbols should be allowed to overlap symbols in this layer.
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Boolean getIconIgnorePlacement() throws MBFormatException {
        return parse.getBoolean(layout, "icon-ignore-placement", false);
    }

    /**
     * Wraps {@link #getIconIgnorePlacement()} in a GeoTools expression.
     *
     * <p>(Optional) Defaults to false. Requires icon-image. If true, other symbols can be visible even if they collide
     * with the icon.
     *
     * @return Whether or not other symbols should be allowed to overlap symbols in this layer.
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconIgnorePlacement() {
        return parse.bool(layout, "icon-ignore-placement", false);
    }

    /**
     * (Optional) Defaults to false. Requires icon-image. Requires text-field.
     *
     * <p>If true, text will display without their corresponding icons when the icon collides with other symbols and the
     * text does not.
     *
     * @return Whether or not the label may be drawn when the icon is not drawn due to collisions
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Boolean getIconOptional() throws MBFormatException {
        return parse.getBoolean(layout, "icon-optional", false);
    }

    /**
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires icon-image. In combination with
     * symbol-placement, determines the rotation
     *
     * <p>Wraps {@link #getIconOptional()} in a GeoTools expression. (Optional) Defaults to false. Requires icon-image.
     * Requires text-field.
     *
     * <p>If true, text will display without their corresponding icons when the icon collides with other symbols and the
     * text does not.
     *
     * @return Whether or not the label may be drawn when the icon is not drawn due to collisions
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconOptional() {
        return parse.bool(layout, "icon-optional", false);
    }

    /**
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires icon-image. In combination with
     * symbol-placement, determines the rotation behavior of icons.
     *
     * <p>Possible values:
     *
     * <p>{@link Alignment#MAP} When symbol-placement is set to point, aligns icons east-west. When symbol-placement is
     * set to line, aligns icon x-axes with the line.
     *
     * <p>{@link Alignment#VIEWPORT} Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless
     * of the value of symbol-placement.
     *
     * <p>{@link Alignment#AUTO} When symbol-placement is set to point, this is equivalent to viewport. When
     * symbol-placement is set to line, this is equivalent to map.
     *
     * @return The icon rotation alignment
     */
    public Alignment getIconRotationAlignment() {
        Object value = layout.get("icon-rotation-alignment");
        if (value != null && "map".equalsIgnoreCase((String) value)) {
            return Alignment.MAP;
        } else if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return Alignment.VIEWPORT;
        } else {
            return Alignment.AUTO;
        }
    }

    /**
     * Converts {@link #getIconRotationAlignment()} to a GeoTools expression. Returns an expression that evaluates to
     * one of "map", "viewport", or "auto".
     *
     * @return Expression providing icon rotation alignment.
     */
    public Expression iconRotationAlignment() {
        return parse.enumToExpression(layout, "icon-rotation-alignment", Alignment.class, Alignment.AUTO);
    }

    /**
     * (Optional) Defaults to 1. Requires icon-image.
     *
     * <p>Scale factor for icon. 1 is original size, 3 triples the size.
     *
     * @return The icon size.
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Number getIconSize() throws MBFormatException {
        return parse.optional(Number.class, layout, "icon-size", 1.0);
    }

    /**
     * Access icon-size, defaults to 1.
     *
     * @return Expression of icon size.
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconSize() {
        return parse.percentage(layout, "icon-size", 1.0);
    }

    /**
     * (Optional) One of none, width, height, both. Defaults to none. Requires icon-image. Requires text-field. Scales
     * the icon to fit around the associated text.
     *
     * @return How the icon should be scaled to fit the associated text
     */
    public IconTextFit getIconTextFit() {
        Object value = layout.get("icon-text-fit");
        if (value != null && "width".equalsIgnoreCase((String) value)) {
            return IconTextFit.WIDTH;
        } else if (value != null && "height".equalsIgnoreCase((String) value)) {
            return IconTextFit.HEIGHT;
        } else if (value != null && "both".equalsIgnoreCase((String) value)) {
            return IconTextFit.BOTH;
        } else {
            return IconTextFit.NONE;
        }
    }

    /**
     * Wraps {@link #getIconTextFit()} in a GeoTools expression.
     *
     * <p>(Optional) One of none, width, height, both. Defaults to none. Requires icon-image. Requires text-field.
     * Scales the icon to fit around the associated text.
     *
     * @return How the icon should be scaled to fit the associated text
     */
    public Expression iconTextFit() {
        return parse.string(layout, "icon-text-fit", "none");
    }

    /**
     * (Optional) Units in pixels. Defaults to 0,0,0,0. Requires icon-image. Requires text-field. Requires icon-text-fit
     * = one of both, width, height.
     *
     * <p>Size of the additional area added to dimensions determined by icon-text-fit, in clockwise order: top, right,
     * bottom, left.
     *
     * @return The padding to add to icon-text-fit
     */
    public List<Number> getIconTextFitPadding() {
        // Not currently supported. (GT padding does not support multiple values).
        // json.get("icon-text-fit-padding")
        return null;
    }

    /**
     * (Optional) Units in pixels. Defaults to 0,0,0,0. Requires icon-image. Requires text-field. Requires icon-text-fit
     * = one of both, width, height.
     *
     * <p>Size of the additional area added to dimensions determined by icon-text-fit, in clockwise order: top, right,
     * bottom, left.
     *
     * @return The padding to add to icon-text-fit
     */
    public Expression iconTextFitPadding() {
        // Not curreently supported. (GT padding does not support multiple values).
        // json.get("icon-text-fit-padding")
        return null;
    }

    /**
     * (Optional) A string with {tokens} replaced, referencing the data property to pull from.
     *
     * @return The name of the icon image
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public String getIconImage() throws MBFormatException {
        return parse.optional(String.class, layout, "icon-image", null);
    }

    /** @return True if the layer has a icon-image explicitly provided. */
    public boolean hasIconImage() throws MBFormatException {
        return parse.isDefined(layout, "icon-image");
    }

    /**
     * Access icon-image as literal or function expression
     *
     * @return The name of the icon image
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconImage() {
        return parse.string(layout, "icon-image", "");
    }

    /**
     * (Optional) Units in degrees. Defaults to 0. Requires icon-image.
     *
     * <p>Rotates the icon clockwise.
     *
     * @return The icon rotation
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Number getIconRotate() throws MBFormatException {
        return parse.optional(Number.class, layout, "icon-rotate", 0.0);
    }

    /**
     * Access icon-rotate as literal or function expression
     *
     * @return The icon rotation
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconRotate() throws MBFormatException {
        return parse.percentage(layout, "icon-rotate", 0);
    }

    /**
     * (Optional) Units in pixels. Defaults to 2. Requires icon-image.
     *
     * <p>Size of the additional area around the icon bounding box used for detecting symbol collisions.
     *
     * @return Padding around the icon for collision-detection, or the default 2 when the padding is an expression
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Number getIconPadding() throws MBFormatException {
        // only picks the larger of text and icon padding; spaceAround uses iconPadding() itself
        Expression padding = parse.number(layout, "icon-padding", 2.0);
        return padding instanceof Literal ? padding.evaluate(null, Double.class) : 2.0;
    }

    /**
     * Access icon-padding as literal or function expression
     *
     * @return Padding around the icon for collision-detection.
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconPadding() throws MBFormatException {
        return parse.number(layout, "icon-padding", 2.0);
    }

    /**
     * (Optional) Defaults to false. Requires icon-image. Requires icon-rotation-alignment = map. Requires
     * symbol-placement = line.
     *
     * <p>If true, the icon may be flipped to prevent it from being rendered upside-down.
     *
     * @return Whether to flip the icon if the orientation of the geometry would cause it to be rendered upside-down
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Boolean getIconKeepUpright() throws MBFormatException {
        return parse.getBoolean(layout, "icon-keep-upright", false);
    }

    /**
     * Wraps {@link #getIconKeepUpright()} in a GeoTools expression.
     *
     * <p>(Optional) Defaults to false. Requires icon-image. Requires icon-rotation-alignment = map. Requires
     * symbol-placement = line.
     *
     * <p>If true, the icon may be flipped to prevent it from being rendered upside-down.
     *
     * @return Whether to flip the icon if the orientation of the geometry would cause it to be rendered upside-down
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Expression iconKeepUpright() {
        return parse.bool(layout, "icon-keep-upright", false);
    }

    /**
     * (Optional) Defaults to 0,0. Requires icon-image.
     *
     * <p>Offset distance of icon from its anchor. Positive values indicate right and down, while negative values
     * indicate left and up. When combined with icon-rotate the offset will be as if the rotated direction was up.
     *
     * @return Offset of the icon from its anchor
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public double[] getIconOffset() throws MBFormatException {
        return parse.array(layout, "icon-offset", new double[] {0.0, 0.0});
    }

    /**
     * Access icon-offset
     *
     * @return Offset of the icon from its anchor
     * @throws MBFormatException JSON provided inconsistent with specification
     */
    public Point iconOffset() throws MBFormatException {
        if (layout.get("icon-offset") != null) {
            JSONArray array = (JSONArray) layout.get("icon-offset");
            Number x = (Number) array.get(0);
            Number y = (Number) array.get(1);
            return new Point(x.intValue(), y.intValue());
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * Maps {@link #getIconOffset()} to a {@link Displacement}
     *
     * <p>(Optional) Defaults to 0,0. Requires icon-image. Offset distance of icon from its anchor. Positive values
     * indicate right and down, while negative values indicate left and up. When combined with icon-rotate the offset
     * will be as if the rotated direction was up.
     *
     * @return Icon offset
     */
    public Displacement iconOffsetDisplacement() {
        return parse.displacement(layout, "icon-offset", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    /**
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires text-field. Orientation of text when map is
     * pitched.
     *
     * <p>Possible values:
     *
     * <p>{@link Alignment#MAP} The text is aligned to the plane of the map.
     *
     * <p>{@link Alignment#VIEWPORT} The text is aligned to the plane of the viewport.
     *
     * <p>{@link Alignment#AUTO} Automatically matches the value of text-rotation-alignment.
     *
     * @return Text alignment when the map is pitched.
     */
    public Alignment getTextPitchAlignment() {
        Object value = layout.get("text-pitch-alignment");
        if (value != null && "map".equalsIgnoreCase((String) value)) {
            return Alignment.MAP;
        } else if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return Alignment.VIEWPORT;
        } else {
            return Alignment.AUTO;
        }
    }

    /**
     * Converts {@link #getTextPitchAlignment()} to a GeoTools expression. Returns an expression that evaluates to one
     * of "map", "viewport", or "auto".
     *
     * @return Expression of text pitch alignment
     */
    public Expression textPitchAlignment() {
        return parse.enumToExpression(layout, "text-pitch-alignment", Alignment.class, Alignment.AUTO);
    }

    /**
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires text-field. In combination with
     * symbol-placement, determines the rotation behavior of the individual glyphs forming the text.
     *
     * <p>Possible values:
     *
     * <p>{@link Alignment#MAP} When symbol-placement is set to point, aligns text east-west. When symbol-placement is
     * set to line, aligns text x-axes with the line.
     *
     * <p>{@link Alignment#VIEWPORT} Produces glyphs whose x-axes are aligned with the x-axis of the viewport,
     * regardless of the value of symbol-placement.
     *
     * <p>{@link Alignment#AUTO} When symbol-placement is set to point, this is equivalent to viewport. When
     * symbol-placement is set to line, this is equivalent to map.
     *
     * @return Text alignment when the map is rotated.
     */
    public Alignment getTextRotationAlignment() {
        Object value = layout.get("text-rotation-alignment");
        if (value != null && "map".equalsIgnoreCase((String) value)) {
            return Alignment.MAP;
        } else if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return Alignment.VIEWPORT;
        } else {
            return Alignment.AUTO;
        }
    }

    /**
     * Converts {@link #getTextRotationAlignment()} to a GeoTools expression.
     *
     * @return A GeoTools expression that evaluates to "map", "viewport", or "auto".
     * @see #getTextRotationAlignment()
     */
    public Expression textRotationAlignment() {
        return parse.enumToExpression(layout, "text-rotation-alignment", Alignment.class, Alignment.AUTO);
    }

    /**
     * (Optional) Value to use for a text label. Feature properties are specified using tokens like {field_name}.
     *
     * @return Value to use for a text label
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public String getTextField() throws MBFormatException {
        return parse.optional(String.class, layout, "text-field", "");
    }

    /**
     * Access text-field as literal or function expression
     *
     * @return Value to use for a text label
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textField() throws MBFormatException {
        return parse.string(layout, "text-field", "");
    }

    /** @return True if the layer has a text-field explicitly provided. */
    private boolean hasTextField() throws MBFormatException {
        return parse.isDefined(layout, "text-field");
    }

    /**
     * (Optional) Font stack to use for displaying text.
     *
     * <p>Defaults to <code>["Open Sans Regular","Arial Unicode MS Regular"]</code>. Requires text-field.
     *
     * @return The font to use for the label
     */
    public List<String> getTextFont() {
        Object textFont = layout.get("text-font");
        // a data-driven expression (e.g. ["case", ...]) is handled by the dynamic textFont() path
        if (textFont instanceof JSONObject
                || (textFont instanceof JSONArray array
                        && !array.isEmpty()
                        && array.get(0) instanceof String op
                        && MBExpression.canCreate(op))) {
            return null;
        }
        String[] fonts = parse.array(
                String.class, layout, "text-font", new String[] {"Open Sans Regular", "Arial Unicode MS Regular"});
        return Arrays.asList(fonts);
    }

    /**
     * Access text-font as a literal or function expression.
     *
     * @return The font to use for the label
     */
    public Expression textFont() throws MBFormatException {
        return parse.font(layout, "text-font");
    }

    /**
     * (Optional) Units in pixels. Defaults to 16. Requires text-field.
     *
     * <p>Font size.
     *
     * @return The font size
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextSize() throws MBFormatException {
        return parse.optional(Number.class, layout, "text-size", 16.0);
    }

    /**
     * Access text-size as literal or function expression
     *
     * @return The font size
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textSize() throws MBFormatException {
        return parse.percentage(layout, "text-size", 16.0);
    }

    /**
     * (Optional) Units in ems. Defaults to 10. Requires text-field.
     *
     * <p>The maximum line width for text wrapping.
     *
     * @return Maximum label width
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextMaxWidth() throws MBFormatException {
        return parse.optional(Number.class, layout, "text-max-width", 10.0);
    }

    /**
     * Access text-max-width as literal or function expression
     *
     * @return Maximum label width
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textMaxWidth() throws MBFormatException {
        return parse.percentage(layout, "text-max-width", 10.0);
    }

    /** @return True if the layer has a text-max-width explicitly provided. */
    public boolean hasTextMaxWidth() throws MBFormatException {
        return parse.isDefined(layout, "text-max-width");
    }

    /**
     * (Optional) Units in ems. Defaults to 1.2. Requires text-field.
     *
     * <p>Text leading value for multi-line text.
     *
     * @return Label line height
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextLineHeight() throws MBFormatException {
        return parse.optional(Number.class, layout, "text-line-height", 1.2);
    }

    /**
     * Access text-line-height as literal or function expression
     *
     * @return Label line height
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textLineHeight() throws MBFormatException {
        return parse.percentage(layout, "text-line-height", 1.2);
    }

    /**
     * (Optional) Units in ems. Defaults to 0. Requires text-field.
     *
     * <p>Text tracking amount.
     *
     * @return Spacing between label characters
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextLetterSpacing() throws MBFormatException {
        return parse.optional(Number.class, layout, "text-letter-spacing", 0.0);
    }

    /**
     * Access text-letter-spacing as literal or function expression
     *
     * @return Spacing between label characters
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textLetterSpacing() throws MBFormatException {
        return parse.percentage(layout, "text-letter-spacing", 0.0);
    }

    /**
     * Optional enum. One of left, center, right. Defaults to center. Requires text-field.
     *
     * <p>Text justification options:
     *
     * <p>{@link Justification#LEFT} The text is aligned to the left.
     *
     * <p>{@link Justification#CENTER} The text is centered.
     *
     * <p>{@link Justification#RIGHT} The text is aligned to the right.
     *
     * @return The label justification.
     */
    public Justification getTextJustify() {
        Object value = layout.get("text-justify");
        if (value != null && "left".equalsIgnoreCase((String) value)) {
            return Justification.LEFT;
        } else if (value != null && "right".equalsIgnoreCase((String) value)) {
            return Justification.RIGHT;
        } else {
            return Justification.CENTER;
        }
    }

    /**
     * Converts {@link #getTextJustify()} to a GeoTools expression. Returns an expression that evaluates to one of
     * "left", "right", or "center".
     *
     * @see #getTextJustify()
     * @return Expression of text justification
     */
    public Expression textJustify() {
        return parse.enumToExpression(layout, "text-justify", Justification.class, Justification.CENTER);
    }

    /**
     * Part of the text placed closest to the anchor (requires text-field).
     *
     * <p>Optional enum. One of center, left, right, top, bottom, top-left, top-right, bottom-left, bottom-right.
     * Defaults to center. Requires text-field. Part of the text placed closest to the anchor.
     *
     * <p>{@link TextAnchor#CENTER} The center of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#LEFT} The left side of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#RIGHT} The right side of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#TOP} The top of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#BOTTOM} The bottom of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#TOP_LEFT} The top left corner of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#TOP_RIGHT} The top right corner of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#BOTTOM_LEFT} The bottom left corner of the text is placed closest to the anchor.
     *
     * <p>{@link TextAnchor#BOTTOM_RIGHT} The bottom right corner of the text is placed closest to the anchor.
     *
     * @return part of the text placed closest to the anchor.
     */
    public TextAnchor getTextAnchor() {
        String json = parse.get(layout, "text-anchor", "center");
        if (json == null) {
            return null;
        }
        return TextAnchor.parse(json);
    }

    /**
     * Converts {@link #getTextAnchor()} to a GeoTools expression. Returns an expression that evaluates to one of
     * "center", "left", or "right", "top", "bottom", "top_left", "top_right", "bottom_left", "bottom_right".
     *
     * @see #getTextAnchor()
     * @return Expression of text anchor
     */
    public Expression textAnchor() {
        return parse.enumToExpression(layout, "text-anchor", TextAnchor.class, TextAnchor.CENTER);
    }

    /**
     * Layout "text-anchor" provided as {@link AnchorPoint}.
     *
     * @return AnchorPoint defined by "text-anchor".
     */
    public AnchorPoint anchorPoint() {
        return anchorPointByProperty("text-anchor");
    }

    public AnchorPoint iconAnchorPoint() {
        return anchorPointByProperty("icon-anchor");
    }

    public AnchorPoint anchorPointByProperty(String propertyName) {
        Expression expression = parse.string(layout, propertyName, TextAnchor.CENTER.name());
        if (expression == null) {
            return null;
        }
        if (expression instanceof Literal) {
            TextAnchor anchor = TextAnchor.parse(expression.evaluate(null, String.class));
            return sf.anchorPoint(ff.literal(anchor.getX()), ff.literal(anchor.getY()));
        }
        // it's a generic expression, need to map it to values
        return sf.anchorPoint(
                ff.function("mbAnchor", expression, ff.literal("x")),
                ff.function("mbAnchor", expression, ff.literal("y")));
    }

    /**
     * (Optional) Units in degrees. Defaults to 45. Requires text-field. Requires symbol-placement = line.
     *
     * <p>Maximum angle change between adjacent characters.
     *
     * @return Maximum label angle between characters when following a line
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextMaxAngle() throws MBFormatException {
        return parse.optional(Number.class, layout, "text-max-angle", 45.0);
    }

    /**
     * Access text-max-angle as literal or function expression
     *
     * @return Maximum label angle between characters when following a line
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textMaxAngle() {
        return parse.percentage(layout, "text-max-angle", 45.0);
    }

    /** @return True if the layer has a text-max-angle explicitly provided. */
    private boolean hasTextMaxAngle() throws MBFormatException {
        return parse.isDefined(layout, "text-max-angle");
    }

    /**
     * (Optional) Units in degrees. Defaults to 0. Requires text-field.
     *
     * <p>Rotates the text clockwise.
     *
     * @return Rotation angle of the label
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextRotate() throws MBFormatException {
        return parse.optional(Number.class, layout, "text-rotate", 0.0);
    }

    /**
     * Access text-rotate as literal or function expression
     *
     * @return Rotation angle of the label
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textRotate() throws MBFormatException {
        return parse.percentage(layout, "text-rotate", 0.0);
    }

    /**
     * (Optional) Units in pixels. Defaults to 2. Requires text-field.
     *
     * <p>Size of the additional area around the text bounding box used for detecting symbol collisions.
     *
     * @return Padding around the label for detecting collisions, or the default 2 when the padding is an expression
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextPadding() throws MBFormatException {
        // only picks the larger of text and icon padding; spaceAround uses textPadding() itself
        Expression padding = parse.number(layout, "text-padding", 2.0);
        return padding instanceof Literal ? padding.evaluate(null, Double.class) : 2.0;
    }

    /**
     * Access text-padding as literal or function expression
     *
     * @return Padding around the label for detecting collisions
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textPadding() throws MBFormatException {
        return parse.number(layout, "text-padding", 2.0);
    }

    /**
     * (Optional) Defaults to true. Requires text-field. Requires text-rotation-alignment = map. Requires
     * symbol-placement = line.
     *
     * <p>If true, the text may be flipped vertically to prevent it from being rendered upside-down.
     *
     * @return Whether to flip the label if the orientation of the geometry would cause it to be rendered upside-down
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Boolean getTextKeepUpright() throws MBFormatException {
        return parse.getBoolean(layout, "text-keep-upright", true);
    }

    /**
     * Wraps {@link #getTextKeepUpright()} in a GeoTools expression (Optional) Defaults to true. Requires text-field.
     * Requires text-rotation-alignment = map. Requires symbol-placement = line.
     *
     * <p>If true, the text may be flipped vertically to prevent it from being rendered upside-down.
     *
     * @return Boolean
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textKeepUpright() {
        return parse.bool(layout, "text-keep-upright", true);
    }

    /**
     * One of none, uppercase, lowercase. Defaults to none. Requires text-field.
     *
     * <p>Specifies how to capitalize text, similar to the CSS text-transform property.
     *
     * <p>{@link TextTransform#NONE} The text is not altered.
     *
     * <p>{@link TextTransform#UPPERCASE} Forces all letters to be displayed in uppercase.
     *
     * <p>{@link TextTransform#LOWERCASE} Forces all letters to be displayed in lowercase.
     *
     * @return The tranformation to apply to the label
     */
    public TextTransform getTextTransform() {
        Object value = layout.get("text-transform");
        if (value != null && "uppercase".equalsIgnoreCase((String) value)) {
            return TextTransform.UPPERCASE;
        } else if (value != null && "lowercase".equalsIgnoreCase((String) value)) {
            return TextTransform.LOWERCASE;
        } else {
            return TextTransform.NONE;
        }
    }

    /**
     * Returns true if the a text-transform property explicitly provided
     *
     * @return true if text-transform provided
     */
    public boolean hasTextTransform() {
        return parse.isDefined(layout, "text-transform");
    }

    /**
     * Converts {@link #getTextTransform()} to a GeoTools expression. Returns an expression that evaluates to one of
     * "uppercase", "lowercase", "none".
     *
     * @see #getTextTransform()
     * @return Expression providing text transformation
     */
    public Expression textTransform() {
        return parse.enumToExpression(layout, "text-transform", TextTransform.class, TextTransform.NONE);
    }

    /**
     * (Optional) Units in ems. Defaults to 0,0. Requires text-field.
     *
     * <p>Offset distance of text from its anchor. Positive values indicate right and down, while negative values
     * indicate left and up.
     *
     * @return Offset of the label from its anchor.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public double[] getTextOffset() throws MBFormatException {
        return parse.array(layout, "text-offset", new double[] {0.0, 0.0});
    }

    /**
     * Access text-offset
     *
     * @return Offset of the label from its anchor.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Point textOffset() throws MBFormatException {
        if (layout.get("text-offset") != null) {
            JSONArray array = (JSONArray) layout.get("text-offset");
            Number x = (Number) array.get(0);
            Number y = (Number) array.get(1);
            return new Point(x.intValue(), y.intValue());
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * Maps {@link #getTextOffset()} to a {@link Displacement}.
     *
     * @return (Optional) Units in ems. Defaults to 0,0. Requires text-field.
     */
    public Displacement textOffsetDisplacement() {
        return parse.displacement(layout, "text-offset", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    private boolean hasTextOffset() {
        return parse.isDefined(layout, "text-offset");
    }

    /**
     * Returns every name in text-variable-anchor, across all zoom stops, in order and without repeats. The caller drops
     * the names that are not anchors, such as "step" or "center".
     */
    private List<String> getTextVariableAnchor() {
        Object value = layout.get("text-variable-anchor");
        if (!(value instanceof JSONArray)) {
            return Collections.emptyList();
        }
        Set<String> anchors = new LinkedHashSet<>();
        collectStrings((JSONArray) value, anchors);
        return new ArrayList<>(anchors);
    }

    /** Returns true for the zoom form of text-variable-anchor, a "step" on zoom. */
    private boolean isZoomDrivenVariableAnchor() {
        return layout.get("text-variable-anchor") instanceof JSONArray array
                && !array.isEmpty()
                && "step".equals(array.get(0));
    }

    /** Recursively appends every String scalar found in the array (and any nested arrays) to {@code out}. */
    private static void collectStrings(JSONArray array, Set<String> out) {
        for (Object element : array) {
            if (element instanceof JSONArray nested) {
                collectStrings(nested, out);
            } else if (element instanceof String s) {
                out.add(s);
            }
        }
    }

    /**
     * Maps text-variable-anchor positions to a comma-separated displacementMode value. The anchor names the side of the
     * label placed AT the point, so the label sits on the opposite side: "left" (label to the right) -> E, "right" ->
     * W, and so on. "center" and unknowns carry no directional flip and are dropped.
     */
    private String displacementModeFromVariableAnchor() {
        List<String> modes = new ArrayList<>();
        for (String anchor : getTextVariableAnchor()) {
            String mode =
                    switch (anchor.trim().toLowerCase()) {
                        case "left" -> "E";
                        case "right" -> "W";
                        case "top" -> "S";
                        case "bottom" -> "N";
                        case "top-left" -> "SE";
                        case "top-right" -> "SW";
                        case "bottom-left" -> "NE";
                        case "bottom-right" -> "NW";
                        default -> null;
                    };
            if (mode != null && !modes.contains(mode)) {
                modes.add(mode);
            }
        }
        return String.join(",", modes);
    }

    /**
     * (Optional) Units in ems. Defaults to 0. Requires text-field. Distance of the label from the point, away from the
     * anchor named by text-variable-anchor, or by text-anchor without variable anchors.
     */
    public Expression textRadialOffset() {
        return parse.number(layout, "text-radial-offset", 0);
    }

    /**
     * Mapbox pads both colliding boxes, the label cache pads only the new label: double the padding to keep the same
     * distance between labels. When two labels use different paddings, the label placed second decides the distance.
     */
    private String spaceAround(Expression padding) {
        return MBStyleTransformer.vendorOptionExpression(ff.multiply(ff.literal(2), padding));
    }

    private boolean hasTextLetterSpacing() {
        Object spacing = layout.get("text-letter-spacing");
        return spacing != null && !(spacing instanceof Number number && number.doubleValue() == 0);
    }

    /** A zero radial offset leaves the label to text-offset, as Mapbox does without variable anchors. */
    private boolean hasTextRadialOffset() {
        Object offset = layout.get("text-radial-offset");
        return offset != null && !(offset instanceof Number number && number.doubleValue() == 0);
    }

    /**
     * Returns the anchor the radial offset moves the label away from, as a name or as a zoom step of names: the first
     * variable anchor, or text-anchor without variable anchors. Returns null for forms that cannot be mapped.
     */
    @SuppressWarnings("unchecked") // json-simple JSONArray is a raw ArrayList
    private Object radialAnchor() {
        Object variable = layout.get("text-variable-anchor");
        if (variable == null) {
            Object anchor = layout.get("text-anchor");
            if (anchor == null) {
                return "center";
            }
            return anchor instanceof String ? anchor : null;
        }
        if (!(variable instanceof JSONArray anchors) || anchors.isEmpty()) {
            return null;
        }
        if ("literal".equals(anchors.get(0)) && anchors.get(1) instanceof JSONArray names && !names.isEmpty()) {
            return names.get(0);
        }
        if (anchors.get(0) instanceof String first && !"step".equals(first)) {
            return first;
        }
        // zoom step: keep input and stops, replace each ["literal", [anchors]] output with its first anchor
        JSONArray step = new JSONArray();
        step.addAll(anchors.subList(0, 2));
        for (int i = 2; i < anchors.size(); i += 2) {
            if (!(anchors.get(i) instanceof JSONArray literal
                    && literal.size() == 2
                    && literal.get(1) instanceof JSONArray names
                    && !names.isEmpty())) {
                return null;
            }
            step.add(names.get(0));
            if (i + 1 < anchors.size()) {
                step.add(anchors.get(i + 1));
            }
        }
        return step;
    }

    /** Moves the label away from its anchor by the radial offset, plus the text padding with variable anchors. */
    private Displacement radialDisplacement(Object radialAnchor, Expression fontSize) {
        Expression offset = ff.multiply(fontSize, ff.function("max", textRadialOffset(), ff.literal(0)));
        Displacement displacement = sf.displacement(
                ff.multiply(offset, perAnchor(radialAnchor, a -> awayFrom(a.getX()) * diagonal(a))),
                ff.multiply(offset, perAnchor(radialAnchor, a -> awayFrom(a.getY()) * diagonal(a))));
        if (hasTextVariableAnchor()) {
            addVariableAnchorPadding(
                    displacement,
                    perAnchor(radialAnchor, a -> awayFrom(a.getX())),
                    perAnchor(radialAnchor, a -> awayFrom(a.getY())));
        }
        return displacement;
    }

    /**
     * Moves the label away from its variable anchor by the length of text-offset on each axis, plus the text padding,
     * as Mapbox does: the sign of each axis comes from the anchor, not from the offset.
     */
    private Displacement variableAnchorDisplacement(Object anchor, Expression fontSize) {
        double[] offset = getTextOffset();
        Expression signX = perAnchor(anchor, a -> awayFrom(a.getX()));
        Expression signY = perAnchor(anchor, a -> awayFrom(a.getY()));
        Displacement displacement = sf.displacement(
                ff.multiply(fontSize, ff.multiply(ff.literal(Math.abs(offset[0])), signX)),
                ff.multiply(fontSize, ff.multiply(ff.literal(Math.abs(offset[1])), signY)));
        addVariableAnchorPadding(displacement, signX, signY);
        return displacement;
    }

    /**
     * Returns a value of the radial anchor: a literal for an anchor name, a zoom step expression for a step of names.
     */
    @SuppressWarnings("unchecked") // json-simple JSONArray and JSONObject are raw collections
    private Expression perAnchor(Object radialAnchor, ToDoubleFunction<TextAnchor> value) {
        if (radialAnchor instanceof String name) {
            return ff.literal(value.applyAsDouble(TextAnchor.parse(name)));
        }
        // after the step input, outputs and stops alternate: output, stop, output, ...
        JSONArray step = new JSONArray();
        step.addAll((JSONArray) radialAnchor);
        for (int i = 2; i < step.size(); i += 2) {
            step.set(i, value.applyAsDouble(TextAnchor.parse((String) step.get(i))));
        }
        JSONObject holder = new JSONObject();
        holder.put("value", step);
        return parse.number(holder, "value", 0);
    }

    /**
     * Returns the way the label moves along one axis, away from the side of the label placed at the point: 1 from the
     * low side (anchor 0), -1 from the high side (anchor 1), 0 when centered. SLD y grows upwards.
     */
    private static double awayFrom(double anchor) {
        return Math.signum(0.5 - anchor);
    }

    /** Corners move along the diagonal, the same distance as sides, like Mapbox does. */
    private static double diagonal(TextAnchor anchor) {
        return anchor.getX() != 0.5 && anchor.getY() != 0.5 ? Math.sqrt(0.5) : 1;
    }

    private boolean hasTextVariableAnchor() {
        return layout.get("text-variable-anchor") != null;
    }

    /**
     * Mapbox lays out labels with variable anchors on a collision box grown by the text padding, so the label moves
     * away from the point by the padding too, on each axis its offset points along (sign -1, 0 or 1).
     */
    private void addVariableAnchorPadding(Displacement offset, Expression signX, Expression signY) {
        offset.setDisplacementX(ff.add(offset.getDisplacementX(), ff.multiply(textPadding(), signX)));
        offset.setDisplacementY(ff.add(offset.getDisplacementY(), ff.multiply(textPadding(), signY)));
    }

    /**
     * Returns the maxDisplacement vendor option for variable anchors. The renderer searches the other anchors only
     * while its radius, which starts at the length of the label displacement, stays within maxDisplacement: return just
     * above that length, enough to flip the label to the other anchors, not so much it wanders. The length follows zoom
     * and feature like the displacement. Without a displacement, allow a one-font-size flip.
     */
    private String maxLabelDisplacement(Displacement displacement, Expression fontSize) {
        Expression length = fontSize;
        if (displacement != null) {
            Expression dx = displacement.getDisplacementX();
            Expression dy = displacement.getDisplacementY();
            length = ff.function("sqrt", ff.add(ff.multiply(dx, dx), ff.multiply(dy, dy)));
        }
        return MBStyleTransformer.vendorOptionExpression(ff.add(ff.function("ceil", length), ff.literal(1)));
    }

    /**
     * (Optional) Defaults to false. Requires text-field.
     *
     * <p>If true, the text will be visible even if it collides with other previously drawn symbols.
     *
     * @return Whether or not the text should be allowed to overlap other symbols
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Boolean getTextAllowOverlap() throws MBFormatException {
        return parse.getBoolean(layout, "text-allow-overlap", false);
    }

    /**
     * Wraps {@link #getTextAllowOverlap()} in a GeoTools {@link Expression}.
     *
     * <p>(Optional) Defaults to false. Requires text-field.
     *
     * <p>If true, the text will be visible even if it collides with other previously drawn symbols.
     *
     * @return Whether or not the symbols should be allowed to overlap other symbols
     */
    public Expression textAllowOverlap() throws MBFormatException {
        return parse.bool(layout, "text-allow-overlap", false);
    }

    /**
     * Defaults to false. Requires text-field.
     *
     * <p>If true, other symbols can be visible even if they collide with the text.
     *
     * @return Whether or not other symbols should be allowed to overlap text in this layer.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Boolean getTextIgnorePlacement() throws MBFormatException {
        return parse.getBoolean(layout, "text-ignore-placement", false);
    }

    /**
     * Wraps {@link #getTextIgnorePlacement()} in a GeoTools expression Defaults to false. Requires text-field.
     *
     * <p>If true, other symbols can be visible even if they collide with the text.
     *
     * @return Boolean
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textIgnorePlacement() {
        return parse.bool(layout, "text-ignore-placement", false);
    }

    /**
     * Defaults to false. Requires text-field. Requires icon-image.
     *
     * <p>If true, icons will display without their corresponding text when the text collides with other symbols and the
     * icon does not.
     *
     * @return Whether or not the symbol may be drawn when the label is not drawn due to collisions
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Boolean getTextOptional() throws MBFormatException {
        return parse.getBoolean(layout, "text-optional", false);
    }

    /**
     * Wraps {@link #getTextOptional()} in a GeoTools expression.
     *
     * <p>Defaults to false. Requires text-field. Defaults to false. Requires text-field. Requires icon-image.
     *
     * <p>If true, icons will display without their corresponding text when the text collides with other symbols and the
     * icon does not.
     *
     * @return Boolean
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textOptional() {
        return parse.bool(layout, "text-optional", false);
    }

    /**
     * (Optional) Defaults to 1. Requires icon-image.
     *
     * <p>The opacity at which the icon will be drawn.
     *
     * @return Opacity of the icon
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getIconOpacity() throws MBFormatException {
        return parse.optional(Number.class, paint, "icon-opacity", 1.0);
    }

    /**
     * Access icon-opacity as literal or function expression
     *
     * @return Opacity of the icon
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression iconOpacity() throws MBFormatException {
        return parse.percentage(paint, "icon-opacity", 1.0);
    }

    /**
     * (Optional) Defaults to #000000. Requires icon-image.
     *
     * <p>The color of the icon. This can only be used with sdf icons.
     *
     * @return Color of the icon.
     */
    public Color getIconColor() {
        return parse.optional(Color.class, paint, "icon-color", Color.BLACK);
    }

    /**
     * Access icon-color as literal or function expression, defaults to black.
     *
     * @return Color of the icon as an Expression
     */
    public Expression iconColor() {
        return parse.color(paint, "icon-color", Color.BLACK);
    }

    /**
     * (Optional) Defaults to rgba(0, 0, 0, 0). Requires icon-image.
     *
     * <p>The color of the icon's halo. Icon halos can only be used with SDF icons.
     *
     * @return Color of the icon's halo.
     */
    public Color getIconHaloColor() {
        return parse.optional(Color.class, paint, "icon-halo-color", new Color(0, 0, 0, 0));
    }

    /**
     * Access icon-halo-color as literal or function expression, defaults to black.
     *
     * @return Color of the icon's halo.
     */
    public Expression iconHaloColor() {
        return parse.color(paint, "icon-halo-color", Color.BLACK);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0. Requires icon-image.
     *
     * <p>Distance of halo to the icon outline.
     *
     * @return Width of the icon halo
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getIconHaloWidth() throws MBFormatException {
        return parse.optional(Number.class, paint, "icon-halo-width", 0.0);
    }

    /**
     * Access icon-halo-width as literal or function expression
     *
     * @return Width of the icon halo
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression iconHaloWidth() {
        return parse.percentage(paint, "icon-halo-width", 0.0);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0. Requires icon-image.
     *
     * <p>Fade out the halo towards the outside.
     *
     * @return Size of the halo fade
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getIconHaloBlur() throws MBFormatException {
        return parse.optional(Number.class, paint, "icon-halo-blur", 0.0);
    }

    /**
     * Access icon-halo-blur as literal or function expression
     *
     * @return Size of the halo fade
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression iconHaloBlur() {
        return parse.percentage(paint, "icon-halo-blur", 0.0);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0,0. Requires icon-image.
     *
     * <p>Distance that the icon's anchor is moved from its original placement. Positive values indicate right and down,
     * while negative values indicate left and up.
     *
     * @return Translation of the icon from its origin
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public int[] getIconTranslate() throws MBFormatException {
        return parse.array(paint, "icon-translate", new int[] {0, 0});
    }

    /**
     * Units in pixels. Defaults to 0,0. Requires icon-image.
     *
     * <p>Distance that the icon's anchor is moved from its original placement. Positive values indicate right and down,
     * while negative values indicate left and up.
     *
     * @return Translation of the icon from its origin
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Point iconTranslate() {
        int[] translate = getIconTranslate();
        return new Point(translate[0], translate[1]);
    }

    /**
     * Maps {@link #getIconTranslate()} to a {@link Displacement}
     *
     * <p>(Optional) Units in pixels. Defaults to 0,0. Requires icon-image. Distance that the icon's anchor is moved
     * from its original placement. Positive values indicate right and down, while negative values indicate left and up.
     *
     * @return Icon translate displacement
     */
    public Displacement iconTranslateDisplacement() {
        return parse.displacement(paint, "icon-translate", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    /**
     * (Optional) One of map, viewport. Defaults to map. Requires icon-image. Requires icon-translate.
     *
     * <p>Controls the translation reference point.
     *
     * <p>{@link TranslateAnchor#MAP}: Icons are translated relative to the map.
     *
     * <p>{@link TranslateAnchor#VIEWPORT}: Icons are translated relative to the viewport.
     *
     * <p>Defaults to {@link TranslateAnchor#MAP}.
     *
     * @return The location of the translation anchor.
     */
    public TranslateAnchor getIconTranslateAnchor() {
        Object value = paint.get("icon-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return TranslateAnchor.VIEWPORT;
        } else {
            return TranslateAnchor.MAP;
        }
    }

    /**
     * Converts {@link #getIconTranslateAnchor()} to a GeoTools expression. Returns an expression that evaluates to one
     * of "map", "viewport".
     *
     * @see #getIconTranslateAnchor()
     * @return an expression that evaluates to one of "map", "viewport".
     */
    public Expression iconTranslateAnchor() {
        return parse.enumToExpression(layout, "icon-translate-anchor", TranslateAnchor.class, TranslateAnchor.MAP);
    }

    /**
     * (Optional) Defaults to 1. Requires text-field.
     *
     * <p>The opacity at which the text will be drawn.
     *
     * @return Opacity of the label
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextOpacity() throws MBFormatException {
        return parse.optional(Number.class, paint, "text-opacity", 1.0);
    }

    /**
     * Access text-opacity as literal or function expression
     *
     * @return Opacity of the label
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textOpacity() throws MBFormatException {
        return parse.percentage(paint, "text-opacity", 1.0);
    }

    /**
     * Defaults to #000000. Requires text-field.
     *
     * <p>The color with which the text will be drawn.
     *
     * @return The label color.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Color getTextColor() throws MBFormatException {
        return parse.convertToColor(parse.optional(String.class, paint, "text-color", "#000000"));
    }

    /**
     * Access text-color as literal or function expression, defaults to black.
     *
     * @return The label color.
     */
    public Expression textColor() {
        return parse.color(paint, "text-color", Color.BLACK);
    }

    /**
     * Defaults to rgba(0, 0, 0, 0). Requires text-field.
     *
     * <p>The color of the text's halo, which helps it stand out from backgrounds.
     *
     * @return The label halo color.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Color getTextHaloColor() throws MBFormatException {
        if (!paint.containsKey("text-halo-color")) {
            return DEFAULT_HALO_COLOR;
        } else {
            return parse.convertToColor(parse.optional(String.class, paint, "text-halo-color", "#000000"));
        }
    }

    /**
     * Access text-halo-color as literal or function expression, defaults to black.
     *
     * @return The label halo color.
     */
    public Expression textHaloColor() {
        return parse.color(paint, "text-halo-color", DEFAULT_HALO_COLOR);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0. Requires text-field.
     *
     * <p>Distance of halo to the font outline. Max text halo width is 1/4 of the font-size.
     *
     * @return Size of the label halo
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextHaloWidth() throws MBFormatException {
        return parse.optional(Number.class, paint, "text-halo-width", 0.0);
    }

    /**
     * Access text-halo-width as literal or function expression
     *
     * @return Size of the label halo
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textHaloWidth() throws MBFormatException {
        return parse.percentage(paint, "text-halo-width", 0.0);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0. Requires text-field.
     *
     * <p>The halo's fadeout distance towards the outside.
     *
     * @return Size of the label halo fade
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Number getTextHaloBlur() throws MBFormatException {
        return parse.optional(Number.class, paint, "text-halo-blur", 0.0);
    }

    /**
     * Access text-halo-blur as literal or function expression
     *
     * @return Size of the label halo fade
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Expression textHaloBlur() throws MBFormatException {
        return parse.percentage(paint, "text-halo-blur", 0.0);
    }

    /**
     * (Optional) Units in pixels. Defaults to 0,0. Requires text-field.
     *
     * <p>Distance that the text's anchor is moved from its original placement. Positive values indicate right and down,
     * while negative values indicate left and up.
     *
     * @return The translation of hte lable form its anchor.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public int[] getTextTranslate() {
        return parse.array(paint, "text-translate", new int[] {0, 0});
    }

    /**
     * (Optional) Units in pixels. Defaults to 0,0. Requires text-field.
     *
     * <p>Distance that the text's anchor is moved from its original placement. Positive values indicate right and down,
     * while negative values indicate left and up.
     *
     * @return The translation of hte lable form its anchor.
     * @throws MBFormatException JSON definition inconsistent with specification
     */
    public Point textTranslate() {
        int[] translate = getTextTranslate();
        return new Point(translate[0], translate[1]);
    }

    private boolean hasTextTranslate() {
        return parse.isDefined(layout, "text-translate");
    }

    /**
     * Maps {@link #getTextTranslate()} to a {@link Displacement}.
     *
     * <p>Distance that the text's anchor is moved from its original placement. Positive values indicate right and down,
     * while negative values indicate left and up. (Optional) Units in pixels. Defaults to 0,0. Requires text-field.
     *
     * @return Displacement defined by text-translate
     */
    public Displacement textTranslateDisplacement() {
        return parse.displacement(paint, "text-translate", sf.displacement(ff.literal(0), ff.literal(0)));
    }

    /**
     * (Optional) One of map, viewport. Defaults to map. Requires text-field. Requires text-translate.
     *
     * <p>Controls the translation reference point.
     *
     * <p>{@link TranslateAnchor#MAP}: The text is translated relative to the map.
     *
     * <p>{@link TranslateAnchor#VIEWPORT}: The text is translated relative to the viewport.
     *
     * <p>Defaults to {@link TranslateAnchor#MAP}.
     *
     * @return The anchor the tect is translated relative to
     */
    public TranslateAnchor getTextTranslateAnchor() {
        Object value = paint.get("text-translate-anchor");
        if (value != null && "viewport".equalsIgnoreCase((String) value)) {
            return TranslateAnchor.VIEWPORT;
        } else {
            return TranslateAnchor.MAP;
        }
    }

    /**
     * Converts {@link #getTextTranslateAnchor()} to a GeoTools expression. Returns an expression that evaluates to one
     * of "map", "viewport".
     *
     * @see #getTextTranslateAnchor()
     * @return Expresesion of text translate anchor
     */
    public Expression textTranslateAnchor() {
        return parse.enumToExpression(layout, "text-translate-anchor", TranslateAnchor.class, TranslateAnchor.MAP);
    }

    /**
     * Transform {@link SymbolMBLayer} to GeoTools FeatureTypeStyle.
     *
     * @param styleContext The MBStyle to which this layer belongs, used as a context for things like resolving sprite
     *     and glyph names to full urls.
     * @return FeatureTypeStyle
     */
    @Override
    public List<FeatureTypeStyle> transformInternal(MBStyle styleContext) {
        MBStyleTransformer transformer = new MBStyleTransformer(parse);
        StyleBuilder sb = new StyleBuilder();
        List<Symbolizer> symbolizers = new ArrayList<>();

        LabelPlacement labelPlacement;
        // Create point or line placement

        // Functions not yet supported for symbolPlacement, so try to evaluate or use default.
        String symbolPlacementVal = MBStyleTransformer.requireLiteral(
                symbolPlacement(), String.class, "point", "symbol-placement", getId());
        Expression fontSize = textSize();
        if ("point".equalsIgnoreCase(symbolPlacementVal.trim())) {
            // Point Placement (default)
            PointPlacement pointP = sb.createPointPlacement();
            // Set anchor point (translated by text-translate)
            // GeoTools AnchorPoint doesn't seem to have an effect on PointPlacement
            pointP.setAnchorPoint(anchorPoint());

            // MapBox text-translate: +y means down, expressed in px
            Displacement displacement = null;
            if (hasTextTranslate()) {
                Displacement textTranslate = textTranslateDisplacement();
                textTranslate.setDisplacementY(ff.multiply(ff.literal(-1), textTranslate.getDisplacementY()));
                displacement = textTranslate;
            }
            // MapBox text-radial-offset: moves the label away from its anchor, in ems, and wins over text-offset
            // with variable anchors and no offset Mapbox places the label as with a zero radial offset
            boolean radial = hasTextRadialOffset() || hasTextVariableAnchor() && !hasTextOffset();
            // with variable anchors the label sits at the first anchor, and text-offset moves it away from it
            boolean anchored = radial || hasTextVariableAnchor();
            Object radialAnchor = anchored ? radialAnchor() : null;
            if (anchored && radialAnchor == null) {
                LOGGER.warning("Mapbox label anchor ignored on layer '" + getId()
                        + "': its text-anchor or text-variable-anchor form is not supported");
            }
            Displacement textOffset = null;
            if (radialAnchor != null) {
                pointP.setAnchorPoint(sf.anchorPoint(
                        perAnchor(radialAnchor, TextAnchor::getX), perAnchor(radialAnchor, TextAnchor::getY)));
                textOffset = radial
                        ? radialDisplacement(radialAnchor, fontSize)
                        : variableAnchorDisplacement(radialAnchor, fontSize);
            } else if (hasTextOffset()) {
                // MapBox text-offset: +y means down, in ems
                textOffset = textOffsetDisplacement();
                textOffset.setDisplacementX(ff.multiply(fontSize, textOffset.getDisplacementX()));
                textOffset.setDisplacementY(
                        ff.multiply(fontSize, ff.multiply(ff.literal(-1), textOffset.getDisplacementY())));
            }
            if (textOffset != null) {
                if (displacement == null) {
                    displacement = textOffset;
                } else {
                    displacement.setDisplacementX(
                            ff.add(displacement.getDisplacementX(), textOffset.getDisplacementX()));
                    displacement.setDisplacementY(
                            ff.add(displacement.getDisplacementY(), textOffset.getDisplacementY()));
                }
            }
            pointP.setDisplacement(displacement);
            pointP.setRotation(textRotate());

            labelPlacement = pointP;
        } else {
            // Line Placement
            LinePlacement lineP = sb.createLinePlacement(null);
            lineP.setRepeated(true);

            // pixels (geotools) vs ems (mapbox) for text-offset
            lineP.setPerpendicularOffset(ff.multiply(
                    fontSize,
                    ff.multiply(ff.literal(-1), textOffsetDisplacement().getDisplacementY())));

            labelPlacement = lineP;
        }

        // the default value of the halo color is rgba(0,0,0,0) that is, no halo drawn,
        // regardless of the value of other halo parameters
        Expression haloColor = textHaloColor();
        Halo halo = null;
        if (!(haloColor instanceof Literal)
                || haloColor.evaluate(null, Color.class).getAlpha() > 0) {
            halo = sf.halo(sf.fill(null, haloColor, null), textHaloWidth());
        }
        Fill fill = sf.fill(null, textColor(), textOpacity());

        // leverage GeoTools ability to have several distinct fonts, inherited from SLD 1.0
        List<Font> fonts = new ArrayList<>();
        List<String> staticFonts = getTextFont();
        if (staticFonts != null) {
            for (String textFont : staticFonts) {
                FontAttributesExtractor fae = new FontAttributesExtractor(textFont);
                if (!fae.getBaseName().equals(textFont)) {
                    fonts.add(exactFaceFont(ff.literal(textFont), fontSize));
                }
                Font font = sb.createFont(
                        ff.function(FontAlternativesFunction.NAME.getName(), ff.literal(fae.getBaseName())),
                        ff.literal(fae.isItalic() ? "italic" : "normal"),
                        ff.literal(fae.isBold() ? "bold" : "normal"),
                        fontSize);
                fonts.add(font);
            }
        } else if (textFont() != null) {
            Expression dynamicFont = textFont();
            fonts.add(exactFaceFont(dynamicFont, fontSize));
            Font font = sb.createFont(
                    ff.function(
                            FontAlternativesFunction.NAME.getName(),
                            ff.function(MapBoxFontBaseNameFunction.NAME.getName(), dynamicFont)),
                    ff.function(MapBoxFontStyleFunction.NAME.getName(), dynamicFont),
                    ff.function(MapBoxFontWeightFunction.NAME.getName(), dynamicFont),
                    fontSize);
            fonts.add(font);
        }

        // If the textField is a literal string (not a function), then
        // we need to support Mapbox token replacement.
        Expression textExpression = textField();
        boolean pureSymbol = false;
        if (textExpression instanceof Literal) {
            String text = textExpression.evaluate(null, String.class);
            if (text.trim().isEmpty()) {
                textExpression = ff.literal(" ");
                pureSymbol = true;
            } else {
                textExpression = transformer.cqlExpressionFromTokens(text);
            }
        }
        if (hasTextTransform()) {
            textExpression = ff.function("StringTransform", textExpression, textTransform());
        }

        TextSymbolizer symbolizer = sf.textSymbolizer(
                getId(),
                ff.property((String) null),
                sf.description(Text.text("text"), null),
                Units.PIXEL,
                textExpression,
                null,
                labelPlacement,
                halo,
                fill);
        symbolizer.fonts().clear();
        // no need to add fonts and alternatives if we are displaying a simple symbol, use a tiny
        // font to help centering the symbol along the line instead
        if (pureSymbol) {
            symbolizer.setFont(TINY_FONT);
        } else {
            symbolizer.fonts().addAll(fonts);
        }

        Number symbolSpacing =
                MBStyleTransformer.requireLiteral(symbolSpacing(), Number.class, 250, "symbol-spacing", getId());
        Map<String, String> options = symbolizer.getOptions();
        options.put(TextSymbolizer.LABEL_REPEAT_KEY, String.valueOf(symbolSpacing));

        // Mapbox text-variable-anchor: the sides the label may flip to when the preferred spot collides.
        // maxDisplacement must be set too, or the renderer never searches the other anchors
        if (labelPlacement instanceof PointPlacement) {
            String displacementMode = displacementModeFromVariableAnchor();
            if (!displacementMode.isEmpty()) {
                options.put(TextSymbolizer.DISPLACEMENT_MODE_KEY, displacementMode);
                // Mapbox tries each anchor with the label fully on that side, never across the point
                options.put(TextSymbolizer.DISPLACEMENT_ANCHOR_KEY, "outside");
                Displacement displacement = ((PointPlacement) labelPlacement).getDisplacement();
                options.put(TextSymbolizer.MAX_DISPLACEMENT_KEY, maxLabelDisplacement(displacement, fontSize));
                if (isZoomDrivenVariableAnchor()) {
                    LOGGER.warning("Mapbox 'text-variable-anchor' is zoom-driven on layer '"
                            + getId()
                            + "'; displacementMode is a static vendor option, so the union of all zoom stops ("
                            + displacementMode
                            + ") is applied at every zoom.");
                }
            }
        }

        // text max angle - only for line placement
        // throw MBFormatException if point placement
        if (labelPlacement instanceof LinePlacement) {
            // Ensure the symbol follows the line, if there is no text (e.g. one ways)
            if (pureSymbol) {
                options.put(TextSymbolizer.FORCE_LEFT_TO_RIGHT_KEY, "false");
            } else {
                // otherwise align with the text being drawn (e.g. label shields)
                options.put(TextSymbolizer.FORCE_LEFT_TO_RIGHT_KEY, String.valueOf(textKeepUpright()));
            }
            // "followLine" will be true if line placement, it is an implied default of MBstyles.
            options.put(TextSymbolizer.FOLLOW_LINE_KEY, "true");
            options.put(TextSymbolizer.MAX_ANGLE_DELTA_KEY, String.valueOf(getTextMaxAngle()));
            options.put(TextSymbolizer.GROUP_KEY, "true");
            options.put(TextSymbolizer.LABEL_ALL_GROUP_KEY, "true");
            // a road split in segments ranks like its best segment, Mapbox places each segment on its own
            options.put(TextSymbolizer.GROUP_PRIORITY_KEY, GroupPriority.MAX.name());
        } else if (hasTextMaxAngle()) {
            throw new MBFormatException(
                    "Property text-max-angle requires symbol-placement = line but symbol-placement = "
                            + symbolPlacementVal);
        }
        // conflictResolution
        // Mapbox allows text overlap and icon overlap separately. GeoTools only has
        // conflictResolution.
        Boolean textAllowOverlap = MBStyleTransformer.requireLiteral(
                textAllowOverlap(), Boolean.class, false, "text-allow-overlap", getId());
        Boolean iconAllowOverlap = MBStyleTransformer.requireLiteral(
                iconAllowOverlap(), Boolean.class, false, "icon-allow-overlap", getId());

        options.put(TextSymbolizer.CONFLICT_RESOLUTION_KEY, String.valueOf(!(textAllowOverlap || iconAllowOverlap)));

        String textFitVal = MBStyleTransformer.requireLiteral(
                        iconTextFit(), String.class, "none", "icon-text-fit", getId())
                .trim();
        if ("height".equalsIgnoreCase(textFitVal) || "width".equalsIgnoreCase(textFitVal)) {
            options.put(TextSymbolizer.GRAPHIC_RESIZE_KEY, STRETCH.name());
        } else if ("both".equalsIgnoreCase(textFitVal)) {
            options.put(TextSymbolizer.GRAPHIC_RESIZE_KEY, PROPORTIONAL.name());
        } else {
            // Default
            options.put(TextSymbolizer.GRAPHIC_RESIZE_KEY, NONE.name());
        }

        // Kept commented out as a reminder not to bring this back. It breaks rendering
        // on server side, as the labels are actually cut at tile borders, unlike client side
        // where they can just continue over, and one can hope they won't overlap
        //        if (!getSymbolAvoidEdges()) {
        //            symbolizer.getOptions().put(PARTIALS_KEY, "true");
        //        }
        options.put(TextSymbolizer.PARTIALS_KEY, "false");

        // Mapbox allows you to sapecify an array of values, one for each side
        if (getIconTextFitPadding() != null && !getIconTextFitPadding().isEmpty()) {
            options.put(
                    TextSymbolizer.GRAPHIC_MARGIN_KEY,
                    String.valueOf(getIconTextFitPadding().get(0)));
        } else {
            options.put(TextSymbolizer.GRAPHIC_MARGIN_KEY, "0");
        }

        // text-padding default value is 2 in mapbox, will override Geoserver defaults
        if (!hasIconImage()
                || "point".equalsIgnoreCase(symbolPlacementVal.trim())
                || getTextPadding().doubleValue() >= getIconPadding().doubleValue()) {
            options.put(TextSymbolizer.SPACE_AROUND_KEY, spaceAround(textPadding()));
        }
        // halo blur
        // layer.textHaloBlur();

        // text-max-width is in ems, autoWrap in pixels.
        // Mapbox wraps point labels at 10 ems even without text-max-width, and never wraps line labels
        if ("point".equalsIgnoreCase(symbolPlacementVal.trim())) {
            options.put(
                    TextSymbolizer.AUTO_WRAP_KEY,
                    MBStyleTransformer.vendorOptionExpression(ff.multiply(textMaxWidth(), fontSize)));
        }
        // text-letter-spacing is in ems, charSpacing in pixels
        if (hasTextLetterSpacing()) {
            options.put(
                    TextSymbolizer.CHAR_SPACING_KEY,
                    MBStyleTransformer.vendorOptionExpression(ff.multiply(textLetterSpacing(), fontSize)));
        }

        // If the layer has an icon image, add it to our symbolizer
        if (hasIconImage()) {
            // Check to see that hasTextField() is true check to see if IconPadding is greater to
            // put to spaceAround
            if (!hasTextField()
                    || getIconPadding().doubleValue() > getTextPadding().doubleValue()
                            && !"point".equalsIgnoreCase(symbolPlacementVal.trim())) {
                options.put(TextSymbolizer.SPACE_AROUND_KEY, spaceAround(iconPadding()));
            }
            // If we have an icon with a Point placement force graphic placement independ
            // of the label final position (each one gets its own anchor and displacement)
            Graphic graphic = getGraphic(transformer, styleContext);
            if ("point".equalsIgnoreCase(symbolPlacementVal.trim())) {
                options.put(TextSymbolizer.GRAPHIC_PLACEMENT_KEY, INDEPENDENT.name());
            }
            // the mapbox-gl library does not paint the graphic if the icon cannot be found
            options.put(PointSymbolizer.FALLBACK_ON_DEFAULT_MARK, "false");
            symbolizer.setGraphic(graphic);
        }

        // make sure rendering paints the labels in the same layer based order that
        // Mapbox GL would use
        if (labelPriority != null) {
            symbolizer.setPriority(labelPriority());
        }

        symbolizers.add(symbolizer);
        MBFilter filter = getFilter();

        // List of opengis rules here (needed for constructor)
        List<org.geotools.api.style.Rule> rules = new ArrayList<>();
        Rule rule = sf.rule(getId(), null, null, 0.0, Double.POSITIVE_INFINITY, symbolizers, filter.filter());

        rules.add(rule);

        return Collections.singletonList(sf.featureTypeStyle(
                getId(),
                sf.description(Text.text("MBStyle " + getId()), Text.text("Generated for " + getSourceLayer())),
                null, // (unused)
                Collections.emptySet(),
                filter.semanticTypeIdentifiers(), // we only expect this to be applied to
                // polygons
                rules));
    }

    /**
     * Returns a font for the exact face named, such as "Arial Black", at normal style and weight. The face carries its
     * own weight and style. A missing face resolves to no font, so the next font in the list takes over.
     */
    private Font exactFaceFont(Expression faceName, Expression fontSize) {
        return sf.createFont(
                ff.function(FontAlternativesFunction.NAME.getName(), faceName),
                ff.literal("normal"),
                ff.literal("normal"),
                fontSize);
    }

    /**
     * Get a graphic for this style's 'icon-image'. It will usually be an {@link ExternalGraphic} to be handled by the
     * {@link SpriteGraphicFactory}, but this method also supports GeoTools {@link Mark}s as a special case.
     *
     * @param styleContext The containing style (used to get the sprite source)
     * @return A graphic based on this style's 'icon-image' property.
     */
    private Graphic getGraphic(MBStyleTransformer transformer, MBStyle styleContext) {
        // If the iconImage is a literal string (not a function), then
        // we need to support Mapbox token replacement.
        // Note: the URL is expected to be a CQL STRING ...
        Expression iconExpression = iconImage();
        if (iconExpression instanceof Literal) {
            iconExpression = transformer.cqlExpressionFromTokens(iconExpression.evaluate(null, String.class));
        }

        Expression graphicSize = null;
        GraphicalSymbol gs;

        // In the special case that the 'sprite' source designates the internal GeoTools marks, then
        // create a mark graphic.
        // Otherwise, create a sprite-based external graphic.
        String spriteSheetLocation = styleContext.getSprite() == null
                ? ""
                : styleContext.getSprite().trim().toLowerCase();
        Expression iconSize = iconSize();
        if (MARK_SHEET_ALIASES.contains(spriteSheetLocation)) {
            Fill f = sf.fill(null, iconColor(), null);
            Stroke s = sf.stroke(iconColor(), null, null, null, null, null, null);
            gs = sf.mark(iconExpression, f, s);
        } else {
            gs = transformer.createExternalGraphicForSprite(iconExpression, iconSize, styleContext);
        }

        if (gs instanceof Mark) {
            // The graphicSize is specified in pixels, so only set it on the Graphic if the
            // GraphicalSymbol is a mark.
            // If it is an ExternalGraphic from a sprite sheet, the absolute size of the icon is
            // unknown at this point.
            graphicSize = ff.multiply(ff.literal(MARK_ICON_DEFAULT_SIZE), iconSize);
        }

        Graphic g = sf.graphic(Arrays.asList(gs), iconOpacity(), graphicSize, iconRotate(), null, null);
        // From the specification:
        // Offset distance of icon from its anchor. Positive values indicate right and down, while
        // negative values indicate left and up. Each component is multiplied by the value of
        // icon-size to obtain the final offset in pixels
        Displacement d = iconOffsetDisplacement();
        d.setDisplacementY(ff.multiply(iconSize, ff.multiply(ff.literal(-1), d.getDisplacementY())));
        d.setDisplacementX(ff.multiply(iconSize, d.getDisplacementX()));
        g.setDisplacement(d);
        g.setAnchorPoint(iconAnchorPoint());

        return g;

        // ADD A VENDOR OPTION FOR THE POINT INDEPENDENT LOCATION!!
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

    /** Returns the label priority: the layer priority, raised by the symbol-sort-key offset if the layer has one. */
    private Expression labelPriority() {
        if (!parse.isDefined(layout, "symbol-sort-key")) {
            return ff.literal(labelPriority);
        }
        // a feature without the key sorts as 0, like in Mapbox: a NaN priority would beat every other label
        Expression key = parse.number(layout, "symbol-sort-key", 0);
        Expression sortKey = ff.function("if_then_else", ff.function("isNull", key), ff.literal(0), key);
        return ff.add(ff.literal(labelPriority), sortKeyOffset(sortKey));
    }

    /**
     * Maps a symbol-sort-key to an offset between 1 and 999, added to the layer priority. Each layer owns a 1000 wide
     * priority band (see {@link MBStyle#layers()}), so the labels of a layer stay above its priority and below the next
     * layer. Mapbox places lower keys first, while GeoTools places higher priorities first, so the offset decreases as
     * the key grows:
     *
     * <pre>{@code
     * key:     -1e15 ... -1000    -1    0    1   1000 ... 1e15
     * offset:    999 ...   600   510  500  490    400 ...    1
     * }</pre>
     *
     * The offset follows the logarithm of the key, so small keys (e.g. zoom levels) and keys in the millions both keep
     * distinct priorities. Keys beyond +-1e15 get the end offsets:
     *
     * <pre>{@code
     * distance = SORT_KEY_HALF_RANGE * min(1, ln(1 + abs(key)) / LOG_MAX_SORT_KEY)
     * offset   = SORT_KEY_MIDDLE + distance   if key < 0
     *            SORT_KEY_MIDDLE - distance   otherwise
     * }</pre>
     */
    private Expression sortKeyOffset(Expression sortKey) {
        Expression logKey = ff.function("log", ff.add(ff.literal(1), ff.function("abs_4", sortKey)));
        Expression scaled = ff.function("min", ff.literal(1), ff.divide(logKey, ff.literal(LOG_MAX_SORT_KEY)));
        Expression distance = ff.multiply(ff.literal(SORT_KEY_HALF_RANGE), scaled);
        return ff.function(
                "if_then_else",
                ff.function("lessThan", sortKey, ff.literal(0)),
                ff.add(ff.literal(SORT_KEY_MIDDLE), distance),
                ff.subtract(ff.literal(SORT_KEY_MIDDLE), distance));
    }

    public Integer getLabelPriority() {
        return labelPriority;
    }

    public void setLabelPriority(Integer labelPriority) {
        this.labelPriority = labelPriority;
    }
}
