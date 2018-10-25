/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.s3.geotiff;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.s3.S3Connector;
import org.geotools.s3.S3ImageInputStreamImpl;
import org.geotools.util.factory.Hints;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * Just a basic wrapper around GeoTiffFormat in order to support GeoTiff over S3. Hopefully this
 * wrapper either won't be permanent, or won't require overriding very many properties/methods
 */
public class S3GeoTiffFormat extends GeoTiffFormat {

    private Properties prop;

    private static final Logger LOGGER = Logger.getLogger(S3GeoTiffFormat.class.getName());

    private static final DefaultParameterDescriptor<String> AWS_REGION =
            new DefaultParameterDescriptor<String>(
                    "AwsRegion", String.class, (String[]) null, "US_EAST_1");

    public S3GeoTiffFormat() {
        writeParameters = null;
        mInfo = new HashMap<String, String>();
        mInfo.put("name", "S3GeoTiff");
        mInfo.put(
                "description", "Tagged Image File Format with Geographic information hosted on S3");
        mInfo.put("vendor", "Boundless Geo");
        mInfo.put("version", "1.0");

        // reading parameters
        readParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo,
                                new GeneralParameterDescriptor[] {
                                    READ_GRIDGEOMETRY2D,
                                    INPUT_TRANSPARENT_COLOR,
                                    SUGGESTED_TILE_SIZE,
                                    AWS_REGION
                                }));

        // writing parameters
        writeParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo,
                                new GeneralParameterDescriptor[] {
                                    RETAIN_AXES_ORDER,
                                    AbstractGridFormat.GEOTOOLS_WRITE_PARAMS,
                                    AbstractGridFormat.PROGRESS_LISTENER
                                }));
        try {
            if (prop == null) {
                prop = new Properties();
                String property = System.getProperty(S3Connector.S3_GEOTIFF_CONFIG_PATH);
                if (property != null) {
                    InputStream resourceAsStream = new FileInputStream(property);
                    prop.load(resourceAsStream);
                } else {
                    LOGGER.severe(
                            "Properties are missing! The system property 's3.properties.location' should be set "
                                    + "and contain the path to the s3.properties file.");
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public GeoTiffReader getReader(Object source, Hints hints) {
        // in practice here source is probably almost always going to be a string.
        try {
            // big old try block since we can't do anything meaningful with an exception anyway
            S3ImageInputStreamImpl inStream;
            if (source instanceof File) {
                throw new UnsupportedOperationException("Can't instantiate S3 with a File handle");
            } else if (source instanceof String) {
                inStream = new S3ImageInputStreamImpl((String) source);
            } else if (source instanceof URL) {
                inStream = new S3ImageInputStreamImpl((URL) source);
            } else {
                throw new IllegalArgumentException(
                        "Can't create S3ImageInputStream from input of "
                                + "type: "
                                + source.getClass());
            }

            return new S3GeoTiffReader(inStream, hints);
        } catch (Exception e) {
            LOGGER.log(
                    Level.FINE,
                    "Exception raised trying to instantiate S3 image input "
                            + "stream from source.",
                    e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean accepts(Object o, Hints hints) {
        if (o == null) {
            return false;
        } else {
            boolean accepts = false;
            if (o instanceof String) {
                String url = (String) o;
                if (url.contains("://")) {
                    accepts = containsS3orAliasPrefix(url.split("://")[0]);
                }
            } else if (o instanceof URL) {
                String protocol = ((URL) o).getProtocol();
                accepts = containsS3orAliasPrefix(protocol);
            }
            return accepts;
        }
    }

    @Override
    public boolean accepts(Object source) {
        return this.accepts(source, null);
    }

    private boolean containsS3orAliasPrefix(String prefix) {
        return "s3".equals(prefix) || prop.get(prefix + ".s3.user") != null;
    }
}
