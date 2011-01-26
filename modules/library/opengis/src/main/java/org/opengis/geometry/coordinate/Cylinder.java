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
 * A {@linkplain GriddedSurface gridded surface} given as a family of circles whose positions
 * vary along a set of parallel lines, keeping the cross sectional horizontal curves of a constant
 * shape. Given the same working assumptions as in {@linkplain GriddedSurface gridded surface}, a
 * cylinder can be given by two circles, giving us control points of the form
 *
 * &lt;&lt;P1, P2, P3&gt;, &lt;P4, P5, P6&gt;&gt;.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_Cylinder", specification=ISO_19107)
public interface Cylinder extends GriddedSurface {
}
