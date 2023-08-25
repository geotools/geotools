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
 * The "LinePlacement" specifies where and how a text label should be rendered relative to a line.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Ian Turton, CCG
 * @since GeoAPI 2.2
 */
public interface LinePlacement extends LabelPlacement {

    /**
     * Returns the expression that is used to compute how far from the lines the text will be drawn.
     * The distance must evaluate to a non-negative number.
     *
     * @return compute how far from the line the text will be drawn
     */
    Expression getPerpendicularOffset();

    /**
     * InitialGap specifies how far away the first graphic will be drawn relative to the start of
     * the rendering line
     *
     * @return Expression
     */
    Expression getInitialGap();

    /**
     * Gap gives the distance between two graphics.
     *
     * @return Expression
     */
    Expression getGap();

    /**
     * If IsRepeated is "true", the label will be repeatedly drawn along the line with InitialGap
     * and Gap defining the spaces at the beginning and between labels.
     *
     * @return boolean
     */
    boolean isRepeated();

    /** Correct method name violation from GeoAPI. */
    boolean isAligned();

    /**
     * GeneralizeLine allows the actual geometry, be it a linestring or polygon to be generalized
     * for label placement. This is e.g. useful for labelling polygons inside their interior when
     * there is need for the label to resemble the shape of the polygon.
     *
     * @return boolean
     */
    boolean isGeneralizeLine();
}
