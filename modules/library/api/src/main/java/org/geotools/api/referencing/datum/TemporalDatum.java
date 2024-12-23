/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.datum;

import java.util.Date;
import org.geotools.api.util.InternationalString;

/**
 * A temporal datum defines the origin of a temporal coordinate reference system.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface TemporalDatum extends Datum {
    /**
     * The date and time origin of this temporal datum.
     *
     * @return The date and time origin of this temporal datum.
     */
    Date getOrigin();

    /**
     * This attribute is defined in the {@link Datum} parent interface, but is not used by a temporal datum.
     *
     * @return Always {@code null}.
     */
    @Override
    InternationalString getAnchorPoint();

    /**
     * This attribute is defined in the {@link Datum} parent interface, but is not used by a temporal datum.
     *
     * @return Always {@code null}.
     */
    @Override
    Date getRealizationEpoch();
}
