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

import org.opengis.annotation.XmlElement;

/**
 * The OverlapBehavior element tells a system how to behave when multiple
 * raster images in a layer  overlap each other, for example with
 * satellite-image scenes. LATEST_ON_TOP and EARLIEST_ON_TOP refer to the
 * time the scene was captured.   AVERAGE means to average multiple scenes
 * together.   This can produce blurry results if the source images are
 * not perfectly aligned in their geo-referencing. RANDOM means to select
 * an image (or piece thereof) randomly and place it on top.  This can
 * produce crisper  results than AVERAGE potentially more efficiently than
 * LATEST_ON_TOP or EARLIEST_ON_TOP.   The default behaviour is
 * system-dependent.
 *
 * @return LATEST_ON_TOP, EARLIEST_ON_TOP, AVERAGE or RANDOM
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2  
 */
@XmlElement("OverlapBehavior")
public enum OverlapBehavior {
    LATEST_ON_TOP, 
    EARLIEST_ON_TOP, 
    AVERAGE,
    RANDOM
}
