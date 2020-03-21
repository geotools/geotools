/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.List;
import java.util.Map;
import org.opengis.filter.expression.Expression;

/**
 * A symbolizer describes how a feature should appear on a map.
 *
 * <p>A symbolizer is obtained by specifying one of a small number of different types of symbolizer
 * and then supplying parameters to override its default behaviour.
 *
 * <p>The text symbolizer describes how to display text labels and the like.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xsd:element name="TextSymbolizer" substitutionGroup="sld:Symbolizer">
 *   &lt;xsd:annotation>
 *     &lt;xsd:documentation>
 *       A "TextSymbolizer" is used to render text labels according to
 *       various graphical parameters.
 *     &lt;/xsd:documentation>
 *   &lt;/xsd:annotation>
 *   &lt;xsd:complexType>
 *     &lt;xsd:complexContent>
 *       &lt;xsd:extension base="sld:SymbolizerType">
 *         &lt;xsd:sequence>
 *           &lt;xsd:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Label" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Font" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:LabelPlacement" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Halo" minOccurs="0"/>
 *           &lt;xsd:element ref="sld:Fill" minOccurs="0"/>
 *         &lt;/xsd:sequence>
 *       &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </code></pre>
 *
 * <p>Renderers can use this information when displaying styled features, though it must be
 * remembered that not all renderers will be able to fully represent strokes as set out by this
 * interface. For example, opacity may not be supported.
 *
 * <p>Notes:
 *
 * <ul>
 *   <li>The graphical parameters and their values are derived from SVG/CSS2 standards with names
 *       and semantics which are as close as possible.
 * </ul>
 *
 * $Id$
 *
 * @author Ian Turton, CCG
 */
public interface TextSymbolizer extends org.opengis.style.TextSymbolizer, Symbolizer {

    /**
     * If true, geometries with the same labels are grouped and considered a single entity to be
     * labeled. This allows to avoid or control repeated labels
     */
    public static String GROUP_KEY = "group";

    /** Default grouping value, false */
    public boolean DEFAULT_GROUP = false;

    /** The minimum distance between two labels, in pixels */
    public static String SPACE_AROUND_KEY = "spaceAround";

    /** By default, don't add space around labels */
    public static int DEFAULT_SPACE_AROUND = 0;

    /**
     * The distance, in pixel, a label can be displaced from its natural position in an attempt to
     * find a position that does not conflict with already drawn labels.
     */
    public static String MAX_DISPLACEMENT_KEY = "maxDisplacement";

    /** Default max displacement */
    public static int DEFAULT_MAX_DISPLACEMENT = 0;

    /**
     * Minimum distance between two labels in the same label group. To be used when both
     * displacement and repeat are used to avoid having two labels too close to each other
     */
    public static String MIN_GROUP_DISTANCE_KEY = "minGroupDistance";

    /** Default min distance between labels in the same group (-1 means no min distance) */
    public static int DEFAULT_MIN_GROUP_DISTANCE = -1;

    /**
     * When positive it's the desired distance between two subsequent labels on a "big" geometry.
     * Works only on lines at the moment. If zero only one label is drawn no matter how big the
     * geometry is
     */
    public static String LABEL_REPEAT_KEY = "repeat";

    /** Default repetition distance for labels (<= 0 -> no repetition) */
    public static int DEFAULT_LABEL_REPEAT = 0;

    /**
     * When false, only the biggest geometry in a group is labelled (the biggest is obtained by
     * merging, when possible, the original geometries). When true, also the smaller items in the
     * group are labeled. Works only on lines at the moment.
     */
    public static String LABEL_ALL_GROUP_KEY = "labelAllGroup";

    /** If in case of grouping all resulting lines have to be labelled */
    public static boolean DEFAULT_LABEL_ALL_GROUP = false;

    /**
     * When false does not allow labels on lines to get beyond the beginning/end of the line. By
     * default a partial overrun is tolerated, set to false to disallow it.
     */
    public static String ALLOW_OVERRUNS_KEY = "allowOverruns";

    /** We allow labels that are longer than the line to appear by default */
    public static boolean DEFAULT_ALLOW_OVERRUNS = true;

    /**
     * If, in case of grouping, self overlaps have to be taken into account and removed (expensive!)
     */
    public static String REMOVE_OVERLAPS_KEY = "removeOverlaps";

    /** Default value for REMOVE_OVERLAPS_KEY */
    public static boolean DEFAULT_REMOVE_OVERLAPS = false;

