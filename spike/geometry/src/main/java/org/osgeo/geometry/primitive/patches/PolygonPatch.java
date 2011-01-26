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

import java.util.List;

import org.osgeo.geometry.primitive.Ring;

/**
 * A {@link PolygonPatch} is a planar {@link SurfacePatch} that is defined by a set of boundary curves and an underlying
 * surface to which these curves adhere. The curves are coplanar and the polygon uses planar interpolation in its
 * interior. Implements <code>GM_Polygon</code> of ISO 19107.
 * <p>
 * Please note that a {@link PolygonPatch} is not restricted to use linear interpolation for its exterior and interior
 * rings.
 * </p>
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface PolygonPatch extends SurfacePatch {

    /**
     * Returns the boundary rings (interior + exteriors)
     * 
     * @return the boundary rings, list may be empty (but not null)
     */
    public List<? extends Ring> getBoundaryRings();

    /**
     * Returns the exterior ring of the patch.
     * <p>
     * Please note that the exterior may be empty (null). The following explanation is from the GML 3.1.1 spec (section
     * 9.2.2.5): In the normal 2D case, one of these rings is distinguished as being the exterior boundary. In a general
     * manifold this is not always possible, in which case all boundaries shall be listed as interior boundaries, and
     * the exterior will be empty.
     * 
     * @return the exterior ring, or null
     */
    public Ring getExteriorRing();

    /**
     * Returns the interior rings (holes) of the patch.
     * 
     * @return the interior rings (holes) of the patch, list may be empty (but not null)
     */
    public List<Ring> getInteriorRings();
}
