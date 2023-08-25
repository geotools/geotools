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
 * Uses the format specified by ISO 8601 for exchanging information about the duration of a period.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 * @todo Uses Java 1.5 {@link javax.xml.datatype.Duration}.
 */
public interface PeriodDuration extends Duration {
    /**
     * A mandatory element which designates that the returned string represents the duration of a
     * period.
     */
    InternationalString getDesignator();

    /**
     * A positive integer, followed by the character "Y", which indicated the number of years in the
     * period.
     */
    InternationalString getYears();

    /**
     * A positive integer, followed by the character "M", which indicated the number of months in
     * the period.
     */
    InternationalString getMonths();

    /**
     * A positive integer, followed by the character "D", which indicated the number of days in the
     * period.
     */
    InternationalString getDays();

    /** Included whenever the sequence includes values for units less than a day. */
    InternationalString getTimeIndicator();

    /**
     * A positive integer, followed by the character "H", which indicated the number of hours in the
     * period.
     */
    InternationalString getHours();

    /**
     * A positive integer, followed by the character "M", which indicated the number of minutes in
     * the period.
     */
    InternationalString getMinutes();

    /**
     * A positive integer, followed by the character "S", which indicated the number of seconds in
     * the period.
     */
    InternationalString getSeconds();
}
