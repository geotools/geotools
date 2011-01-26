/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.commons;

import java.util.Calendar;
import java.util.Date;


/**
 * Duration utility class
 * <p>
 * Maintains convenient methods to manipulate the duration information.
 * </p>
 * 
 * @since 2.5
 * 
 * @author Mauricio Pazos - Axios Engineering
 * @author Gabriel Roldan - Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/library/cql/src/main/java/org/geotools/text/filter/cql2/DurationUtil.java $
 */
final class DurationUtil {
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final int YEARS = 0;
    private static final int MONTHS = 1;
    private static final int DAYS = 2;

    private static final int HOURS = 0;
    private static final int MINUTES = 1;
    private static final int SECONDS = 2;

    /** H,M,S */
    private static int[] DURATION_TIME = new int[3];

    private DurationUtil() {
        // utility class
    }

    /**
     * Extract from duration string the values of years, month and days
     *
     * @param duration
     * @return int[3] with years,months,days, if some value are not present -1
     *         will be returned.
     */
    private static int[] extractDurationDate(final String duration) {
        // initializes duration date container
        /** Y,M,D */
        int[] durationDate = new int[3];
        
        for (int i = 0; i < durationDate.length; i++) {
            durationDate[i] = -1;
        }

        // if has not duration date return array with -1 values
        int cursor = duration.indexOf("P");

        if (cursor == -1) {
            return durationDate;
        }

        // extracts duration date and set duration array
        cursor++;

        // years
        int endYears = duration.indexOf("Y", cursor);

        if (endYears >= 0) {
            String strYears = duration.substring(cursor, endYears);
            int years = Integer.parseInt(strYears);
            durationDate[YEARS] = years;

            cursor = endYears + 1;
        }

        // months
        int endMonths = duration.indexOf("M", cursor);

        if (endMonths >= 0) {
            String strMonths = duration.substring(cursor, endMonths);
            int months = Integer.parseInt(strMonths);
            durationDate[MONTHS] = months;

            cursor = endMonths + 1;
        }

        // days
        int endDays = duration.indexOf("D", cursor);

        if (endDays >= 0) {
            String strDays = duration.substring(cursor, endDays);
            int days = Integer.parseInt(strDays);
            durationDate[DAYS] = days;
        }

        return durationDate;
    }

    /**
     * Extract from duration string the values of hours, minutes and seconds
     *
     * @param duration
     * @return int[3] with hours, minutes and seconds if some value are not
     *         present -1 will be returned.
     */
    private static int[] extractDurationTime(final String duration) {
        for (int i = 0; i < DURATION_TIME.length; i++) {
            DURATION_TIME[i] = -1;
        }

        int cursor = duration.indexOf("T");

        if (cursor == -1) {
            return DURATION_TIME;
        }

        cursor++;

        // hours
        int endHours = duration.indexOf("H", cursor);

        if (endHours >= 0) {
            String strHours = duration.substring(cursor, endHours);
            int hours = Integer.parseInt(strHours);
            DURATION_TIME[HOURS] = hours;

            cursor = endHours + 1;
        }

        // minute
        int endMinutes = duration.indexOf("M", cursor);

        if (endMinutes >= 0) {
            String strMinutes = duration.substring(cursor, endMinutes);
            int minutes = Integer.parseInt(strMinutes);
            DURATION_TIME[MINUTES] = minutes;

            cursor = endMinutes + 1;
        }

        // seconds
        int endSeconds = duration.indexOf("S", cursor);

        if (endSeconds >= 0) {
            String strSeconds = duration.substring(cursor, endSeconds);
            int seconds = Integer.parseInt(strSeconds);
            DURATION_TIME[SECONDS] = seconds;
        }

        return DURATION_TIME;
    }

    /**
     * Add duration to date
     *
     * @param date
     *            a Date
     * @param duration
     *            a String formated like "P##Y##M##D"
     *
     * @return a Date
     *
     */
    public static Date addDurationToDate(final Date date, final String duration)
        throws NumberFormatException {
        final int positive = 1;

        Date computedDate = null;

        computedDate = computeDateFromDurationDate(date, duration, positive);

        computedDate = computeDateFromDurationTime(computedDate, duration, positive);

        return computedDate;
    }

    /**
     * Adds years, month and days (duration) to initial date.
     *
     * @param date
     *            initial date
     * @param duration
     *            a String with format: PddYddMddD
     * @return Date a computed date. if duration have not got duration "P"
     *         return date value.
     *
     */
    private static Date computeDateFromDurationDate(final Date date, final String duration, int sign) {

        int[] durationDate = new int[3];
        durationDate = extractDurationDate(duration);

        if (isNull(durationDate)) {
            return date;
        }

        CALENDAR.setTime(date);

        // years
        if (durationDate[YEARS] >= 0) {
            CALENDAR.add(Calendar.YEAR, sign * durationDate[YEARS]);
        }

        // months
        if (durationDate[MONTHS] >= 0) {
            CALENDAR.add(Calendar.MONTH, sign * durationDate[MONTHS]);
        }

        // days
        if (durationDate[DAYS] >= 0) {
            CALENDAR.add(Calendar.DATE, sign * durationDate[DAYS]);
        }

        Date lastDate = CALENDAR.getTime();

        return lastDate;
    }

    /**
     * durDate is null if all his values are -1
     *
     * @param durDate
     * @return true if has some greater than or equal 0
     */
    private static boolean isNull(int[] durDate) {
        for (int i = 0; i < durDate.length; i++) {
            if (durDate[i] >= 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Add or subtract time duration to initial date.
     *
     * @param date
     *            initial date
     * @param duration
     *            a String with format: TddHddMddS
     * @param sign
     *            1 or -1 (add or subract)
     * @return Date a computed date. if duration have not got duration "T"
     *         return date value.
     */
    private static Date computeDateFromDurationTime(final Date date, final String duration,
        final int sign) {
        DURATION_TIME = extractDurationTime(duration);

        if (isNull(DURATION_TIME)) {
            return date;
        }

        CALENDAR.setTime(date);

        // hours
        if (DURATION_TIME[HOURS] >= 0) {
            CALENDAR.add(Calendar.HOUR, sign * DURATION_TIME[HOURS]);
        }

        // minute
        if (DURATION_TIME[MINUTES] >= 0) {
            CALENDAR.add(Calendar.MINUTE, sign * DURATION_TIME[MINUTES]);
        }

        // seconds
        if (DURATION_TIME[SECONDS] >= 0) {
            CALENDAR.add(Calendar.SECOND, sign * DURATION_TIME[SECONDS]);
        }

        Date lastDate = CALENDAR.getTime();

        return lastDate;
    }

    /**
     * Subtracts duration to date
     *
     * @param date
     *            a Date
     * @param duration
     *            a String formated like "P##Y##M##D"
     *
     * @return a Date
     */
    public static Date subtractDurationToDate(Date date, String duration) {
        final int negative = -1;

        Date computedDate = null;

        computedDate = computeDateFromDurationDate(date, duration, negative);

        computedDate = computeDateFromDurationTime(computedDate, duration, negative);

        return computedDate;
    }
}
