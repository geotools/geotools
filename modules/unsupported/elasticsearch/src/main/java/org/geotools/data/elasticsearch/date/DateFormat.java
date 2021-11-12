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
/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geotools.data.elasticsearch.date;

/**
 * Values based on reference doc - https://www.elastic.co/guide/reference/mapping/date-format/. The
 * patterns are taken from this documentation and slightly adapted so that a Java {@link
 * java.time.format.DateTimeFormatter} produces the same values as the Elasticsearch formatter.
 *
 * @author Jakub Vavrik
 * @author Tim te Beek
 * @author Peter-Josef Meisch
 * @author Sascha Woo
 */
public enum DateFormat {
    basic_date("uuuuMMdd"), //
    basic_date_time("uuuuMMdd'T'HHmmss.SSS[X]", "uuuuMMdd'T'HHmmss.SSS[VV][X]"), //
    basic_date_time_no_millis("uuuuMMdd'T'HHmmss[X]", "uuuuMMdd'T'HHmmss[VV][X]"), //
    basic_ordinal_date("uuuuDDD"), //
    basic_ordinal_date_time(
            "yyyyDDD'T'HHmmss.SSS[X]", "yyyyDDD'T'HHmmss[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    basic_ordinal_date_time_no_millis("yyyyDDD'T'HHmmss[X]", "yyyyDDD'T'HHmmss[VV][X]"), //
    basic_time("HHmmss[.SSS][X]", "HHmmss[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    basic_time_no_millis("HHmmss[X]", "HHmmss[VV][X]"), //
    basic_t_time("'T'HHmmss[.SSS][X]", "'T'HHmmss[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    basic_t_time_no_millis("'T'HHmmss[X]", "'T'HHmmss[VV][X]"), //
    basic_week_date("Y'W'wwe"), // week-based-year!
    basic_week_date_time(
            "Y'W'wwe'T'HHmmss[.SSS][X]",
            "Y'W'wwe'T'HHmmss[.SSSSSSSSS][.SSS][.S][VV][X]"), // here Elasticsearch uses a different
    // zone format
    basic_week_date_time_no_millis("Y'W'wwe'T'HHmmss[X]", "Y'W'wwe'T'HHmmss[VV][X]"), //
    date("u-M-d"), //
    date_hour("u-M-d'T'H"), //
    date_hour_minute("u-M-dd'T'H:m"), //
    date_hour_minute_second("u-M-d'T'H:m:s"), //
    date_hour_minute_second_fraction(
            "uuuu-MM-dd'T'HH:mm:ss[.SSS]", "[uuuu-MM-dd'T'HH:mm:ss[.SSSSSSSSS][.SSS][.S]]"), //
    date_hour_minute_second_millis(
            "uuuu-MM-dd'T'HH:mm:ss.SSS", "uuuu-MM-dd'T'HH:mm:ss[.SSSSSSSSS][.SSS][.S]"), //
    // insanely flexible format, has to be built by hand
    date_optional_time("date_optional_time"), //
    date_time(
            "uuuu-MM-dd'T'HH:mm:ss[.SSS][X]",
            "u-M-d'T'H:m:s[.SSSSSSSSS][.SSS][.SS][.S][VV][X]"), //
    date_time_no_millis("uuuu-MM-dd'T'HH:mm:ss[X]", "u-M-d'T'H:m:s[VV][X]"),
    epoch_millis("epoch_millis"), //
    epoch_second("epoch_second"), //
    hour("HH", "H"), //
    hour_minute("HH:mm", "H:m"), //
    hour_minute_second("HH:mm:ss", "H:m:s"), //
    hour_minute_second_fraction("HH:mm:ss[.SSS]", "H:m:s[.SSSSSSSSS][.SSS][.S]"), //
    hour_minute_second_millis("HH:mm:ss[.SSS]", "H:m:s[.SSSSSSSSS][.SSS][.S]"), //
    ordinal_date("uuuu-DDD", "u-D"), //
    ordinal_date_time(
            "uuuu-DDD'T'HH:mm:ss[.SSS][X]", "u-D'T'H:m:s[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    ordinal_date_time_no_millis("uuuu-DDD'T'HH:mm:ss[X]", "u-D'T'H:m:s[VV][X]"), //
    time("HH:mm:ss.SSS[X]", "H:m:s[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    time_no_millis("HH:mm:ss[X]", "H:m:s[VV][X]"), //
    t_time("'T'HH:mm:ss[.SSS][X]", "'T'H:m:s[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    t_time_no_millis("'T'HH:mm:ss[X]", "'T'H:m:s[VV][X]"), //
    week_date("YYYY-'W'ww-e", "Y-'W'w-e"), //
    week_date_time(
            "YYYY-'W'ww-e'T'HH:mm:ss[.SSS][X]",
            "Y-'W'w-e'T'H:m:s[.SSSSSSSSS][.SSS][.S][VV][X]"), //
    week_date_time_no_millis("YYYY-'W'ww-e'T'HH:mm:ss[X]", "Y-'W'w-e'T'H:m:s[VV][X]"), //
    year("uuuu", "u"), //
    year_month("uuuu-MM", "u-M"), //
    year_month_day("uuuu-MM-dd", "u-M-d");

    private final String parsePattern;
    private final String formatPattern;

    DateFormat(String pattern) {
        this(pattern, pattern);
    }

    DateFormat(String formatPattern, String parsePattern) {
        this.formatPattern = formatPattern;
        this.parsePattern = parsePattern;
    }

    public String getParsePattern() {
        return parsePattern;
    }

    public String getFormatPattern() {
        return formatPattern;
    }
}
