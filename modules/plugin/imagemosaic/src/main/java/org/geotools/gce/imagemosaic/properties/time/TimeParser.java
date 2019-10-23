/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties.time;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.geotools.util.DateTimeParser;

/**
 * Parses the {@code time} parameter of the request. The date, time and period are expected to be
 * formatted according ISO-8601 standard.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @deprecated use #org.geotools.util.DateTimeParser instead.
 */
@Deprecated
public class TimeParser {

    private DateTimeParser parser;

    /** Creates the parser */
    public TimeParser() {
        parser =
                new DateTimeParser(
                        -1,
                        DateTimeParser.FLAG_GET_TIME_ON_CURRENT
                                | DateTimeParser.FLAG_GET_TIME_ON_NOW
                                | DateTimeParser.FLAG_GET_TIME_ON_PRESENT
                                | DateTimeParser.FLAG_IS_LENIENT);
    }

    /**
     * Parses the date given in parameter. The date format should comply to ISO-8601 standard. The
     * string may contains either a single date, or a start time, end time and a period. In the
     * first case, this method returns a singleton containing only the parsed date. In the second
     * case, this method returns a list including all dates from start time up to the end time with
     * the interval specified in the {@code value} string.
     *
     * @param value The date, time and period to parse.
     * @return A list of dates, or an empty list of the {@code value} string is null or empty.
     * @throws ParseException if the string can not be parsed.
     */
    public List<Date> parse(String value) throws ParseException {
        Collection results = parser.parse(value);
        List<Date> dates =
                (List<Date>)
                        results.stream()
                                .filter(o -> o instanceof Date)
                                .map(o -> (Date) o)
                                .collect(Collectors.toList());
        return dates;
    }
}
