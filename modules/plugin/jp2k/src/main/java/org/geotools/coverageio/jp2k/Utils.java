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
package org.geotools.coverageio.jp2k;

import it.geosolutions.imageio.stream.AccessibleStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.api.data.DataSourceException;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;

/**
 * Sparse utilities for the various classes. I use them to extract complex code from other places.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 */
class Utils {

    static final IOFileFilter FILEFILTER = createFilter();

    /** Logger. */
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(Utils.class);

    static URL checkSource(Object source) throws MalformedURLException, DataSourceException {
        URL sourceURL = null;
        // /////////////////////////////////////////////////////////////////////
        //
        // Check source
        //
        // /////////////////////////////////////////////////////////////////////
        // if it is a URL or a String let's try to see if we can get a file to
        // check if we have to build the index
        if (source instanceof URL) {
            sourceURL = (URL) source;
            source = URLs.urlToFile(sourceURL);
        } else if (source instanceof String) {
            // is it a File?
            final String tempSource = (String) source;
            File tempFile = new File(tempSource);
            if (!tempFile.exists()) {
                // is it a URL
                try {
                    sourceURL = new URL(tempSource);
                    source = URLs.urlToFile(sourceURL);
                } catch (MalformedURLException e) {
                    sourceURL = null;
                    source = null;
                }
            } else {
                sourceURL = URLs.fileToUrl(tempFile);
                source = tempFile;
            }
        } else if (source instanceof AccessibleStream && ((AccessibleStream) source).getTarget() instanceof File) {
            final File inputFile = (File) ((AccessibleStream) source).getTarget();
            source = inputFile;
        }

        // at this point we have tried to convert the thing to a File as hard as
        // we could, let's see what we can do
        if (source instanceof File) {
            final File sourceFile = (File) source;
            if (!sourceFile.isDirectory()) sourceURL = ((File) source).toURI().toURL();
        } else sourceURL = null;
        return sourceURL;
    }

    /**
     * Look for an {@link ImageReader} instance that is able to read the provided {@link ImageInputStream}, which must
     * be non null.
     *
     * <p>In case no reader is found, <code>null</code> is returned.
     *
     * @param inStream an instance of {@link ImageInputStream} for which we need to find a suitable {@link ImageReader}.
     * @return a suitable instance of {@link ImageReader} or <code>null</code> if one cannot be found.
     */
    static ImageReader getReader(final ImageInputStream inStream) {
        Utilities.ensureNonNull("inStream", inStream);
        // get a reader
        //		inStream.mark();
        try {
            if (inStream instanceof AccessibleStream && ((AccessibleStream) inStream).getTarget() instanceof File) {
                final File file = (File) ((AccessibleStream) inStream).getTarget();
                if (FILEFILTER.accept(file))
                    return JP2KFormatFactory.getCachedSpi().createReaderInstance();
            }
            return null;

        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) LOGGER.warning(e.getLocalizedMessage());
            return null;
        }
    }

    /** Retrieves an {@link ImageInputStream} for the provided input {@link File} . */
    static ImageInputStream getInputStream(final File file) throws IOException {
        final ImageInputStream inStream = ImageIO.createImageInputStream(file);
        return inStream;
    }

    /** Setup a double from an array of bytes. */
    public static double bytes2double(final byte[] bytes, final int start) {
        int i = 0;
        final int length = 8;
        int count = 0;
        final byte[] tmp = new byte[length];
        for (i = start; i < start + length; i++) {
            tmp[count] = bytes[i];
            count++;
        }
        long accum = 0;
        i = 0;
        for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
            accum |= (long) (tmp[i] & 0xff) << shiftBy;
            i++;
        }
        return Double.longBitsToDouble(accum);
    }

    /** Setup a long from an array of bytes. */
    public static long bytes2long(final byte[] bytes, final int start) {
        int i = 0;
        final int length = 4;
        int count = 0;
        final byte[] tmp = new byte[length];
        for (i = start; i < start + length; i++) {
            tmp[count] = bytes[i];
            count++;
        }
        long accum = 0;
        i = 0;
        for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
            accum |= (long) (tmp[i] & 0xff) << shiftBy;
            i++;
        }
        return accum;
    }

    private static IOFileFilter createFilter() {
        IOFileFilter fileFilter = Utils.includeFilters(
                FileFilterUtils.suffixFileFilter("jp2"),
                FileFilterUtils.suffixFileFilter("JP2"),
                FileFilterUtils.suffixFileFilter("j2c"),
                FileFilterUtils.suffixFileFilter("J2C"),
                FileFilterUtils.suffixFileFilter("jpx"),
                FileFilterUtils.suffixFileFilter("JPX"),
                FileFilterUtils.suffixFileFilter("jp2k"),
                FileFilterUtils.suffixFileFilter("JP2K"),
                FileFilterUtils.nameFileFilter("jpeg2000"));
        return fileFilter;
    }

    static IOFileFilter excludeFilters(final IOFileFilter inputFilter, IOFileFilter... filters) {
        IOFileFilter retFilter = inputFilter;
        for (IOFileFilter filter : filters) {
            retFilter = FileFilterUtils.and(retFilter, FileFilterUtils.notFileFilter(filter));
        }
        return retFilter;
    }

    static IOFileFilter includeFilters(final IOFileFilter inputFilter, IOFileFilter... filters) {
        IOFileFilter retFilter = inputFilter;
        for (IOFileFilter filter : filters) {
            retFilter = FileFilterUtils.or(retFilter, filter);
        }
        return retFilter;
    }
}
