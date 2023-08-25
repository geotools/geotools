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

import java.sql.Time;
import java.util.Date;
import org.geotools.api.util.InternationalString;

/**
 * A union class that consists of one of the data types listed as its attributes. Date, Time, and
 * DateTime are basic data types defined in ISO/TS 19103, and may be used for describing temporal
 * positions referenced to the Gregorian calendar and UTC.
 *
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 */
public interface Position {
    /**
     * {@linkplain TemporalPosition} and its subtypes shall be used for describing temporal
     * positions referenced to other reference systems, and may be used for temporal positions
     * referenced to any calendar or clock, including the Gregorian calendar and UTC.
     *
     * @return TemporalPosition
     */
    TemporalPosition anyOther();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the Gregorian
     * calendar and UTC.
     *
     * @return {@linkplain InternationalString}
     */
    Date getDate();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the Gregorian
     * calendar and UTC.
     *
     * @return {@linkplain InternationalString}
     */
    Time getTime();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the Gregorian
     * calendar and UTC.
     *
     * @return {@linkplain InternationalString}
     */
    InternationalString getDateTime();
}
