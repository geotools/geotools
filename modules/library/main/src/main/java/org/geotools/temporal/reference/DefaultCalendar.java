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
import java.util.GregorianCalendar;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.temporal.object.DefaultCalendarDate;
import org.geotools.temporal.object.DefaultDateAndTime;
import org.geotools.temporal.object.DefaultJulianDate;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.temporal.Calendar;
import org.opengis.temporal.CalendarDate;
import org.opengis.temporal.CalendarEra;
import org.opengis.temporal.Clock;
import org.opengis.temporal.ClockTime;
import org.opengis.temporal.DateAndTime;
import org.opengis.temporal.JulianDate;
import org.opengis.temporal.TemporalCoordinateSystem;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultCalendar extends DefaultTemporalReferenceSystem implements Calendar {

    /** Collection of CalendarEra which uses this Calendar as a reference. */
    private Collection<CalendarEra> basis;
    /** */
    private Clock timeBasis;

    /** Creates a new instance of DefaultCalendar */
    public DefaultCalendar(ReferenceIdentifier name, Extent domainOfValidity) {
        super(name, domainOfValidity);
    }

    /**
     * Convert a TemporalPosition representing by a CalendarDate and a ClockTime to a Julian date.
     */
    public JulianDate dateTrans(CalendarDate calDate, ClockTime time) {
        JulianDate response;
        if (calDate != null && time != null) {
            DateAndTime dateAndTime =
                    new DefaultDateAndTime(
                            this,
                            calDate.getIndeterminatePosition(),
                            calDate.getCalendarEraName(),
                            calDate.getCalendarDate(),
                            time.getClockTime());
            return dateTrans(dateAndTime);
        }
        GregorianCalendar gc = new GregorianCalendar(-4713, 1, 1);
        gc.set(GregorianCalendar.ERA, GregorianCalendar.BC);
        final int julianGre = 15 + 31 * (10 + 12 * 1582);
        Number coordinateValue = 0;
        TemporalCoordinateSystem refSystem =
                new DefaultTemporalCoordinateSystem(
                        new NamedIdentifier(
                                Citations.CRS, new SimpleInternationalString("Julian calendar")),
                        null,
                        gc.getTime(),
                        new SimpleInternationalString("day"));
        if (calDate != null) {
            int[] cal = calDate.getCalendarDate();
            int year = 0;
            int month = 0;
            int day = 0;
            if (cal.length > 3) {
                throw new IllegalArgumentException(
                        "The CalendarDate integer array is malformed ! see ISO 8601 format.");
            } else {
                year = cal[0];
                if (cal.length > 0) {
                    month = cal[1];
                }
                if (cal.length > 1) {
                    day = cal[2];
                }
                int julianYear = year;
                if (year < 0) {
                    julianYear++;
                }
                int julianMonth = month;
                if (month > 2) {
                    julianMonth++;
                } else {
                    julianYear--;
                    julianMonth += 13;
                }
                double julian =
                        (java.lang.Math.floor(365.25 * julianYear)
                                + java.lang.Math.floor(30.6001 * julianMonth)
                                + day
                                + 1720995.0);
                if (day + 31 * (month + 12 * year) >= julianGre) {
                    // change over to Gregorian calendar
                    int ja = (int) (0.01 * julianYear);
                    julian += 2 - ja + (0.25 * ja);
                }
                coordinateValue = java.lang.Math.floor(julian);
                response = new DefaultJulianDate(refSystem, null, coordinateValue);
                return response;
            }
        } else if (time != null) {
            Number[] clk = time.getClockTime();
            Number hour = 0;
            Number minute = 0;
            Number second = 0;
            if (clk.length > 3) {
                throw new IllegalArgumentException(
                        "The ClockTime Number array is malformed ! see ISO 8601 format.");
            } else {
                hour = clk[0];
                if (clk.length > 0) {
                    minute = clk[1];
                }
                if (clk.length > 1) {
                    second = clk[2];
                }
                double julian =
                        ((hour.doubleValue() - 12) / 24)
                                + (minute.doubleValue() / 1440)
                                + (second.doubleValue() / 86400);
                coordinateValue = julian;
                response = new DefaultJulianDate(refSystem, null, coordinateValue);
                return response;
            }
        } else {
            throw new IllegalArgumentException(
                    "the both CalendarDate and ClockTime cannot be null !");
        }
    }

    /**
     * This method is called by the Overrided method dateTrans() which take 2 arguments CalendarDate
     * and ClockTime.
     */
    public JulianDate dateTrans(DateAndTime dateAndTime) {
        JulianDate response;
        GregorianCalendar gc = new GregorianCalendar(-4713, 1, 1);
        gc.set(GregorianCalendar.ERA, GregorianCalendar.BC);
        final int julianGre = 15 + 31 * (10 + 12 * 1582);
        TemporalCoordinateSystem refSystem =
                new DefaultTemporalCoordinateSystem(
                        new NamedIdentifier(
                                Citations.CRS, new SimpleInternationalString("Julian calendar")),
                        null,
                        gc.getTime(),
                        new SimpleInternationalString("day"));
        Number coordinateValue = 0;
        int year = 0, month = 0, day = 0;
        Number hour = 0, minute = 0, second = 0;
        if (dateAndTime == null) {
            throw new IllegalArgumentException("The DateAndTime cannot be null ! ");
        }
        if (dateAndTime.getCalendarDate() != null) {
            int[] cal = dateAndTime.getCalendarDate();
            if (cal.length > 3) {
                throw new IllegalArgumentException(
                        "The CalendarDate integer array is malformed ! see ISO 8601 format.");
            } else {
                year = cal[0];
                if (cal.length > 0) {
                    month = cal[1];
                }
                if (cal.length > 1) {
                    day = cal[2];
                }
                int julianYear = year;
                if (year < 0) {
                    julianYear++;
                }
                int julianMonth = month;
                if (month > 2) {
                    julianMonth++;
                } else {
                    julianYear--;
                    julianMonth += 13;
                }
                double julian =
                        (java.lang.Math.floor(365.25 * julianYear)
                                + java.lang.Math.floor(30.6001 * julianMonth)
                                + day
                                + 1720995.0);
                if (day + 31 * (month + 12 * year) >= julianGre) {
                    int ja = (int) (0.01 * julianYear);
                    julian += 2 - ja + (0.25 * ja);
                }
                coordinateValue = java.lang.Math.floor(julian);
            }
        }
        if (dateAndTime.getClockTime() != null) {
            Number[] clk = dateAndTime.getClockTime();
            if (clk.length > 3) {
                throw new IllegalArgumentException(
                        "The ClockTime Number array is malformed ! see ISO 8601 format.");
            } else {
                hour = clk[0];
                if (clk.length > 0) {
                    minute = clk[1];
                }
                if (clk.length > 1) {
                    second = clk[2];
                }
                double julian =
                        ((hour.doubleValue() - 12) / 24)
                                + (minute.doubleValue() / 1440)
                                + (second.doubleValue() / 86400);
                coordinateValue = coordinateValue.doubleValue() + julian;
            }
        }
        response = new DefaultJulianDate(refSystem, null, coordinateValue);
        return response;
    }

    /** Convert a JulianDate to CalendarDate */
    public CalendarDate julTrans(JulianDate jdt) {
        if (jdt == null) return null;

        CalendarDate response = null;

        int JGREG = 15 + 31 * (10 + 12 * 1582);
        int jalpha, ja, jb, jc, jd, je, year, month, day;
        ja = (int) jdt.getCoordinateValue().intValue();
        if (ja >= JGREG) {
            jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
            ja = ja + 1 + jalpha - jalpha / 4;
        }

        jb = ja + 1524;
        jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
        jd = 365 * jc + jc / 4;
        je = (int) ((jb - jd) / 30.6001);
        day = jb - jd - (int) (30.6001 * je);
        month = je - 1;
        if (month > 12) {
            month = month - 12;
        }
        year = jc - 4715;
        if (month > 2) {
            year--;
        }
        if (year <= 0) {
            year--;
        }
        int[] calendarDate = {year, month, day};
        response = new DefaultCalendarDate(this, null, null, calendarDate);
        return response;
    }

    public Collection<CalendarEra> getBasis() {
        return basis;
    }

    public Clock getClock() {
        return timeBasis;
    }

    public void setBasis(Collection<CalendarEra> basis) {
        this.basis = basis;
    }

    public void setClock(Clock clock) {
        this.timeBasis = clock;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultCalendar && super.equals(object)) {
            final DefaultCalendar that = (DefaultCalendar) object;

            return Utilities.equals(this.basis, that.basis)
                    && Utilities.equals(this.timeBasis, that.timeBasis);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (this.timeBasis != null ? this.timeBasis.hashCode() : 0);
        hash = 37 * hash + (this.basis != null ? this.basis.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Calendar:").append('\n');
        if (timeBasis != null) {
            s.append("clock:").append(timeBasis).append('\n');
        }
        if (basis != null) {
            s.append("basis:").append(basis).append('\n');
        }
        return super.toString().concat("\n").concat(s.toString());
    }
}
