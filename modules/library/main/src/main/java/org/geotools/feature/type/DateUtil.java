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
/*
  Copyright (c) 2002-2004, Dennis M. Sosnoski.
  All rights reserved.
  Redistribution and use in source and binary forms, with or without modification,
  are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright notice, this
     list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice,
     this list of conditions and the following disclaimer in the documentation
     and/or other materials provided with the distribution.
* Neither the name of JiBX nor the names of its contributors may be used
     to endorse or promote products derived from this software without specific
     prior written permission.
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.geotools.feature.type;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Utility class supplying static methods. Date serialization is based on the algorithms published by Peter Baum
 * (http://www.capecod.net/~pbaum). All date handling is done according to the W3C Schema specification, which uses a
 * proleptic Gregorian calendar with no year 0. Note that this differs from the Java date handling, which uses a
 * discontinuous Gregorian calendar.
 *
 * @author Dennis M. Sosnoski
 * @version 1.0
 */
public abstract class DateUtil {
    /** Number of milliseconds in a minute. */
    private static final int MSPERMINUTE = 60000;

    /** Number of milliseconds in an hour. */
    private static final int MSPERHOUR = MSPERMINUTE * 60;

    /** Number of milliseconds in a day. */
    private static final int MSPERDAY = MSPERHOUR * 24;

    /** Number of milliseconds in a day as a long. */
    private static final long LMSPERDAY = MSPERDAY;

    /** Number of milliseconds in a (non-leap) year. */
    private static final long MSPERYEAR = LMSPERDAY * 365;

    /** Average number of milliseconds in a year within century. */
    private static final long MSPERAVGYEAR = (long) (MSPERDAY * 365.25);

    /** Number of milliseconds in a normal century. */
    private static final long MSPERCENTURY = (long) (MSPERDAY * 36524.25);

    /**
     * Millisecond value of base time for internal representation. This gives the bias relative to January 1 of the year
     * 1 C.E.
     */
    private static final long TIME_BASE = (1969 * MSPERYEAR) + (((1969 / 4) - 19 + 4) * LMSPERDAY);