    /**
     * When true activates curved labels on linear geometries. The label will follow the shape of
     * the current line, as opposed to being drawn a tangent straight line
     */
    public static String FOLLOW_LINE_KEY = "followLine";

    /** If labels with a line placement should follow the line shape or be just tangent */
    public static boolean DEFAULT_FOLLOW_LINE = false;

    /**
     * When TRUE labels text will be underline, the underline will have the same color of the text.
     */
    String UNDERLINE_TEXT_KEY = "underlineText";

    /** If labels text should be underline or not. */
    boolean DEFAULT_UNDERLINE_TEXT = false;

    /**
     * When TRUE labels text will be stroked through, the line will have the same color of the text.
     */
    String STRIKETHROUGH_TEXT_KEY = "strikethroughText";

    /** If labels text should be stroke through or not. */
    boolean DEFAULT_STRIKETHROUGH_TEXT = false;

    /**
     * When drawing curved labels, max allowed angle between two subsequent characters. Higher
     * angles may cause disconnected words or overlapping characters
     */
    public static String MAX_ANGLE_DELTA_KEY = "maxAngleDelta";

    /**
     * When label follows line, the max angle change between two subsequent characters, keeping it
     * low avoids chars overlaps. When the angle is exceeded the label placement will fail.
     */
    public static double DEFAULT_MAX_ANGLE_DELTA = 22.5;

    /**
     * Number of pixels are which a long label should be split into multiple lines. Works on all
     * geometries, on lines it is mutually exclusive with the followLine option
     */
    public static String AUTO_WRAP_KEY = "autoWrap";

    /** Auto wrapping long labels default */
    public static final int DEFAULT_AUTO_WRAP = 0;

    /**
     * When true forces labels to a readable orientation, when false they make follow the line
     * orientation even if that means the label will look upside down (useful when using TTF symbol
     * fonts to add direction markers along a line)
     */
    public static String FORCE_LEFT_TO_RIGHT_KEY = "forceLeftToRight";

    /** Force labels to a readable orientation (so that they don't look "upside down") */
    public static final boolean DEFAULT_FORCE_LEFT_TO_RIGHT = true;

    /**
     * Enables conflict resolution (default, true) meaning no two labels will be allowed to overlap.
     * Symbolizers with conflict resolution off are considered outside of the conflict resolution
     * game, they don't reserve area and can overlap with other labels.
     */
    public static String CONFLICT_RESOLUTION_KEY = "conflictResolution";

    /** By default, put each label in the conflict resolution map */
    public static final boolean DEFAULT_CONFLICT_RESOLUTION = true;

    /**
     * Sets the percentage of the label that must sit inside the geometry to allow drawing the
     * label. Works only on polygons.
     */
    public static String GOODNESS_OF_FIT_KEY = "goodnessOfFit";

    /** Default value for the goodness of fit threshold */
    public static final double DEFAULT_GOODNESS_OF_FIT = 0.5;

    /** Option overriding manual rotation to align label rotation automatically for polygons. */
    public static String POLYGONALIGN_KEY = "polygonAlign";

    /**
     * Pixels between the stretched graphic and the text, applies when graphic stretching is in use
     */
    public static String GRAPHIC_MARGIN_KEY = "graphic-margin";

    /**
     * Stretches the graphic below a label to fit the label size. Possible values are 'stretch',
     * 'proportional'.
     */
    public static String GRAPHIC_RESIZE_KEY = "graphic-resize";

    /** Enumerated options that can be used with polygonAlign. */
    public static enum PolygonAlignOptions {
        /** Disable automatic rotation alignment (default) */
        NONE,
        /** Rotate label orthogonally with x-axis if the polygon is higher than wider */
        ORTHO,
        /**
         * Rotate label using the minimum bounding rectangle of the polygon, if a default horizontal
         * label will not fit into the polygon.
         */
        MBR
    };

    /** Default value for the polygonAlign */
    public static final PolygonAlignOptions DEFAULT_POLYGONALIGN = PolygonAlignOptions.NONE;

    /**
     * Option to truncate labels placed on the border of the displayArea (display partial labels).
     */
    String PARTIALS_KEY = "partials";

    /**
     * Default behaviour is to not truncate labels on the border of the display area. Instead,
     * labels are hidden or moved to be fully displayed inside the area.
     */
    boolean DEFAULT_PARTIALS = false;

    /** Option to enable automatic adjustment of the space between characters */
    public static final String KERNING_KEY = "kerning";

    /** Default behaviour is to perform kerning */
    public static final boolean DEFAULT_KERNING = true;

