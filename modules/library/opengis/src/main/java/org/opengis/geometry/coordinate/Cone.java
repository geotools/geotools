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
 * A {@linkplain GriddedSurface gridded surface} given as a family of conic sections whose
 * {@linkplain #getControlPoints control points} vary linearly. A 5-point ellipse with all
 * defining positions identical is a point. Thus, a truncated elliptical cone can be given
 * as a 2&times;5 set of control points
 *
 * &lt;&lt;P1, P1, P1, P1, P1&gt;, &lt;P2, P3, P4, P5, P6&gt;&gt;.
 *
 * P1 is the apex of the cone. P2, P3, P4, P5, and P6 are any five distinct points around
 * the base ellipse of the cone. If the horizontal curves are circles as opposed to ellipses,
 * then a circular cone can be constructed using &lt;&lt;P1, P1, P1&gt;, &lt;P2, P3, P4&gt;&gt;.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/Cone.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_Cone", specification=ISO_19107)
public interface Cone extends GriddedSurface {
}
