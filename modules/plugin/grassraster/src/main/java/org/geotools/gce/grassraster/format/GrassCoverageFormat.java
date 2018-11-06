/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.format;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.grassraster.GrassCoverageReader;
import org.geotools.gce.grassraster.GrassCoverageWriter;
import org.geotools.gce.grassraster.JGrassUtilities;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * Provides basic information about the grass raster format IO.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public final class GrassCoverageFormat extends AbstractGridFormat implements Format {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GrassCoverageFormat.class);

    /** Creates an instance and sets the metadata. */
    public GrassCoverageFormat() {
        mInfo = new HashMap<String, String>();
        mInfo.put("name", "grass");
        mInfo.put("description", "Grass Coverage Format");
        mInfo.put("vendor", "Geotools");

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

    public GrassCoverageReader getReader(final Object o) {
        return getReader(o, null);
    }

    public GrassCoverageWriter getWriter(final Object destination, Hints hints) {
        try {
            return new GrassCoverageWriter(destination);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public GridCoverageWriter getWriter(Object destination) {
        return getWriter(destination, null);
    }

    public boolean accepts(final Object o, Hints hints) {
        File fileToUse;

        if (o instanceof File) {
            fileToUse = (File) o;
        } else if (o instanceof URL) {
            fileToUse = URLs.urlToFile((URL) o);
        } else if (o instanceof String) {
            fileToUse = new File((String) o);
        } else {
            return false;
        }
        if (!fileToUse.exists()) {
            return false;
        }
        if (JGrassUtilities.isGrass(fileToUse.getAbsolutePath())) {
            return true;
        }
        return false;
    }

    public GrassCoverageReader getReader(final Object o, Hints hints) {

        try {
            GrassCoverageReader coverageReader = new GrassCoverageReader(o);
            return coverageReader;
        } catch (Exception e) {
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
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        return null;
    }
}
