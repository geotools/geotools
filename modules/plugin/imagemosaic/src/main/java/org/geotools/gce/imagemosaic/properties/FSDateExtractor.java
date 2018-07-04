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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/** @author Niels Charlier */
class FSDateExtractor extends PropertiesCollector {

    private static final Logger LOGGER = Logging.getLogger(FSDateExtractor.class);

    private Date date = null;

    public FSDateExtractor(PropertiesCollectorSPI spi, List<String> propertyNames) {
        super(spi, propertyNames);
    }

    @Override
    public PropertiesCollector collect(final File file) {
        try {
            BasicFileAttributes attributes =
                    Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            date = new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
        return this;
    }

    @Override
    public void setProperties(SimpleFeature feature) {
        if (date != null) {
            for (String propertyName : getPropertyNames()) {
                // set the property
                feature.setAttribute(propertyName, date);
            }
        }
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        if (date != null) {
            for (String propertyName : getPropertyNames()) {
                // set the property
                map.put(propertyName, date);
            }
        }
    }
}
