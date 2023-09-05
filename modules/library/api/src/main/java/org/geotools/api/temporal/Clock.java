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

import org.geotools.api.util.InternationalString;

/**
 * Provides a basis for defining temporal position within a day.
 *
 * @author Alexander Petkov
 * @todo Retrofit in the referencing framework.
 */
public interface Clock extends TemporalReferenceSystem {
    /** Event used as the datum for this clock. */
    InternationalString getReferenceEvent();

    /** Time of the reference Event for this clock, usually the origin of the clock scale. */
    ClockTime getReferenceTime();

    /** Provides the 24-hour local or UTC time that corresponds to the reference time. */
    ClockTime getUTCReference();

    /** Converts UTC time to a time on this clock. */
    ClockTime clkTrans(ClockTime clkTime);

    /** Converts UTC time to a time on this clock. */
    ClockTime utcTrans(ClockTime uTime);
}
