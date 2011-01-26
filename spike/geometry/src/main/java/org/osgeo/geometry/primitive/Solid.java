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

/**
 * <code>Solid</code> instances are 3D-geometries that ...
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface Solid extends GeometricPrimitive {

    /**
     * Convenience enum type for discriminating the different solid variants.
     */
    public enum SolidType {
        /** Generic solid that consists of an arbitrary number of */
        Solid,
        /** Solid composited from multiple members solids. */
        CompositeSolid
    }

    /**
     * Must always return {@link GeometricPrimitive.PrimitiveType#Solid}.
     * 
     * @return {@link GeometricPrimitive.PrimitiveType#Solid}
     */
    public PrimitiveType getPrimitiveType();

    /**
     * Returns the type of solid.
     * 
     * @return the type of solid
     */
    public SolidType getSolidType();

    /**
     * Returns the exterior surface (shell) of the solid.
     * <p>
     * Please note that this method may return null. The following explanation is from the GML 3.1.1 schema
     * (geometryPrimitives.xsd): In normal 3-dimensional Euclidean space, one (composite) surface is distinguished as
     * the exterior. In the more general case, this is not always possible.
     * 
     * @return the exterior surface, or null if no surface is distinguished as being the exterior boundary
     */
    public Surface getExteriorSurface();

    /**
     * Returns the interior surfaces of the solid.
     * 
     * @return the interior surfaces, list may be empty (but not null)
     */
    public List<Surface> getInteriorSurfaces();
}
