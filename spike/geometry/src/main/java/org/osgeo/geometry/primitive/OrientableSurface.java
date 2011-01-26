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


/**
 * An <code>OrientableSurface</code> consists of a wrapped base {@link Surface} and an additional orientation.
 * <p>
 * If the orientation is *not* reversed, then the <code>OrientableSurface</code> is identical to the base curve. If the
 * orientation is reversed, then the OrientableSurface is a reference to a surface with an up-normal that reverses the
 * direction for this OrientableSurface, the sense of "the top of the surface".
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface OrientableSurface extends Surface {

    /**
     * Must always return {@link Surface.SurfaceType#OrientableSurface}.
     *
     * @return {@link Surface.SurfaceType#OrientableSurface}
     */
    public SurfaceType getSurfaceType();

    /**
     * Returns whether the orientation of this surface is reversed compared to the base surface.
     *
     * @return true, if the orientation is reversed, false otherwise
     */
    public boolean isReversed();

    /**
     * Returns the {@link Surface} that this <code>OrientableSurface</code> is based on.
     *
     * @return the base surface
     */
    public Surface getBaseSurface();
}
