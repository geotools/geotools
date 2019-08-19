/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * Coverage format for rasters stored in a PostGIS database as described by the <a
 * href="https://postgis.net/docs/RT_reference.html">PostGIS Raster Reference</a>.
 */
public class PGRasterFormat extends AbstractGridFormat {

    static final Logger LOG = Logging.getLogger(PGRasterFormat.class);

    public PGRasterFormat() {
        final HashMap<String, String> info = new HashMap<String, String>();
        info.put("name", "PGRaster");
        info.put("description", "PostGIS Raster Mosaic Plugin");
        info.put("vendor", "OpenNRM");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        readParameters =
                new ParameterGroup(
                        new DefaultParameterDescriptorGroup(
                                mInfo,
                                new GeneralParameterDescriptor[] {
                                    READ_GRIDGEOMETRY2D, TIME, OVERVIEW_POLICY
                                }));

        writeParameters = null;
    }

    @Override
    public PGRasterReader getReader(Object source) {
        return getReader(source, null);
    }

    @Override
    public PGRasterReader getReader(Object source, Hints hints) {
        try {
            PGRasterConfig config = toConfig(source);
            return config != null ? new PGRasterReader(config, this, hints) : null;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error loading reader", ex);
        }
        return null;
    }

    @Override
    public boolean accepts(Object source, Hints hints) {
        if (source instanceof DataSource) {
            return true;
        }

        return toConfigFile(source) != null;
    }

    File toConfigFile(Object source) {
        if (source instanceof Path) {
            source = ((Path) source).toFile();
        }

        if (source instanceof String) {
            source = new File(source.toString());
        }

        if (source instanceof File) {
            File file = (File) source;
            if (file.getName().toLowerCase().endsWith(".xml")) {
                return file;
            }
        }
        return null;
    }

    PGRasterConfig toConfig(Object source) {
        if (source instanceof PGRasterConfig) {
            return (PGRasterConfig) source;
        }

        File file = toConfigFile(source);
        if (file != null) {
            return new PGRasterConfig(file);
        }

        return null;
    }

    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw writeNotSupported();
    }

    @Override
    public GridCoverageWriter getWriter(Object destination) {
        throw writeNotSupported();
    }

    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw writeNotSupported();
    }

    private UnsupportedOperationException writeNotSupported() {
        return new UnsupportedOperationException("Write support not implemented");
    }
}
