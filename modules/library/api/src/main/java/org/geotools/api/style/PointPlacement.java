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
 * A PointPlacement specifies how a text label is positioned relative to a geometric point.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Ian Turton
 * @since GeoAPI 2.2
 */
public interface PointPlacement extends LabelPlacement {

    /**
     * Returns the AnchorPoint which identifies the location inside a textlabel to use as an
     * "anchor" for positioning it relative to a point geometry.
     *
     * @return anchorPoint from the relative to the original geometry
     */
    AnchorPoint getAnchorPoint();

    /**
     * sets the AnchorPoint which identifies the location inside a textlabel to use as an "anchor"
     * for positioning it relative to a point geometry.
     *
     * @param anchorPoint relative to the original geometry
     */
    void setAnchorPoint(AnchorPoint anchorPoint);

    /**
     * Returns the Displacement which gives X and Y offset displacements to use for rendering a text
     * label near a point.
     *
     * @return Offset to use when rendering text near a point
     */
    Displacement getDisplacement();

    /**
     * sets the Displacement which gives X and Y offset displacements to use for rendering a text
     * label near a point.
     */
    void setDisplacement(Displacement displacement);

    /**
     * Returns the rotation of the label.
     *
     * @return rotation of the label as a dynamic expression
     */
    Expression getRotation();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * sets the rotation of the label.
     *
     * <p>Sets the rotation of the label.
     */
    void setRotation(Expression rotation);
}
