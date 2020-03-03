/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

/**
 * Used to represent a Rule (or other construct) in a user interface or legend.
 *
 * @author Jody
 */
public interface GraphicLegend extends org.opengis.style.GraphicLegend {

    public AnchorPoint getAnchorPoint();

    public void setAnchorPoint(org.opengis.style.AnchorPoint anchor);

    public Displacement getDisplacement();

    public void setDisplacement(org.opengis.style.Displacement displacement);

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
    public Expression getOpacity();

    /**
     * Graphic opacity.
     *
     * @param opacity New value of property opacity.
     */
    public void setOpacity(Expression opacity);

    /**
     * This parameter defines the rotation of a graphic in the clockwise direction about its centre
     * point in decimal degrees. The value encoded as a floating point number.
     *
     * @return The angle of rotation in decimal degrees. Negative values represent counter-clockwise
     *     rotation. The default is 0.0 (no rotation).
     */
    public Expression getRotation();

    /**
     * This parameter defines the rotation of a graphic in the clockwise direction about its centre
     * point in decimal degrees. The value encoded as a floating point number.
     *
     * @param rotation in decimal degrees
     */
    void setRotation(Expression rotation);

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
    public Expression getSize();

    /**
     * Indicates the size at which the graphic should be displayed.
     *
     * <p>If this value is null the natural size of the graphic will be used; or for graphics
     * without a natural size like SVG files 16x16 will be used.
     */
    public void setSize(Expression size);
    /**
     * The items in this list are either a Mark or a ExternalGraphic.
     *
     * <p>This list may be directly edited; the items are considered in order from most preferred
     * (say an SVG file) to least preferred (a simple shape) with the intension that the system will
     * make use of the first entry which it is capabile of displaying.
     */
    public List<GraphicalSymbol> graphicalSymbols();
}
