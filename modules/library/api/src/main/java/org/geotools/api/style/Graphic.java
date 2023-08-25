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

import java.util.List;
import org.geotools.api.filter.expression.Expression;

/**
 * A Graphic is a "graphic symbol" with an inherent shape, color(s), and possibly size. A "graphic"
 * can be very informally defined as "a little picture" and can be of either a raster or
 * vector-graphic source type. The term "graphic" is used since the term "symbol" is similar to
 * "Symbolizer" which is used in a different context in SE.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Graphic {

    /**
     * Returns the list of external image files or marks that comprise this graphic. All elements of
     * the list must be instances of either {@link Mark} or {@link ExternalGraphic}.
     *
     * <p>
     *
     * @return List of Marks or ExternalGraphics; if empty it is to be treated a single default
     *     Mark.
     */
    List<GraphicalSymbol> graphicalSymbols();

    // *************************************************************
    // SVG PARAMETERS
    // *************************************************************

    /**
     * This specifies the level of translucency to use when rendering the graphic.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing
     * totally transparent and 1.0 representing totally opaque, with a linear scale of translucency
     * for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @return The opacity of the Graphic, where 0.0 is completely transparent and 1.0 is completely
     *     opaque.
     */
    Expression getOpacity();

    /**
     * The size of the mark if specified.
     *
     * <p>If unspecified:
     *
     * <ul>
     *   <li>The natural size will be used for formats such as PNG that have an image width and
     *       height
     *   <li>16 x 16 will be used for formats like SVG that do not have a size
     * </ul>
     */
    Expression getSize();

    /**
     * This parameter defines the rotation of a graphic in the clockwise direction about its centre
     * point in decimal degrees. The value encoded as a floating point number.
     *
     * @return The angle of rotation in decimal degrees. Negative values represent counter-clockwise
     *     rotation. The default is 0.0 (no rotation).
     */
    Expression getRotation();

    /**
     * Location inside of the Graphic (or Label) to position the main-geometry point.
     *
     * <p>The coordinates are provided as 0.0 to 1.0 range amounting to a percentage of the graphic
     * width/height. So the default of 0.5/0.5 indicates that the graphic would be centered.
     *
     * <p>Please keep in mind that a system may shuffle things around a bit in order to prevent
     * graphics from overlapping (so this AnchorPoint is only a hint about how things should be if
     * there is enough room).
     *
     * @return AnchorPoint , if null should use a default point X=0.5 Y=0.5
     */
    AnchorPoint getAnchorPoint();

    Displacement getDisplacement();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(StyleVisitor visitor, Object extraData);

    Expression getGap();

    /**
     * accepts a StyleVisitor - used by xmlencoder and other packages which need to walk the style
     * tree
     *
     * @param visitor - the visitor object
     */
    void accept(StyleVisitor visitor);

    Expression getInitialGap();
}
