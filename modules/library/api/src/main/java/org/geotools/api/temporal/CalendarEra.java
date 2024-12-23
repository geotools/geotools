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
 * Characteristics of each calendar era.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
public interface CalendarEra {
    /** Uniquely identifies the calendar era within this calendar. */
    InternationalString getName();

    /**
     * Provides the name or description of a mythical or historic event which fixes the position of the base scale of
     * the calendar era.
     */
    InternationalString getReferenceEvent();

    /** Provides the date of the reference event expressed as a date in the given calendar. */
    CalendarDate getReferenceDate();

    /** Provides the {@linkplain JulianDate julian date} that corresponds to the reference date. */
    JulianDate getJulianReference();

    /**
     * Identifies the {@linkplain Period period} for which the calendar era was used as a reference fro dating.
     *
     * @return The period, where the data type for {@linkplain Period#getBegin begin} and {@link Period#getEnd end} is
     *     {@link JulianDate}.
     */
    Period getEpochOfUse();
}
