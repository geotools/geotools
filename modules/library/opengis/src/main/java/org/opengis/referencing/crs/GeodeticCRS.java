/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.crs;

import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A coordinate reference system associated with a geodetic datum.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.cs.CartesianCS   Cartesian}
 *   {@link org.opengis.referencing.cs.SphericalCS   Spherical}
 *   {@link org.opengis.referencing.cs.EllipsoidalCS Ellipsoidal}
 * </TD></TR></TABLE>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/crs/GeodeticCRS.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.1
 */
@UML(identifier="SC_GeodeticCRS", specification=ISO_19111)
public interface GeodeticCRS extends SingleCRS {
    /**
     * Returns the datum, which must be geodetic.
     */
    @UML(identifier="usesDatum", obligation=MANDATORY, specification=ISO_19111)
    GeodeticDatum getDatum();
}
