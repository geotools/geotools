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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.geotools.util.Utilities;
import org.geotools.util.SimpleInternationalString;
import org.opengis.temporal.CalendarDate;
import org.opengis.temporal.DateAndTime;
import org.opengis.temporal.JulianDate;
import org.opengis.temporal.OrdinalPosition;
import org.opengis.temporal.Position;
import org.opengis.temporal.TemporalCoordinate;
import org.opengis.temporal.TemporalPosition;
import org.opengis.util.InternationalString;

/**
 * A union class that consists of one of the data types listed as its attributes.
 * Date, Time, and DateTime are basic data types defined in ISO/TS 19103,
 * and may be used for describing temporal positions referenced to the
 * Gregorian calendar and UTC.
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultPosition implements Position {

    /**
     * this object represents one of the data types listed as : Date, Time, DateTime, and TemporalPosition with its subtypes
     */
    private final Object position;

    public DefaultPosition(final Date date) {
        this.position = date;
    }

    /**
     * This constructor replace the constructor with further DateTime object which will be included in the futur version of jdk (jdk7).
     * example of datetime argument: format specified by the ISO8601 yyyy-mm-DDTHH:MM:SSZ - example : 2003-02-13T12:28:00.000GMT-08:00.
     * @param dateTime
     * @throws java.text.ParseException
     */
    public DefaultPosition(final InternationalString datetime) throws ParseException {
        this.position = Utils.getDateFromString(datetime.toString());
    }

    /**
     * This constructor set the position property as a TemporalPosition.
     * @param anyOther
     */
    public DefaultPosition(final TemporalPosition anyOther) {
        this.position = anyOther;
    }

    /**
     * {@linkplain TemporalPosition} and its subtypes shall be used
     * for describing temporal positions referenced to other reference systems, and may be used for
     * temporal positions referenced to any calendar or clock, including the Gregorian calendar and UTC.
     * @return TemporalPosition
     */
    public TemporalPosition anyOther() {
        return (this.position instanceof TemporalPosition) ? (TemporalPosition) position : null;
    }

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     * 
     * @TODO all subtypes of TemporalPosition must be implemented.
     */
    public Date getDate() {
        if (this.position instanceof Date) {
            return (Date) position;
        }
        if (this.position instanceof TemporalPosition) {
            if (this.position instanceof JulianDate) {
                return Utils.JulianToDate((DefaultJulianDate) position);
            }
            if (this.position instanceof DateAndTime) {
                return Utils.dateAndTimeToDate((DateAndTime) position);
            }
            if (this.position instanceof CalendarDate) {
                return Utils.calendarDateToDate((CalendarDate) position);
            }
            if (this.position instanceof TemporalCoordinate) {
                return Utils.temporalCoordToDate((TemporalCoordinate) position);
            }
            if (this.position instanceof OrdinalPosition) {
                return Utils.ordinalToDate((OrdinalPosition) position);
            }
        }
        return null;
    }

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    public Time getTime() {
        return (this.position instanceof Time) ? (Time) position : null;
    }

    /**
     * May be used for describing temporal positions in ISO8601 format referenced to the
     * Gregorian calendar and UTC.
     * @return {@linkplain InternationalString}
     */
    public InternationalString getDateTime() {
        if (this.position instanceof Date) {
            String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
            SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
            return new SimpleInternationalString(dateFormat.format(position));
        }
        return null;
    }

    /**
     * Verify if this entry is identical to the specified object.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultPosition) {
            final DefaultPosition that = (DefaultPosition) object;
            return Utilities.equals(this.position, that.position);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.position != null ? this.position.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Position:").append('\n');
        if (position != null) {
            s.append("position:").append(position).append('\n');
        }
        return s.toString();
    }
}

