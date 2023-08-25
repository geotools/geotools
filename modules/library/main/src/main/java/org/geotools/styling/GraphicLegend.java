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
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.GraphicalSymbol;

/**
 * Used to represent a Rule (or other construct) in a user interface or legend.
 *
 * @author Jody
 */
public interface GraphicLegend extends org.geotools.api.style.GraphicLegend {

    public void setAnchorPoint(org.geotools.api.style.AnchorPoint anchor);

    public void setDisplacement(org.geotools.api.style.Displacement displacement);

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
     * @param rotation in decimal degrees
     */
    void setRotation(Expression rotation);

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
    @Override
    public List<GraphicalSymbol> graphicalSymbols();
}
