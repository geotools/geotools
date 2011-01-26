/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.extent;

import java.util.Collection;
import java.util.Date;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.metadata.extent.SpatialTemporalExtent;


/**
 * Boundary enclosing the dataset, expressed as the closed set of
 * (<var>x</var>,<var>y</var>) coordinates of the polygon. The last
 * point replicates first point.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class SpatialTemporalExtentImpl extends TemporalExtentImpl implements SpatialTemporalExtent {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 821702768255546660L;

    /**
     * The spatial extent component of composite
     * spatial and temporal extent.
     */
    private Collection<GeographicExtent> spatialExtent;

    /**
     * Constructs an initially empty spatial-temporal extent.
     */
    public SpatialTemporalExtentImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public SpatialTemporalExtentImpl(final SpatialTemporalExtent source) {
        super(source);
    }

    /**
     * Creates a spatial-temporal extent initialized to the specified values.
     */
    public SpatialTemporalExtentImpl(final Date startTime, final Date endTime,
                                     final Collection<? extends GeographicExtent> spatialExtent)
    {
        super(startTime, endTime);
        setSpatialExtent(spatialExtent);
    }

    /**
     * Returns the spatial extent component of composite
     * spatial and temporal extent.
     *
     * @return The list of geographic extents (never {@code null}).
     */
    public synchronized Collection<GeographicExtent> getSpatialExtent() {
        return spatialExtent = nonNullCollection(spatialExtent, GeographicExtent.class);
    }

    /**
     * Set the spatial extent component of composite
     * spatial and temporal extent.
     */
    public synchronized void setSpatialExtent(
            final Collection<? extends GeographicExtent> newValues)
    {
        spatialExtent = copyCollection(newValues, spatialExtent, GeographicExtent.class);
    }
}
