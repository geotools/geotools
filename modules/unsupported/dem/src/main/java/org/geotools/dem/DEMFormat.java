/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dem;

import java.io.IOException;
import java.util.HashMap;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;

/**
 *
 * @author Devon Tucker
 * @author Niels Charlier
 */
public class DEMFormat extends AbstractGridFormat implements Format {
    
    /** Optional Sorting for the granules of the mosaic.
     * 
     *  <p>It does work only with DBMS as indexes
     */
    public static final ParameterDescriptor<String> SORT_BY = new DefaultParameterDescriptor<String>("SORTING", String.class, null, 
            "location");

    public DEMFormat() {
        HashMap<String,String> info = new HashMap<String,String> ();
        info.put("name", "Digital Elevation Model");
        info.put("description", "DEM based on disparate rasters");
        info.put("vendor", "GeoTools");
        info.put("docURL", "");
        info.put("version", "1.0");
        this.mInfo = info;
        

        // reading parameters
        readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
                new GeneralParameterDescriptor[]{
                        READ_GRIDGEOMETRY2D,
                        INPUT_TRANSPARENT_COLOR,
                ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR,
                USE_JAI_IMAGEREAD,
                ImageMosaicFormat.BACKGROUND_VALUES,
                SUGGESTED_TILE_SIZE,
                ImageMosaicFormat.ALLOW_MULTITHREADING,
                ImageMosaicFormat.MAX_ALLOWED_TILES,
                TIME,
                ELEVATION,
                ImageMosaicFormat.FILTER,
                ImageMosaicFormat.ACCURATE_RESOLUTION,
                SORT_BY,
                ImageMosaicFormat.MERGE_BEHAVIOR,
                ImageMosaicFormat.FOOTPRINT_BEHAVIOR
        }));
    }

    @Override public AbstractGridCoverage2DReader getReader(Object source) {
        return getReader(source, null);
    }

    @Override
    public AbstractGridCoverage2DReader getReader(Object source, Hints hints) {
                
        try {                       
            return new ImageMosaicReader(source, hints, new DEMCatalogManager());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override public GridCoverageWriter getWriter(Object destination) {
        return null;
    }

    @Override public boolean accepts(Object source, Hints hints) {
        return true;
    }

    @Override public GeoToolsWriteParams getDefaultImageIOWriteParameters() {
        return null;
    }

    @Override public GridCoverageWriter getWriter(Object destination, Hints hints) {
        return null;
    }

}
