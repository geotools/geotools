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
 * A <code>Ring</code> is a composition of {@link Curve}s that forms a closed loop.
 * <p>
 * Please note that it extends {@link Curve}, because it has an inherent curve semantic.
 * </p>
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 *
 * @version $Revision:$, $Date:$
 */
public interface Ring extends Curve {

    /**
     * All ring variants.
     */
    public enum RingType {

        /** Just one curve member (with a single segment) with linear interpolation. **/
        LinearRing,

        /** Generic ring: arbitrary number of members with arbitrary interpolation methods. **/
        Ring
    }

    /**
     * Must always return {@link Curve.CurveType#Ring}.
     *
     * @return {@link Curve.CurveType#Ring}
     */
    public CurveType getCurveType();

    /**
     * Returns the type of ring.
     *
     * @return the type of ring
     */
    public RingType getRingType();

    /**
     * Returns the {@link Curve}s that constitute this {@link Ring}.
     *
     * @return the constituting curves
     */
    public List<Curve> getMembers();
}
