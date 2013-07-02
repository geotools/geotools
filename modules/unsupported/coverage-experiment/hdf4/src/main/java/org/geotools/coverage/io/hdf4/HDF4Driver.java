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
package org.geotools.coverage.io.hdf4;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;

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

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.FileDriver;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

/**
 * 
 *
 * @source $URL$
 */
public class HDF4Driver extends DefaultFileDriver implements FileDriver {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(HDF4Driver.class.toString());

    /** A static {@link ImageReaderSpi} to be used to call canDecodeInput */
    static ImageReaderSpi spi;
    
    static String classUsed;

    static {
        try {
            classUsed = "org.geotools.imageio.hdf4.aps.HDF4APSImageReaderSpi";
            Class< ? > clazz = Class.forName(classUsed);

            // FIXME how to handle the fact that now we have two classes
            // Class<?> clazz =
            // Class.forName("org.geotools.imageio.hdf4.terascan.HDF4TeraScanImageReaderSpi");
            spi = (ImageReaderSpi) clazz.newInstance();
        } catch (Throwable e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            spi = null;
        }
    }

    public HDF4Driver() {
        super("HDF4", "HDF4 Coverage Format", "HDF4 Coverage Format", null, Arrays.asList("hdf4", "hdf"));
    }

    protected CoverageAccess connect(java.net.URL source, Map<String, Serializable> params, Hints hints,
            ProgressListener listener) throws IOException {
        return new HDF4Access(this, source, params, hints, listener);
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
            Class.forName(classUsed);
            
            if (available)
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("HDF4Driver is availaible.");
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("HDF4Driver is not availaible.");
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

            // get a stream
            inputStream = (ImageInputStream) ((source instanceof ImageInputStream) ? source
                    : new FileImageInputStreamExtImpl((File)source));
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
