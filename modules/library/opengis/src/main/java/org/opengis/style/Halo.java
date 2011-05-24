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
import org.opengis.filter.expression.Expression;
import org.opengis.annotation.XmlElement;


/**
 * A Halo is a type of Fill that is applied to the backgrounds of font glyphs. The use of
 * halos greatly improves the readability of text labels.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/Halo.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("Halo")
public interface Halo {

    /**
     * Returns the object that indicates how the halo area around the text
     * should be filled.
     *
     * The default halo fill is solid white (Color
     * “#FFFFFF”). The glyph’s fill is plotted on top of the halo. The default font fill is solid
     * black (Color “#000000”).
     *
     * @return Fill or null
     */
    @XmlElement("Fill")
    Fill getFill();

    /**
     * Returns the expression that will be evaluated to get the pixel radius of
     * the halo around the text.
     *
     * The Radius element gives the absolute size of a halo radius in pixels encoded as a
     * floating-point number. The radius is taken from the outside edge of a font glyph to extend
     * the area of coverage of the glyph (and the inside edge of “holes” in the glyphs). The halo
     * of a text label is considered to be a single shape. The default radius is one pixel.
     * Negative values are not allowed.
     *
     * @return Expression or null
     */
    @XmlElement("Radius")
    Expression getRadius();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
