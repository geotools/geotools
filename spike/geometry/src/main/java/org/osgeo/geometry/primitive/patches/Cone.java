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
 * The <code>Cone</code> class represents (according to GML-3.1 spec ) a gridded surface given as a family of
 * conic sections whose control points vary linearly.
 * A 5-point ellipse with all defining positions identical is a point. Thus, a truncated elliptical cone can be
 * given as a 2x5 set of control points <<P1, P1, P1, P1, P1>, <P2, P3, P4, P5, P6>>. P1 is the apex of the
 * cone. P2, P3, P4, P5 and P6 are any five distinct points around the base ellipse of the cone. If the horizontal
 * curves are circles as opposed to ellipses, the circular cone can be constructed using <<P1, P1, P1>, <P2, P3,
 * P4>>.
 *
 * @author <a href="mailto:ionita@lat-lon.de">Andrei Ionita</a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface Cone extends GriddedSurfacePatch {

    // nothing new here, this interface is only necessary for a type-based differentiation

    /**
     * Must always return {@link GriddedSurfacePatch.GriddedSurfaceType#CONE}.
     *
     * @return {@link GriddedSurfacePatch.GriddedSurfaceType#CONE}
     */
    public GriddedSurfaceType getGriddedSurfaceType();
}
