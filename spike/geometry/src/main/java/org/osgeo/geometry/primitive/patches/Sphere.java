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

/**
 * The <code>Sphere</code> class represents (according to GML-3.1 spec) a gridded surface given as a family of circles
 * whose positions vary linearly along the axis of the sphere, and whose radius varies in proportions to the cosine
 * function of the central angle. The horizontal circles resemble lines of constant latitude, and the vertical arcs
 * resemble lines of constant longitude.
 * 
 * @author <a href="mailto:ionita@lat-lon.de">Andrei Ionita</a>
 * 
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 * 
 */
public interface Sphere extends GriddedSurfacePatch {

    // nothing new here, this interface is only necessary for a type-based differentiation

    /**
     * Must always return {@link GriddedSurfacePatch.GriddedSurfaceType#SPHERE}.
     * 
     * @return {@link GriddedSurfacePatch.GriddedSurfaceType#SPHERE}
     */
    public GriddedSurfaceType getGriddedSurfaceType();
}
