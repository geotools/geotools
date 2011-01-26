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
 * The LabelPlacement element is used to position a label relative to a point, line string or
 * polygon.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("LabelPlacement")
public interface LabelPlacement {
    /**
     * Calls the visit method of a StyleVisitor.
     * <p>
     * Please note that LabelPalcement is not intended to be used as is; the StyleVisitor
     * visit method must traverse an instance of LinePlacement or PointPlacement.
     * 
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
}
