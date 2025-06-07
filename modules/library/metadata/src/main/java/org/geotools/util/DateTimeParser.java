/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Parses the {@code time} parameter of the request. The date, time and period are expected to be formatted according
 * ISO-8601 standard.
 *
 * @author Cedric Briancon
 * @author Martin Desruisseaux
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Jonathan Meyer, Applied Information Sciences, jon@gisjedi.com
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @version $Id$
 */
@SuppressWarnings({
    "AlreadyChecked",
    "MixedMutabilityReturnType",
    "MutablePublicArray",
    "StringCaseLocaleUsage",
    "UndefinedEquals",
    "ImmutableEnumChecker",
    "StringSplitter"
}) // Date/time parsing utility with intentional patterns and legacy API
public class DateTimeParser {
    static final Logger LOGGER = Logging.getLogger(DateTimeParser.class);

    private final Integer maxTimes;

    private final int flags;

    /** FLAG to return current time when the String to be parsed is "present" */
    public static final int FLAG_GET_TIME_ON_PRESENT = 1;

    /** FLAG to return current time when the String to be parsed is "now" */
    public static final int FLAG_GET_TIME_ON_NOW = 2;

    /** FLAG to return current time when the String to be parsed is "current" */
    public static final int FLAG_GET_TIME_ON_CURRENT = 4;

    /**
     * FLAG allowing Lenient ISO8601 format alternatives, for example, YYYYMMdd in addition to YYYY-MM-dd). See the
     * LENIENT_FORMATS_XXX Constants for a list of supported values
     */
    public static final int FLAG_IS_LENIENT = 256;

    /** FLAG to return a validity date range for dates with reduced precision */
    public static final int FLAG_SINGLE_DATE_AS_DATERANGE = 65536;

    private static final Set<String> CURRENT_TIME_NAMES = new HashSet<>(Arrays.asList("current", "now", "present"));

    private static final String SIMPLIFIED_FORMAT_MILLISECOND = "yyyyMMdd'T'HHmmssSSS";

    private static final String SIMPLIFIED_FORMAT_SECOND = "yyyyMMdd'T'HHmmss";

    private static final String SIMPLIFIED_FORMAT_MINUTE = "yyyyMMdd'T'HHmm";

    private static final String SIMPLIFIED_FORMAT_HOUR = "yyyyMMdd'T'HH";

    private static final String SIMPLIFIED_FORMAT_DAY = "yyyyMMdd";

    private static final String SIMPLIFIED_FORMAT_MONTH = "yyyyMM";

    private static final String SIMPLIFIED_FORMAT_YEAR = "yyyy";

    public static final String[] LENIENT_FORMATS_MILLISECOND = {
        "yyyyMMdd'T'HHmmssSSS'Z'",
        "yyyy-MM-dd'T'HHmmssSSS'Z'",
        "yyyy-MM-dd'T'HHmmssSSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyyMMdd'T'HH:mm:ss.SSS'Z'",
        "yyyyMMdd'T'HH:mm:ss.SSS",
        SIMPLIFIED_FORMAT_MILLISECOND
    };

    public static final String[] LENIENT_FORMATS_SECOND = {
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HHmmss'Z'",
        "yyyyMMdd'T'HH:mm:ss'Z'",
        "yyyyMMdd'T'HHmmss'Z'",
        "yyyyMMdd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HHmmss",
        SIMPLIFIED_FORMAT_SECOND
    };

    public static final String[] LENIENT_FORMATS_MINUTE = {
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd'T'HHmm'Z'",
        "yyyyMMdd'T'HH:mm'Z'",
        "yyyyMMdd'T'HHmm'Z'",
        "yyyy-MM-dd'T'HHmm",
        "yyyyMMdd'T'HH:mm",
        SIMPLIFIED_FORMAT_MINUTE
    };

    public static final String[] LENIENT_FORMATS_HOUR = {"yyyyMMdd'T'HH'Z'", "yyyy-MM-dd'T'HH", SIMPLIFIED_FORMAT_HOUR};

    public static final String[] LENIENT_FORMATS_DAY = {SIMPLIFIED_FORMAT_DAY};

    public static final String[] LENIENT_FORMATS_MONTH = {SIMPLIFIED_FORMAT_MONTH};

    public static final String[] LENIENT_FORMATS_YEAR = {}; // Intentionally empty

    private static final String ISO8601_CHARS_REGEX = "([^(yyyy)|^(MM)|^(dd)|^(HH)|^(mm)|^(ss)|^(SSS)|^('T')])|('Z')";

