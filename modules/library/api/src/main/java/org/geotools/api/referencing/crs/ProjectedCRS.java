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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19111;

import org.geotools.api.annotation.UML;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.operation.Projection;

/**
 * A 2D coordinate reference system used to approximate the shape of the earth on a planar surface.
 * It is done in such a way that the distortion that is inherent to the approximation is carefully
 * controlled and known. Distortion correction is commonly applied to calculated bearings and
 * distances to produce values that are a close match to actual field values.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.cs.CartesianCS Cartesian}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "SC_ProjectedCRS", specification = ISO_19111)
public interface ProjectedCRS extends GeneralDerivedCRS {
    /** Returns the base coordinate reference system, which must be geographic. */
    @Override
    GeographicCRS getBaseCRS();

    /** Returns the map projection from the {@linkplain #getBaseCRS base CRS} to this CRS. */
    @Override
    Projection getConversionFromBase();

    /** Returns the coordinate system, which must be cartesian. */
    @Override
    @UML(identifier = "usesCS", obligation = MANDATORY, specification = ISO_19111)
    CartesianCS getCoordinateSystem();

    /** Returns the datum. */
    @Override
    GeodeticDatum getDatum();
}
