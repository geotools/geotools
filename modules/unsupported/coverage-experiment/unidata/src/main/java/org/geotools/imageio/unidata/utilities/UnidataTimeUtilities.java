/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.unidata.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;




/**
 * @author User
 *
 */
public class UnidataTimeUtilities {
    
    /** The LOGGER for this class. */
    private static final Logger LOGGER = Logger.getLogger(UnidataTimeUtilities.class.toString());

    public static final int JGREG = 15 + 31 * (10 + 12 * 1582);
    
    public final static TimeZone UTC_TIMEZONE=TimeZone.getTimeZone("GMT");


    /**
     * 
     */
    private UnidataTimeUtilities() {
    }

    /**
     * @param origin
     */
    public static String checkDateDigits( String origin ) {
        String digitsCheckedOrigin = "";
        if (origin.indexOf("-") > 0) {
            String tmp = (origin.indexOf(" ") > 0 ? origin.substring(0, origin.indexOf(" ")) : origin);
            String[] originDateParts = tmp.split("-");
            for( int l = 0; l < originDateParts.length; l++ ) {
                String datePart = originDateParts[l];
                while( datePart.length() % 2 != 0 ) {
                    datePart = "0" + datePart;
                }
    
                digitsCheckedOrigin += datePart;
                digitsCheckedOrigin += (l < (originDateParts.length - 1) ? "-" : "");
            }
        }
    
        if (origin.indexOf(":") > 0) {
            digitsCheckedOrigin += " ";
            String tmp = (origin.indexOf(" ") > 0 ? origin.substring(origin.indexOf(" ") + 1) : origin);
            String[] originDateParts = tmp.split(":");
            for( int l = 0; l < originDateParts.length; l++ ) {
                String datePart = originDateParts[l];
                while( datePart.length() % 2 != 0 ) {
                    datePart = "0" + datePart;
                }
    
                digitsCheckedOrigin += datePart;
                digitsCheckedOrigin += (l < (originDateParts.length - 1) ? ":" : "");
            }
        }
    
        if (digitsCheckedOrigin.length() > 0)
            return digitsCheckedOrigin;
    
        return origin;
    }

    public static GregorianCalendar fromJulian( double injulian ) {
        int jalpha, ja, jb, jc, jd, je, year, month, day;
        // double julian = injulian + HALFSECOND / 86400.0;
        ja = (int) injulian;
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
        if (month > 12)
            month = month - 12;
        year = jc - 4715;
        if (month > 2)
            year--;
        if (year <= 0)
            year--;
    
        // Calendar Months are 0 based
        return new GregorianCalendar(year, month - 1, day);
    }

    /**
     * 
     */
    public static int getTimeSubUnitsValue( String units, Double vd ) {
        if ("days".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.HOUR) {
                double hours = vd * 24;
                return (int) hours;
            }
    
            if (subUnit == Calendar.MINUTE) {
                double hours = vd * 24 * 60;
                return (int) hours;
            }
    
            if (subUnit == Calendar.SECOND) {
                double hours = vd * 24 * 60 * 60;
                return (int) hours;
            }
    
            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }
    
            return 0;
        }
    
        if ("hours".equalsIgnoreCase(units) || "hour".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.MINUTE) {
                double hours = vd * 24 * 60;
                return (int) hours;
            }
    
            if (subUnit == Calendar.SECOND) {
                double hours = vd * 24 * 60 * 60;
                return (int) hours;
            }
    
            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }
    
            return 0;
        }
    
        if ("minutes".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.SECOND) {
                double hours = vd * 24 * 60 * 60;
                return (int) hours;
            }
    
            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }
    
            return 0;
        }
    
        if ("seconds".equalsIgnoreCase(units)) {
            int subUnit = getTimeUnits(units, vd);
            if (subUnit == Calendar.MILLISECOND) {
                double hours = vd * 24 * 60 * 60 * 1000;
                return (int) hours;
            }
    
            return 0;
        }
    
        return 0;
    }

    /**
     * Converts NetCDF time units into opportune Calendar ones.
     * 
     * @param units
     *                {@link String}
     * @param d
     * @return int
     */
    public static int getTimeUnits( String units, Double vd ) {
        if ("months".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                // if no day, it is the first day
                return 1;
            else {
                // TODO: FIXME
            }
        } else if ("days".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.DATE;
            else {
                double hours = vd * 24;
                if (hours - Math.floor(hours) == 0.0)
                    return Calendar.HOUR;
    
                double minutes = vd * 24 * 60;
                if (minutes - Math.floor(minutes) == 0.0)
                    return Calendar.MINUTE;
    
                double seconds = vd * 24 * 60 * 60;
                if (seconds - Math.floor(seconds) == 0.0)
                    return Calendar.SECOND;
    
                return Calendar.MILLISECOND;
            }
        }
        if ("hours".equalsIgnoreCase(units) || "hour".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.HOUR;
            else {
                double minutes = vd * 24 * 60;
                if (minutes - Math.floor(minutes) == 0.0)
                    return Calendar.MINUTE;
    
                double seconds = vd * 24 * 60 * 60;
                if (seconds - Math.floor(seconds) == 0.0)
                    return Calendar.SECOND;
    
                return Calendar.MILLISECOND;
            }
        }
        if ("minutes".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.MINUTE;
            else {
                double seconds = vd * 24 * 60 * 60;
                if (seconds - Math.floor(seconds) == 0.0)
                    return Calendar.SECOND;
    
                return Calendar.MILLISECOND;
            }
        }
        if ("seconds".equalsIgnoreCase(units)) {
            if (vd == null || vd == 0.0)
                return Calendar.SECOND;
            else {
                return Calendar.MILLISECOND;
            }
        }
    
        return -1;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static String trimFractionalPart(String value) {
        value = value.trim();
        for (int i = value.length(); --i >= 0;) {
            switch (value.charAt(i)) {
            case '0':
                continue;
            case '.':
                return value.substring(0, i);
            default:
                return value;
            }
        }
        return value;
    }

    /**
     * Add a quantity of time units to a Calendar 
     * @param cal Calendar to add to
     * @param unit Calendar unit to add
     * @param val Quantity of unit to add
     */
    public static void addTimeUnit(Calendar cal, int unit, int val) {
    	addTimeUnit(cal, unit, (long) val); 
    }

    /**
     * Add a quantity of time units to a Calendar 
     * @param cal Calendar to add to
     * @param unit Calendar unit to add
     * @param val Quantity of TimeUnit to add
     */
    public static void addTimeUnit(Calendar cal, int unit, long val) {
    	if (unit == Calendar.DATE) {
    		cal.setTimeInMillis(cal.getTimeInMillis() + TimeUnit.DAYS.toMillis(val));
    	} else if (unit == Calendar.HOUR) {
    		cal.setTimeInMillis(cal.getTimeInMillis() + TimeUnit.HOURS.toMillis(val));
    	} else if (unit == Calendar.MINUTE) {
    		cal.setTimeInMillis(cal.getTimeInMillis() + TimeUnit.MINUTES.toMillis(val));
    	} else if (unit == Calendar.SECOND) {
    		cal.setTimeInMillis(cal.getTimeInMillis() + TimeUnit.SECONDS.toMillis(val));
    	} else if (unit == Calendar.MILLISECOND) {
    		cal.setTimeInMillis(cal.getTimeInMillis() + val);
    	} else {
    		int intVal = (int) val;
    		if (intVal != val) {
    			throw new IllegalArgumentException("Can't convert " + val + " to an int without losing data");
    		}
    		cal.add(unit, intVal);
    	}
    }
}
