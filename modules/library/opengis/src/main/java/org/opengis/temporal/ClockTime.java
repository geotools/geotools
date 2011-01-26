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

import java.util.Collection;
import java.lang.Number;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A data type that shall be used to identify a temporal position within a day.
 * Because {@linkplain TemporalPosition temporal position} cannot by itself completely
 * identify a single temporal position; it shall be used with {@linkplain CalendarDate
 * calendar date} for that purpose. It may be also used to identify the time of occurrence
 * of an event that recurs every day.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
@UML(identifier="TM_ClockTime", specification=ISO_19108)
public interface ClockTime extends TemporalPosition {
    /**
     * A sequence of numbers with a structure similar to that of {@link CalendarDate#getCalendarDate
     * CalendarDate}. The first number integer identifies a specific instance of the unit used at the
     * highest level of the clock hierarchy, the second number identifies a specific instance of the
     * unit used at the next lower level, and so on. All but the last number in the sequence shall be
     * integers; the last number may be integer or real.
     *
     * @todo Should we returns an array of some primitive type instead?
     * @todo Method name doesn't match the UML attribute name.
     */
    @UML(identifier="clkTime", obligation=MANDATORY, specification=ISO_19108)
    Number[] getClockTime();
}