    /**
     * Option expand/shrink character spacing, expressed as a value in pixels. A negative value will
     * shrink spacing, making character eventually overlap.
     */
    public static final String CHAR_SPACING_KEY = "charSpacing";

    /** Default extra char spacing (no extra) */
    public static final double DEFAULT_CHAR_SPACING = 0;

    /**
     * Option expand word spacing, expressed as a value in pixels. This option does not support
     * negative values
     */
    public static final String WORD_SPACING_KEY = "wordSpacing";

    /** Default extra word spacing (no extra) */
    public static final double DEFAULT_WORD_SPACING = 0;

    /**
     * Option expressed in value of pixels. This option does not support negative values. Works only
     * on polygons.
     */
    public static final String FONT_SHRINK_SIZE_MIN = "fontShrinkSizeMin";

    /** Default font shrink size */
    public static final int DEFAULT_FONT_SHRINK_SIZE_MIN = 0;

    /**
     * Option to control displacement of labels. Available values are defined as enum values @see
     * {@link DisplacementMode}
     */
    public static final String DISPLACEMENT_MODE_KEY = "displacementMode";

    /** DisplacementMode associates an angle with each enum value */
    public static enum DisplacementMode {
        N(90),
        W(180),
        E(0),
        S(270),
        NW(135),
        NE(45),
        SW(225),
        SE(315);

        int angle;

        DisplacementMode(int angle) {
            this.angle = angle;
        }

        public int getAngle() {
            return angle;
        }
    }

    /** Placement of the graphic element, see GraphicPlacement enumeration for possible value */
    public static final String GRAPHIC_PLACEMENT_KEY = "graphicPlacement";

    /** GraphicPlacemnts controls the position of the Graphic associated with the label */
    public static enum GraphicPlacement {
        /**
         * Centered with the label, thus moving with it considering its offsets and anchors relative
         * to the label point. If not specified, this is the default behavior.
         */
        LABEL,
        /**
         * Places the graphic independent of the label own offset and anchor, but applying the
         * Graphic offset and anchor instead, on top of the chosen label point. This allows to
         * create graphics that are at an offset compared to the label
         */
        INDEPENDENT
    }

    /**
     * Returns the expression that will be evaluated to determine what text is displayed.
     *
     * @return Expression that will be evaulated as a String to display on screen
     */
    Expression getLabel();

    /**
     * Sets the expression that will be evaluated to determine what text is displayed. See {@link
     * #getLabel} for details.
     */
    void setLabel(Expression label);

    /**
     * Direct access to device independent Fonts used to render the label. Fonts are used in order
     * of priority given.
     *
     * @return device independent Font used to render the label.
     */
    List<Font> fonts();

    /**
     * Initial Font to use when rendering this symbolizer. For alternatives see {@link #fonts()}.
     *
     * @return Initial Font used to render label, or null if unavailable.
     */
    Font getFont();

    /**
     * Set initial font used to render label. This will replace the initial entry in the {@link
     * #fonts()} list.
     */
    public void setFont(org.opengis.style.Font font);

    /**
     * A LabelPlacement specifies how a text element should be rendered relative to its geometric
     * point or line.
     */
    LabelPlacement getLabelPlacement();

    /**
     * A LabelPlacement specifies how a text element should be rendered relative to its geometric
     * point or line.
     */
    void setLabelPlacement(org.opengis.style.LabelPlacement labelPlacement);

    /**
     * A halo fills an extended area outside the glyphs of a rendered text label to make the label
     * easier to read over a background.
     */
    Halo getHalo();

    /**
     * A halo fills an extended area outside the glyphs of a rendered text label to make the label
     * easier to read over a background.
     */
    void setHalo(org.opengis.style.Halo halo);

    /** Returns the object that indicates how the text will be filled. */
    Fill getFill();

    /**
     * Sets the object that indicates how the text will be filled. See {@link #getFill} for details.
     */
    void setFill(org.opengis.style.Fill fill);

    /**
     * Priority -- null = use the default labeling priority Expression = an expression that
     * evaluates to a number (ie. Integer, Long, Double...) Larger = more likely to be rendered
     */
    void setPriority(Expression e);

    /**
     * Priority -- null = use the default labeling priority Expression = an expression that
     * evaluates to a number (ie. Integer, Long, Double...) Larger = more likely to be rendered
     */
    Expression getPriority();

    /**
     * return the map of option
     *
     * @return null - no options set
     */
    Map<String, String> getOptions();
}
