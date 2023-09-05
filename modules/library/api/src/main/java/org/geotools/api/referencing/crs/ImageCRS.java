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
import org.geotools.api.referencing.cs.AffineCS;
import org.geotools.api.referencing.datum.ImageDatum;

/**
 * An engineering coordinate reference system applied to locations in images. Image coordinate
 * reference systems are treated as a separate sub-type because a separate user community exists for
 * images with its own terms of reference.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.cs.CartesianCS Cartesian},
 *   {@link org.geotools.api.referencing.cs.AffineCS    Affine}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface ImageCRS extends SingleCRS {
    /** Returns the cartesian coordinate system. */
    @Override
    @UML(
            identifier = "usesObliqueCartesianCS, usesCartesianCS",
            obligation = MANDATORY,
            specification = ISO_19111)
    AffineCS getCoordinateSystem();

    /** Returns the datum, which must be an image one. */
    @Override
    ImageDatum getDatum();
}
