/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;


/**
 * Two distinct {@linkplain org.opengis.geometry.DirectPosition direct positions}
 * (the {@linkplain #getStartPoint start point} and {@linkplain #getEndPoint end point}) joined
 * by a straight line. Thus its interpolation attribute shall be
 * {@link org.opengis.geometry.primitive.CurveInterpolation#LINEAR LINEAR}.
 * The default parameterization is:
 *
 * <blockquote><pre>
 * L = {@linkplain #getEndParam endParam} - {@linkplain #getStartParam startParam}
 * c(s) = ControlPoint[1]+((s-{@linkplain #getStartParam startParam})/L)*(ControlPoint[2]-ControlPoint[1])
 * </pre></blockquote>
 *
 * Any other point in the control point array must fall on this line. The control points of a
 * {@code LineSegment} shall all lie on the straight line between its start point and end
 * point. Between these two points, other positions may be interpolated linearly. The linear
 * interpolation, given using a constructive parameter <var>t</var>, 0 ? <var>t</var> ? 1.0,
 * where c(o) = c.{@linkplain #getStartPoint startPoint} and c(1)=c.{@link #getEndPoint endPoint},
 * is:
 *
 * <blockquote>
 * <var>c</var>(<var>t</var>) = <var>c</var>(0)(1-<var>t</var>) + <var>c</var>(1)<var>t</var>
 * </blockquote>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see GeometryFactory#createLineSegment
 */
@UML(identifier="GM_LineSegment", specification=ISO_19107)
public interface LineSegment extends LineString {
}
