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
import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;

/**
 * An AnchorPoint identifies the location inside a textlabel to use as an
 * "anchor" for positioning it relative to a point geometry.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Ian Turton
 * @since GeoAPI 2.2
 */
@XmlElement("AnchorPoint")
public interface AnchorPoint {

    /**
     * get the x coordinate of the anchor point
     *
     * @return the expression which represents the X coordinate
     */
    @XmlElement("AnchorPointX")
    Expression getAnchorPointX();

    /**
     * get the y coordinate of the anchor point
     *
     * @return the expression which represents the Y coordinate
     */
    @XmlElement("AnchorPointY")
    Expression getAnchorPointY();
    
    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
