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
 * Indicates how text will be drawn.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface TextSymbolizer extends Symbolizer {

    /**
     * Returns the expression that will be evaluated to determine what text is displayed. If a Label
     * element is not provided in a TextSymbolizer, then no text shall be rendered.
     *
     * @return Expression
     */
    Expression getLabel();

    /**
     * Returns the Font to apply on the text.
     *
     * @return Font
     */
    Font getFont();

    /**
     * Returns the object that indicates how the text should be placed with respect to the feature
     * geometry. This object will either be an instance of {@link LinePlacement} or {@link
     * PointPlacement}.
     *
     * @return {@link LinePlacement} or {@link PointPlacement}.
     */
    LabelPlacement getLabelPlacement();

    /**
     * Returns the object that indicates if a Halo will be drawn around the text. If null, a halo
     * will not be drawn.
     *
     * @return Halo
     */
    Halo getHalo();

    /**
     * Returns the object that indicates how the text will be filled.
     *
     * @return Fill
     */
    Fill getFill();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    Object accept(StyleVisitor visitor, Object extraData);
}
