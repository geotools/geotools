/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/LineSegmentImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

import org.opengis.geometry.coordinate.LineSegment;

/**
 * Two distinct {@linkplain org.opengis.geometry.coordinate.DirectPosition direct positions} (the
 * {@linkplain #getStartPoint start point} and {@linkplain #getEndPoint end point}) joined by a
 * straight line. Thus its interpolation attribute shall be {@link
 * org.opengis.geometry.primitive.CurveInterpolation#LINEAR LINEAR}. The default parameterization
 * is:
 *
 * <blockquote>
 *
 * <pre>
 * L = {@linkplain #getEndParam endParam} - {@linkplain #getStartParam startParam}
 * c(s) = ControlPoint[1]+((s-{@linkplain #getStartParam startParam})/L)*(ControlPoint[2]-ControlPoint[1])
 * </pre>
 *
 * </blockquote>
 *
 * Any other point in the control point array must fall on this line. The control points of a {@code
 * LineSegment} shall all lie on the straight line between its start point and end point. Between
 * these two points, other positions may be interpolated linearly. The linear interpolation, given
 * using a constructive parameter <var>t</var>, 0 ? <var>t</var> ? 1.0, where c(o) = c.{@linkplain
 * #getStartPoint startPoint} and c(1)=c.{@link #getEndPoint endPoint}, is:
 *
 * <blockquote>
 *
 * <var>c</var>(<var>t</var>) = <var>c</var>(0)(1-<var>t</var>) + <var>c</var>(1)<var>t</var>
 *
 * </blockquote>
 *
 * @UML datatype GM_LineSegment
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 * @see GeometryFactory#createLineSegment
 */
public class LineSegmentImpl extends LineStringImpl implements LineSegment {

    // *************************************************************************
    //  Fields
    // *************************************************************************

    // *************************************************************************
    //  Constructor
    // *************************************************************************

    // *************************************************************************
    //  implement the LineSegment interface
    // *************************************************************************

}
