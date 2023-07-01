/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.cs;

import static org.geotools.api.annotation.Specification.ISO_19111;

import org.geotools.api.annotation.UML;

/**
 * A three-dimensional coordinate system with one distance measured from the origin and two angular
 * coordinates. Not to be confused with an {@linkplain EllipsoidalCS ellipsoidal coordinate system}
 * based on an ellipsoid "degenerated" into a sphere. A {@code SphericalCS} shall have three
 * {@linkplain #getAxis axis associations}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.crs.GeocentricCRS  Geocentric},
 *   {@link org.geotools.api.referencing.crs.EngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "CS_SphericalCS", specification = ISO_19111)
public interface SphericalCS extends CoordinateSystem {}
