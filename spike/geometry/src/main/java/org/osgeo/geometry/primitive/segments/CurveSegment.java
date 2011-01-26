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
package org.osgeo.geometry.primitive.segments;

import org.osgeo.geometry.primitive.Curve;
import org.osgeo.geometry.primitive.Point;

/**
 * A <code>CurveSegment</code> is a portion of a {@link Curve} which uses a single interpolation method.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface CurveSegment {

    /**
     * Convenience enum type for discriminating the different curve segment variants in switch statements.
     */
    public enum CurveSegmentType {
        /** Segment is an {@link Arc}. */
        ARC,
        /** Segment is an {@link ArcByBulge}. */
        ARC_BY_BULGE,
        /** Segment is an {@link ArcByCenterPoint}. */
        ARC_BY_CENTER_POINT,
        /** Segment is an {@link ArcString}. */
        ARC_STRING,
        /** Segment is an {@link ArcStringByBulge}. */
        ARC_STRING_BY_BULGE,
        /** Segment is a {@link Bezier}. */
        BEZIER,
        /** Segment is a {@link BSpline}. */
        BSPLINE,
        /** Segment is a {@link Circle}. */
        CIRCLE,
        /** Segment is a {@link CircleByCenterPoint}. */
        CIRCLE_BY_CENTER_POINT,
        /** Segment is a {@link Clothoid}. */
        CLOTHOID,
        /** Segment is a {@link CubicSpline}. */
        CUBIC_SPLINE,
        /** Segment is a {@link Geodesic}. */
        GEODESIC,
        /** Segment is a {@link GeodesicString}. */
        GEODESIC_STRING,
        /** Segment is a {@link LineStringSegment}. */
        LINE_STRING_SEGMENT,
        /** Segment is an {@link OffsetCurve}. */
        OFFSET_CURVE
    }

    /**
     * Returns the type of curve segment.
     * 
     * @return the type of curve segment
     */
    public CurveSegmentType getSegmentType();    
    
    /**
     * Returns the coordinate dimension, i.e. the dimension of the space that the curve is embedded in.
     * 
     * @return the coordinate dimension
     */
    public int getCoordinateDimension();

    /**
     * Returns the start point of the segment.
     * 
     * @return the start point of the segment
     */
    public Point getStartPoint();

    /**
     * Returns the end point of the segment.
     * 
     * @return the end point of the segment
     */
    public Point getEndPoint();
}
