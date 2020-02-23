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
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;

/**
 * Enum that can be use to distinguish between relative paths and absolute paths when trying to load
 * a granuleDescriptor for a mosaic.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties URLs
 */
public enum PathType {
    RELATIVE {

        @Override
        public URL resolvePath(final String parentLocation, final String location) {
            // initial checks
            Utilities.ensureNonNull("parentLocation", parentLocation);
            Utilities.ensureNonNull("location", location);
            if (LOGGER.isLoggable(Level.FINE)) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Trying to resolve path:").append("\n");
                builder.append("type:").append(this.toString()).append("\n");
                builder.append("parentLocation:").append(parentLocation).append("\n");
                builder.append("location:").append(location);
                LOGGER.fine(builder.toString());
            }
            // create a URL for the provided location, relative to parent location
            try {
                URL rasterURL = URLs.extendUrl(new URL(parentLocation), location);
                if (!Utils.checkURLReadable(rasterURL)) {
                    if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.info("Unable to read image for file " + rasterURL);

                    return null;
                }
                return rasterURL;
            } catch (MalformedURLException e) {
                return null;
            }
        }
    },

    ABSOLUTE {

        @Override
        public URL resolvePath(final String parentLocation, final String location) {

            Utilities.ensureNonNull("location", location);
            if (LOGGER.isLoggable(Level.FINE)) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Trying to resolve path:").append("\n");
                builder.append("type:").append(this.toString()).append("\n");
                if (parentLocation != null)
                    builder.append("parentLocation:").append(parentLocation).append("\n");
                LOGGER.fine(builder.toString());
            }
            // create a file for the provided location ignoring the parent type
            // create a URL for the provided location, relative to parent location
            try {
                File rasterFile = new File(location);
                if (!Utils.checkFileReadable(rasterFile)) {
                    URL rasterURL = new URL(location);
                    if (!Utils.checkURLReadable(rasterURL)) {
                        if (LOGGER.isLoggable(Level.INFO))
                            LOGGER.info("Unable to read image for file " + rasterURL);

                        return null;
                    }
                    return rasterURL;

                } else {
                    return URLs.fileToUrl(rasterFile);
                }

            } catch (MalformedURLException e) {
                return null;
            }
        }
    },

    URL {
        @Override
        public URL resolvePath(final String parentLocation, final String location) {
            Utilities.ensureNonNull("location", location);
            if (LOGGER.isLoggable(Level.FINE)) {
                final StringBuilder builder = new StringBuilder();
                builder.append("Trying to resolve path:").append("\n");
                builder.append("type:").append(this.toString()).append("\n");
                LOGGER.fine(builder.toString());
            }
            try {
                return new URL(location);
            } catch (MalformedURLException e) {
                return null;
            }
        }
    };

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(PathType.class);

    /**
     * Resolve a path for a granuleDescriptor given the parent location and location itself.
     *
     * <p>the location can never be null, while the parent location could be null, as an instance
     * when the path is relative.
     *
     * @return a {@link File} instance that points to a location which could be relative or absolute
     *     depending on the flavor of the enum where this method is applied. This method might
     *     return <code>null</code> in case something bad happens.
     */
    public abstract URL resolvePath(final String parentLocation, final String location);
}
