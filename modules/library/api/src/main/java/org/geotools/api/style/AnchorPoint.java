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
 * An AnchorPoint identifies the location inside a textlabel to use as an "anchor" for positioning
 * it relative to a point geometry.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Ian Turton
 * @since GeoAPI 2.2
 */
public interface AnchorPoint {

    /**
     * get the x coordinate of the anchor point
     *
     * @return the expression which represents the X coordinate
     */
    Expression getAnchorPointX();

    /**
     * get the y coordinate of the anchor point
     *
     * @return the expression which represents the Y coordinate
     */
    Expression getAnchorPointY();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    void accept(StyleVisitor visitor);
}
