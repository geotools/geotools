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
 * Indicate that one of a few predefined shapes will be drawn at the points of the geometry.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Mark extends GraphicalSymbol {

    /**
     * This parameter gives the well-known name of the shape of the mark.<br>
     * Allowed names include at least "square", "circle", "triangle", "star", "cross" and "x" though
     * renderers may draw a different symbol instead if they don't have a shape for all of these.
     * <br>
     *
     * @return The well-known name of a shape. The default value is "square".
     */
    Expression getWellKnownName();

    /**
     * Mark defined by an external resource.
     *
     * @return ExternalMark or null if WellKNownName is being used
     */
    ExternalMark getExternalMark();

    /**
     * This parameter defines which fill style to use when rendering the Mark.
     *
     * @return the Fill definition to use when rendering the Mark.
     */
    Fill getFill();

    /**
     * This paramterer defines which stroke style should be used when rendering the Mark.
     *
     * @return The Stroke definition to use when rendering the Mark.
     */
    Stroke getStroke();

    void accept(StyleVisitor visitor);
}
