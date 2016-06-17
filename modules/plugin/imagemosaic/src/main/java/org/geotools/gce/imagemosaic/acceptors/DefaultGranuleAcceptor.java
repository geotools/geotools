/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.acceptors;

import java.io.File;
import java.util.logging.Level;

import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.imagemosaic.ImageMosaicConfigHandler;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Default predicate for accepting/rejecting granules in an image mosaic
 */
public class DefaultGranuleAcceptor implements GranuleAcceptor {
    @Override
    public boolean accepts(GridCoverage2DReader coverage, String coverageName,
            File fileBeingProcessed, ImageMosaicConfigHandler mosaicConfig) {
        CoordinateReferenceSystem expectedCRS = mosaicConfig.getDefaultCRS(coverageName);
        CoordinateReferenceSystem actualCRS = coverage.getCoordinateReferenceSystem();

        if (!(CRS.equalsIgnoreMetadata(expectedCRS, actualCRS))) {
            return false;
        }



        return true;
    }
}
