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
 * A {@linkplain GriddedSurface gridded surface} given as a family of circles whose positions vary
 * linearly along the axis of the sphere, and whose radius varies in proportion to the cosine function
 * of the central angle. The horizontal circles resemble lines of constant latitude, and the vertical
 * arcs resemble lines of constant longitude. If the {@linkplain #getControlPoints control points}
 * are sorted in terms of increasing longitude, and increasing latitude, the
 * {@linkplain #getUpNormal upNormal} of a sphere is the outward normal.
 * <p>
 * <strong>Example:</strong> If we take a gridded set of latitudes and longitudes in degrees,
 * (<var>u</var>, <var>v</var>), such as
 *
 * <blockquote><pre>
 * (-90, -180) (-90, -90) (-90, 0) (-90, 90) (-90, 180)
 * (-45, -180) (-45, -90) (-45, 0) (-45, 90) (-45, 180)
 * (  0, -180) (  0, -90) (  0, 0) (  0, 90) (  0, 180)
 * ( 45, -180) ( 45, -90) ( 45, 0) (45, -90) ( 45, 180)
 * ( 90, -180) ( 90, -90) ( 90, 0) (90, -90) ( 90, 180)
 * </blockquote></pre>
 *
 * and map these points to 3D using the usual equations
 * (where <var>R</var> is the radius of the required sphere)
 *
 * <blockquote>
 * <var>z</var> = <var>R</var> sin(<var>u</var>)
 * <var>x</var> = <var>R</var> cos(<var>u</var>) sin(<var>v</var>)
 * <var>y</var> = <var>R</var> cos(<var>u</var>) cos(<var>v</var>)
 * </blockquote>
 *
 * we have a sphere of radius <var>R</var>, centered at (0,&nbsp;0), as a gridded surface. Notice
 * that the entire first row and the entire last row of the control points map to a single point
 * each in 3D Euclidean space, North and South poles respectively, and that each horizontal curve
 * closes back on it self forming a geometric cycle. This gives us a metrically bounded (of finite
 * size), topologically unbounded (not having a boundary, a cycle) surface.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/Sphere.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_Sphere", specification=ISO_19107)
public interface Sphere extends GriddedSurface {
}
