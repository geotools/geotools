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
 * The GraphicStroke element both indicates that a repeated-linear-graphic stroke type will
 * be used.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("GraphicStroke")
public interface GraphicStroke extends Graphic{

    /**
     * InitialGap specifies how far away the first graphic will be drawn relative to the start of
     * the rendering line
     *
     * @return Expression
     */
    @XmlElement("InitialGap")
    Expression getInitialGap();

    /**
     * Gap gives the distance between two graphics.
     *
     * @return Expression
     */
    @XmlElement("Gap")
    Expression getGap();
    
    /**
     * Calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
