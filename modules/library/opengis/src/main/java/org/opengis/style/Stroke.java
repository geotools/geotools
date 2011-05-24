/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import org.opengis.annotation.Extension;
import org.opengis.filter.expression.Expression;
import org.opengis.annotation.XmlElement;
import org.opengis.annotation.XmlParameter;


/**
 * Contains all the information needed to draw styled lines.  Stroke objects are contained
 * by {@link LineSymbol}s and {@link PolygonSymbol}s. There are three basic types of strokes:
 * solid-color, {@code GraphicFill} (stipple), and repeated linear {@code GraphicStroke}. A
 * repeated linear graphic is plotted linearly and has its graphic symbol bent around the curves
 * of the line string, and a graphic fill has the pixels of the line rendered with a repeating
 * area-fill pattern. If neither a {@link #getGraphicFill GraphicFill} nor {@link #getGraphicStroke
 * GraphicStroke} element is given, then the line symbolizer will render a solid color.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/Stroke.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("Stroke")
public interface Stroke {

    /**
     * If non-null, indicates that line should be drawn by tiling the (thin) area of the line with
     * the given graphic. Between {@code getGraphicFill()} and {@link #getGraphicStroke()}, only one
     * may return a non-null value since a {@code Stroke} can have a {@code GraphicFill} or a
     * {@code GraphicStroke}, but not both.
     * @return Graphic
     */
    @XmlElement("GraphicFill")
    GraphicFill getGraphicFill();

    /**
     * If non-null, indicates that lines should be drawn by repeatedly plotting the given graphic
     * along the path of the lines, rotating it according to the orientation of the line.
     * Between {@link #getGraphicFill()} and {@code getGraphicStroke}, only one may return a
     * non-null value since a {@code Stroke} can have a {@code GraphicFill} or a {@code GraphicStroke},
     * but not both.
     * @return Graphic
     */
    @XmlElement("GraphicStroke")
    GraphicStroke getGraphicStroke();

    //*************************************************************
    // SVG PARAMETERS
    //*************************************************************

    /**
     * Indicates the color of the line if it is to be solid-color filled.  The format of color
     * values is {@code "#rrggbb"} where {@code rr}, {@code gg}, and {@code bb}, are red, green,
     * and blue intensity values, respectively, represented as two digit hexadecimal integers.
     * The hexadecimal digits between {@code A} and {@code F} may be in either uppercase or lowercase.
     * If null, the default color is {@code "#000000"}, black.
     * @return Expression
     */
    @XmlParameter("stroke")
    Expression getColor();

    /**
     * Indicates the level of translucency as a floating point number whose value is between 0.0
     * and 1.0 (inclusive).  A value of zero means completely transparent.  A value of 1.0 means
     * completely opaque.  If null, the default value is 1.0, totally opaque.
     * @return expression
     */
    @XmlParameter("stroke-opacity")
    Expression getOpacity();

    /**
     * Gives the absolute width in uoms of the line stroke as a floating point number.
     * Fractional numbers are allowed (with system-dependent interpretation), but negative
     * numbers are not.  If null, the default value is 1.0.
     * @return expression
     */
    @XmlParameter("stroke-width")
    Expression getWidth();

    /**
     * Indicates how the various segments of a (thick) line string should be joined.
     * Valid values are "miter", "round", and "bevel".  If null, the default value is
     * system dependent (probably whichever one is fastest to render).
     * @return expression
     */
    @XmlParameter("stroke-linejoin")
    Expression getLineJoin();

    /**
     * Indicates how the beginning and ending segments of a line string will be terminated.
     * Valid values are "butt", "round", and "square".  If null, the default value is system
     * dependent.
     * @return expression
     */
    @XmlParameter("stroke-linecap")
    Expression getLineCap();

    /**
     * If present, indicates the dash pattern as a space-separated sequence of floating point numbers.
     * The first number represents the length of the first dash to draw.  The second number
     * represents the length of space to leave.  This continues to the end of the list then
     * repeats.  If the list contains an odd number of values, then before
     * rendering the list is enlarged by repeating the last value.  If this
     * parameter is omitted, lines will be drawn as solid and unbroken.
     * @return expression
     */
    @XmlParameter("stroke-dasharray")
    float[] getDashArray();

    /**
     * Indicates the distance offset into the dash array to begin drawing.
     * If null, the default value is zero.
     * @return expression
     */
    @XmlParameter("stroke-dashoffset")
    Expression getDashOffset();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
