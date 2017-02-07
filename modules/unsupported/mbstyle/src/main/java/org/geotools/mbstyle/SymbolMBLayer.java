package org.geotools.mbstyle;

import java.awt.Color;
import java.util.List;

import org.json.simple.JSONObject;
import org.opengis.filter.expression.Expression;

public class SymbolMBLayer extends MBLayer {

    private JSONObject layout;

    private JSONObject paint;

    private static String TYPE = "symbol";

    public static enum SymbolPlacement {
        /**
         * The label is placed at the point where the geometry is located.
         */
        POINT,

        /**
         * The label is placed along the line of the geometry. Can only be used on LineString and Polygon geometries.
         */
        LINE
    }

    /**
     * Interpreted differently when applied to different fields.
     */
    public static enum Alignment {
        MAP, VIEWPORT, AUTO
    }

    public static enum IconTextFit {
        /**
         * The icon is displayed at its intrinsic aspect ratio.
         */
        NONE,

        /**
         * The icon is scaled in the x-dimension to fit the width of the text.
         */
        WIDTH,

        /**
         * The icon is scaled in the y-dimension to fit the height of the text.
         */
        HEIGHT,

        /**
         * The icon is scaled in both x- and y-dimensions.
         */
        BOTH
    }

    public static enum Justification {
        /**
         * The text is aligned to the left.
         */
        LEFT,

        /**
         * The text is centered.
         */
        CENTER,

        /**
         * The text is aligned to the right.
         */
        RIGHT
    }

    public static enum TextAnchor {
        /**
         * The center of the text is placed closest to the anchor.
         */
        CENTER,

        /**
         * The left side of the text is placed closest to the anchor.
         */
        LEFT,

        /**
         * The right side of the text is placed closest to the anchor.
         */
        RIGHT,

        /**
         * The top of the text is placed closest to the anchor.
         */
        TOP,

        /**
         * The bottom of the text is placed closest to the anchor.
         */
        BOTTOM,

        /**
         * The top left corner of the text is placed closest to the anchor.
         */
        TOP_LEFT,

        /**
         * The top right corner of the text is placed closest to the anchor.
         */
        TOP_RIGHT,

        /**
         * The bottom left corner of the text is placed closest to the anchor.
         */
        BOTTOM_LEFT,

        /**
         * The bottom right corner of the text is placed closest to the anchor.
         */
        BOTTOM_RIGHT,

    }

    public static enum TextTransform {
        /**
         * The text is not altered.
         */
        NONE,

        /**
         * Forces all letters to be displayed in uppercase.
         */
        UPPERCASE,

        /**
         * Forces all letters to be displayed in lowercase.
         */
        LOWERCASE,

    }

    public static enum TranslateAnchor {
        /**
         * Translation relative to the map.
         */
        MAP,

        /**
         * Translation relative to the viewport.
         */
        VIEWPORT
    }

    /**
     * 
     * @param json
     */
    public SymbolMBLayer(JSONObject json) {
        super(json);
        paint = super.getPaint();
        layout = super.getLayout();
    }

    public SymbolPlacement getSymbolPlacement() {
        // json.get("symbol-placement")
        return null;
    }

    public Expression symbolPlacement() {
        // json.get("symbol-placement")
        return null;
    }

    public Number getSymbolSpacing() {
        // json.get("symbol-spacing")
        return null;
    }

    public Expression symbolSpacing() {
        // json.get("symbol-spacing")
        return null;
    }

    public Boolean getSymbolAvoidEdges() {
        // json.get("symbol-avoid-edges")
        return null;
    }

    public Expression symbolAvoidEdges() {
        // json.get("symbol-avoid-edges")
        return null;
    }

    public Boolean getIconAllowOverlap() {
        // json.get("icon-allow-overlap")
        return null;
    }

    public Expression iconAllowOverlap() {
        // json.get("icon-allow-overlap")
        return null;
    }

    public Boolean getIconIgnorePlacement() {
        // json.get("icon-ignore-placement")
        return null;
    }

    public Expression iconIgnorePlacement() {
        // json.get("icon-ignore-placement")
        return null;
    }

    public Boolean getIconOptional() {
        // json.get("icon-optional")
        return null;
    }

    public Expression iconOptional() {
        // json.get("icon-optional")
        return null;
    }

