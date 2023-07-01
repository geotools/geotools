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
import org.geotools.api.referencing.cs.EllipsoidalCS;

/**
 * A coordinate reference system based on an ellipsoidal approximation of the geoid; this provides
 * an accurate representation of the geometry of geographic features for a large portion of the
 * earth's surface.
 *
 * <p>A Geographic CRS is not suitable for mapmaking on a planar surface, because it describes
 * geometry on a curved surface. It is impossible to represent such geometry in a Euclidean plane
 * without introducing distortions. The need to control these distortions has given rise to the
 * development of the science of {@linkplain org.geotools.api.referencing.operation.Projection map
 * projections}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.cs.EllipsoidalCS Ellipsoidal}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "SC_GeographicCRS", specification = ISO_19111)
public interface GeographicCRS extends GeodeticCRS {
    /** Returns the coordinate system, which must be ellipsoidal. */
    @Override
    @UML(identifier = "usesCS", obligation = MANDATORY, specification = ISO_19111)
    EllipsoidalCS getCoordinateSystem();
}
