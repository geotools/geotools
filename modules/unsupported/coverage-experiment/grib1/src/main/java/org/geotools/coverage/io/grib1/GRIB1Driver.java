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
package org.geotools.coverage.io.grib1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.driver.DefaultFileDriver;
import org.geotools.coverage.io.driver.FileDriver;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

public class GRIB1Driver extends DefaultFileDriver implements FileDriver {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverage.io.grib1");

    /** A static {@link ImageReaderSpi} to be used to call canDecodeInput */
    static ImageReaderSpi spi;

    static {
        try {
            Class<?> clazz = Class
                    .forName("org.geotools.imageio.grib1.GRIB1SpatioTemporalImageReaderSpi");
            spi = (ImageReaderSpi) clazz.newInstance();
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            spi = null;
        }
    }

    public GRIB1Driver() {
        super("GRIB1", "GRIB1 Coverage Format", "GRIB1 Coverage Format", null,
                Arrays.asList("grib1", "grib"));
    }


    protected CoverageAccess connect(java.net.URL source,
            Map<String, Serializable> params, Hints hints,
            ProgressListener listener) throws IOException {
        return new GRIB1Access(this, source, params, hints, listener);
    }

    public boolean isAvailable() {
        return checkClasses();
    }

    public static boolean checkClasses() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {

            Class.forName("javax.media.jai.JAI");
            Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
            Class
                    .forName("org.geotools.imageio.grib1.GRIB1SpatioTemporalImageReaderSpi");
            if (available)
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("GRIB1Driver is availaible.");
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("GRIB1Driver is not availaible.");
            available = false;
        }
        return available;
    }



	@Override
	protected boolean canConnect(URL url, Map<String, Serializable> params) {

        if (url == null) {
            return false;
        }
        ImageInputStream inputStream = null;
        Object source = null;
        try {
            // /////////////////////////////////////////////////////////////
            //
            // URL management
            // In case the URL points to a file we need to get to the file
            // directly and avoid caching. In case it points to http or ftp
            // or it is an open stream we have very small to do and we need
            // to enable caching.
            //
            // /////////////////////////////////////////////////////////////
            if (url.getProtocol().equalsIgnoreCase("file")) {
                File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

                if (file.exists()) {
                    if (!file.canRead() || !file.isFile())
                        return false;
                    // setting source
                    source = file;

                } else
                    return false;
            }

            else {
                if (url.getProtocol().equalsIgnoreCase("http")
                        || url.getProtocol().equalsIgnoreCase("ftp")) {
                    source = url.openStream();

                } else
                    return false;
            }

            // get a stream
            inputStream = (ImageInputStream) ((source instanceof ImageInputStream) ? source
                    : ImageIO.createImageInputStream(source));
            if (inputStream == null) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("Unable to get an ImageInputStream");
                return false;
            }

            inputStream.mark();

            if (!spi.canDecodeInput(inputStream))
                return false;
            return true;
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return false;
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {

                }
            }
            if (source != null && (source instanceof InputStream)) {
                try {
                    ((InputStream) source).close();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
        }
	}


	public EnumSet<DriverOperation> getDriverCapabilities() {
		return EnumSet.of(DriverOperation.CONNECT);
	}
}
