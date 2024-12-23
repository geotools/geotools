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

/**
 * Indicates how to draw point geometries on a map.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification
 *     1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface PointSymbolizer extends Symbolizer {

    /**
     * Boolean vendor option, defaults to true. If true, in case no specified mark or graphics can be used, the default
     * square mark will be used instead. If false, the symbol will not be painted.
     */
    String FALLBACK_ON_DEFAULT_MARK = "fallbackOnDefaultMark";

    /**
     * Provides the graphical-symbolization parameter to use for the point geometry.
     *
     * @return The Graphic to be used when drawing a point.
     */
    Graphic getGraphic();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Override
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /** Provides the graphical-symbolization parameter to use for the point geometry. */
    void setGraphic(Graphic graphic);
}
