/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.extent;

import java.util.Collection;

/**
 * Extent with respect to date/time and spatial boundaries.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface SpatialTemporalExtent extends TemporalExtent {
    /**
     * Returns the spatial extent component of composite spatial and temporal extent.
     *
     * @return The list of geographic extents (never {@code null}).
     */
    Collection<? extends GeographicExtent> getSpatialExtent();
}
