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

import java.util.List;
import org.geotools.api.filter.expression.Expression;

/**
 * The Font element identifies a font of a certain family, style, and size.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Font {

    // *************************************************************
    // SVG PARAMETERS
    // *************************************************************

    /**
     * The "font-family" SvgParameter element gives the family name of a font to use. Allowed values
     * are system-dependent. Any number of font-family SvgParameter elements may be given and they
     * are assumed to be in preferred order.
     *
     * @return live list of font family
     */
    List<Expression> getFamily();

    /**
     * The "font-style" SvgParameter element gives the style to use for a font. The allowed values
     * are "normal", "italic", and "oblique". If null, the default is "normal".
     *
     * @return Expression or Expression.NIL
     */
    Expression getStyle();

    /**
     * The "font-weight" SvgParameter element gives the amount of weight or boldness to use for a
     * font. Allowed values are "normal" and "bold". If null, the default is "normal".
     *
     * @return Expression or or Expression.NIL
     */
    Expression getWeight();

    /**
     * The "font-size" SvgParameter element gives the size to use for the font in pixels. The
     * default is defined to be 10 pixels, though various systems may have restrictions on what
     * sizes are available.
     *
     * @return Expression or null
     */
    Expression getSize();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(StyleVisitor visitor, Object extraData);
}
