/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.properties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/** @author Niels Charlier */
class DateExtractor extends PropertiesCollector {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy:mm:dd hh:mm:ss");

    private static final Logger LOGGER = Logging.getLogger(DateExtractor.class);

    public DateExtractor(PropertiesCollectorSPI spi, List<String> propertyNames) {
        super(spi, propertyNames);
    }

    @Override
    public PropertiesCollector collect(final GridCoverage2DReader gridCoverageReader) {
        String value =
                ((org.geotools.gce.geotiff.GeoTiffReader) gridCoverageReader)
                        .getMetadata()
                        .getAsciiTIFFTag("306");
        if (value != null) {
            addMatch("" + value);
        } else {
            addMatch("");
        }
        return this;
    }

    private Date getDate() {
        String dateStr = getMatches().size() > 0 ? getMatches().get(0) : null;
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                return FORMAT.parse(getMatches().get(0));
            } catch (ParseException e) {
                LOGGER.log(Level.WARNING, "Failed to parse date: " + dateStr, e);
            }
        }
        return null;
    }

    @Override
    public void setProperties(SimpleFeature feature) {
        Date date = getDate();

        if (date != null) {
            for (String propertyName : getPropertyNames()) {
                // set the property
                feature.setAttribute(propertyName, date);
            }
        }
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        Date date = getDate();

        if (date != null) {
            for (String propertyName : getPropertyNames()) {
                // set the property
                map.put(propertyName, date);
            }
        }
    }
}
