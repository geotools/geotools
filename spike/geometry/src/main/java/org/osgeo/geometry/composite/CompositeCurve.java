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
package org.osgeo.geometry.composite;

import java.util.List;

import org.osgeo.geometry.primitive.Curve;

/**
 * A {@link CompositeCurve} is a geometry with the same topological properties as a (primitive) {@link Curve}. It is
 * defined by a sequence of member curves such that the each curve in the sequence ends at the start point of the
 * subsequent curve in the list.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface CompositeCurve extends Curve, List<Curve> {

    /**
     * Must always return {@link Curve.CurveType#CompositeCurve}.
     * 
     * @return {@link Curve.CurveType#CompositeCurve}
     */
    public CurveType getCurveType();
}
