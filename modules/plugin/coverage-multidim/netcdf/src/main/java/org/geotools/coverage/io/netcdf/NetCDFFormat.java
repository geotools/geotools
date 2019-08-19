/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;

public class NetCDFFormat extends AbstractGridFormat {

    public static final ParameterDescriptor<Filter> FILTER =
            new DefaultParameterDescriptor<Filter>("Filter", Filter.class, null, null);

    private static final Logger LOGGER = Logging.getLogger(NetCDFFormat.class);

    /** Creates an instance and sets the metadata. */
    public NetCDFFormat() {
        setInfo();
    }

    /** Sets the metadata information. */
    private void setInfo() {
        final HashMap<String, String> info = new HashMap<String, String>();
        info.put("name", "NetCDF");
        info.put("description", "NetCDF store plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo,
                                new GeneralParameterDescriptor[] {
                                    READ_GRIDGEOMETRY2D,
                                    //                        INPUT_TRANSPARENT_COLOR,
                                    //                BACKGROUND_VALUES,
                                    //                SUGGESTED_TILE_SIZE,
                                    //                ALLOW_MULTITHREADING,
                                    //                MAX_ALLOWED_TILES,
                                    TIME,
                                    ELEVATION,
                                    FILTER,
                                    //                SORT_BY,
                                    //                MERGE_BEHAVIOR
                                    BANDS
                                }));

        // reading parameters
        writeParameters = null;
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
        try {

            final NetCDFReader reader = new NetCDFReader(source, hints);
            return reader;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw new UnsupportedOperationException("Writing operation not implemented");
    }

    @Override
    public boolean accepts(Object source, Hints hints) {
        File file = null;
        if (source instanceof URL) {
            file = URLs.urlToFile((URL) source);
        } else if (source instanceof File) {
            file = (File) source;
        }
        if (file != null) {
            if (file.isDirectory()) {
                return false;
            }
            String absolutePath = file.getAbsolutePath();

            if (absolutePath.endsWith("nc") || absolutePath.endsWith("ncml")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("Writing operation not implemented");
    }

    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("Writing operation not implemented");
    }
}
