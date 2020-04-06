/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cql2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.factory.Hints;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.TEquals;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

/**
 * Filter Sample Factory
 *
 * <p>Provide samples of filters
 *
 * @author Mauricio Pazos - Axios Engineering
 * @author Gabriel Roldan - Axios Engineering
 * @since 2.5
 */
public class FilterCQLSample {

    private static final FilterFactory FACTORY = CommonFactoryFinder.getFilterFactory((Hints) null);

    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DATE_TIME_FORMATTER_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final Calendar CALENDAR = Calendar.getInstance();
    public static final String LESS_FILTER_SAMPLE = "ATTR1 < 1";
    public static final String LESS_EQ_FILTER_SAMPLE = "ATTR1 <= 1";
    public static final String NOT_EQ_FILTER = "ATTR1 <> 1";
    public static final String EQ_FILTER = "ATTR1 = 1";
    public static final String FILTER_WITH_PAREN_ROUNDTRIP_EXPR = "ATTR1 > ((1 + 2) / 3)";
    public static final String FILTER_WITH_NESTED_PAREN_EXPR = "ATTR1 < (1 + ((2 / 3) * 4))";
    public static final String FILTER_SIMPLE_EXPR = "ATTR1 > 1 + 2";
    public static final String FILTER_WITH_BRACKET_ROUNDTRIP_EXPR = "ATTR1 > [[1 + 2] / 3]";
    public static final String PROPERTY_IS_NULL = "ATTR1 IS NULL";
    public static final String PROPERTY_IS_NOT_NULL = "ATTR1 IS NOT NULL";
    private static final String FIRST_DATE = "2006-11-30T01:30:00Z";
    private static final String LAST_DATE = "2006-12-31T01:30:00Z";
    private static final String FIRST_DATE_MILLIS = "2006-11-30T01:30:00.123Z";
    private static final String LAST_DATE_MILLIS = "2006-12-31T01:30:00.456Z";
    public static final String FILTER_EQUAL_DATETIME = "ATTR1 TEQUALS " + FIRST_DATE;
    public static final String FILTER_BEFORE_DATE = "ATTR1 BEFORE " + FIRST_DATE;
    public static final String FILTER_BEFORE_DATE_MILLIS = "ATTR1 BEFORE " + FIRST_DATE_MILLIS;
    public static final String FILTER_BEFORE_PERIOD_BETWEEN_DATES =
            "ATTR1 BEFORE " + FIRST_DATE + "/" + LAST_DATE;
    public static final String FILTER_BEFORE_PERIOD_BETWEEN_DATES_MILLIS =
            "ATTR1 BEFORE " + FIRST_DATE_MILLIS + "/" + LAST_DATE_MILLIS;
    public static final String FILTER_BEFORE_PERIOD_DATE_AND_DAYS =
            "ATTR1 BEFORE  " + FIRST_DATE + "/" + "P30D";
    public static final String FILTER_BEFORE_PERIOD_DATE_AND_YEARS =
            "ATTR1 BEFORE " + FIRST_DATE + "/P1Y";
    public static final String FILTER_BEFORE_PERIOD_DATE_AND_MONTHS =
            "ATTR1 BEFORE " + FIRST_DATE + "/P12M";
    public static final String FILTER_AFTER_DATE = "ATTR1 AFTER " + LAST_DATE;
    public static final String FILTER_AFTER_PERIOD_BETWEEN_DATES =
            "ATTR1 AFTER " + FIRST_DATE + "/" + LAST_DATE;
    private static final String DURATION_DATE = "10";
    public static final String FILTER_AFTER_PERIOD_DATE_DAYS =
            "ATTR1 AFTER " + FIRST_DATE + "/P" + DURATION_DATE + "D";
    public static final String FILTER_AFTER_PERIOD_DATE_MONTH =
            "ATTR1 AFTER " + FIRST_DATE + "/P" + DURATION_DATE + "M";
    public static final String FILTER_AFTER_PERIOD_DATE_YEARS =
            "ATTR1 AFTER " + FIRST_DATE + "/P" + DURATION_DATE + "Y";
    public static final String FILTER_AFTER_PERIOD_DATE_YEARS_MONTH =
            "ATTR1 AFTER " + FIRST_DATE + "/P" + DURATION_DATE + "Y" + DURATION_DATE + "M";
    private static final String DURATION_TIME = "5";
    public static final String FILTER_AFTER_PERIOD_DATE_HOURS =
            "ATTR1 AFTER " + FIRST_DATE + "/T" + DURATION_TIME + "H";
    public static final String FILTER_AFTER_PERIOD_DATE_MINUTES =
            "ATTR1 AFTER " + FIRST_DATE + "/T" + DURATION_TIME + "M";
    public static final String FILTER_AFTER_PERIOD_DATE_SECONDS =
            "ATTR1 AFTER " + FIRST_DATE + "/T" + DURATION_TIME + "S";
    public static final String FILTER_AFTER_PERIOD_DATE_YMD_HMS =
            "ATTR1 AFTER "
                    + FIRST_DATE
                    + "/P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S";
    public static final String FILTER_DURING_PERIOD_BETWEEN_DATES =
            "ATTR1 DURING " + FIRST_DATE + "/" + LAST_DATE;
    public static final String FILTER_DURING_PERIOD_DATE_YMD_HMS =
            "ATTR1 DURING "
                    + FIRST_DATE
                    + "/P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S";
    public static final String FILTER_DURING_PERIOD_YMD_HMS_DATE =
            "ATTR1 DURING "
                    + "P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S"
                    + "/"
                    + LAST_DATE;
    public static final String FILTER_BEFORE_PERIOD_YMD_HMS_DATE =
            "ATTR1 BEFORE "
                    + "P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S"
                    + "/"
                    + LAST_DATE;
    public static final String FILTER_BEFORE_OR_DURING_PERIOD_BETWEEN_DATES =
            "ATTR1 BEFORE OR DURING " + FIRST_DATE + "/" + LAST_DATE;
    public static final String FILTER_BEFORE_OR_DURING_PERIOD_YMD_HMS_DATE =
            "ATTR1 BEFORE OR DURING "
                    + "P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S"
                    + "/"
                    + LAST_DATE;
    public static final String FILTER_BEFORE_OR_DURING_PERIOD_DATE_YMD_HMS =
            "ATTR1 BEFORE OR DURING "
                    + FIRST_DATE
                    + "/"
                    + "P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S";
    public static final String FILTER_DURING_OR_AFTER_PERIOD_BETWEEN_DATES =
            "ATTR1 DURING OR AFTER " + FIRST_DATE + "/" + LAST_DATE;
    public static final String FILTER_DURING_OR_AFTER_PERIOD_YMD_HMS_DATE =
            "ATTR1 DURING OR AFTER "
                    + "P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S"
                    + "/"
                    + LAST_DATE;
    public static final String FILTER_DURING_OR_AFTER_PERIOD_DATE_YMD_HMS =
            "ATTR1 DURING OR AFTER "
                    + FIRST_DATE
                    + "/"
                    + "P"
                    + DURATION_DATE
                    + "Y"
                    + DURATION_DATE
                    + "M"
                    + DURATION_DATE
                    + "D"
                    + "T"
                    + DURATION_TIME
                    + "H"
                    + DURATION_TIME
                    + "M"
                    + DURATION_TIME
                    + "S";
    public static final String ATTRIBUTE_NAME_EXISTS = "ATTR1 EXISTS";
    public static final String ATTRIBUTE_NAME_DOES_NOT_EXIST = "ATTR1 DOES-NOT-EXIST";
    public static final String FILTER_AND = "ATTR1 < 10 AND ATTR2 < 2";
    public static final String FILTER_OR = "ATTR1 > 10 OR ATTR2 < 2";
    public static final String FILTER_OR_AND = "ATTR1 < 10 AND ATTR2 < 2 OR ATTR3 > 10";
    public static final String FILTER_OR_AND_PARENTHESIS =
            "ATTR3 < 4 AND (ATTR1 > 10 OR ATTR2 < 2)";
    public static final String FILTER_AND_NOT_AND =
            "ATTR3 < 4 AND (NOT( ATTR1 < 10 AND ATTR2 < 2))";
    public static final String FILTER_AND_NOT_COMPARASION =
            "ATTR1 < 1 AND (NOT (ATTR2 < 2)) AND ATTR3 < 3";
    public static final String FILTER_WITH_FUNCTION_ABS = "ATTR1 < abs(10)";
    public static final String FILTER__WITH_FUNCTION_STR_CONCAT = "ATTR1 = strConcat(A, '1')";
    public static final String LIKE_FILTER = "ATTR1 LIKE 'abc%'";
    public static final String NOT_LIKE_FILTER = "ATTR1 NOT LIKE 'abc%'";
    public static final String BETWEEN_FILTER = "ATTR1 BETWEEN 10 AND 20";
    public static final String NOT_BETWEEN_FILTER = "ATTR1 NOT BETWEEN 10 AND 20";

