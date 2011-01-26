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
 * An <code>OrientableCurve</code> consists of a wrapped base {@link Curve} and an additional orientation.
 * <p>
 * If the orientation is *not* reversed, then the <code>OrientableCurve</code> is identical to the base curve. If the
 * orientation is reversed, then the <code>OrientableCurve</code> is related to the base curve with a parameterization
 * that reverses the sense of the curve traversal.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 *
 * @version $Revision:$, $Date:$
 */
public interface OrientableCurve extends Curve {

    /**
     * Must always return {@link Curve.CurveType#OrientableCurve}.
     *
     * @return {@link Curve.CurveType#OrientableCurve}
     */
    public CurveType getCurveType();

    /**
     * Returns whether the orientation of this curve is reversed compared to the base curve.
     *
     * @return true, if the orientation is reversed, false otherwise
     */
    public boolean isReversed();

    /**
     * Returns the {@link Curve} that this <code>OrientableCurve</code> is based on.
     *
     * @return the base <code>Curve</code>
     */
    public Curve getBaseCurve();
}
