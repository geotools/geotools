/*
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.primitive.patches;

import org.osgeo.geometry.primitive.Surface;

/**
 * A {@link SurfacePatch} describes a continuous portion of a {@link Surface}.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface SurfacePatch {

    /**
     * Convenience enum type for discriminating the different surface patch variants in switch statements.
     */
    public enum SurfacePatchType {
        /** Patch is a {@link GriddedSurfacePatch}. */
        GRIDDED_SURFACE_PATCH,
        /** Patch is a {@link PolygonPatch}. */
        POLYGON_PATCH,
        /** Patch is a {@link Rectangle}. */
        RECTANGLE,
        /** Patch is a {@link Triangle}. */
        TRIANGLE
    }

    /**
     * Returns the type of surface patch.
     * 
     * @return the type of surface patch
     */
    public SurfacePatchType getSurfacePatchType();

    /**
     * Returns the coordinate dimension, i.e. the dimension of the space that the patch is embedded in.
     * 
     * @return the coordinate dimension
     */
    public int getCoordinateDimension();
}
