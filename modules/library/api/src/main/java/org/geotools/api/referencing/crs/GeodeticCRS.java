/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.crs;

import org.geotools.api.referencing.datum.GeodeticDatum;

/**
 * A coordinate reference system associated with a geodetic datum.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.cs.CartesianCS   Cartesian}
 *   {@link org.geotools.api.referencing.cs.SphericalCS   Spherical}
 *   {@link org.geotools.api.referencing.cs.EllipsoidalCS Ellipsoidal}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 */
public interface GeodeticCRS extends SingleCRS {
    /** Returns the datum, which must be geodetic. */
    @Override
    GeodeticDatum getDatum();
}
