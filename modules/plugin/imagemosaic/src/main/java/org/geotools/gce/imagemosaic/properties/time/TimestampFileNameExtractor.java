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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.RegExPropertiesCollector;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/** @author Simone Giannecchini, GeoSolutions SAS */
class TimestampFileNameExtractor extends RegExPropertiesCollector {
    private static final Logger LOGGER = Logging.getLogger(TimestampFileNameExtractor.class);

    private static final TimeParser parser = new TimeParser();

    private DateFormat customFormat;

    /**
     * @deprecated use {@link TimestampFileNameExtractor#TimestampFileNameExtractor(PropertiesCollectorSPI, List, String, String, boolean)
     */
    public TimestampFileNameExtractor(
            PropertiesCollectorSPI spi, List<String> propertyNames, String regex, String format) {
        this(spi, propertyNames, regex, format, false);
    }

    public TimestampFileNameExtractor(
            PropertiesCollectorSPI spi,
            List<String> propertyNames,
            String regex,
            String format,
            boolean fullPath) {
        super(spi, propertyNames, regex, fullPath);
        if (format != null) {
            customFormat = new SimpleDateFormat(format);
            customFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
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
                    dates.add(parsed);
                } else {
                    List<Date> parsed = parser.parse(match);
                    dates.addAll(parsed);
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
