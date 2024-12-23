/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

import java.util.Date;
import org.geotools.api.util.InternationalString;

/**
 * A temporal coordinate system to simplify the computation of temporal distances between points and the functional
 * description of temporal operations.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @todo Retrofit in {@link org.geotools.api.referencing.cs.TimeCS}.
 */
public interface TemporalCoordinateSystem extends TemporalReferenceSystem {
    /**
     * Position of the origin of the scale on which the temporal coordinate system is based expressed as a date in the
     * Gregorian calendar and time of day in UTC.
     */
    Date getOrigin();

    /**
     * Identifies the base interval for this temporal coordinate system as a unit of measure specified by ISO 31-1, or a
     * multiple of one of those units, as specified by ISO 1000.
     */
    InternationalString getInterval();

    /**
     * Transforms a value of a {@linkplain TemporalCoordinate coordinate} within this temporal coordinate system and
     * returns the equivalent {@linkplain DateAndTime date and time} in the Gregorian Calendar and UTC
     */
    Date transformCoord(TemporalCoordinate coordinates);

    /**
     * Transforms a {@linkplain DateAndTime date and time} in the Gregorian Calendar and UTC to an equivalent
     * {@linkplain TemporalCoordinate coordinate} within this temporal coordinate system.
     */
    TemporalCoordinate transformDateTime(Date datetime);
}
