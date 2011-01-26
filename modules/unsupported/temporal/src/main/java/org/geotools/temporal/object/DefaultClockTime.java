/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.temporal.object;

import org.geotools.util.Utilities;
import org.opengis.temporal.ClockTime;
import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.TemporalReferenceSystem;

/**
 *A data type that shall be used to identify a temporal position within a day.
 * Because {@linkplain TemporalPosition temporal position} cannot by itself completely
 * identify a single temporal position; it shall be used with {@linkplain CalendarDate
 * calendar date} for that purpose. It may be also used to identify the time of occurrence
 * of an event that recurs every day.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultClockTime extends DefaultTemporalPosition implements ClockTime {

    /**
     * This is a sequence of positive numbers with a structure similar to a CalendarDate.
     */
    private Number[] clockTime;

    public DefaultClockTime(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition, Number[] clockTime) {
        super(frame, indeterminatePosition);
        this.clockTime = clockTime;
    }

    /**
     * A sequence of numbers with a structure similar to that of {@link CalendarDate#getCalendarDate
     * CalendarDate}. The first number integer identifies a specific instance of the unit used at the
     * highest level of the clock hierarchy, the second number identifies a specific instance of the
     * unit used at the next lower level, and so on. All but the last number in the sequence shall be
     * integers; the last number may be integer or real.
     *
     */
    public Number[] getClockTime() {
        return clockTime;
    }

    public void setClockTime(Number[] clockTime) {
        this.clockTime = clockTime;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultClockTime && super.equals(object)) {
            final DefaultClockTime that = (DefaultClockTime) object;

            return Utilities.equals(this.clockTime, that.clockTime);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.clockTime != null ? this.clockTime.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("ClockTime:").append('\n');
        if (clockTime != null) {
            s.append("clockTime:").append(clockTime).append('\n');
        }
        return s.toString();
    }
}
