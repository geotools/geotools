/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import org.geotools.api.filter.expression.Expression;

/**
 * Contains all the information needed to draw styled lines. Stroke objects are contained by {@link
 * LineSymbol}s and {@link PolygonSymbol}s. There are three basic types of strokes: solid-color,
 * {@code GraphicFill} (stipple), and repeated linear {@code GraphicStroke}. A repeated linear
 * graphic is plotted linearly and has its graphic symbol bent around the curves of the line string,
 * and a graphic fill has the pixels of the line rendered with a repeating area-fill pattern. If
 * neither a {@link #getGraphicFill GraphicFill} nor {@link #getGraphicStroke GraphicStroke} element
 * is given, then the line symbolizer will render a solid color.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Stroke {

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the
     * fill graphic to use.
     *
     * @return The graphic to use as a stipple fill. If null, then no Stipple fill should be used.
     */
    Graphic getGraphicFill();

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke type will be used and
     * specifies the graphic to use.
     *
     * <p>Proper stroking with a linear graphic requires two "hot-spot" points within the space of
     * the graphic to indicate where the rendering line starts and stops. In the case of raster
     * images with no special mark-up, this line will be assumed to be the middle pixel row of the
     * image, starting from the first pixel column and ending at the last pixel column.
     *
     * @return The graphic to use as a linear graphic. If null, then no graphic stroke should be
     *     used.
     */
    Graphic getGraphicStroke();

    // *************************************************************
    // SVG PARAMETERS
    // *************************************************************

    /**
     * Indicates the color of the line if it is to be solid-color filled. The format of color values
     * is {@code "#rrggbb"} where {@code rr}, {@code gg}, and {@code bb}, are red, green, and blue
     * intensity values, respectively, represented as two digit hexadecimal integers. The
     * hexadecimal digits between {@code A} and {@code F} may be in either uppercase or lowercase.
     * If null, the default color is {@code "#000000"}, black.
     *
     * @return Expression
     */
    Expression getColor();

    /**
     * Indicates the level of translucency as a floating point number whose value is between 0.0 and
     * 1.0 (inclusive). A value of zero means completely transparent. A value of 1.0 means
     * completely opaque. If null, the default value is 1.0, totally opaque.
     *
     * @return expression
     */
    Expression getOpacity();

    /**
     * Gives the absolute width in uoms of the line stroke as a floating point number. Fractional
     * numbers are allowed (with system-dependent interpretation), but negative numbers are not. If
     * null, the default value is 1.0.
     *
     * @return expression
     */
    Expression getWidth();

    /** This parameter controls how line strings should be joined together. */
    Expression getLineJoin();

    /** This parameter controls how line strings should be capped. */
    Expression getLineCap();

    /**
     * If present, indicates the dash pattern as a space-separated sequence of floating point
     * numbers. The first number represents the length of the first dash to draw. The second number
     * represents the length of space to leave. This continues to the end of the list then repeats.
     * If the list contains an odd number of values, then before rendering the list is enlarged by
     * repeating the last value. If this parameter is omitted, lines will be drawn as solid and
     * unbroken.
     *
     * @return expression
     */
    float[] getDashArray();

    /**
     * A dash array need not start from the beginning. This method allows for an offset into the
     * dash array before starting it.
     */
    Expression getDashOffset();

    void accept(org.geotools.api.style.StyleVisitor visitor);
}
