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
 * Indicates how the interior of polygons will be filled.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Fill {

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the
     * fill graphic to use.
     *
     * @return The graphic to use as a stipple fill. If null then no stipple fill should be used.
     */
    Graphic getGraphicFill();

    // *************************************************************
    // SVG PARAMETERS
    // *************************************************************

    /**
     * This parameter gives the solid color that will be used for a Fill.<br>
     * The color value is RGB-encoded using two hexidecimal digits per primary-color component, in
     * the order Red, Green, Blue, prefixed with the hash (#) sign. The hexidecimal digits beetween
     * A and F may be in either upper or lower case. For example, full red is encoded as "#ff0000"
     * (with no quotation marks). The default color is defined to be 50% gray ("#808080"). Note: in
     * CSS this parameter is just called Fill and not Color.
     *
     * @return The color of the Fill encoded as a hexidecimal RGB value.
     */
    Expression getColor();

    /**
     * This specifies the level of translucency to use when rendering the fill. <br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing
     * totally transparent and 1.0 representing totally opaque, with a linear scale of translucency
     * for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @return The opacity of the fill, where 0.0 is completely transparent and 1.0 is completely
     *     opaque.
     */
    Expression getOpacity();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(StyleVisitor visitor, Object extraData);

    void accept(StyleVisitor visitor);
}
