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
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface PolygonSymbolizer extends Symbolizer {

    /**
     * Provides the graphical-symbolization parameter to use for the outline of the Polygon.
     *
     * @return The Stroke style to use when rendering lines.
     */
    Stroke getStroke();

    /**
     * Provides the graphical-symbolization parameter to use to fill the area of the geometry. Note
     * that the area should be filled first before the outline is rendered.
     *
     * @return The Fill style to use when rendering the area.
     */
    Fill getFill();

    /**
     * Displacement from the original geometry in pixels.
     *
     * @return Displacement above and to the right of the indicated point; default x=0, y=0
     */
    Displacement getDisplacement();

    /**
     * PerpendicularOffset works as defined for LineSymbolizer, allowing to draw polygons smaller or
     * larger than their actual geometry. The distance is in uoms and is positive to the outside of
     * the polygon. Negative numbers mean drawing the polygon smaller. The default offset is 0.
     *
     * @return Expression
     */
    Expression getPerpendicularOffset();
}
