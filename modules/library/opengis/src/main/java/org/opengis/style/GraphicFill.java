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

/**
 * A GraphicFill is a simple interface with only a graphic but
 * additional parameters for the GraphicFill may be provided in the
 * future to provide more control the exact style of filling.
 * We keep this class to reduce future code changes.
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/GraphicFill.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("GraphicFill")
public interface GraphicFill extends Graphic{
    
    /**
     * Calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
