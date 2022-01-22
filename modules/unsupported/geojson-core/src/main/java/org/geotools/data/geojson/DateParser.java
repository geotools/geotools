/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import static org.geotools.data.geojson.GeoJSONWriter.DEFAULT_TIME_ZONE;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.time.FastDateFormat;
import org.geotools.util.logging.Logging;

/**
 * A date parser that tries different formats for single dates, but not as loose as {@link
 * org.geotools.util.DateTimeParser} (and does not support periods and list of times either). For
 * reading GeoJSON, especially when guessing if a field is a date, we want some flexibility, without
 * mis-guessing a date field.
 */
class DateParser {

    static final Logger LOGGER = Logging.getLogger(DateParser.class);

    static final List<FastDateFormat> DEFAULT_FAST_DATE_FORMATS =
            Arrays.asList(
                    FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSX", DEFAULT_TIME_ZONE),
                    FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS", DEFAULT_TIME_ZONE),
                    FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssX", DEFAULT_TIME_ZONE),
                    FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss", DEFAULT_TIME_ZONE),
                    FastDateFormat.getInstance("yyyy-MM-ddX", DEFAULT_TIME_ZONE),
                    FastDateFormat.getInstance("yyyy-MM-dd", DEFAULT_TIME_ZONE));

    List<FastDateFormat> formats = DEFAULT_FAST_DATE_FORMATS;

    /**
     * Sets the Timezone used to format the date fields. <code>null</code> is a valid value, the JVM
     * local timezone will be used in that case.
     */
    public void setTimeZone(TimeZone tz) {
        this.formats =
                formats.stream()
                        .map(f -> FastDateFormat.getInstance(f.getPattern(), tz))
                        .collect(Collectors.toList());
    }

    /** Returns the timezone used to format dates. Defaults to GMT. */
    public TimeZone getTimeZone() {
        return formats.get(0).getTimeZone();
    }

    /**
     * Sets the date formats for time parsing
     *
     * @param pattern {@link java.text.SimpleDateFormat} compatible * pattern
     */
    public void setDatePattern(String... pattern) {
        if (pattern == null || pattern.length == 0)
            throw new IllegalArgumentException("Date patterns must be non null, and non empty");
        this.formats =
                Arrays.stream(pattern)
                        .map(p -> FastDateFormat.getInstance(p, getTimeZone()))
                        .collect(Collectors.toList());
    }

    /** Returns the date formatter pattern. Defaults to DEFAULT_DATE_FORMAT */
    public String[] getDatePatterns() {
        return this.formats.stream().map(f -> f.getPattern()).toArray(n -> new String[n]);
    }

    /**
     * Tries out the various date patterns and returns the result, or returns null if none could
     * parse the date
     */
    public Date parse(String text) {
        for (FastDateFormat format : formats) {
            try {
                return format.parse(text);
            } catch (ParseException e) {
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(
                            Level.FINEST,
                            "Failed to parse " + text + " using " + format.getPattern(),
                            e);
            }
        }
        return null;
    }
}
