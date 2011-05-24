/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.extent;

import java.util.Collection;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Extent with respect to date/time and spatial boundaries.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/extent/SpatialTemporalExtent.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="EX_SpatialTemporalExtent", specification=ISO_19115)
public interface SpatialTemporalExtent extends TemporalExtent {
    /**
     * Returns the spatial extent component of composite
     * spatial and temporal extent.
     *
     * @return The list of geographic extents (never {@code null}).
     */
    @UML(identifier="spatialExtent", obligation=MANDATORY, specification=ISO_19115)
    Collection<? extends GeographicExtent> getSpatialExtent();
}
