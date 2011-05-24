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

import java.util.List;
import org.opengis.annotation.Extension;
import org.opengis.filter.expression.Expression;
import org.opengis.annotation.XmlElement;
import org.opengis.annotation.XmlParameter;


/**
 * A Graphic is a "graphic symbol" with an inherent shape, color(s), and possibly size. A
 * "graphic" can be very informally defined as "a little picture" and can be of either a raster
 * or vector-graphic source type. The term "graphic" is used since the term "symbol" is
 * similar to "Symbolizer" which is used in a different context in SE.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/Graphic.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("Graphic")
public interface Graphic {

    /**
     * Returns the list of external image files or marks that comprise this graphic.
     * All elements of the list must be instances of either {@link Mark} or {@link ExternalGraphic}.
     * <p>
     * @return List of Marks or ExternalGraphics; if empty it is to be treated a single default Mark.
     */
    @XmlElement("ExternalGraphic,Mark")
    List<GraphicalSymbol> graphicalSymbols();

    //*************************************************************
    // SVG PARAMETERS
    //*************************************************************

    /**
     * Indicates the level of translucency as a floating point number whose value is between 0.0
     * and 1.0 (inclusive).  A value of zero means completely transparent.  A value of 1.0 means
     * completely opaque.  If null, the default value is 1.0, totally opaque.
     * @return expression
     */
    @XmlParameter("stroke-opacity")
    Expression getOpacity();

    /**
     * The Size element gives the absolute size of the graphic in uoms encoded as a floating-
     * point number. The default size for an object is context-dependent. Negative values are
     * not allowed.
     * The default size of an image format (such as GIF) is the inherent size of the image. The
     * default size of a format without an inherent size (such as SVG which are not specially
     * marked) is defined to be 16 pixels in height and the corresponding aspect in width. If a
     * size is specified, the height of the graphic will be scaled to that size and the
     * corresponding aspect will be used for the width. An expected common use case will be
     * for image graphics to be on the order of 200 pixels in linear size and to be scaled to lower
     * sizes. On systems that can resample these graphic images "smoothly," the results will be
     * visually pleasing.
     *
     * @return Expression
     */
    @XmlParameter("Size")
    Expression getSize();

    /**
     * Returns the expression that will be used to calculate the rotation of the
     * graphic when it is drawn.
     *
     * The Rotation element gives the rotation of a graphic in the clockwise direction about its
     * center point in decimal degrees, encoded as a floating-point number. Negative values
     * mean counter-clockwise rotation. The default value is 0.0 (no rotation). Note that there is
     * no connection between source geometry types and rotations; the point used for plotting
     * has no inherent direction. Also, the point within the graphic about which it is rotated is
     * format dependent. If a format does not include an inherent rotation point, then the point
     * of rotation should be the centroid.
     *
     * @return Expression
     */
    @XmlParameter("Rotation")
    Expression getRotation();

    /**
     * The AnchorPoint element of a PointSymbolizer gives the location inside of a Graphic
     * (or label - see 11.4.4) to use for anchoring the graphic to the main-geometry point. The
     * coordinates are given as two floating-point numbers in the AnchorPointX and
     * AnchorPointY elements each with values between 0.0 and 1.0 inclusive. The bounding
     * box of the graphic/label to be rendered is considered to be in a coordinate space from 0.0
     * (lower-left corner) to 1.0 (upper-right corner), and the anchor position is specified as a
     * point in this space. The default point is X=0.5, Y=0.5, which is at the middle height and
     * middle length of the graphic/label text. A system may choose different anchor points to
     * de-conflict graphics/labels.
     *
     * @return AnchorPoint , if null should use a default point X=0.5 Y=0.5
     */
    @XmlParameter("AnchorPoint")
    AnchorPoint getAnchorPoint();

    /**
     * The Displacement gives the X and Y displacements from the "hot-spot" point. This
     * element may be used to avoid over-plotting of multiple graphic symbols used as part of
     * the same point symbol. The displacements are in units of measure above and to the right
     * of the point. The default displacement is X=0, Y=0.
     *
     * If Displacement is used in conjunction with Size and/or Rotation then the graphic
     * symbol shall be scaled and/or rotated before it is displaced.s
     *
     * @return Displacement
     */
    @XmlParameter("Displacement")
    Displacement getDisplacement();

    /**
     * Calls the visit method of a StyleVisitor
     * <p>
     * Please note StlyeVisitor has methods to directly visit a Graphic, GraphicLegend, or GraphicFill or GraphicStroke; please
     * call the most appropriate method.
     *  
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
