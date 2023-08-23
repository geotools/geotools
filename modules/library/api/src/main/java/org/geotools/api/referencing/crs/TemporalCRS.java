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

import org.geotools.api.referencing.cs.TimeCS;
import org.geotools.api.referencing.datum.TemporalDatum;

/**
 * A 1D coordinate reference system used for the recording of time.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.api.referencing.cs.TimeCS Time}
 * </TD></TR></TABLE>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface TemporalCRS extends SingleCRS {
    /** Returns the coordinate system, which must be temporal. */
    @Override
    TimeCS getCoordinateSystem();

    /** Returns the datum, which must be temporal. */
    @Override
    TemporalDatum getDatum();
}
