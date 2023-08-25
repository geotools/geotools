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

/**
 * A 1-, 2-, or 3-dimensional coordinate system. Gives the position of points relative to orthogonal
 * straight axes in the 2- and 3-dimensional cases. In the 1-dimensional case, it contains a single
 * straight coordinate axis. In the multi-dimensional case, all axes shall have the same length unit
 * of measure. A {@code CartesianCS} shall have one, two, or three {@linkplain #getAxis axis
 * associations}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.crs.GeocentricCRS  Geocentric},
 *   {@link org.geotools.api.referencing.crs.ProjectedCRS   Projected},
 *   {@link org.geotools.api.referencing.crs.EngineeringCRS Engineering},
 *   {@link org.geotools.api.referencing.crs.ImageCRS       Image}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see AffineCS
 */
public interface CartesianCS extends AffineCS {}