    public static enum FormatAndPrecision {
        MILLISECOND("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Calendar.MILLISECOND, LENIENT_FORMATS_MILLISECOND),
        SECOND("yyyy-MM-dd'T'HH:mm:ss'Z'", Calendar.SECOND, LENIENT_FORMATS_SECOND),
        MINUTE("yyyy-MM-dd'T'HH:mm'Z'", Calendar.MINUTE, LENIENT_FORMATS_MINUTE),
        HOUR("yyyy-MM-dd'T'HH'Z'", Calendar.HOUR_OF_DAY, LENIENT_FORMATS_HOUR),
        DAY("yyyy-MM-dd", Calendar.DAY_OF_MONTH, LENIENT_FORMATS_DAY),
        MONTH("yyyy-MM", Calendar.MONTH, LENIENT_FORMATS_MONTH),
        YEAR(SIMPLIFIED_FORMAT_YEAR, Calendar.YEAR, LENIENT_FORMATS_YEAR);

        public final String format;
        public final int precision;
        public final String[] lenientFormats;

        FormatAndPrecision(final String format, int precision, final String... lenientFormats) {
            this.format = format;
            this.precision = precision;
            this.lenientFormats = lenientFormats;
        }

        public SimpleDateFormat getFormat() {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ROOT);
            sdf.setTimeZone(UTC_TZ);
            return sdf;
        }

        public String[] getLenientFormats() {
            return lenientFormats;
        }

        public DateRange expand(Date d) {
            return expandToPrecision(d, this.precision);
        }

        public static DateRange expandFromCustomFormat(Date d, String format) {
            int precision = findPrecision(format);
            return expandToPrecision(d, precision);
        }

        private static DateRange expandToPrecision(Date d, int precision) {
            Calendar c = new GregorianCalendar(UTC_TZ);
            c.setTime(d);
            c.add(precision, 1);
            c.add(Calendar.MILLISECOND, -1);
            return new DateRange(d, c.getTime());
        }

        private static int findPrecision(String format) {
            // Converting the custom format to a simplified ISO8601
            String simplifiedFormat = format.replaceAll(ISO8601_CHARS_REGEX, "");
            if (SIMPLIFIED_FORMAT_MILLISECOND.equalsIgnoreCase(simplifiedFormat)) return Calendar.MILLISECOND;
            else if (SIMPLIFIED_FORMAT_SECOND.equalsIgnoreCase(simplifiedFormat)) return Calendar.SECOND;
            else if (SIMPLIFIED_FORMAT_MINUTE.equalsIgnoreCase(simplifiedFormat)) return Calendar.MINUTE;
            else if (SIMPLIFIED_FORMAT_HOUR.equalsIgnoreCase(simplifiedFormat)) return Calendar.HOUR;
            else if (SIMPLIFIED_FORMAT_DAY.equalsIgnoreCase(simplifiedFormat)) return Calendar.DAY_OF_MONTH;
            else if (SIMPLIFIED_FORMAT_MONTH.equalsIgnoreCase(simplifiedFormat)) return Calendar.MONTH;
            else if (SIMPLIFIED_FORMAT_YEAR.equalsIgnoreCase(simplifiedFormat)) return Calendar.YEAR;
            // No ISO8601 strict variant has been identified.
            // Try finding the precision through the ending part of the format
            else if (simplifiedFormat.endsWith("SSS")) return Calendar.MILLISECOND;
            else if (simplifiedFormat.endsWith("ss")) return Calendar.SECOND;
            else if (simplifiedFormat.endsWith("mm")) return Calendar.MINUTE;
            else if (simplifiedFormat.endsWith("HH")) return Calendar.HOUR;
            else if (simplifiedFormat.endsWith("dd")) return Calendar.DAY_OF_MONTH;
            else if (simplifiedFormat.endsWith("mm")) return Calendar.MONTH;
            else if (simplifiedFormat.endsWith("yyyy")) return Calendar.YEAR;
            throw new IllegalArgumentException(
                    "Unable to identify an ISO8601 format corresponding to the provided format:" + format);
        }
    }

    /** UTC timezone to serve as reference */
    public static final TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");

    /** Amount of milliseconds in a day. */
    public static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    /** Builds a default TimeParser with no provided maximum number of times */
    public DateTimeParser() {
        this(-1); // No limits on max results
    }

