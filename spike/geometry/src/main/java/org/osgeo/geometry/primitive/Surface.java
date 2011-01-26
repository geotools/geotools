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
package org.osgeo.geometry.primitive;

import java.util.List;

import org.osgeo.geometry.primitive.patches.PolygonPatch;
import org.osgeo.geometry.primitive.patches.SurfacePatch;
import org.osgeo.geometry.primitive.patches.Triangle;

/**
 * <code>Surface</code> instances are 2D-geometries that consist of an arbitrary number of surface patches which are not
 * necessarily planar.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface Surface extends GeometricPrimitive {

    /**
     * Convenience enum type for discriminating the different surface variants.
     */
    public enum SurfaceType {
        /** Generic surface that consists of an arbitrary number of surface patches which are not necessarily planar. */
        Surface,
        /** Surface that consists of a single planar surface patch ({@link PolygonPatch}). */
        Polygon,
        /** Surface that consists of (planar) {@link PolygonPatch}es only. */
        PolyhedralSurface,
        /** Surface that consists of {@link Triangle}s only. */
        TriangulatedSurface,
        /** Surface that consists of {@link Triangle}s only (which meet the Delaunay criterion). */
        Tin,
        /** Surface composited from multiple members surfaces. */
        CompositeSurface,
        /** Surface that wraps a base surface with additional orientation flag. */
        OrientableSurface,
    }

    /**
     * Must always return {@link GeometricPrimitive.PrimitiveType#Surface}.
     * 
     * @return {@link GeometricPrimitive.PrimitiveType#Surface}
     */
    public PrimitiveType getPrimitiveType();

    /**
     * Returns the type of surface.
     * 
     * @return the type of surface
     */
    public SurfaceType getSurfaceType();

    /**
     * Returns the patches that constitute this surface.
     * 
     * @return the patches that constitute this surface
     */
    public List<? extends SurfacePatch> getPatches();
}
