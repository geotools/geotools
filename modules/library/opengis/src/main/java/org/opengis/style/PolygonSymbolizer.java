/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import org.opengis.annotation.Extension;
import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;


/**
 * Holds the information that indicates how to draw the lines and the interior of polygons.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/PolygonSymbolizer.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("PolygonSymbolizer")
public interface PolygonSymbolizer extends Symbolizer {

    /**
     * Returns the object containing all the information necessary to draw
     * styled lines.  This is used for the edges of polygons.
     *
     * @return Stroke
     */
    @XmlElement("Stroke")
    Stroke getStroke();

    /**
     * Returns the object that holds the information about how the interior of
     * polygons should be filled.  This may be null if the polygons are not to
     * be filled at all.
     *
     * @return Fill
     */
    @XmlElement("Fill")
    Fill getFill();

    /**
     * The Displacement gives the X and Y displacements from the original geometry. This
     * element may be used to avoid over-plotting of multiple PolygonSymbolizers for one
     * geometry or supplying "shadows" of polygon gemeotries. The displacements are in units
     * of pixels above and to the right of the point. The default displacement is X=0, Y=0.
     *
     * @return Displacement
     */
    @XmlElement("Displacement")
    Displacement getDisplacement();

    /**
     * PerpendicularOffset works as defined for LineSymbolizer, allowing to draw polygons
     * smaller or larger than their actual geometry. The distance is in uoms and is positive to the
     * outside of the polygon. Negative numbers mean drawing the polygon smaller. The default
     * offset is 0.

     * @return Expression
     */
    @XmlElement("PerpendicularOffset")
    Expression getPerpendicularOffset();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
