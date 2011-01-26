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

import org.osgeo.geometry.primitive.segments.LineStringSegment;

/**
 * A {@link TriangulatedSurface} that uses the Delaunay algorithm or a similar algorithm complemented with consideration
 * of breaklines, stoplines, and maximum length of triangle sides. These networks satisfy the Delaunay's criterion away
 * from the modifications: For each triangle in the network, the circle passing through its vertices does not contain,
 * in its interior, the vertex of any other triangle.
 * <p>
 * One can notice that the useful information provided for the Tin element is solely the trianglePatches, since the
 * stopLines and breakLines (along with maxLength and ControlPoints) are only needed to obtain the triangulation.
 * However, GML allows to specify both, so the interface provides access to them.
 * </p>
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author <a href="mailto:ionita@lat-lon.de">Andrei Ionita</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface Tin extends TriangulatedSurface {

    /**
     * Must always return {@link Surface.SurfaceType#Tin}.
     * 
     * @return {@link Surface.SurfaceType#Tin}
     */
    public SurfaceType getSurfaceType();

    /**
     * Returns the stop lines that must be respected by the triangulation.
     * 
     * @return the stop lines
     */
    public List<List<LineStringSegment>> getStopLines();

    /**
     * Returns the break lines that must be respected by the triangulation.
     * 
     * @return the break lines
     */
    public List<List<LineStringSegment>> getBreakLines();
}