    /** Catalog of samples */
    public static Map<String, Object> SAMPLES = new HashMap<String, Object>();

    static {
        // Samples initialization
        {
            Filter filter;

            // ---------------------------------------
            filter = FACTORY.less(FACTORY.property("ATTR1"), FACTORY.literal(1));
            SAMPLES.put(LESS_FILTER_SAMPLE, filter);

            // ---------------------------------------
            filter = FACTORY.lessOrEqual(FACTORY.property("ATTR1"), FACTORY.literal(1));
            SAMPLES.put(LESS_EQ_FILTER_SAMPLE, filter);

            // ---------------------------------------
            filter = FACTORY.not(FACTORY.equals(FACTORY.property("ATTR1"), FACTORY.literal(1)));
            SAMPLES.put(NOT_EQ_FILTER, filter);

            // ---------------------------------------
            filter = FACTORY.equals(FACTORY.property("ATTR1"), FACTORY.literal(1));
            SAMPLES.put(EQ_FILTER, filter);

            // ---------------------------------------
            filter =
                    FACTORY.greater(
                            FACTORY.property("ATTR1"),
                            FACTORY.divide(
                                    FACTORY.add(FACTORY.literal(1), FACTORY.literal(2)),
                                    FACTORY.literal(3)));
            SAMPLES.put(FILTER_WITH_PAREN_ROUNDTRIP_EXPR, filter);

            // ---------------------------------------
            filter =
                    FACTORY.greater(
                            FACTORY.property("ATTR1"),
                            FACTORY.divide(
                                    FACTORY.add(FACTORY.literal(1), FACTORY.literal(2)),
                                    FACTORY.literal(3)));
            SAMPLES.put(FILTER_WITH_BRACKET_ROUNDTRIP_EXPR, filter);

            // ---------------------------------------
            // ATTR1 < (1 + ((2 / 3) * 4))
            filter =
                    FACTORY.less(
                            FACTORY.property("ATTR1"),
                            FACTORY.add(
                                    FACTORY.literal(1),
                                    FACTORY.multiply(
                                            FACTORY.divide(FACTORY.literal(2), FACTORY.literal(3)),
                                            FACTORY.literal(4))));
            SAMPLES.put(FILTER_WITH_NESTED_PAREN_EXPR, filter);
            // ---------------------------------------
            filter =
                    FACTORY.greater(
                            FACTORY.property("ATTR1"),
                            FACTORY.add(FACTORY.literal(1), FACTORY.literal(2)));
            SAMPLES.put(FILTER_SIMPLE_EXPR, filter);
        }

        {
            // ---------------------------------------
            // ATTR1 IS NULL
            Filter nullFilter = FACTORY.isNull(FACTORY.property("ATTR1"));

            SAMPLES.put(PROPERTY_IS_NULL, nullFilter);

            // ---------------------------------------
            // ATTR1 IS NOT NULL
            Filter notNullFilter = FACTORY.not(FACTORY.isNull(FACTORY.property("ATTR1")));

            SAMPLES.put(PROPERTY_IS_NOT_NULL, notNullFilter);
        }
        {
            TEquals tEqualsFilter = null;

            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMATTER);
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dateTime = dateFormatter.parse(FIRST_DATE);
                tEqualsFilter =
                        FACTORY.tequals(FACTORY.property("ATTR1"), FACTORY.literal(dateTime));
            } catch (ParseException e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }

