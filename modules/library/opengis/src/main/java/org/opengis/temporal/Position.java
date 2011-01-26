/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.temporal;

import java.util.Date;
import java.sql.Time;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A union class that consists of one of the data types listed as its attributes.
 * Date, Time, and DateTime are basic data types defined in ISO/TS 19103,
 * and may be used for describing temporal positions referenced to the
 * Gregorian calendar and UTC.
 *
 * @author Alexander Petkov
 * @author Martin Desruisseaux (IRD)
 *
 */
@UML(identifier="TM_Position", specification=ISO_19108)
public interface Position {
    /**
     * {@linkplain TemporalPosition} and its subtypes shall be used
     * for describing temporal positions referenced to other reference systems, and may be used for
     * temporal positions referenced to any calendar or clock, including the Gregorian calendar and UTC.
     * @return TemporalPosition
     */
    @UML(identifier="anyOther", obligation=OPTIONAL, specification=ISO_19108)
    TemporalPosition anyOther();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="date8601", obligation=OPTIONAL, specification=ISO_19108)
    Date getDate();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="time8601", obligation=OPTIONAL, specification=ISO_19108)
    Time getTime();

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    @UML(identifier="dateTime8601", obligation=OPTIONAL, specification=ISO_19108)
    InternationalString getDateTime();

}
