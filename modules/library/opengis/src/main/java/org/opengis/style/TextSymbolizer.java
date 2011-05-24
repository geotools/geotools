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
 * Indicates how text will be drawn.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/TextSymbolizer.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("TextSymbolizer")
public interface TextSymbolizer extends Symbolizer {

    /**
     * Returns the expression that will be evaluated to determine what text is
     * displayed.
     * If a Label element is not provided in a TextSymbolizer, then no text shall be rendered.
     *
     * @return Expression
     */
    @XmlElement("Label")
    Expression getLabel();

    /**
     * Returns the Font to apply on the text.
     * @return Font
     */
    @XmlElement("Font")
    Font getFont();

    /**
     * Returns the object that indicates how the text should be placed with
     * respect to the feature geometry.  This object will either be an instance
     * of {@link LinePlacement} or {@link PointPlacement}.
     * @return {@link LinePlacement} or {@link PointPlacement}.
     */
    @XmlElement("LabelPlacement")
    LabelPlacement getLabelPlacement();

    /**
     * Returns the object that indicates if a Halo will be drawn around the
     * text.  If null, a halo will not be drawn.
     * @return Halo
     */
    @XmlElement("Halo")
    Halo getHalo();

    /**
     * Returns the object that indicates how the text will be filled.
     * @return Fill
     */
    @XmlElement("Fill")
    Fill getFill();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