            // ATTR1 BEFORE 2006-12-31T01:30:00Z
            SAMPLES.put(FILTER_EQUAL_DATETIME, tEqualsFilter);
        }
        {
            // ---------------------------------------
            // Result filter for BEFORE tests
            Before beforeFilter = null;

            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMATTER);
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dateTime = dateFormatter.parse(FIRST_DATE);
                beforeFilter = FACTORY.before(FACTORY.property("ATTR1"), FACTORY.literal(dateTime));
            } catch (ParseException e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }

            // ATTR1 BEFORE 2006-12-31T01:30:00Z
            SAMPLES.put(FILTER_BEFORE_DATE, beforeFilter);

            // ATTR1 BEFORE 2006-11-30T01:30:00Z/2006-12-30T01:30:00Z
            SAMPLES.put(FILTER_BEFORE_PERIOD_BETWEEN_DATES, beforeFilter);

            // "ATTR1 BEFORE 2006-11-31T01:30:00Z/P30D"
            SAMPLES.put(FILTER_BEFORE_PERIOD_DATE_AND_DAYS, beforeFilter);

            // "ATTR1 BEFORE 2006-11-31T01:30:00Z/P1Y"
            SAMPLES.put(FILTER_BEFORE_PERIOD_DATE_AND_YEARS, beforeFilter);

            // "ATTR1 BEFORE 2006-11-31T01:30:00Z/P12M"
            SAMPLES.put(FILTER_BEFORE_PERIOD_DATE_AND_MONTHS, beforeFilter);

            {
                // create before with date and period
                try {
                    Date lastDate = strToDate(LAST_DATE);
                    Date firstDate = subtractDuration(lastDate, DURATION_DATE, DURATION_TIME);

                    Before before =
                            FACTORY.before(FACTORY.property("ATTR1"), FACTORY.literal(firstDate));

                    SAMPLES.put(FILTER_BEFORE_PERIOD_YMD_HMS_DATE, before);
                } catch (ParseException e) {
                    java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                }
            }
        }

        {
            // ---------------------------------------
            // Result filter for MILLIS tests
            Before beforeFilter = null;

            try {
                SimpleDateFormat dateFormatterWithMillis =
                        new SimpleDateFormat(DATE_TIME_FORMATTER_MILLIS);
                dateFormatterWithMillis.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dateTime = dateFormatterWithMillis.parse(FIRST_DATE_MILLIS);
                beforeFilter = FACTORY.before(FACTORY.property("ATTR1"), FACTORY.literal(dateTime));
            } catch (ParseException e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }

            // ATTR1 BEFORE 2006-12-31T01:30:00.123Z
            SAMPLES.put(FILTER_BEFORE_DATE_MILLIS, beforeFilter);

            // ATTR1 BEFORE 2006-11-30T01:30:00.123Z/2006-12-30T01:30:00.123Z
            SAMPLES.put(FILTER_BEFORE_PERIOD_BETWEEN_DATES_MILLIS, beforeFilter);
        }

        {
            // ---------------------------------------
            // Result filter for AFTER tests
            After afterFilter = null;

            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMATTER);
                dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dateTime = dateFormatter.parse(LAST_DATE);
                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(dateTime));
            } catch (ParseException e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }

            SAMPLES.put(FILTER_AFTER_DATE, afterFilter);

            SAMPLES.put(FILTER_AFTER_PERIOD_BETWEEN_DATES, afterFilter);

            // sample for period with date and duration
            try {

                Date firstDate = strToDate(FIRST_DATE);

                Date lastDate;
                // adds Days
                CALENDAR.setTime(firstDate);

                int days = Integer.parseInt(DURATION_DATE);
                CALENDAR.add(Calendar.DATE, days);
                lastDate = CALENDAR.getTime();

                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_DAYS, afterFilter);

                // adds Years
                int years = Integer.parseInt(DURATION_DATE);
                CALENDAR.setTime(firstDate);
                CALENDAR.add(Calendar.YEAR, years);
                lastDate = CALENDAR.getTime();
                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_YEARS, afterFilter);

                // adds Months
                CALENDAR.setTime(firstDate);

                int months = Integer.parseInt(DURATION_DATE);
                CALENDAR.add(Calendar.MONTH, months);
                lastDate = CALENDAR.getTime();
                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_MONTH, afterFilter);

                // years and months
                CALENDAR.setTime(firstDate);

                int duration = Integer.parseInt(DURATION_DATE);

                CALENDAR.add(Calendar.YEAR, duration);
                CALENDAR.add(Calendar.MONTH, duration);
                lastDate = CALENDAR.getTime();
                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_YEARS_MONTH, afterFilter);

                // date and duration hours
                CALENDAR.setTime(firstDate);

                int hours = Integer.parseInt(DURATION_TIME);
                CALENDAR.add(Calendar.HOUR, hours);
                lastDate = CALENDAR.getTime();

                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_HOURS, afterFilter);

                // date and duration minutes
                CALENDAR.setTime(firstDate);

                int minutes = Integer.parseInt(DURATION_TIME);
                CALENDAR.add(Calendar.MINUTE, minutes);
                lastDate = CALENDAR.getTime();

                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_MINUTES, afterFilter);

                // date and duration seconds
                CALENDAR.setTime(firstDate);

                int seconds = Integer.parseInt(DURATION_TIME);
                CALENDAR.add(Calendar.SECOND, seconds);
                lastDate = CALENDAR.getTime();

                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_SECONDS, afterFilter);

                // date and duration YMD_HMS
                lastDate = addDuration(firstDate, DURATION_DATE, DURATION_TIME);

                afterFilter = FACTORY.after(FACTORY.property("ATTR1"), FACTORY.literal(lastDate));

                SAMPLES.put(FILTER_AFTER_PERIOD_DATE_YMD_HMS, afterFilter);
            } catch (ParseException e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }
        }

        // ---------------------------------------
        // Result filter for DURING tests
        try {
            // During with period between dates
            Period period = createPeriod(FIRST_DATE, LAST_DATE);
            During duringFilter =
                    FACTORY.during(FACTORY.property("ATTR1"), FACTORY.literal(period));

            SAMPLES.put(FILTER_DURING_PERIOD_BETWEEN_DATES, duringFilter);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        try {
            // creates during with period between date and duration
            {
                final Date firstDate = strToDate(FIRST_DATE);

                // date and duration YMD_HMS
                final Date lastDate = addDuration(firstDate, DURATION_DATE, DURATION_TIME);

                // creates an And filter firstDate <= prop <= lastDate
                Period period = createPeriod(firstDate, lastDate);

                During duringFilter =
                        FACTORY.during(FACTORY.property("ATTR1"), FACTORY.literal(period));

                SAMPLES.put(FILTER_DURING_PERIOD_DATE_YMD_HMS, duringFilter);
            }

            {
                // create during with period during and date
                final Date lastDate = strToDate(LAST_DATE);

                Date firstDate = subtractDuration(lastDate, DURATION_DATE, DURATION_TIME);

                Period period = createPeriod(firstDate, lastDate);

                During duringFilter =
                        FACTORY.during(FACTORY.property("ATTR1"), FACTORY.literal(period));

                SAMPLES.put(FILTER_DURING_PERIOD_YMD_HMS_DATE, duringFilter);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // ------------------------------------------------
        // result filter for Before or During test
        try {

            final Date firstDate = strToDate(FIRST_DATE);
            final Date lastDate = strToDate(LAST_DATE);

            final PropertyName property = FACTORY.property("ATTR1");

            Or filter = buildBeforeOrDuringFilter(property, firstDate, lastDate);

            SAMPLES.put(FILTER_BEFORE_OR_DURING_PERIOD_BETWEEN_DATES, filter);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        try {
            // Period: duration / date

            final Date lastDate = strToDate(LAST_DATE);
            final Date firstDate = subtractDuration(lastDate, DURATION_DATE, DURATION_TIME);

            final PropertyName property = FACTORY.property("ATTR1");

            Or filter = buildBeforeOrDuringFilter(property, firstDate, lastDate);

            SAMPLES.put(FILTER_BEFORE_OR_DURING_PERIOD_YMD_HMS_DATE, filter);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        try {
            // Period: date / duration
            final Date firstDate = strToDate(FIRST_DATE);

            Date lastDate = addDuration(firstDate, DURATION_DATE, DURATION_TIME);

            final PropertyName property = FACTORY.property("ATTR1");

            Or filter = buildBeforeOrDuringFilter(property, firstDate, lastDate);

            SAMPLES.put(FILTER_BEFORE_OR_DURING_PERIOD_DATE_YMD_HMS, filter);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // ---------------------------------------
        // DURING OR AFTER samples
        try {
            // period: first date / last date
            final PropertyName property = FACTORY.property("ATTR1");

            Period period = createPeriod(FIRST_DATE, LAST_DATE);

            Or filter = buildDuringOrAfterFilter(property, period);

            SAMPLES.put(FILTER_DURING_OR_AFTER_PERIOD_BETWEEN_DATES, filter);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        try {
            // period: duration / last date
            final Date lastDate = strToDate(LAST_DATE);

            final Date firstDate = subtractDuration(lastDate, DURATION_DATE, DURATION_TIME);
            final Period period = createPeriod(firstDate, lastDate);

            final PropertyName property = FACTORY.property("ATTR1");

            Or filter = buildDuringOrAfterFilter(property, period);

            SAMPLES.put(FILTER_DURING_OR_AFTER_PERIOD_YMD_HMS_DATE, filter);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        try {
            // period: first date / duration
            final Date firstDate = strToDate(FIRST_DATE);
            Date lastDate = addDuration(firstDate, DURATION_DATE, DURATION_TIME);
            final PropertyName property = FACTORY.property("ATTR1");

            Period period = createPeriod(firstDate, lastDate);
            Or filter = buildDuringOrAfterFilter(property, period);
            SAMPLES.put(FILTER_DURING_OR_AFTER_PERIOD_DATE_YMD_HMS, filter);

        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // ---------------------------------------
        // Existence predicate samples
        try {
            // EXISTS sample
            Expression[] args = new Expression[1];
            args[0] = FACTORY.literal("ATTR1");

            Function function = FACTORY.function("PropertyExists", args);
            Literal literalTrue = FACTORY.literal(Boolean.TRUE);

            PropertyIsEqualTo propExists = FACTORY.equals(function, literalTrue);

            SAMPLES.put(ATTRIBUTE_NAME_EXISTS, propExists);

            // DOES NOT EXIST sample
            Not notEq = FACTORY.not(propExists);
            SAMPLES.put(ATTRIBUTE_NAME_DOES_NOT_EXIST, notEq);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // ---------------------------------------
        // boolean value expression
        try {
            PropertyName attr1 = FACTORY.property("ATTR1");
            PropertyName attr2 = FACTORY.property("ATTR2");
            PropertyName attr3 = FACTORY.property("ATTR3");

            // "ATT1 < 10 AND ATT2 < 2";
            And andFilter =
                    FACTORY.and(
                            FACTORY.less(attr1, FACTORY.literal(10)),
                            FACTORY.less(attr2, FACTORY.literal(2)));

            SAMPLES.put(FILTER_AND, andFilter);

            // "ATT1 > 10 OR ATT2 < 2";
            Or orFilter =
                    FACTORY.or(
                            FACTORY.greater(attr1, FACTORY.literal(10)),
                            FACTORY.less(attr2, FACTORY.literal(2)));

            SAMPLES.put(FILTER_OR, orFilter);

            // ATTR1 < 10 AND ATTR2 < 2 OR ATTR3 > 10
            Or orAndFilter = FACTORY.or(andFilter, FACTORY.greater(attr3, FACTORY.literal(10)));

            SAMPLES.put(FILTER_OR_AND, orAndFilter);

            // ATTR3 < 4 AND (ATTR1 > 10 OR ATTR2 < 2)
            And parenthesisFilter = FACTORY.and(FACTORY.less(attr3, FACTORY.literal(4)), orFilter);

            SAMPLES.put(FILTER_OR_AND_PARENTHESIS, parenthesisFilter);

            // ATTR3 < 4 AND (NOT( ATTR1 < 10 AND ATTR2 < 2))
            And andNotAnd =
                    FACTORY.and(
                            FACTORY.less(attr3, FACTORY.literal(4)),
                            FACTORY.not(
                                    FACTORY.and(
                                            FACTORY.less(attr1, FACTORY.literal(10)),
                                            FACTORY.less(attr2, FACTORY.literal(2)))));

            SAMPLES.put(FILTER_AND_NOT_AND, andNotAnd);

            // ATTR1 < 1 AND (NOT (ATTR2 < 2)) AND ATTR3 < 3
            And andNotComparasion =
                    FACTORY.and(
                            FACTORY.and(
                                    FACTORY.less(attr1, FACTORY.literal(1)),
                                    FACTORY.not(FACTORY.less(attr2, FACTORY.literal(2)))),
                            FACTORY.less(attr3, FACTORY.literal(3)));

            SAMPLES.put(FILTER_AND_NOT_COMPARASION, andNotComparasion);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // Unary Expression sampel
        try {
            // User defined Function Sample
            Expression[] absArgs = new Expression[1];
            absArgs[0] = FACTORY.literal(10);
            Function abs = FACTORY.function("abs", absArgs);

            PropertyIsLessThan lessFilter = FACTORY.less(FACTORY.property("ATTR1"), abs);

            SAMPLES.put(FILTER_WITH_FUNCTION_ABS, lessFilter);

            // builds ATTR1 = strConcat(A, '1')
            Expression[] strConcatArgs = new Expression[2];
            strConcatArgs[0] = FACTORY.literal("A");
            strConcatArgs[1] = FACTORY.literal("1");
            Function strConcat = FACTORY.function("strConcat", strConcatArgs);

            PropertyIsEqualTo eqFilter = FACTORY.equals(FACTORY.property("ATTR1"), strConcat);

            SAMPLES.put(FILTER__WITH_FUNCTION_STR_CONCAT, eqFilter);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // Like filter
        try {
            final String WC_MULTI = "%";
            final String WC_SINGLE = "_";
            final String ESCAPE = "\\";

            PropertyName property = FACTORY.property("ATTR1");

            PropertyIsLike likeFilter =
                    FACTORY.like(property, "abc" + WC_MULTI, WC_MULTI, WC_SINGLE, ESCAPE);

            SAMPLES.put(LIKE_FILTER, likeFilter);

            Not notLikeFileter = FACTORY.not(likeFilter);
            SAMPLES.put(NOT_LIKE_FILTER, notLikeFileter);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        // bettween filter
        try {
            PropertyName property = FACTORY.property("ATTR1");
            Literal inf = FACTORY.literal(10);
            Literal sup = FACTORY.literal(20);

            PropertyIsBetween betweenFilter = FACTORY.between(property, inf, sup);

            SAMPLES.put(BETWEEN_FILTER, betweenFilter);

            Not notBetweenFileter = FACTORY.not(betweenFilter);
            SAMPLES.put(NOT_BETWEEN_FILTER, notBetweenFileter);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    } // end static initialization

    private static Or buildDuringOrAfterFilter(final PropertyName property, Period period)
            throws ParseException {
        During during = FACTORY.during(property, FACTORY.literal(period));

        final Date lastDate = period.getEnding().getPosition().getDate();

        After after = FACTORY.after(property, FACTORY.literal(lastDate));
        Or filter = FACTORY.or(during, after);
        return filter;
    }

    protected FilterCQLSample() {
        // factory class
    }

    private static Or buildBeforeOrDuringFilter(
            final PropertyName property, final Date firstDate, final Date lastDate) {
        Before before = FACTORY.before(property, FACTORY.literal(firstDate));

        Period period = createPeriod(firstDate, lastDate);
        During during = FACTORY.during(property, FACTORY.literal(period));
        Or filter = FACTORY.or(before, during);
        return filter;
    }

    private static Date strToDate(String firstDate) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMATTER);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatter.parse(firstDate);
    }

    private static Period createPeriod(String firstDate, String lastDate) throws ParseException {

        return new DefaultPeriod(dateToInstant(firstDate), dateToInstant(lastDate));
    }

    private static Period createPeriod(Date firstDate, Date lastDate) {
        Instant i1 = new DefaultInstant(new DefaultPosition(firstDate));
        Instant i2 = new DefaultInstant(new DefaultPosition(lastDate));
        return new DefaultPeriod(i1, i2);
    }

    /** Add duration to date */
    private static Date addDuration(
            final Date date, final String durationDate, final String durationTime) {
        CALENDAR.setTime(date);

        int durDate = Integer.parseInt(durationDate);
        CALENDAR.add(Calendar.YEAR, durDate);
        CALENDAR.add(Calendar.MONTH, durDate);
        CALENDAR.add(Calendar.DATE, durDate);

        int durTime = Integer.parseInt(durationTime);
        CALENDAR.add(Calendar.HOUR, durTime);
        CALENDAR.add(Calendar.MINUTE, durTime);
        CALENDAR.add(Calendar.SECOND, durTime);

        Date lastDate = CALENDAR.getTime();

        return lastDate;
    }

    /**
     * Subtract duration to date
     *
     * @param lastDate a Date
     * @return Date
     */
    private static Date subtractDuration(
            final Date lastDate, final String durationDate, final String durationTime) {
        CALENDAR.setTime(lastDate);

        int durDate = -1 * Integer.parseInt(durationDate);
        CALENDAR.add(Calendar.YEAR, durDate);
        CALENDAR.add(Calendar.MONTH, durDate);
        CALENDAR.add(Calendar.DATE, durDate);

        int durTime = -1 * Integer.parseInt(durationTime);
        CALENDAR.add(Calendar.HOUR, durTime);
        CALENDAR.add(Calendar.MINUTE, durTime);
        CALENDAR.add(Calendar.SECOND, durTime);

        Date firstDate = CALENDAR.getTime();

        return firstDate;
    }

    public static Filter getSample(final String sampleRequested) {
        Filter sample = (Filter) SAMPLES.get(sampleRequested);

        assert (sample != null) : "There is not a sample for " + sampleRequested;

        return sample;
    }

    private static Instant dateToInstant(String strDate) throws ParseException {

        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMATTER);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date date;
        try {
            date = dateFormatter.parse(strDate);
        } catch (ParseException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        }

        Instant instant = new DefaultInstant(new DefaultPosition(date));

        return instant;
    }
}