    /**
     * Parses times throwing an exception if the final list exceeds maxTimes
     *
     * @param maxTimes Maximum number of times to parse, or a non positive number to have no limit
     */
    public DateTimeParser(int maxTimes) {
        this(maxTimes, 0);
    }

    /**
     * Parses times throwing an exception if the final list exceeds maxTimes
     *
     * @param maxTimes Maximum number of times to parse, or a non positive number to have no limit
     * @param flags a combination (bitwise OR) of FLAG_XXX to customize the parsing.
     */
    public DateTimeParser(int maxTimes, int flags) {
        this.maxTimes = maxTimes;
        this.flags = flags;
    }

    private boolean isFlagSet(final int referenceFlag) {
        return ((flags & referenceFlag) == referenceFlag);
    }

    /**
     * Parses the date given in parameter. The date format should comply to ISO-8601 standard. The string may contains
     * either a single date, or a start time, end time and a period. In the first case, this method returns a singleton
     * containing only the parsed date. In the second case, this method returns a list including all dates from start
     * time up to the end time with the interval specified in the {@code value} string.
     *
     * @param value The date, time and period to parse.
     * @return A list of dates, or an empty list of the {@code value} string is null or empty.
     * @throws ParseException if the string can not be parsed.
     */
    @SuppressWarnings("unchecked")
    public Collection parse(String value) throws ParseException {
        if (value == null) {
            return Collections.emptyList();
        }
        value = value.trim();
        if (value.length() == 0) {
            return Collections.emptyList();
        }

        final Set result = new TreeSet<>((o1, o2) -> {
            final boolean o1Date = o1 instanceof Date;
            final boolean o2Date = o2 instanceof Date;

            if (o1 == o2) {
                return 0;
            }

            // o1 date
            if (o1Date) {
                final Date dateLeft = (Date) o1;
                if (o2Date) {
                    // o2 date
                    return dateLeft.compareTo((Date) o2);
                }
                // o2 daterange
                return dateLeft.compareTo(((DateRange) o2).getMinValue());
            }

            // o1 date range
            final DateRange left = (DateRange) o1;
            if (o2Date) {
                // o2 date
                return left.getMinValue().compareTo(((Date) o2));
            }
            // o2 daterange
            return left.getMinValue().compareTo(((DateRange) o2).getMinValue());
        });
        String[] listDates = value.split(",");
        int maxValues = maxTimes;
        for (String date : listDates) {
            // is it a date or a period?
            if (date.indexOf("/") <= 0) {
                Object o = getFuzzyDate(date);
                if (o instanceof Date) {
                    addDate(result, (Date) o);
                } else {
                    addPeriod(result, (DateRange) o);
                }
            } else {
                // period
                String[] period = date.split("/");

                //
                // Period like one of the following:
                // yyyy-MM-ddTHH:mm:ssZ/yyyy-MM-ddTHH:mm:ssZ/P1D
                // May be one of the following possible ISO 8601 Time Interval formats with trailing
                // period for
                // breaking the interval by given period:
                // TIME/TIME/PERIOD
                // DURATION/TIME/PERIOD
                // TIME/DURATION/PERIOD
                //
                if (period.length == 3) {
                    Date[] range = parseTimeDuration(period);

                    final long millisIncrement = parsePeriod(period[2]);
                    final long startTime = range[0].getTime();
                    final long endTime = range[1].getTime();
                    long time;
                    int j = 0;
                    while ((time = j * millisIncrement + startTime) <= endTime) {
                        final Calendar calendar = new GregorianCalendar(UTC_TZ);
                        calendar.setTimeInMillis(time);
                        if (!addDate(result, calendar.getTime()) && j >= maxValues) {
                            // prevent infinite loops
                            throw new RuntimeException(
                                    "Exceeded " + maxValues + " iterations parsing times, bailing out.");
                        }
                        j++;
                        checkMaxTimes(result, maxValues);
                    }
                }
                // Period like : yyyy-MM-ddTHH:mm:ssZ/yyyy-MM-ddTHH:mm:ssZ, it is an extension
                // of WMS that works with continuos period [Tb, Te].
                // May be one of the following possible ISO 8601 Time Interval formats, as in ECQL
                // Time Period:
                // TIME/DURATION
                // DURATION/TIME
                // TIME/TIME
                else if (period.length == 2) {
                    Date[] range = parseTimeDuration(period);
                    addPeriod(result, new DateRange(range[0], range[1]));
                } else {
                    throw new ParseException("Invalid time period: " + Arrays.toString(period), 0);
                }
            }
            checkMaxTimes(result, maxValues);
        }

        return new ArrayList<>(result);
    }