    /**
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires icon-image. In combination with symbol-placement, determines the rotation
     * behavior of icons.
     * 
     * Possible values:
     * 
     * {@link Alignment#MAP} When symbol-placement is set to point, aligns icons east-west. When symbol-placement is set to line, aligns icon x-axes
     * with the line.
     * 
     * {@link Alignment#VIEWPORT} Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the value of
     * symbol-placement.
     * 
     * {@link Alignment#AUTO} When symbol-placement is set to point, this is equivalent to viewport. When symbol-placement is set to line, this is
     * equivalent to map.
     * 
     * @return
     */
    public Alignment getIconRotationAlignment() {
        // json.get("icon-rotation-alignment")
        return null;
    }

    public Expression iconRotationAlignment() {
        // json.get("icon-rotation-alignment")
        return null;
    }

    public Number getIconSize() {
        // json.get("icon-size")
        return null;
    }

    public Expression iconSize() {
        // json.get("icon-size")
        return null;
    }

    public IconTextFit getIconTextFit() {
        // json.get("icon-text-fit")
        return null;
    }

    public Expression iconTextFit() {
        // json.get("icon-text-fit")
        return null;
    }

    public List<Number> getIconTextFitPadding() {
        // json.get("icon-text-fit-padding")
        return null;
    }

    public Expression iconTextFitPadding() {
        // json.get("icon-text-fit-padding")
        return null;
    }

    public String getIconImage() {
        // json.get("icon-image")
        return null;
    }

    public Expression iconImage() {
        // json.get("icon-image")
        return null;
    }

    public Number getIconRotate() {
        // json.get("icon-rotate")
        return null;
    }

    public Expression iconRotate() {
        // json.get("icon-rotate")
        return null;
    }

    public Number getIconPadding() {
        // json.get("icon-padding")
        return null;
    }

    public Expression iconPadding() {
        // json.get("icon-padding")
        return null;
    }

    public Boolean getIconKeepUpright() {
        // json.get("icon-keep-upright")
        return null;
    }

    public Expression iconKeepUpright() {
        // json.get("icon-keep-upright")
        return null;
    }

    public List<Number> getIconOffset() {
        // json.get("icon-offset")
        return null;
    }

    public Expression iconOffset() {
        // json.get("icon-offset")
        return null;
    }

    /**
     * 
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires text-field. Orientation of text when map is pitched.
     * 
     * Possible values:
     * 
     * {@link Alignment#MAP} The text is aligned to the plane of the map.
     * 
     * {@link Alignment#VIEWPORT} The text is aligned to the plane of the viewport.
     * 
     * {@link Alignment#AUTO} Automatically matches the value of text-rotation-alignment.
     * 
     * @return
     */
    public Alignment getTextPitchAlignment() {
        // json.get("text-pitch-alignment")
        return null;
    }

    public Expression textPitchAlignment() {
        // json.get("text-pitch-alignment")
        return null;
    }

    /**
     * 
     * Optional enum. One of map, viewport, auto. Defaults to auto. Requires text-field. In combination with symbol-placement, determines the rotation
     * behavior of the individual glyphs forming the text.
     * 
     * Possible values:
     * 
     * {@link Alignment#MAP} When symbol-placement is set to point, aligns text east-west. When symbol-placement is set to line, aligns text x-axes
     * with the line.
     * 
     * {@link Alignment#VIEWPORT} Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the value of
     * symbol-placement.
     * 
     * {@link Alignment#AUTO} When symbol-placement is set to point, this is equivalent to viewport. When symbol-placement is set to line, this is
     * equivalent to map.
     * 
     * @return
     */
    public Alignment getTextRotationAlignment() {
        // json.get("text-rotation-alignment")
        return null;
    }

    public Expression textRotationAlignment() {
        // json.get("text-rotation-alignment")
        return null;
    }

    public String getTextField() {
        // json.get("text-field")
        return null;
    }

    public Expression textField() {
        // json.get("text-field")
        return null;
    }

    public List<String> getTextFont() {
        // json.get("text-font")
        return null;
    }

    public Expression textFont() {
        // json.get("text-font")
        return null;
    }

    public Number getTextSize() {
        // json.get("text-size")
        return null;
    }

    public Expression textSize() {
        // json.get("text-size")
        return null;
    }

    public Number getTextMaxWidth() {
        // json.get("text-max-width")
        return null;
    }

    public Expression textMaxWidth() {
        // json.get("text-max-width")
        return null;
    }

    public Number getTextLineHeight() {
        // json.get("text-line-height")
        return null;
    }

    public Expression textLineHeight() {
        // json.get("text-line-height")
        return null;
    }

    public Number getTextLetterSpacing() {
        // json.get("text-letter-spacing")
        return null;
    }

    public Expression textLetterSpacing() {
        // json.get("text-letter-spacing")
        return null;
    }

    public Justification getTextJustify() {
        // json.get("text-justify")
        return null;
    }

