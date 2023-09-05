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
 * Gives directions for how to draw lines on a map.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface LineSymbolizer extends Symbolizer {

    /**
     * Returns the object containing all the information necessary to draw styled lines.
     *
     * @return Stroke object, contain information about how to draw lines
     */
    Stroke getStroke();

    /**
     * PerpendicularOffset allows to draw lines in parallel to the original geometry. For complex
     * line strings these parallel lines have to be constructed so that the distance between
     * original geometry and drawn line stays equal. This construction can result in drawn lines
     * that are actually smaller or longer than the original geometry.
     *
     * <p>The distance is in uoms and is positive to the left-hand side of the line string. Negative
     * numbers mean right. The default offset is 0.
     *
     * @return Expression
     */
    Expression getPerpendicularOffset();

    /**
     * Calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * Provides the graphical-symbolization parameter to use for the linear geometry.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    void setStroke(Stroke stroke);

    /**
     * Define an offset to draw lines in parallel to the original geometry.
     *
     * @param offset Distance in UOMs to offset line; left-hand side is positive; right-hand side is
     *     negative; the default value is 0
     */
    void setPerpendicularOffset(Expression offset);
}
