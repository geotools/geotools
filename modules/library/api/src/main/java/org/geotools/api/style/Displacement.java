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
 * The Displacement gives the X and Y displacements from the original geometry. This element may be
 * used to avoid over-plotting of multiple PolygonSymbolizers for one geometry or supplying
 * "shadows" of polygon gemeotries. The displacements are in units of pixels above and to the right
 * of the point. The default displacement is X=0, Y=0.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Ian Turton, CCG
 * @since GeoAPI 2.2
 */
public interface Displacement {

    /**
     * Returns an expression that computes a pixel offset from the geometry point. This offset point
     * is where the text's anchor point gets located. If this expression is null, the default offset
     * of zero is used.
     *
     * @return Horizontal offeset
     */
    Expression getDisplacementX();

    /**
     * Returns an expression that computes a pixel offset from the geometry point. This offset point
     * is where the text's anchor point gets located. If this expression is null, the default offset
     * of zero is used.
     *
     * @return Expression
     */
    Expression getDisplacementY();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(StyleVisitor visitor, Object extraData);

    void accept(StyleVisitor visitor);
}