    public void checkMaxTimes(Set result, int maxValues) {
        // limiting number of elements we can create
        if (maxValues > 0 && result.size() > maxValues) {
            throw new RuntimeException("More than " + maxValues + " times specified in the request, bailing out.");
        }
    }

    private Date[] parseTimeDuration(final String... period) throws ParseException {
        Date[] range = null;

        if (period.length == 2 || period.length == 3) {
            Date begin = null;
            Date end = null;

            // Check first to see if we have any duration value within TIME parameter
            if (period[0].toUpperCase().startsWith("P")
                    || period[1].toUpperCase().startsWith("P")) {
                long durationOffset = Long.MIN_VALUE;

                // Attempt to parse a time or duration from the first portion of the
                if (period[0].toUpperCase().startsWith("P")) {
                    durationOffset = parsePeriod(period[0]);
                } else {
                    begin = beginning(getFuzzyDate(period[0]));
                }

                if (period[1].toUpperCase().startsWith("P")
                        && !period[1].toUpperCase().startsWith("PRESENT")) {
                    // Invalid time period of the format:
                    // DURATION/DURATION[/PERIOD]
                    if (durationOffset != Long.MIN_VALUE) {
                        throw new ParseException(
                                "Invalid time period containing duration with no paired time value: "
                                        + Arrays.toString(period),
                                0);
                    }
                    // Time period of the format:
                    // DURATION/TIME[/PERIOD]
                    else {
                        durationOffset = parsePeriod(period[1]);
                        final Calendar calendar = new GregorianCalendar();
                        calendar.setTimeInMillis(begin.getTime() + durationOffset);
                        end = calendar.getTime();
                    }
                }
                // Time period of the format:
                // TIME/DURATION[/PERIOD]
                else {
                    end = end(getFuzzyDate(period[1]));
                    final Calendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(end.getTime() - durationOffset);
                    begin = calendar.getTime();
                }
            }
            // Time period of the format:
            // TIME/TIME[/PERIOD]
            else {
                begin = beginning(getFuzzyDate(period[0]));
                end = end(getFuzzyDate(period[1]));
            }

            range = new Date[2];
            range[0] = begin;
            range[1] = end;
        }

        return range;
    }

    private static Date beginning(Object dateOrDateRange) {
        if (dateOrDateRange instanceof DateRange) {
            return ((DateRange) dateOrDateRange).getMinValue();
        } else {
            return (Date) dateOrDateRange;
        }
    }

    private static Date end(Object dateOrDateRange) {
        if (dateOrDateRange instanceof DateRange) {
            return ((DateRange) dateOrDateRange).getMaxValue();
        } else {
            return (Date) dateOrDateRange;
        }
    }

    /** Tries to avoid insertion of multiple time values. */
    private static void addPeriod(Collection<Object> result, DateRange newRange) {
        for (Iterator it = result.iterator(); it.hasNext(); ) {
            final Object element = it.next();
            if (element instanceof Date) {
                // convert
                final Date local = (Date) element;
                if (newRange.contains(local)) {
                    it.remove();
                }
            } else {
                // convert
                final DateRange local = (DateRange) element;
                if (local.contains(newRange)) return;
                if (newRange.contains(local)) it.remove();
            }
        }
        result.add(newRange);
    }

    private static boolean addDate(Collection<Object> result, Date newDate) {
        for (final Object element : result) {
            if (element instanceof Date) {
                if (newDate.equals(element)) return false;
            } else if (((DateRange) element).contains(newDate)) {
                return false;
            }
        }
        return result.add(newDate);
    }

    /**
     * Parses date given in parameter according the ISO-8601 standard. This parameter should follow a syntax defined in
     * the #FormatAndPrecision array to be validated. Lenient version are allowed too if the Lenient flag has been set
     *
     * @param value The date to parse.
     * @return A date found in the request.
     * @throws ParseException if the string can not be parsed.
     */
    private Object getFuzzyDate(final String value) throws ParseException {
        String computedValue = value;

        // special handling for 'current', 'now' and 'present' keyword (we accept both wms and wcs
        // ways)
        if (CURRENT_TIME_NAMES.contains(computedValue.toLowerCase())) {
            if ((computedValue.equalsIgnoreCase("current") && isFlagSet(FLAG_GET_TIME_ON_CURRENT))
                    || (computedValue.equalsIgnoreCase("now") && isFlagSet(FLAG_GET_TIME_ON_NOW))
                    || (computedValue.equalsIgnoreCase("present") && isFlagSet(FLAG_GET_TIME_ON_PRESENT))) {
                Calendar now = Calendar.getInstance();
                now.set(Calendar.MILLISECOND, 0);
                computedValue = FormatAndPrecision.MILLISECOND.getFormat().format(now.getTime());
            } else {
                return null;
            }
        }

        for (FormatAndPrecision f : FormatAndPrecision.values()) {
            Object parsedTime = parseTime(f, computedValue);
            if (parsedTime != null) {
                return parsedTime;
            }
        }

        throw new ParseException("Invalid date: " + value, 0);
    }

