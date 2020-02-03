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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.RegExPropertiesCollector;
import org.geotools.util.DateRange;
import org.geotools.util.DateTimeParser;
import org.geotools.util.DateTimeParser.FormatAndPrecision;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/** @author Simone Giannecchini, GeoSolutions SAS */
class TimestampFileNameExtractor extends RegExPropertiesCollector {
    private static final Logger LOGGER = Logging.getLogger(TimestampFileNameExtractor.class);

    private static final int DEFAULT_TIME_PARSER_FLAGS =
            DateTimeParser.FLAG_GET_TIME_ON_CURRENT
                    | DateTimeParser.FLAG_GET_TIME_ON_NOW
                    | DateTimeParser.FLAG_GET_TIME_ON_PRESENT
                    | DateTimeParser.FLAG_IS_LENIENT;

    private static final DateTimeParser PARSER =
            new DateTimeParser(
                    -1, DEFAULT_TIME_PARSER_FLAGS | DateTimeParser.FLAG_SINGLE_DATE_AS_DATERANGE);

    private DateFormat customFormat;

    private String format;

    private boolean useHighTime = false;

    public TimestampFileNameExtractor(
            PropertiesCollectorSPI spi,
            List<String> propertyNames,
            String regex,
            String format,
            boolean fullPath,
            boolean useHighTime) {
        super(spi, propertyNames, regex, fullPath);
        if (format != null) {
            this.format = format;
            customFormat = new SimpleDateFormat(format);
            customFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        this.useHighTime = useHighTime;
    }

    @Override
    public void setProperties(SimpleFeature feature) {

        // get all the matches and convert them in times
        final List<Date> dates = new ArrayList<Date>();
        for (String match : getMatches()) {
            // try to convert to date

            try {
                if (customFormat != null) {
                    Date parsed = customFormat.parse(match);
                    if (useHighTime) {
                        // Special case to make t and z upper case for ISO8601 matching.
                        format = format.replace("t", "T").replace("z", "Z");
                        parsed =
                                FormatAndPrecision.expandFromCustomFormat(parsed, format)
                                        .getMaxValue();
                    }
                    dates.add(parsed);
                } else {
                    Collection parsed = PARSER.parse(match);
                    parsed.stream()
                            .forEach(
                                    d -> {
                                        DateRange range = (DateRange) d;
                                        Date date =
                                                useHighTime
                                                        ? range.getMaxValue()
                                                        : range.getMinValue();
                                        dates.add(date);
                                    });
                }
            } catch (ParseException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }

        // set the properties, only if we have matches!
        if (dates.size() <= 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No matches found for this property extractor:");
            }
            throw new IllegalArgumentException("No matches found for this property extractor");
        }
        int index = 0;
        for (String propertyName : getPropertyNames()) {
            // set the property

            feature.setAttribute(propertyName, dates.get(index++));

            // do we have more dates?
            if (index >= dates.size()) return;
        }
    }
}
