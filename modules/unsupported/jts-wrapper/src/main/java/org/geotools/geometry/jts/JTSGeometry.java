/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.geometry.jts;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface implemented by the various geometry classes of LiteGO1 that allows
 * a user to retrieve the equivalent JTS geometry.  The coordinate reference
 * system of the geometry is attached as the "userData" property of the
 * returned JTS object.
 *
 * @source $URL$
 */
public interface JTSGeometry {
    /**
     * Retrieves the equivalent JTS geometry for this object.  Note that this
     * operation may be expensive if the geometry must be computed.
     */
    public Geometry getJTSGeometry();

    /**
     * This method is invoked to cause the JTS object to be recalculated the
     * next time it is requested.  This method will be called by the
     * underlying guts of the code when something has changed.
     */
    public void invalidateCachedJTSPeer();
}
