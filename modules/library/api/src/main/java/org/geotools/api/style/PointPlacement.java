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
     * Returns the AnchorPoint which identifies the location inside a text label to use as an
     * "anchor" for positioning it relative to a point geometry.
     *
     * @return anchorPoint from the relative to the original geometry
     */
    AnchorPoint getAnchorPoint();

    /**
     * Returns the Displacement which gives X and Y offset displacements to use for rendering a text
     * label near a point.
     *
     * @return Offset to use when rendering text near a point
     */
    Displacement getDisplacement();

    /**
     * Returns the rotation of the label.
     *
     * @return rotation of the label as a dynamic expression
     */
    Expression getRotation();
}
