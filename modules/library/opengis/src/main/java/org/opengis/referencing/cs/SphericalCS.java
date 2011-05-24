/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.cs;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * A three-dimensional coordinate system with one distance measured from the origin and two
 * angular coordinates. Not to be confused with an {@linkplain EllipsoidalCS ellipsoidal
 * coordinate system} based on an ellipsoid "degenerated" into a sphere.
 * A {@code SphericalCS} shall have three {@linkplain #getAxis axis associations}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.crs.GeocentricCRS  Geocentric},
 *   {@link org.opengis.referencing.crs.EngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/cs/SphericalCS.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="CS_SphericalCS", specification=ISO_19111)
public interface SphericalCS extends CoordinateSystem {
}
