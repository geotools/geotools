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
 * Same as an {@linkplain Arc arc}, but closed to form a full circle.
 * The {@linkplain #getStartAngle start} and {@linkplain #getEndAngle end bearing}
 * are equal and shall be the bearing for the first {@linkplain #getControlPoints
 * control point} listed.
 *
 * This still requires at least 3 distinct non-co-linear points to be unambiguously
 * defined. The arc is simply extended until the first point is encountered.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/Circle.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_Circle", specification=ISO_19107)
public interface Circle extends Arc {
}
