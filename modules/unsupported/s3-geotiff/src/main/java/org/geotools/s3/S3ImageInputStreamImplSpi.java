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
package org.geotools.s3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

/**
 * Implementation of an {@link ImageInputStreamSpi} for instantiating an {@link ImageInputStream}
 * capable of connecting to a {@link S3File}
 *
 * @see ImageInputStream
 * @see ImageInputStreamSpi
 * @see ImageIO#createImageInputStream(Object)
 * @author Andrew Curtis, Boundless
 */
public class S3ImageInputStreamImplSpi extends ImageInputStreamSpi {

    /** Logger. */
    private static final Logger LOGGER =
            Logger.getLogger("it.geosolutions.imageio.stream.input.s3");

    private static final String vendorName = "GeoSolutions";

    private static final String version = "1.0";

    private static final Class<String> inputClass = String.class;

    private static volatile boolean useFileChannel;

    /**
     * Constructs a blank {@link ImageInputStreamSpi}. It is up to the subclass to initialize
     * instance variables and/or override method implementations in order to provide working
     * versions of all methods.
     */
    public S3ImageInputStreamImplSpi() {
        super(vendorName, version, inputClass);
    }

    /** @see ImageInputStreamSpi#getDescription(Locale). */
    public String getDescription(Locale locale) {
        return "Service provider that wraps a file in S3";
    }

    public void onRegistration(ServiceRegistry registry, Class<?> category) {
        super.onRegistration(registry, category);
        Class<ImageInputStreamSpi> targetClass = ImageInputStreamSpi.class;
        for (Iterator<? extends ImageInputStreamSpi> i =
                        registry.getServiceProviders(targetClass, true);
                i.hasNext(); ) {
            ImageInputStreamSpi other = i.next();

            if (this != other) registry.setOrdering(targetClass, this, other);
        }
    }
    /**
     * Returns an instance of the ImageInputStream implementation associated with this service
     * provider.
     *
     * @param input an object of the class type returned by getInputClass.
     * @param useCache a boolean indicating whether a cache eraf should be used, in cases where it
     *     is optional.
     * @param cacheDir a File indicating where the cache eraf should be created, or null to use the
     *     system directory.
     * @return an ImageInputStream instance.
     * @throws IllegalArgumentException if input is not an instance of the correct class or is null.
     */
    public ImageInputStream createInputStreamInstance(
            Object input, boolean useCache, File cacheDir) {
        LOGGER.warning("S3ImageInputStreamImplSpi.createInputStream(" + input.getClass() + ")");

        if (input instanceof S3ImageInputStreamImpl) {
            try {
                return new S3ImageInputStreamImpl(((S3ImageInputStreamImpl) input).getUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String path = (String) input;
            return new S3ImageInputStreamImpl(path);
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return null;
        }
    }
}
