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
 */
package org.geotools.geopkg.mosaic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.factory.gridshift.DataUtilities;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;

/**
 * GeoPackage Grid Format (supports the GP mosaic datastore).
 * 
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPackageFormat extends AbstractGridFormat {
       
    private final static Logger LOGGER = Logging.getLogger(GeoPackageFormat.class.getPackage().getName());

    public static File getFileFromSource(Object source) {
        if (source == null) {
            return null;
        }

        File sourceFile = null;

        try {
            if (source instanceof File) {
                sourceFile = (File) source;
            } else if (source instanceof URL) {
                if (((URL) source).getProtocol().equals("file")){
                    sourceFile = DataUtilities.urlToFile((URL) source);
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
        try {
            return new GeoPackageReader(source, hints);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public GridCoverageWriter getWriter(Object destination) {
        return getWriter(destination, null);
    }
    
    @Override
    public GridCoverageWriter getWriter(Object destination, Hints hints) {
        throw new UnsupportedOperationException("Unsupported method: Geopackage format is read-only.");
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
        
        //TODO: check if it is proper sqlite and geopackage file
        return sourceFile.getName().endsWith(".gpkg");
    }

    @Override
    public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        throw new UnsupportedOperationException("Unsupported method.");
    }
    
    /**
     * Creates an instance and sets the metadata.
     */
    public GeoPackageFormat() {
        setInfo();
    }

    /**
     * Sets the metadata information.
     */
    private void setInfo() {
        final HashMap<String,String> info = new HashMap<String,String> ();
        info.put("name", "GeoPackage (mosaic)");
        info.put("description", "GeoPackage mosaic plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
                new GeneralParameterDescriptor[]{
                        READ_GRIDGEOMETRY2D /*,
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
                FOOTPRINT_BEHAVIOR*/
        }));

        // reading parameters
        writeParameters = null;
    }

}
