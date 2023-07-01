/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.api.feature.simple.SimpleFeature;

/**
 * {@link PropertiesCollector} that is able to collect properties from a file name.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class RuntimeExtractor extends PropertiesCollector {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(RuntimeExtractor.class);

    Date date;

    enum RuntimeType {
        MODIFY_TIME
    }

    String type = null;

    public RuntimeExtractor(PropertiesCollectorSPI spi, List<String> propertyNames, String type) {
        super(spi, propertyNames);
        this.type = type;
    }

    @Override
    public void setProperties(SimpleFeature feature) {

        for (String propertyName : getPropertyNames()) {
            // set the property
            feature.setAttribute(propertyName, date);
        }
    }

    @Override
    public void setProperties(Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RuntimeExtractor collect(File file) {
        super.collect(file);

        // get name of the file
        if (type.equalsIgnoreCase(RuntimeType.MODIFY_TIME.toString())) {

            // TODO Need to take into account locale?
            date = new Date(file.lastModified());
        }
        // final String name = FilenameUtils.getBaseName(file.getAbsolutePath());

        return this;
    }

    @Override
    public RuntimeExtractor collect(URL url) {
        super.collect(url);
        if (type.equalsIgnoreCase(RuntimeType.MODIFY_TIME.toString())) {
            String warningMessage = null;
            try {
                URLConnection connection = url.openConnection();
                long lastModified = connection.getHeaderFieldDate("Last-Modified", -1);
                if (lastModified != -1) {
                    date = new Date(lastModified);
                } else {
                    date = new Date();
                    warningMessage =
                            "Unable to extract the last modified date from the provided url " + url;
                }
            } catch (IOException ioe) {
                warningMessage =
                        "Unable to extract the last modified date from the provided url "
                                + url
                                + " due to "
                                + ioe.getLocalizedMessage();
            }
            if (warningMessage != null && LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(warningMessage);
            }
        }
        return this;
    }
}
