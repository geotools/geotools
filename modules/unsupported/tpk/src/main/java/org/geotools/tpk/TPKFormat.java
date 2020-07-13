/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.tpk;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

public class TPKFormat extends AbstractGridFormat {

    private static final Logger LOGGER = Logging.getLogger(TPKFormat.class);

    public static File getFileFromSource(Object source) {
        if (source == null) {
            return null;
        }

        File sourceFile = null;

        try {
            if (source instanceof File) {
                sourceFile = (File) source;
            } else if (source instanceof URL) {
                if (((URL) source).getProtocol().equals("file")) {
                    sourceFile = URLs.urlToFile((URL) source);
                }
            } else if (source instanceof String) {
                sourceFile = new File((String) source);
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }

            return null;
        }

        return sourceFile;
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
        return new TPKReader(source, hints);
    }

    @Override
    public GridCoverageWriter getWriter(Object destination) {
        return getWriter(destination, null);
    }

    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("Unsupported method: TPK format is read-only.");
    }

    @Override
    public boolean accepts(Object source, Hints hints) {
        if (source == null) {
            return false;
        }

        File sourceFile = getFileFromSource(source);

        if (sourceFile == null) {
            return false;
        }

        // accept files ending with either .tpk or .tpkx
        return sourceFile.getName().matches("^.*\\.tpkx*$");
    }

    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("Unsupported method.");
    }

    /** Creates an instance and sets the metadata. */
    public TPKFormat() {
        setInfo();
    }

    /** Sets the metadata information. */
    private void setInfo() {
        final HashMap<String, String> info = new HashMap<>();
        info.put("name", "TPK");
        info.put("description", "TPK (ESRI Compact Cache) plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo,
                                new GeneralParameterDescriptor[] {READ_GRIDGEOMETRY2D /*,
                       INPUT_TRANSPARENT_COLOR,
                OUTPUT_TRANSPARENT_COLOR,
                USE_JAI_IMAGEREAD,
                BACKGROUND_VALUES,
                SUGGESTED_TILE_SIZE,
                ALLOW_MULTITHREADING,
                MAX_ALLOWED_TILES,
                TIME,
                ELEVATION,
                FILTER,
                ACCURATE_RESOLUTION,
                SORT_BY,
                MERGE_BEHAVIOR,
                FOOTPRINT_BEHAVIOR*/}));

        // reading parameters
        writeParameters = null;
    }
}