    /**
     * Parses the increment part of a period and returns it in milliseconds.
     *
     * @param period A string representation of the time increment according the ISO-8601:1988(E) standard. For example:
     *     {@code "P1D"} = one day.
     * @return The increment value converted in milliseconds.
     * @throws ParseException if the string can not be parsed.
     */
    public static long parsePeriod(final String period) throws ParseException {
        final int length = period.length();
        if (length != 0 && Character.toUpperCase(period.charAt(0)) != 'P') {
            throw new ParseException("Invalid period increment given: " + period, 0);
        }
        long millis = 0;
        boolean time = false;
        int lower = 0;
        while (++lower < length) {
            char letter = Character.toUpperCase(period.charAt(lower));
            if (letter == 'T') {
                time = true;
                if (++lower >= length) {
                    break;
                }
            }
            int upper = lower;
            letter = period.charAt(upper);
            while (!Character.isLetter(letter) || letter == 'e' || letter == 'E') {
                if (++upper >= length) {
                    throw new ParseException("Missing symbol in \"" + period + "\".", lower);
                }
                letter = period.charAt(upper);
            }
            letter = Character.toUpperCase(letter);
            final double value = Double.parseDouble(period.substring(lower, upper));
            final double factor;
            if (time) {
                switch (letter) {
                    case 'S':
                        factor = 1000;
                        break;
                    case 'M':
                        factor = 60 * 1000;
                        break;
                    case 'H':
                        factor = 60 * 60 * 1000;
                        break;
                    default:
                        throw new ParseException("Unknown time symbol: " + letter, upper);
                }
            } else {
                switch (letter) {
                    case 'D':
                        factor = MILLIS_IN_DAY;
                        break;
                    case 'W':
                        factor = 7 * MILLIS_IN_DAY;
                        break;
                        // TODO: handle months in a better way than just taking the average length.
                    case 'M':
                        factor = 30 * MILLIS_IN_DAY;
                        break;
                    case 'Y':
                        factor = 365.25 * MILLIS_IN_DAY;
                        break;
                    default:
                        throw new ParseException("Unknown period symbol: " + letter, upper);
                }
            }
            millis += Math.round(value * factor);
            lower = upper;
        }
        return millis;
    }

    private Object parseTime(FormatAndPrecision f, String value) {
        ParsePosition pos = new ParsePosition(0);
        Date time = f.getFormat().parse(value, pos);
        Object parsed = parseAsDateRangeWithFormatPrecision(time, value, pos, f);

        // Let's try with the lenient alternatives in case we didn't get a result yet
        if (parsed == null && isFlagSet(FLAG_IS_LENIENT)) {
            for (String lenientFormat : f.getLenientFormats()) {
                // rebuild formats at each parse since date formats are not thread safe
                final SimpleDateFormat format = new SimpleDateFormat(lenientFormat, Locale.ROOT);
                format.setTimeZone(UTC_TZ);
                // The lenientFormat is already somehow a lenient version of the
                // official format. no need to have it lenient too.
                format.setLenient(false);
                time = format.parse(value, pos);
                parsed = parseAsDateRangeWithFormatPrecision(time, value, pos, f);
                if (parsed != null) {
                    return parsed;
                }
            }
        }
        return parsed;
    }

    private Object parseAsDateRangeWithFormatPrecision(
            Date time, String computedValue, ParsePosition pos, FormatAndPrecision f) {
        if (time != null && pos != null && pos.getIndex() == computedValue.length()) {
            DateRange range = f.expand(time);
            if (range.getMinValue().equals(range.getMaxValue()) || !isFlagSet(FLAG_SINGLE_DATE_AS_DATERANGE)) {
                return range.getMinValue();
            } else {
                return range;
            }
        }
        return null;
    }
}
