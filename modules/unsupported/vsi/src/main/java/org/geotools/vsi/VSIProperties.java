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
package org.geotools.vsi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.gdal.gdal.gdal;

/**
 * GDAL config loader for VSI Virtual File Systems
 *
 * @author Matthew Northcott <matthewnorthcott@catalyst.net.nz>
 */
public final class VSIProperties extends Properties {

    /**
     * Name of property that defines where the vsi.properties file is Supplied using
     * -D{LOCATION_PROPERTY}=/path/to/vsi.properties in JAVA_OPTS
     */
    public static final String LOCATION_PROPERTY = "vsi.properties.location";

    private final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(VSIFormatFactory.class);

    /** Constructor which parses vsi.properties initially by default */
    public VSIProperties() throws IOException {
        super();
        parse();
    }

    /**
     * Read the vsi.properties file for GDAL configuration options.
     *
     * @throws IOException
     */
    public void parse() throws IOException {
        final String location = System.getProperty(LOCATION_PROPERTY);

        if (location == null) {
            throw new IOException("The system property 'vsi.properties.location' is unset.");
        }

        try (FileInputStream inStream = new FileInputStream(location)) {
            load(inStream);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(
                    "The system property 'vsi.properties.location' is set but "
                            + location
                            + " does not exist.");
        } catch (IOException ex) {
            throw new IOException(
                    "The system property 'vsi.properties.location' is set but "
                            + location
                            + " could not be read.");
        }
    }

    /**
     * Set the configuration options in GDAL, using those found in vsi.properties
     *
     * @throws IOException
     */
    public void apply() throws IOException {
        for (String option : stringPropertyNames()) {
            String value = getProperty(option);

            gdal.SetConfigOption(option, value);
            LOGGER.fine("Set GDAL config option: " + option);
        }
    }
}