    /** Day number for start of month in non-leap year. */
    private static final int[] MONTHS_NONLEAP = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    /** Day number for start of month in leap year. */
    private static final int[] MONTHS_LEAP = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366};

    /** Millisecond count prior to start of month in March 1-biased year. */
    private static final long[] BIAS_MONTHMS = {
        0 * LMSPERDAY, 0 * LMSPERDAY, 0 * LMSPERDAY, 0 * LMSPERDAY,
        31 * LMSPERDAY, 61 * LMSPERDAY, 92 * LMSPERDAY, 122 * LMSPERDAY,
        153 * LMSPERDAY, 184 * LMSPERDAY, 214 * LMSPERDAY, 245 * LMSPERDAY,
        275 * LMSPERDAY, 306 * LMSPERDAY, 337 * LMSPERDAY
    };

    /** Pad character for base64 encoding. */
    private static final char PAD_CHAR = '=';

    /** Characters used in base64 encoding. */
    private static final char[] s_base64Chars = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
        'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /** Values corresponding to characters used in bas64 encoding. */
    private static final byte[] s_base64Values = new byte[128];

    static {
        for (int i = 0; i < s_base64Values.length; i++) {
            s_base64Values[i] = -1;
        }

        s_base64Values[PAD_CHAR] = 0;

        for (int i = 0; i < s_base64Chars.length; i++) {
            s_base64Values[s_base64Chars[i]] = (byte) i;
        }
    }

    /**
     * Parse digits in text as integer value. This internal method is used for number values embedded within lexical
     * structures. Only decimal digits can be included in the text range parsed.
     *
     * @param text text to be parsed
     * @param offset starting offset in text
     * @param length number of digits to be parsed
     * @return converted positive integer value
     * @throws IllegalArgumentException on parse error
     */
    private static int parseDigits(String text, int offset, int length) throws IllegalArgumentException {
        // check if overflow a potential problem
        int value = 0;

        if (length > 9) {
            // use library parse code for potential overflow
            try {
                value = Integer.parseInt(text.substring(offset, offset + length));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        } else {
            // parse with no overflow worries
            int limit = offset + length;

            while (offset < limit) {
                char chr = text.charAt(offset++);

                if ((chr >= '0') && (chr <= '9')) {
                    value = (value * 10) + (chr - '0');
                } else {
                    throw new IllegalArgumentException("Non-digit in number value");
                }
            }
        }

        return value;
    }

    /**
     * Parse integer value from text. Integer values are parsed with optional leading sign flag, followed by any number
     * of digits.
     *
     * @param text text to be parsed
     * @return converted integer value
     * @throws IllegalArgumentException on parse error
     */
    public static int parseInt(String text) throws IllegalArgumentException {
        // make sure there's text to be processed
        text = text.trim();

        int offset = 0;
        int limit = text.length();

        if (limit == 0) {
            throw new IllegalArgumentException("Empty number value");
        }

        // check leading sign present in text
        boolean negate = false;
        char chr = text.charAt(0);

        if (chr == '-') {
            if (limit > 9) {
                // special case to make sure maximum negative value handled
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException(ex.getMessage());
                }
            } else {
                negate = true;
                offset++;
            }
        } else if (chr == '+') {
            offset++;
        }

        if (offset >= limit) {
            throw new IllegalArgumentException("Invalid number format");
        }

        // handle actual value conversion
        int value = parseDigits(text, offset, limit - offset);

        if (negate) {
            return -value;
        } else {
            return value;
        }
    }

    /**
     * Serialize int value to text.
     *
     * @param value int value to be serialized
     * @return text representation of value
     */
    public static String serializeInt(int value) {
        return Integer.toString(value);
    }

    /**
     * Parse long value from text. Long values are parsed with optional leading sign flag, followed by any number of
     * digits.
     *
     * @param text text to be parsed
     * @return converted long value
     * @throws IllegalArgumentException on parse error
     */
    public static long parseLong(String text) throws IllegalArgumentException {
        // make sure there's text to be processed
        text = text.trim();

        int offset = 0;
        int limit = text.length();

        if (limit == 0) {
            throw new IllegalArgumentException("Empty number value");
        }

        // check leading sign present in text
        boolean negate = false;
        char chr = text.charAt(0);

        if (chr == '-') {
            negate = true;
            offset++;
        } else if (chr == '+') {
            offset++;
        }

        if (offset >= limit) {
            throw new IllegalArgumentException("Invalid number format");
        }

        // check if overflow a potential problem
        long value = 0;

        if ((limit - offset) > 18) {
            // pass text to library parse code (less leading +)
            if (chr == '+') {
                text = text.substring(1);
            }

            try {
                value = Long.parseLong(text);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        } else {
            // parse with no overflow worries
            while (offset < limit) {
                chr = text.charAt(offset++);

                if ((chr >= '0') && (chr <= '9')) {
                    value = (value * 10) + (chr - '0');
                } else {
                    throw new IllegalArgumentException("Non-digit in number value");
                }
            }

            if (negate) {
                value = -value;
            }
        }

        return value;
    }

    /**
     * Serialize long value to text.
     *
     * @param value long value to be serialized
     * @return text representation of value
     */
    public static String serializeLong(long value) {
        return Long.toString(value);
    }

    /**
     * Parse short value from text. Short values are parsed with optional leading sign flag, followed by any number of
     * digits.
     *
     * @param text text to be parsed
     * @return converted short value
     * @throws IllegalArgumentException on parse error
     */
    public static short parseShort(String text) throws IllegalArgumentException {
        int value = parseInt(text);

        if ((value < Short.MIN_VALUE) || (value > Short.MAX_VALUE)) {
            throw new IllegalArgumentException("Value out of range");
        }

        return (short) value;
    }

    /**
     * Serialize short value to text.
     *
     * @param value short value to be serialized
     * @return text representation of value
     */
    public static String serializeShort(short value) {
        return Short.toString(value);
    }

    /**
     * Parse byte value from text. Byte values are parsed with optional leading sign flag, followed by any number of
     * digits.
     *
     * @param text text to be parsed
     * @return converted byte value
     * @throws IllegalArgumentException on parse error
     */
    public static byte parseByte(String text) throws IllegalArgumentException {
        int value = parseInt(text);

        if ((value < Byte.MIN_VALUE) || (value > Byte.MAX_VALUE)) {
            throw new IllegalArgumentException("Value out of range");
        }

        return (byte) value;
    }

    /**
     * Serialize byte value to text.
     *
     * @param value byte value to be serialized
     * @return text representation of value
     */
    public static String serializeByte(byte value) {
        return Byte.toString(value);
    }

    /**
     * Parse boolean value from text. Boolean values are parsed as either text "true" and "false", or "1" and "0"
     * numeric equivalents.
     *
     * @param text text to be parsed
     * @return converted boolean value
     * @throws IllegalArgumentException on parse error
     */
    public static boolean parseBoolean(String text) throws IllegalArgumentException {
        text = text.trim();

        if ("true".equals(text) || "1".equals(text)) {
            return true;
        } else if ("false".equals(text) || "0".equals(text)) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid boolean value");
        }
    }

    /**
     * Serialize boolean value to text. This serializes the value using the text representation as "true" or "false".
     *
     * @param value boolean value to be serialized
     * @return text representation of value
     */
    public static String serializeBoolean(boolean value) {
        return value ? "true" : "false";
    }

    /**
     * Parse char value from text as unsigned 16-bit integer. Char values are parsed with optional leading sign flag,
     * followed by any number of digits.
     *
     * @param text text to be parsed
     * @return converted char value
     * @throws IllegalArgumentException on parse error
     */
    public static char parseChar(String text) throws IllegalArgumentException {
        int value = parseInt(text);

        if ((value < Character.MIN_VALUE) || (value > Character.MAX_VALUE)) {
            throw new IllegalArgumentException("Value out of range");
        }

        return (char) value;
    }

    /**
     * Serialize char value to text as unsigned 16-bit integer.
     *
     * @param value char value to be serialized
     * @return text representation of value
     */
    public static String serializeChar(char value) {
        return Integer.toString(value);
    }

    /**
     * Parse char value from text as character value. This requires that the string must be of length one.
     *
     * @param text text to be parsed
     * @return converted char value
     * @throws IllegalArgumentException on parse error
     */
    public static char parseCharString(String text) throws IllegalArgumentException {
        if (text.length() == 1) {
            return text.charAt(0);
        } else {
            throw new IllegalArgumentException("Input must be a single character");
        }
    }

    /**
     * Deserialize char value from text as character value. This requires that the string must be null or of length one.
     *
     * @param text text to be parsed (may be <code>null</code>)
     * @return converted char value
     * @throws IllegalArgumentException on parse error
     */
    public static char deserializeCharString(String text) throws IllegalArgumentException {
        if (text == null) {
            return 0;
        } else {
            return parseCharString(text);
        }
    }

    /**
     * Serialize char value to text as string of length one.
     *
     * @param value char value to be serialized
     * @return text representation of value
     */
    public static String serializeCharString(char value) {
        return String.valueOf(value);
    }

    /**
     * Parse float value from text. This uses the W3C XML Schema format for floats, with the exception that it will
     * accept "+NaN" and "-NaN" as valid formats. This is not in strict compliance with the specification, but is
     * included for interoperability with other Java XML processing.
     *
     * @param text text to be parsed
     * @return converted float value
     * @throws IllegalArgumentException on parse error
     */
    public static float parseFloat(String text) throws IllegalArgumentException {
        text = text.trim();

        if ("-INF".equals(text)) {
            return Float.NEGATIVE_INFINITY;
        } else if ("INF".equals(text)) {
            return Float.POSITIVE_INFINITY;
        } else {
            try {
                return Float.parseFloat(text);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
    }

    /**
     * Serialize float value to text.
     *
     * @param value float value to be serialized
     * @return text representation of value
     */
    public static String serializeFloat(float value) {
        if (Float.isInfinite(value)) {
            return (value < 0.0f) ? "-INF" : "INF";
        } else {
            return Float.toString(value);
        }
    }

    /**
     * Parse double value from text. This uses the W3C XML Schema format for doubles, with the exception that it will
     * accept "+NaN" and "-NaN" as valid formats. This is not in strict compliance with the specification, but is
     * included for interoperability with other Java XML processing.
     *
     * @param text text to be parsed
     * @return converted double value
     * @throws IllegalArgumentException on parse error
     */
    public static double parseDouble(String text) throws IllegalArgumentException {
        text = text.trim();

        if ("-INF".equals(text)) {
            return Double.NEGATIVE_INFINITY;
        } else if ("INF".equals(text)) {
            return Double.POSITIVE_INFINITY;
        } else {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
    }

    /**
     * Serialize double value to text.
     *
     * @param value double value to be serialized
     * @return text representation of value
     */
    public static String serializeDouble(double value) {
        if (Double.isInfinite(value)) {
            return (value < 0.0f) ? "-INF" : "INF";
        } else {
            return Double.toString(value);
        }
    }

    /**
     * Convert gYear text to Java date. Date values are expected to be in W3C XML Schema standard format as CCYY, with
     * optional leading sign.
     *
     * @param text text to be parsed
     * @return start of year date as millisecond value from 1 C.E.
     * @throws IllegalArgumentException on parse error
     */
    public static long parseYear(String text) throws IllegalArgumentException {
        // start by validating the length
        text = text.trim();

        boolean valid = true;
        int minc = 4;
        char chr = text.charAt(0);

        if (chr == '-') {
            minc = 5;
        } else if (chr == '+') {
            valid = false;
        }

        if (text.length() < minc) {
            valid = false;
        }

        if (!valid) {
            throw new IllegalArgumentException("Invalid year format");
        }

        // handle year conversion
        int year = parseInt(text);

        if (year == 0) {
            throw new IllegalArgumentException("Year value 0 is not allowed");
        }

        if (year > 0) {
            year--;
        }

        long day = ((((long) year) * 365) + (year / 4)) - (year / 100) + (year / 400);

        return (day * MSPERDAY) - TIME_BASE;
    }

    /**
     * Convert gYearMonth text to Java date. Date values are expected to be in W3C XML Schema standard format as
     * CCYY-MM, with optional leading sign.
     *
     * @param text text to be parsed
     * @return start of month in year date as millisecond value
     * @throws IllegalArgumentException on parse error
     */
    public static long parseYearMonth(String text) throws IllegalArgumentException {
        // start by validating the length and basic format
        text = text.trim();

        boolean valid = true;
        int minc = 7;
        char chr = text.charAt(0);

        if (chr == '-') {
            minc = 8;
        } else if (chr == '+') {
            valid = false;
        }

        int split = text.length() - 3;

        if (text.length() < minc) {
            valid = false;
        } else {
            if (text.charAt(split) != '-') {
                valid = false;
            }
        }

        if (!valid) {
            throw new IllegalArgumentException("Invalid date format");
        }

        // handle year and month conversion
        int year = parseInt(text.substring(0, split));

        if (year == 0) {
            throw new IllegalArgumentException("Year value 0 is not allowed");
        }

        int month = parseDigits(text, split + 1, 2) - 1;

        if ((month < 0) || (month > 11)) {
            throw new IllegalArgumentException("Month value out of range");
        }

        boolean leap = ((year % 4) == 0) && !(((year % 100) == 0) && ((year % 400) != 0));

        if (year > 0) {
            year--;
        }

        long day = ((((long) year) * 365) + (year / 4))
                - (year / 100)
                + (year / 400)
                + (leap ? MONTHS_LEAP : MONTHS_NONLEAP)[month];

        return (day * MSPERDAY) - TIME_BASE;
    }

    /**
     * Convert date text to Java date. Date values are expected to be in W3C XML Schema standard format as CCYY-MM-DD,
     * with optional leading sign and trailing time zone (though the time zone is ignored in this case).
     *
     * @param text text to be parsed
     * @return start of day in month and year date as millisecond value
     * @throws IllegalArgumentException on parse error
     */
    public static long parseDate(String text) throws IllegalArgumentException {
        // start by validating the length and basic format
        boolean valid = true;
        int minc = 10;
        char chr = text.charAt(0);

        if (chr == '-') {
            minc = 11;
        } else if (chr == '+') {
            valid = false;
        }

        int split = text.length() - 6;

        if (text.length() < minc) {
            valid = false;
        } else {
            if ((text.charAt(split) != '-') || (text.charAt(split + 3) != '-')) {
                valid = false;
            }
        }

        if (!valid) {
            throw new IllegalArgumentException("Invalid date format");
        }

        // handle year, month, and day conversion
        int year = parseInt(text.substring(0, split));

        if (year == 0) {
            throw new IllegalArgumentException("Year value 0 is not allowed");
        }

        int month = parseDigits(text, split + 1, 2) - 1;

        if ((month < 0) || (month > 11)) {
            throw new IllegalArgumentException("Month value out of range");
        }

        long day = parseDigits(text, split + 4, 2) - 1L;
        boolean leap = ((year % 4) == 0) && !(((year % 100) == 0) && ((year % 400) != 0));
        int[] starts = leap ? MONTHS_LEAP : MONTHS_NONLEAP;

        if ((day < 0) || (day >= (starts[month + 1] - starts[month]))) {
            throw new IllegalArgumentException("Day value out of range");
        }

        if (year > 0) {
            year--;
        }

        day += (((((long) year) * 365) + (year / 4)) - (year / 100) + (year / 400) + starts[month]);

        return (day * MSPERDAY) - TIME_BASE;
    }

    /**
     * Deserialize date from text. Date values are expected to match W3C XML Schema standard format as CCYY-MM-DD, with
     * optional leading minus sign if necessary. This method follows standard JiBX deserializer usage requirements by
     * accepting a <code>null</code> input.
     *
     * @param text text to be parsed (may be <code>null</code>)
     * @return converted date, or <code>null</code> if passed <code>null</code> input
     * @throws IllegalArgumentException on parse error
     */
    public static Date deserializeDate(String text) throws IllegalArgumentException {
        if (text == null) {
            return null;
        } else {
            return new Date(parseDate(text));
        }
    }

    /**
     * Deserialize SQL date from text. Date values are expected to match W3C XML Schema standard format as CCYY-MM-DD,
     * with optional leading minus sign if necessary. This method follows standard JiBX deserializer usage requirements
     * by accepting a <code>null</code> input.
     *
     * @param text text to be parsed (may be <code>null</code>)
     * @return converted date, or <code>null</code> if passed <code>null</code> input
     * @throws IllegalArgumentException on parse error
     */
    public static java.sql.Date deserializeSqlDate(String text) throws IllegalArgumentException {
        if (text == null) {
            return null;
        } else {
            // convert value, assuming maximum daylight time change of 2 hours
            long time = parseDate(text);
            time += (TimeZone.getDefault().getRawOffset() + (MSPERHOUR * 2));

            return new java.sql.Date(time);
        }
    }

    /**
     * Parse general time value from text. Time values are expected to be in W3C XML Schema standard format as
     * hh:mm:ss.fff, with optional leading sign and trailing time zone.
     *
     * @param text text to be parsed
     * @param start offset of first character of time value
     * @param length number of characters in time value
     * @return converted time as millisecond value
     * @throws IllegalArgumentException on parse error
     */
    public static long parseTime(String text, int start, int length) throws IllegalArgumentException {
        // validate time value following date
        long milli = 0;
        boolean valid = (length > (start + 7)) && (text.charAt(start + 2) == ':') && (text.charAt(start + 5) == ':');

        if (valid) {
            int hour = parseDigits(text, start, 2);
            int minute = parseDigits(text, start + 3, 2);
            int second = parseDigits(text, start + 6, 2);

            if ((hour > 23) || (minute > 59) || (second > 60)) {
                valid = false;
            } else {
                // convert to base millisecond in day
                milli = ((((hour * 60L) + minute) * 60) + second) * 1000L;
                start += 8;

                if (length > start) {
                    // adjust for time zone
                    if (text.charAt(length - 1) == 'Z') {
                        length--;
                    } else {
                        char chr = text.charAt(length - 6);

                        if ((chr == '-') || (chr == '+')) {
                            hour = parseDigits(text, length - 5, 2);
                            minute = parseDigits(text, length - 2, 2);

                            if ((hour > 23) || (minute > 59)) {
                                valid = false;
                            } else {
                                int offset = ((hour * 60) + minute) * 60 * 1000;

                                if (chr == '-') {
                                    milli += offset;
                                } else {
                                    milli -= offset;
                                }
                            }

                            length -= 6;
                        }
                    }

                    // check for trailing fractional second
                    if (text.charAt(start) == '.') {
                        double fraction = Double.parseDouble(text.substring(start, length));
                        milli = (long) (milli + (fraction * 1000.0));
                    } else if (length > start) {
                        valid = false;
                    }
                }
            }
        }

        // check for valid result
        if (valid) {
            return milli;
        } else {
            throw new IllegalArgumentException("Invalid dateTime format");
        }
    }

    /**
     * Parse general dateTime value from text. Date values are expected to be in W3C XML Schema standard format as
     * CCYY-MM-DDThh:mm:ss.fff, with optional leading sign and trailing time zone.
     *
     * @param text text to be parsed
     * @return converted date as millisecond value
     * @throws IllegalArgumentException on parse error
     */
    public static long parseDateTime(String text) throws IllegalArgumentException {
        // split text to convert portions separately
        int split = text.indexOf('T');

        if (split < 0) {
            throw new IllegalArgumentException("Missing 'T' separator in dateTime");
        }

        return parseDate(text.substring(0, split)) + parseTime(text, split + 1, text.length());
    }

    /**
     * Deserialize date from general dateTime text. Date values are expected to match W3C XML Schema standard format as
     * CCYY-MM-DDThh:mm:ss, with optional leading minus sign and trailing seconds decimal, as necessary. This method
     * follows standard JiBX deserializer usage requirements by accepting a <code>null</code> input.
     *
     * @param text text to be parsed (may be <code>null</code>)
     * @return converted date, or <code>null</code> if passed <code>null</code> input
     * @throws IllegalArgumentException on parse error
     */
    public static Date deserializeDateTime(String text) throws IllegalArgumentException {
        if (text == null) {
            return null;
        } else {
            return new Date(parseDateTime(text));
        }
    }

    /**
     * Deserialize timestamp from general dateTime text. Timestamp values are represented in the same way as regular
     * dates, but allow more precision in the fractional second value (down to nanoseconds). This method follows
     * standard JiBX deserializer usage requirements by accepting a <code>null</code> input.
     *
     * @param text text to be parsed (may be <code>null</code>)
     * @return converted timestamp, or <code>null</code> if passed <code>null</code> input
     * @throws IllegalArgumentException on parse error
     */
    public static Timestamp deserializeTimestamp(String text) throws IllegalArgumentException {
        if (text == null) {
            return null;
        } else {
            // check for fractional second value present
            int split = text.indexOf('.');
            int nano = 0;

            if (split > 0) {
                // make sure there aren't multiple decimal points
                if (text.indexOf('.', split) > 0) {
                    throw new IllegalArgumentException("Not a valid dateTime value");
                }

                // scan through all digits following decimal point
                int limit = text.length();
                int scan = split;

                while (++scan < limit) {
                    char chr = text.charAt(scan);

                    if ((chr < '0') || (chr > '9')) {
                        break;
                    }
                }

                // parse digits following decimal point
                int length = scan - split - 1;

                if (length > 9) {
                    length = 9;
                }

                nano = parseDigits(text, split + 1, length);

                // convert to number of nanoseconds
                while (length < 9) {
                    nano *= 10;
                    length++;
                }

                // strip fractional second off text
                if (scan < limit) {
                    text = text.substring(0, split) + text.substring(scan);
                } else {
                    text = text.substring(0, split);
                }
            }

            // return timestamp value with nanoseconds
            Timestamp stamp = new Timestamp(parseDateTime(text));
            stamp.setNanos(nano);

            return stamp;
        }
    }

    /**
     * Deserialize time from text. Time values obey the rules of the time portion of a dataTime value. This method
     * follows standard JiBX deserializer usage requirements by accepting a <code>null</code> input.
     *
     * @param text text to be parsed (may be <code>null</code>)
     * @return converted time, or <code>null</code> if passed <code>null</code> input
     * @throws IllegalArgumentException on parse error
     */
    public static Time deserializeSqlTime(String text) throws IllegalArgumentException {
        if (text == null) {
            return null;
        } else {
            return new Time(parseTime(text, 0, text.length()));
        }
    }

    /**
     * Format year number consistent with W3C XML Schema definitions, using a minimum of four digits padded with zeros
     * if necessary. A leading minus sign is included for years prior to 1 C.E.
     *
     * @param year number to be formatted
     * @param buff text formatting buffer
     */
    protected static void formatYearNumber(long year, StringBuffer buff) {
        // start with minus sign for dates prior to 1 C.E.
        if (year <= 0) {
            buff.append('-');
            year = -(year - 1);
        }

        // add padding if needed to bring to length of four
        if (year < 1000) {
            buff.append('0');

            if (year < 100) {
                buff.append('0');

                if (year < 10) {
                    buff.append('0');
                }
            }
        }

        // finish by converting the actual year number
        buff.append(year);
    }

    /**
     * Format a positive number as two digits. This uses an optional leading zero digit for values less than ten.
     *
     * @param value number to be formatted (<code>0</code> to <code>99</code>)
     * @param buff text formatting buffer
     */
    protected static void formatTwoDigits(int value, StringBuffer buff) {
        if (value < 10) {
            buff.append('0');
        }

        buff.append(value);
    }

    /**
     * Format time in milliseconds to year number. The resulting year number format is consistent with W3C XML Schema
     * definitions, using a minimum of four digits padded with zeros if necessary. A leading minus sign is included for
     * years prior to 1 C.E.
     *
     * @param value time in milliseconds to be converted (from 1 C.E.)
     * @param buff text formatting buffer
     */
    protected static void formatYear(long value, StringBuffer buff) {
        // find the actual year and month number; this uses a integer arithmetic
        //  conversion based on Baum, first making the millisecond count
        //  relative to March 1 of the year 0 C.E., then using simple arithmetic
        //  operations to compute century, year, and month; it's slightly
        //  different for pre-C.E. values because of Java's handling of divisions.
        long time = value + (306 * LMSPERDAY) + ((LMSPERDAY * 3) / 4);
        long century = time / MSPERCENTURY; // count of centuries
        long adjusted = time + ((century - (century / 4)) * MSPERDAY);
        int year = (int) (adjusted / MSPERAVGYEAR); // year in March 1 terms

        if (adjusted < 0) {
            year--;
        }

        long yms = (adjusted + (LMSPERDAY / 4)) - (((year * 365) + (year / 4)) * LMSPERDAY);
        int yday = (int) (yms / LMSPERDAY); // day number in year
        int month = ((5 * yday) + 456) / 153; // (biased) month number

        if (month > 12) { // convert start of year
            year++;
        }

        // format year to text
        formatYearNumber(year, buff);
    }

    /**
     * Format time in milliseconds to year number and month number. The resulting year number format is consistent with
     * W3C XML Schema definitions, using a minimum of four digits for the year and exactly two digits for the month.
     *
     * @param value time in milliseconds to be converted (from 1 C.E.)
     * @param buff text formatting buffer
     * @return number of milliseconds into month
     */
    protected static long formatYearMonth(long value, StringBuffer buff) {
        // find the actual year and month number; this uses a integer arithmetic
        //  conversion based on Baum, first making the millisecond count
        //  relative to March 1 of the year 0 C.E., then using simple arithmetic
        //  operations to compute century, year, and month; it's slightly
        //  different for pre-C.E. values because of Java's handling of divisions.
        long time = value + (306 * LMSPERDAY) + ((LMSPERDAY * 3) / 4);
        long century = time / MSPERCENTURY; // count of centuries
        long adjusted = time + ((century - (century / 4)) * MSPERDAY);
        int year = (int) (adjusted / MSPERAVGYEAR); // year in March 1 terms

        if (adjusted < 0) {
            year--;
        }

        long yms = (adjusted + (LMSPERDAY / 4)) - (((year * 365) + (year / 4)) * LMSPERDAY);
        int yday = (int) (yms / LMSPERDAY); // day number in year

        if (yday == 0) { // special for negative

            boolean bce = year < 0;

            if (bce) {
                year--;
            }

            boolean isNormalLeapYear = ((year % 4) == 0);
            boolean is400LeapYear = ((year % 400) == 0);
            boolean is100NotLeapYear = ((year % 100) == 0);
            int dcnt = (is400LeapYear || (isNormalLeapYear && !is100NotLeapYear)) ? 366 : 365;

            if (!bce) {
                year--;
            }

            yms += (dcnt * LMSPERDAY);
            yday += dcnt;
        }

        int month = ((5 * yday) + 456) / 153; // (biased) month number
        long rem = yms - BIAS_MONTHMS[month] - LMSPERDAY; // ms into month

        if (month > 12) { // convert start of year
            year++;
            month -= 12;
        }

        // format year and month as text
        formatYearNumber(year, buff);
        buff.append('-');
        formatTwoDigits(month, buff);

        // return extra milliseconds into month
        return rem;
    }

    /**
     * Format time in milliseconds to year number, month number, and day number. The resulting year number format is
     * consistent with W3C XML Schema definitions, using a minimum of four digits for the year and exactly two digits
     * each for the month and day.
     *
     * @param value time in milliseconds to be converted (from 1 C.E.)
     * @param buff text formatting buffer
     * @return number of milliseconds into day
     */
    protected static int formatYearMonthDay(long value, StringBuffer buff) {
        // convert year and month
        long extra = formatYearMonth(value, buff);

        // append the day of month
        int day = (int) (extra / MSPERDAY) + 1;
        buff.append('-');
        formatTwoDigits(day, buff);

        // return excess of milliseconds into day
        return (int) (extra % MSPERDAY);
    }

    /**
     * Serialize time to general gYear text. Date values are formatted in W3C XML Schema standard format as CCYY, with
     * optional leading sign included if necessary.
     *
     * @param time time to be converted, as milliseconds from January 1, 1970
     * @return converted gYear text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeYear(long time) throws IllegalArgumentException {
        StringBuffer buff = new StringBuffer(6);
        formatYear(time + TIME_BASE, buff);

        return buff.toString();
    }

    /**
     * Serialize date to general gYear text. Date values are formatted in W3C XML Schema standard format as CCYY, with
     * optional leading sign included if necessary.
     *
     * @param date date to be converted
     * @return converted gYear text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeYear(Date date) throws IllegalArgumentException {
        return serializeYear(date.getTime());
    }

    /**
     * Serialize time to general gYearMonth text. Date values are formatted in W3C XML Schema standard format as
     * CCYY-MM, with optional leading sign included if necessary.
     *
     * @param time time to be converted, as milliseconds from January 1, 1970
     * @return converted gYearMonth text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeYearMonth(long time) throws IllegalArgumentException {
        StringBuffer buff = new StringBuffer(12);
        formatYearMonth(time + TIME_BASE, buff);

        return buff.toString();
    }

    /**
     * Serialize date to general gYearMonth text. Date values are formatted in W3C XML Schema standard format as
     * CCYY-MM, with optional leading sign included if necessary.
     *
     * @param date date to be converted
     * @return converted gYearMonth text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeYearMonth(Date date) throws IllegalArgumentException {
        return serializeYearMonth(date.getTime());
    }

    /**
     * Serialize time to general date text. Date values are formatted in W3C XML Schema standard format as CCYY-MM-DD,
     * with optional leading sign included if necessary.
     *
     * @param time time to be converted, as milliseconds from January 1, 1970
     * @return converted date text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeDate(long time) throws IllegalArgumentException {
        StringBuffer buff = new StringBuffer(12);
        formatYearMonthDay(time + TIME_BASE, buff);

        return buff.toString();
    }

    /**
     * Serialize date to general date text. Date values are formatted in W3C XML Schema standard format as CCYY-MM-DD,
     * with optional leading sign included if necessary.
     *
     * @param date date to be converted
     * @return converted date text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeDate(Date date) throws IllegalArgumentException {
        long time = date.getTime();
        time += TimeZone.getDefault().getOffset(time);
        return serializeDate(time);
    }

    /**
     * Serialize SQL date to general date text. Date values are formatted in W3C XML Schema standard format as
     * CCYY-MM-DD, with optional leading sign included if necessary.
     *
     * @param date date to be converted
     * @return converted date text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeSqlDate(java.sql.Date date) throws IllegalArgumentException {
        return serializeDate(date);
    }

    /**
     * Serialize time to general time text in buffer. Time values are formatted in W3C XML Schema standard format as
     * hh:mm:ss, with optional trailing seconds decimal, as necessary. This form uses a supplied buffer to support
     * flexible use, including with dateTime combination values.
     *
     * @param time time to be converted, as milliseconds in day
     * @param buff buffer for appending time text
     * @throws IllegalArgumentException on conversion error
     */
    public static void serializeTime(int time, StringBuffer buff) throws IllegalArgumentException {
        // append the hour, minute, and second
        formatTwoDigits(time / MSPERHOUR, buff);
        time = time % MSPERHOUR;
        buff.append(':');
        formatTwoDigits(time / MSPERMINUTE, buff);
        time = time % MSPERMINUTE;
        buff.append(':');
        formatTwoDigits(time / 1000, buff);
        time = time % 1000;

        // check if decimals needed on second
        if (time > 0) {
            buff.append('.');
            buff.append(time / 100);
            time = time % 100;

            if (time > 0) {
                buff.append(time / 10);
                time = time % 10;

                if (time > 0) {
                    buff.append(time);
                }
            }
        }
    }

    /**
     * Serialize time to general dateTime text. Date values are formatted in W3C XML Schema standard format as
     * CCYY-MM-DDThh:mm:ss, with optional leading sign and trailing seconds decimal, as necessary.
     *
     * @param time time to be converted, as milliseconds from January 1, 1970
     * @param zone flag for trailing 'Z' to be appended to indicate UTC
     * @return converted dateTime text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeDateTime(long time, boolean zone) throws IllegalArgumentException {
        // start with the year, month, and day
        StringBuffer buff = new StringBuffer(25);
        int extra = formatYearMonthDay(time + TIME_BASE, buff);

        // append the time for full form
        buff.append('T');
        serializeTime(extra, buff);

        // return full text with optional trailing zone indicator
        if (zone) {
            buff.append('Z');
        }

        return buff.toString();
    }

    /**
     * Serialize time to general dateTime text. This method is provided for backward compatibility. It generates the
     * dateTime text without the trailing 'Z' to indicate UTC.
     *
     * @param time time to be converted, as milliseconds from January 1, 1970
     * @return converted dateTime text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeDateTime(long time) throws IllegalArgumentException {
        return serializeDateTime(time, false);
    }

    /**
     * Serialize date to general dateTime text. Date values are formatted in W3C XML Schema standard format as
     * CCYY-MM-DDThh:mm:ss, with optional leading sign and trailing seconds decimal, as necessary.
     *
     * @param date date to be converted
     * @return converted dateTime text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeDateTime(Date date) throws IllegalArgumentException {
        long time = date.getTime();
        time += TimeZone.getDefault().getOffset(time);
        return serializeDateTime(time, false);
    }

    /**
     * Serialize timestamp to general dateTime text. Timestamp values are represented in the same way as regular dates,
     * but allow more precision in the fractional second value (down to nanoseconds).
     *
     * @param stamp timestamp to be converted
     * @return converted dateTime text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeTimestamp(Timestamp stamp) throws IllegalArgumentException {
        // check for nanosecond value to be included
        int nano = stamp.getNanos();

        if (nano > 0) {
            // convert the number of nanoseconds to text
            String value = serializeInt(nano);

            // pad with leading zeros if less than 9 digits
            StringBuffer digits = new StringBuffer(9);

            if (value.length() < 9) {
                int lead = 9 - value.length();

                for (int i = 0; i < lead; i++) {
                    digits.append('0');
                }
            }

            digits.append(value);

            // strip trailing zeros from value
            int last = 9;

            while (--last > 0) {
                if (digits.charAt(last) != '0') {
                    break;
                }
            }

            digits.setLength(last);

            // finish by appending to time with decimal separator
            return serializeDateTime(stamp.getTime(), false) + '.' + digits + 'Z';
        } else {
            return serializeDateTime(stamp.getTime(), true);
        }
    }

    /**
     * Serialize time to standard text. Time values are formatted in W3C XML Schema standard format as hh:mm:ss, with
     * optional trailing seconds decimal, as necessary. The standard conversion does not append a time zone indication.
     *
     * @param time time to be converted
     * @return converted time text
     * @throws IllegalArgumentException on conversion error
     */
    public static String serializeSqlTime(Time time) throws IllegalArgumentException {
        StringBuffer buff = new StringBuffer(12);
        long t = time.getTime();
        t += TimeZone.getDefault().getOffset(t);
        int extra = formatYearMonthDay(t + TIME_BASE, buff);
        buff.delete(0, buff.length());
        serializeTime(extra, buff);

        return buff.toString();
    }

    /**
     * General object comparison method. Don't know why Sun hasn't seen fit to include this somewhere, but at least it's
     * easy to write (over and over again).
     *
     * @param a first object to be compared
     * @param b second object to be compared
     * @return <code>true</code> if both objects are <code>null</code>, or if <code>a.equals(b)
     *     </code>; <code>false</code> otherwise
     */
    public static boolean isEqual(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * Find text value in enumeration. This first does a binary search through an array of allowed text matches. If a
     * separate array of corresponding values is supplied, the value at the matched position is returned; otherwise the
     * match index is returned directly.
     *
     * @param target text to be found in enumeration
     * @param enums ordered array of texts included in enumeration
     * @param vals array of values to be returned for corresponding text match positions (position returned directly if
     *     this is <code>null</code>)
     * @return enumeration value for target text
     * @throws IllegalArgumentException if target text not found in enumeration
     */
    public static int enumValue(String target, String[] enums, int[] vals) throws IllegalArgumentException {
        int base = 0;
        int limit = enums.length - 1;

        while (base <= limit) {
            int cur = (base + limit) >> 1;
            int diff = target.compareTo(enums[cur]);

            if (diff < 0) {
                limit = cur - 1;
            } else if (diff > 0) {
                base = cur + 1;
            } else if (vals != null) {
                return vals[cur];
            } else {
                return cur;
            }
        }

        throw new IllegalArgumentException("Target value \"" + target + "\" not found in enumeration");
    }

    /**
     * Decode a chunk of data from base64 encoding. The length of a chunk is always 4 characters in the base64
     * representation, but may be 1, 2, or 3 bytes of data, as determined by whether there are any pad characters at the
     * end of the base64 representation
     *
     * @param base starting offset within base64 character array
     * @param chrs character array for base64 text representation
     * @param fill starting offset within byte data array
     * @param byts byte data array
     * @return number of decoded bytes
     * @throws IllegalArgumentException if invalid character in base64 representation
     */
    @SuppressWarnings("FallThrough")
    private static int decodeChunk(int base, char[] chrs, int fill, byte[] byts) throws IllegalArgumentException {
        // find the byte count to be decoded
        int length = 3;

        if (chrs[base + 3] == PAD_CHAR) {
            length = 2;

            if (chrs[base + 2] == PAD_CHAR) {
                length = 1;
            }
        }

        // get 6-bit values
        int v0 = s_base64Values[chrs[base + 0]];
        int v1 = s_base64Values[chrs[base + 1]];
        int v2 = s_base64Values[chrs[base + 2]];
        int v3 = s_base64Values[chrs[base + 3]];

        // convert and store bytes of data
        switch (length) {
            case 3:
                byts[fill + 2] = (byte) ((v2 << 6) | v3);

            case 2:
                byts[fill + 1] = (byte) ((v1 << 4) | (v2 >> 2));

            case 1:
                byts[fill] = (byte) ((v0 << 2) | (v1 >> 4));

                break;
        }

        return length;
    }

    /**
     * Parse base64 data from text. This converts the base64 data into a byte array of the appopriate length. In keeping
     * with the recommendations,
     *
     * @param text text to be parsed (may include extra characters)
     * @return byte array of data
     * @throws IllegalArgumentException if invalid character in base64 representation
     */
    public static byte[] parseBase64(String text) throws IllegalArgumentException {
        // convert raw text to base64 character array
        char[] chrs = new char[text.length()];
        int length = 0;

        for (int i = 0; i < text.length(); i++) {
            char chr = text.charAt(i);

            if ((chr < 128) && (s_base64Values[chr] >= 0)) {
                chrs[length++] = chr;
            }
        }

        // check the text length
        if ((length % 4) != 0) {
            throw new IllegalArgumentException("Text length for base64 must be a multiple of 4");
        } else if (length == 0) {
            return new byte[0];
        }

        // find corresponding byte count for data
        int blength = length / 4 * 3;

        if (chrs[length - 1] == PAD_CHAR) {
            blength--;

            if (chrs[length - 2] == PAD_CHAR) {
                blength--;
            }
        }

        // convert text to actual bytes of data
        byte[] byts = new byte[blength];
        int fill = 0;

        for (int i = 0; i < length; i += 4) {
            fill += decodeChunk(i, chrs, fill, byts);
        }

        if (fill != blength) {
            throw new IllegalArgumentException("Embedded padding characters in byte64 text");
        }

        return byts;
    }

    /**
     * Parse base64 data from text. This converts the base64 data into a byte array of the appopriate length. In keeping
     * with the recommendations,
     *
     * @param text text to be parsed (may be null, or include extra characters)
     * @return byte array of data
     * @throws IllegalArgumentException if invalid character in base64 representation
     */
    public static byte[] deserializeBase64(String text) throws IllegalArgumentException {
        if (text == null) {
            return null;
        } else {
            return parseBase64(text);
        }
    }

    /**
     * Encode a chunk of data to base64 encoding. Converts the next three bytes of data into four characters of text
     * representation, using padding at the end of less than three bytes of data remain.
     *
     * @param base starting offset within byte array
     * @param byts byte data array
     * @param buff buffer for encoded text
     */
    public static void encodeChunk(int base, byte[] byts, StringBuffer buff) {
        // get actual byte data length to be encoded
        int length = 3;

        if ((base + length) > byts.length) {
            length = byts.length - base;
        }

        // convert up to three bytes of data to four characters of text
        int b0 = byts[base];
        int value = (b0 >> 2) & 0x3F;
        buff.append(s_base64Chars[value]);

        if (length > 1) {
            int b1 = byts[base + 1];
            value = ((b0 & 3) << 4) + ((b1 >> 4) & 0x0F);
            buff.append(s_base64Chars[value]);

            if (length > 2) {
                int b2 = byts[base + 2];
                value = ((b1 & 0x0F) << 2) + ((b2 >> 6) & 3);
                buff.append(s_base64Chars[value]);
                value = b2 & 0x3F;
                buff.append(s_base64Chars[value]);
            } else {
                value = (b1 & 0x0F) << 2;
                buff.append(s_base64Chars[value]);
                buff.append(PAD_CHAR);
            }
        } else {
            value = (b0 & 3) << 4;
            buff.append(s_base64Chars[value]);
            buff.append(PAD_CHAR);
            buff.append(PAD_CHAR);
        }
    }

    /**
     * Serialize byte array to base64 text. In keeping with the specification, this adds a line break every 76
     * characters in the encoded representation.
     *
     * @param byts byte data array
     * @return base64 encoded text
     */
    public static String serializeBase64(byte[] byts) {
        StringBuffer buff = new StringBuffer((byts.length + 2) / 3 * 4);

        for (int i = 0; i < byts.length; i += 3) {
            encodeChunk(i, byts, buff);

            if ((i > 0) && ((i % 57) == 0) && ((i + 3) < byts.length)) {
                buff.append("\r\n");
            }
        }

        return buff.toString();
    }

    /**
     * Factory method to create a <code>java.util.ArrayList</code> as the implementation of a <code>
     * java.util.List</code>.
     *
     * @return new <code>java.util.ArrayList</code>
     */
    public static List arrayListFactory() {
        return new ArrayList<>();
    }
}
