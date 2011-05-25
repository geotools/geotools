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
package org.geotools.temporal.reference;

import java.util.Collection;
import org.geotools.util.Utilities;
import org.opengis.temporal.Calendar;
import org.opengis.temporal.CalendarDate;
import org.opengis.temporal.CalendarEra;
import org.opengis.temporal.JulianDate;
import org.opengis.temporal.Period;
import org.opengis.util.InternationalString;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
public class DefaultCalendarEra implements CalendarEra {

    /**
     * identify the calendar era within this calendar.
     */
    private InternationalString name;
    /**
     * provide the name or description of a mythical or historic event which fixes the position of the base scale of the calendar era.
     */
    private InternationalString referenceEvent;
    /**
     * provide the date of the reference referenceEvent expressed as a date in the given calendar.
     * In most calendars, this date is the origin (i.e the first day) of the scale, but this is not always true.
     */
    private CalendarDate referenceDate;
    /**
     * provide the Julian date that corresponds to the reference date.
     */
    private JulianDate julianReference;
    /**
     * identify the TM_Period for which the calendar era was used as a basis for dating, the datatype for TM_Period.begin and Tm_Period.end shall be JulianDate.
     */
    private Period epochOfUse;
    /**
     * Collection of TM_Calendars that use this TM_CalendarEra as a reference for dating.
     */
    private Collection<Calendar> datingSystem;

    public DefaultCalendarEra(InternationalString name, InternationalString referenceEvent, CalendarDate referenceDate, JulianDate julianReference, Period epochOfUse) {
        this.name = name;
        this.referenceDate = referenceDate;
        this.referenceEvent = referenceEvent;
        this.julianReference = julianReference;
        this.epochOfUse = epochOfUse;
    }

    public InternationalString getName() {
        return name;
    }

    public InternationalString getReferenceEvent() {
        return referenceEvent;
    }

    public CalendarDate getReferenceDate() {
        return referenceDate;
    }

    public JulianDate getJulianReference() {
        return julianReference;
    }

    public Period getEpochOfUse() {
        return epochOfUse;
    }

    public void setName(InternationalString name) {
        this.name = name;
    }

    public void setReferenceEvent(InternationalString referenceEvent) {
        this.referenceEvent = referenceEvent;
    }

    public void setReferenceDate(CalendarDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public void setJulianReference(JulianDate julianReference) {
        this.julianReference = julianReference;
    }

    public void setEpochOfUse(Period epochOfUse) {
        this.epochOfUse = epochOfUse;
    }

    public Collection<Calendar> getDatingSystem() {
        return datingSystem;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof CalendarEra) {
            final DefaultCalendarEra that = (DefaultCalendarEra) object;

            return Utilities.equals(this.datingSystem, that.datingSystem) &&
                    Utilities.equals(this.epochOfUse, that.epochOfUse) &&
                    Utilities.equals(this.julianReference, that.julianReference) &&
                    Utilities.equals(this.name, that.name) &&
                    Utilities.equals(this.referenceDate, that.referenceDate) &&
                    Utilities.equals(this.referenceEvent, that.referenceEvent);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.datingSystem != null ? this.datingSystem.hashCode() : 0);
        hash = 37 * hash + (this.epochOfUse != null ? this.epochOfUse.hashCode() : 0);
        hash = 37 * hash + (this.julianReference != null ? this.julianReference.hashCode() : 0);
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.referenceDate != null ? this.referenceDate.hashCode() : 0);
        hash = 37 * hash + (this.referenceEvent != null ? this.referenceEvent.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("CalendarEra:").append('\n');
        if (name != null) {
            s.append("name:").append(name).append('\n');
        }
        if (epochOfUse != null) {
            s.append("epochOfUse:").append(epochOfUse).append('\n');
        }
        if (referenceEvent != null) {
            s.append("referenceEvent:").append(referenceEvent).append('\n');
        }
        if (referenceDate != null) {
            s.append("referenceDate:").append(referenceDate).append('\n');
        }
        if (julianReference != null) {
            s.append("julianReference:").append(julianReference).append('\n');
        }
        if (datingSystem != null) {
            s.append("datingSystem:").append(datingSystem).append('\n');
        }
        return s.toString();
    }
}
