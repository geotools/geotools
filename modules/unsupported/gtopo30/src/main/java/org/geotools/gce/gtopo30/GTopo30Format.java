/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.gtopo30;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.DataSourceException;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * Provides basic information about the GTOPO30 format IO.
 *
 * @author Simone Giannecchini
 * @author mkraemer
 */
public final class GTopo30Format extends AbstractGridFormat implements Format {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GTopo30Format.class);

    /** Creates an instance and sets the metadata. */
    public GTopo30Format() {
        mInfo = new HashMap<String, String>();
        mInfo.put("name", "Gtopo30");
        mInfo.put("description", "Gtopo30 Coverage Format");
        mInfo.put("vendor", "Geotools");
        mInfo.put("docURL", "http://edcdaac.usgs.gov/gtopo30/gtopo30.asp");
        mInfo.put("version", "1.0");

        // reading parameters
        readParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo, new GeneralParameterDescriptor[] {READ_GRIDGEOMETRY2D}));

        // reading parameters
        writeParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo, new GeneralParameterDescriptor[] {GEOTOOLS_WRITE_PARAMS}));
    }

    /**
     * Returns a reader object which you can use to read GridCoverages from a given source
     *
     * @param o the the source object. This can be a File, an URL or a String (representing a
     *     filename or an URL)
     * @return a GridCoverageReader object or null if the source object could not be accessed.
     */
    @Override
    public GTopo30Reader getReader(final Object o) {
        return getReader(o, null);
    }

    /**
     * Returns a writer object which you can use to write GridCoverages to a given destination.
     *
     * @param destination The destination object
     * @return a GridCoverageWriter object
     */
    @Override
    public GridCoverageWriter getWriter(final Object destination) {
        try {
            return new GTopo30Writer(destination);
        } catch (DataSourceException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Returns a writer object which you can use to write GridCoverages to a given destination.
     *
     * @param destination The destination object
     * @return a GridCoverageWriter object
     */
    @Override
    public GridCoverageWriter getWriter(final Object destination, Hints hints) {
        try {
            return new GTopo30Writer(destination, hints);
        } catch (DataSourceException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }
    /**
     * Checks if the GTopo30DataSource supports a given file
     *
     * @param o the source object to test for compatibility with this format. This can be a File, an
     *     URL or a String (representing a filename or an URL)
     * @return if the source object is compatible
     */
    @Override
    public boolean accepts(final Object o, Hints hints) {
        URL urlToUse;

        if (o instanceof File) {
            try {
                urlToUse = ((File) o).toURI().toURL();
            } catch (MalformedURLException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                return false;
            }
        } else if (o instanceof URL) {
            // we only allow files
            urlToUse = (URL) o;
        } else if (o instanceof String) {
            try {
                // is it a filename?
                urlToUse = new File((String) o).toURI().toURL();
            } catch (MalformedURLException e) {
                // is it a URL
                try {
                    urlToUse = new URL((String) o);
                } catch (MalformedURLException e1) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    return false;
                }
            }
        } else {
            return false;
        }

        // trying to create a reader
        try {
            final GTopo30Reader reader = new GTopo30Reader(urlToUse);
            reader.dispose();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }

    /**
     * Returns a reader object which you can use to read GridCoverages from a given source
     *
     * @param o the the source object. This can be a File, an URL or a String (representing a
     *     filename or an URL)
     * @return a GridCoverageReader object or null if the source object could not be accessed.
     */
    @Override
    public GTopo30Reader getReader(final Object o, Hints hints) {

        try {
            return new GTopo30Reader(o);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Always returns null since for the moment there are no {@link GeoToolsWriteParams} available
     * for this format.
     *
     * @return always null.
     */
    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        return null;
    }
}
