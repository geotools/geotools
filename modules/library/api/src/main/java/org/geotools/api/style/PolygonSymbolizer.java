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
 * Holds the information that indicates how to draw the lines and the interior of polygons.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification
 *     1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface PolygonSymbolizer extends Symbolizer {

    /** Pixels between each graphic of a polygon fill */
    String GRAPHIC_MARGIN_KEY = "graphic-margin";

    /**
     * Provides the graphical-symbolization parameter to use to fill the area of the geometry. Note that the area should
     * be filled first before the outline is rendered.
     *
     * @param fill The Fill style to use when rendering the area.
     */
    void setFill(Fill fill);

    /**
     * Returns the object containing all the information necessary to draw styled lines. This is used for the edges of
     * polygons.
     *
     * @return Stroke
     */
    Stroke getStroke();

    /**
     * Returns the object that holds the information about how the interior of polygons should be filled. This may be
     * null if the polygons are not to be filled at all.
     *
     * @return Fill
     */
    Fill getFill();

    /**
     * Provides the graphical-symbolization parameter to use for the outline of the Polygon.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    void setStroke(Stroke stroke);

    /**
     * PerpendicularOffset works as defined for LineSymbolizer, allowing to draw polygons smaller or larger than their
     * actual geometry.
     *
     * @param offset Offset from the edge polygon positive outside; negative to the inside with a default of 0.
     */
    void setPerpendicularOffset(Expression offset);

    /**
     * The Displacement gives the X and Y displacements from the original geometry. This element may be used to avoid
     * over-plotting of multiple PolygonSymbolizers for one geometry or supplying "shadows" of polygon gemeotries. The
     * displacements are in units of pixels above and to the right of the point. The default displacement is X=0, Y=0.
     *
     * @return Displacement
     */
    Displacement getDisplacement();

    /**
     * PerpendicularOffset works as defined for LineSymbolizer, allowing to draw polygons smaller or larger than their
     * actual geometry. The distance is in uoms and is positive to the outside of the polygon. Negative numbers mean
     * drawing the polygon smaller. The default offset is 0.
     *
     * @return Expression
     */
    Expression getPerpendicularOffset();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /** Provide x / y offset in pixels used to crate shadows. */
    void setDisplacement(Displacement displacement);
}