    public Expression textJustify() {
        // json.get("text-justify")
        return null;
    }

    public TextAnchor getTextAnchor() {
        // json.get("text-anchor")
        return null;
    }

    public Expression textAnchor() {
        // json.get("text-anchor")
        return null;
    }

    public Number getTextMaxAngle() {
        // json.get("text-max-angle")
        return null;
    }

    public Expression textMaxAngle() {
        // json.get("text-max-angle")
        return null;
    }

    public Number getTextRotate() {
        // json.get("text-rotate")
        return null;
    }

    public Expression textRotate() {
        // json.get("text-rotate")
        return null;
    }

    public Number getTextPadding() {
        // json.get("text-padding")
        return null;
    }

    public Expression textPadding() {
        // json.get("text-padding")
        return null;
    }

    public Boolean getTextKeepUpright() {
        // json.get("text-keep-upright")
        return null;
    }

    public Expression textKeepUpright() {
        // json.get("text-keep-upright")
        return null;
    }

    public TextTransform getTextTransform() {
        // json.get("text-transform")
        return null;
    }

    public Expression textTransform() {
        // json.get("text-transform")
        return null;
    }

    public List<Number> getTextOffset() {
        // json.get("text-offset")
        return null;
    }

    public Expression textOffset() {
        // json.get("text-offset")
        return null;
    }

    public Boolean getTextAllowOverlap() {
        // json.get("text-allow-overlap")
        return null;
    }

    public Expression textAllowOverlap() {
        // json.get("text-allow-overlap")
        return null;
    }

    public Boolean getTextIgnorePlacement() {
        // json.get("text-ignore-placement")
        return null;
    }

    public Expression textIgnorePlacement() {
        // json.get("text-ignore-placement")
        return null;
    }

    public Boolean getTextOptional() {
        // json.get("text-optional")
        return null;
    }

    public Expression textOptional() {
        // json.get("text-optional")
        return null;
    }

    public Number getIconOpacity() {
        // paint.get("icon-opacity")
        return null;
    }

    public Expression iconOpacity() {
        // paint.get("icon-opacity")
        return null;
    }

    public Color getIconColor() {
        // paint.get("icon-color")
        return null;
    }

    public Expression iconColor() {
        // paint.get("icon-color")
        return null;
    }

    public Color getIconHaloColor() {
        // paint.get("icon-halo-color")
        return null;
    }

    public Expression iconHaloColor() {
        // paint.get("icon-halo-color")
        return null;
    }

    public Number getIconHaloWidth() {
        // paint.get("icon-halo-width")
        return null;
    }

    public Expression iconHaloWidth() {
        // paint.get("icon-halo-width")
        return null;
    }

    public Number getIconHaloBlur() {
        // paint.get("icon-halo-blur")
        return null;
    }

    public Expression iconHaloBlur() {
        // paint.get("icon-halo-blur")
        return null;
    }

    public List<Number> getIconTranslate() {
        // paint.get("icon-translate")
        return null;
    }

    public Expression iconTranslate() {
        // paint.get("icon-translate")
        return null;
    }

    public TranslateAnchor getIconTranslateAnchor() {
        // paint.get("icon-translate-anchor")
        return null;
    }

    public Expression iconTranslateAnchor() {
        // paint.get("icon-translate-anchor")
        return null;
    }

    public Number getTextOpacity() {
        // paint.get("text-opacity")
        return null;
    }

    public Expression textOpacity() {
        // paint.get("text-opacity")
        return null;
    }

    public Color getTextColor() {
        // paint.get("text-color")
        return null;
    }

    public Expression textColor() {
        // paint.get("text-color")
        return null;
    }

    public Color getTextHaloColor() {
        // paint.get("text-halo-color")
        return null;
    }

    public Expression textHaloColor() {
        // paint.get("text-halo-color")
        return null;
    }

    public Number getTextHaloWidth() {
        // paint.get("text-halo-width")
        return null;
    }

    public Expression textHaloWidth() {
        // paint.get("text-halo-width")
        return null;
    }

    public Number getTextHaloBlur() {
        // paint.get("text-halo-blur")
        return null;
    }

    public Expression textHaloBlur() {
        // paint.get("text-halo-blur")
        return null;
    }

    public List<Number> getTextTranslate() {
        // paint.get("text-translate")
        return null;
    }

    public Expression textTranslate() {
        // paint.get("text-translate")
        return null;
    }

    public Expression textTranslateAnchor() {
        // paint.get("text-translate-anchor")
        return null;
    }

    public TranslateAnchor getTextTranslateAnchor() {
        // paint.get("text-translate-anchor")
        return null;
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
