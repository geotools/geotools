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
public interface Mark extends GraphicalSymbol, Symbol {

    Mark[] MARKS_EMPTY = new Mark[0];

    /**
     * Returns the expression whose value will indicate the symbol to draw. The WellKnownName
     * element gives the well-known name of the shape of the mark. Allowed values include at least
     * “square”, “circle”, “triangle”, “star”, “cross”, and “x”, though map servers may draw a
     * different symbol instead if they don't have a shape for all of these. The default
     * WellKnownName is “square”. Renderings of these marks may be made solid or hollow depending on
     * Fill and Stroke elements.
     *
     * <p>if the WellKnowname is null, check the ExternalMark before using the default square
     * symbol.
     *
     * <p>Both WellKnowName and ExternalMark canot be set, but both can be null. If none are set
     * then the default square symbol is used.
     *
     * @return Expression or null
     */
    Expression getWellKnownName();

    /**
     * This parameter defines which fill style to use when rendering the Mark.
     *
     * @param fill the Fill definition to use when rendering the Mark.
     */
    void setFill(Fill fill);

    /**
     * The alternative to a WellKnownName is an external mark format. See {@link ExternalMark} for
     * details.
     *
     * <p>Both WellKnowName and ExternalMark cannot be set, but both can be null. If none are set
     * then the default square symbol is used.
     *
     * @return ExternalMark or null
     */
    ExternalMark getExternalMark();

    /**
     * This paramterer defines which stroke style should be used when rendering the Mark.
     *
     * @param stroke The Stroke definition to use when rendering the Mark.
     */
    void setStroke(Stroke stroke);

    /**
     * Returns the object that indicates how the mark should be filled. Null means no fill.
     *
     * @return Fill or null
     */
    Fill getFill();

    /**
     * This parameter gives the well-known name of the shape of the mark.<br>
     * Allowed names include at least "square", "circle", "triangle", "star", "cross" and "x" though
     * renderers may draw a different symbol instead if they don't have a shape for all of these.
     * <br>
     *
     * @param wellKnownName The well-known name of a shape. The default value is "square".
     */
    void setWellKnownName(Expression wellKnownName);

    /**
     * Returns the object that indicates how the edges of the mark will be drawn. Null means that
     * the edges will not be drawn at all.
     *
     * @return stroke or null
     */
    Stroke getStroke();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    void accept(StyleVisitor visitor);

    public Object accept(TraversingStyleVisitor visitor, Object data);

    /**
     * Mark defined by an external resource.
     *
     * @param externalMark Indicate an mark defined by an external resource
     */
    void setExternalMark(ExternalMark externalMark);
}
