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
import java.io.IOException;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.imagemosaic.ImageMosaicConfigHandler;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Check for homogeneous CRS in the upcoming granule.
 *
 * <p>If the upcoming granules has a CRS which is not homogenenous with the one for the mosaic we
 * have to discard it.
 */
public class HomogeneousCRSAcceptor implements GranuleAcceptor {

    @Override
    public boolean accepts(
            GridCoverage2DReader coverage,
            String inputCoverageName,
            File fileBeingProcessed,
            ImageMosaicConfigHandler mosaicConfigHandler)
            throws IOException {
        String targetCoverageName =
                mosaicConfigHandler.getTargetCoverageName(coverage, inputCoverageName);
        MosaicConfigurationBean config =
                mosaicConfigHandler.getConfigurations().get(targetCoverageName);
        return config == null || checkCRS(coverage, config, inputCoverageName);
    }

    private boolean checkCRS(
            GridCoverage2DReader coverage,
            MosaicConfigurationBean config,
            String inputCoverageName) {
        CoordinateReferenceSystem expectedCRS = config.getCrs();
        CoordinateReferenceSystem actualCRS =
                coverage.getCoordinateReferenceSystem(inputCoverageName);

        return CRS.equalsIgnoreMetadata(expectedCRS, actualCRS);
    }
}
