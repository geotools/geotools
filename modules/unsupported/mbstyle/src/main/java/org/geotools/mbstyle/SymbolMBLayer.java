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
        MAP,
        VIEWPORT,
        AUTO
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
        return null;
    }

    public Expression symbolPlacement() {
        return null;
    }

    public Number getSymbolSpacing() {
        return null;
    }

    public Expression symbolSpacing() {
        return null;
    }

    public Boolean getSymbolAvoidEdges() {
        return null;
    }

    public Expression symbolAvoidEdges() {
        return null;
    }

    public Boolean getIconAllowOverlap() {
        return null;
    }

    public Expression iconAllowOverlap() {
        return null;
    }

    public Boolean getIconIgnorePlacement() {
        return null;
    }

    public Expression iconIgnorePlacement() {
        return null;
    }

    public Boolean getIconOptional() {
        return null;
    }

    public Expression iconOptional() {
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
        return null;
    }

    public Expression iconRotationAlignment() {
        return null;
    }

    public Number getIconSize() {
        return null;
    }

    public Expression iconSize() {
        return null;
    }

    public IconTextFit getIconTextFit() {
        return null;
    }

    public Expression iconTextFit() {
        return null;
    }

    public List<Number> getIconTextFitPadding() {
        return null;
    }

    public Expression iconTextFitPadding() {
        return null;
    }

    public String getIconImage() {
        return null;
    }

    public Expression iconImage() {
        return null;
    }

    public Number getIconRotate() {
        return null;
    }

    public Expression iconRotate() {
        return null;
    }

    public Number getIconPadding() {
        return null;
    }

    public Expression iconPadding() {
        return null;
    }

    public Boolean getIconKeepUpright() {
        return null;
    }

    public Expression iconKeepUpright() {
        return null;
    }

    public List<Number> getIconOffset() {
        return null;
    }

    public Expression iconOffset() {
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
        return null;
    }

    public Expression textPitchAlignment() {
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
        return null;
    }

    public Expression textRotationAlignment() {
        return null;
    }

    public String getTextField() {
        return null;
    }

    public Expression textField() {
        return null;
    }

    public List<String> getTextFont() {
        return null;
    }

    public Expression textFont() {
        return null;
    }

    public Number getTextSize() {
        return null;
    }

    public Expression textSize() {
        return null;
    }

    public Number getTextMaxWidth() {
        return null;
    }

    public Expression textMaxWidth() {
        return null;
    }

    public Number getTextLineHeight() {
        return null;
    }

    public Expression textLineHeight() {
        return null;
    }

    public Number getTextLetterSpacing() {
        return null;
    }

    public Expression textLetterSpacing() {
        return null;
    }

    public Justification getTextJustify() {
        return null;
    }

    public Expression textJustify() {
        return null;
    }

    public TextAnchor getTextAnchor() {
        return null;
    }

    public Expression textAnchor() {
        return null;
    }

    public Number getTextMaxAngle() {
        return null;
    }

    public Expression textMaxAngle() {
        return null;
    }

    public Number getTextRotate() {
        return null;
    }

    public Expression textRotate() {
        return null;
    }

    public Number getTextPadding() {
        return null;
    }

    public Expression textPadding() {
        return null;
    }

    public Boolean getTextKeepUpright() {
        return null;
    }

    public Expression textKeepUpright() {
        return null;
    }

    public TextTransform getTextTransform() {
        return null;
    }

    public Expression textTransform() {
        return null;
    }

    public List<Number> getTextOffset() {
        return null;
    }

    public Expression textOffset() {
        return null;
    }

    public Boolean getTextAllowOverlap() {
        return null;
    }

    public Expression textAllowOverlap() {
        return null;
    }

    public Boolean getTextIgnorePlacement() {
        return null;
    }

    public Expression textIgnorePlacement() {
        return null;
    }

    public Boolean getTextOptional() {
        return null;
    }

    public Expression textOptional() {
        return null;
    }

    public Number getIconOpacity() {
        return null;
    }

    public Expression iconOpacity() {
        return null;
    }

    public Color getIconColor() {
        return null;
    }

    public Expression iconColor() {
        return null;
    }

    public Color getIconHaloColor() {
        return null;
    }

    public Expression iconHaloColor() {
        return null;
    }

    public Number getIconHaloWidth() {
        return null;
    }

    public Expression iconHaloWidth() {
        return null;
    }

    public Number getIconHaloBlur() {
        return null;
    }

    public Expression iconHaloBlur() {
        return null;
    }

    public List<Number> getIconTranslate() {
        return null;
    }

    public Expression iconTranslate() {
        return null;
    }

    public TranslateAnchor getIconTranslateAnchor() {
        return null;
    }

    public Expression iconTranslateAnchor() {
        return null;
    }

    public Number getTextOpacity() {
        return null;
    }

    public Expression textOpacity() {
        return null;
    }

    public Color getTextColor() {
        return null;
    }

    public Expression textColor() {
        return null;
    }

    public Color getTextHaloColor() {
        return null;
    }

    public Expression textHaloColor() {
        return null;
    }

    public Number getTextHaloWidth() {
        return null;
    }

    public Expression textHaloWidth() {
        return null;
    }

    public Number getTextHaloBlur() {
        return null;
    }

    public Expression textHaloBlur() {
        return null;
    }

    public List<Number> getTextTranslate() {
        return null;
    }

    public Expression textTranslate() {
        return null;
    }

    public Expression textTranslateAnchor() {
        return null;
    }

    public TranslateAnchor getTextTranslateAnchor() {
        return null;
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
