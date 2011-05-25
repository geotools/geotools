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
import org.opengis.temporal.CalendarDate;
import org.opengis.temporal.CalendarEra;
import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.TemporalReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * A data type that shall be used to identify temporal position within a calendar.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
public class DefaultCalendarDate extends DefaultTemporalPosition implements CalendarDate {

    /**
     * This is the name of the calendar era to which the date is referenced.
     */
    private InternationalString calendarEraName;
    /**
     * This is a sequence of positive integers in which the first integeridentifies a specific instance of the unit used at the highest level of the calendar hierarchy,
     * the second integer identifies a specific instance of the unit used at the next lower level in the hierarchy, and so on.
     * The format defined in ISO 8601 for dates in the Gregorian calendar may be used for any date that is composed of values for year, month and day.
     */
    private int[] calendarDate;

    public DefaultCalendarDate(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition, InternationalString calendarEraName, int[] calendarDate) {
        super(frame, indeterminatePosition);
        this.calendarDate = calendarDate;
        this.calendarEraName = calendarEraName;
    }

    /**
     * Provides the name of the {@linkplain CalendarEra calendar era}
     * to which the date is referenced.
     */
    public InternationalString getCalendarEraName() {
        return calendarEraName;
    }

    /**
     * Provides a sequence of integers in which the first integer identifies a specific instance
     * of the unit used at the highest level of the calendar hierarchy, the second integer
     * identifies a specific instance of the unit used at the next lower level in the hierarchy,
     * and so on. The format defined in ISO 8601 for dates in the Gregorian calendar may be
     * used for any date that is composed of values for year, month and day.
     *
     */
    public int[] getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarEraName(InternationalString calendarEraName) {
        this.calendarEraName = calendarEraName;
    }

    public void setCalendarDate(int[] calendarDate) {
        this.calendarDate = calendarDate;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultCalendarDate && super.equals(object)) {
            final DefaultCalendarDate that = (DefaultCalendarDate) object;

            return Utilities.equals(this.calendarDate, that.calendarDate) &&
                    Utilities.equals(this.calendarEraName, that.calendarEraName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.calendarDate != null ? this.calendarDate.hashCode() : 0);
        hash = 37 * hash + (this.calendarEraName != null ? this.calendarEraName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("CalendarDate:").append('\n');
        if (calendarEraName != null) {
            s.append("calendarEraName:").append(calendarEraName).append('\n');
        }
        if (calendarDate != null) {
            s.append("calendarDate:").append(calendarDate).append('\n');
        }
        return s.toString();
    }
}
