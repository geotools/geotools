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

import org.geotools.api.filter.expression.Function;

/**
 * The ColorReplacement element, which may occur multiple times, allows to replace a color in the
 * ExternalGraphic, the color specified in the OriginalColor sub-element, by another color as a
 * result of a recode function as defined in {@link Interpolate} .
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface ColorReplacement {

    /**
     * Recoding: Transformation of discrete values to any other values. This is needed when integers
     * have to be translated into text or, reversely, text contents into other texts or numeric
     * values or colors.
     *
     * <p>This function recodes values from a property or expression into corresponding values of
     * arbitrary type. The comparisons are performed checking for identical values.
     */
    Function getRecoding();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /** @param function Recoding function to use */
    void setRecoding(Function function);
}
