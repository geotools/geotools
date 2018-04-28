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

/**
 * Class responsible for determining whether a given coverage should or should not be part of the
 * image mosaic.
 */
public interface GranuleAcceptor {
    /**
     * @param coverage the coverage being added to the catalog
     * @param inputCoverageName the name of the coverage being added
     * @param fileBeingProcessed File handle of the coverage being handled
     * @param mosaicConfigHandler the mosaic config handler being used
     * @return whether coverage should be added to the mosaic or not
     */
    boolean accepts(
            GridCoverage2DReader coverage,
            String inputCoverageName,
            File fileBeingProcessed,
            ImageMosaicConfigHandler mosaicConfigHandler)
            throws IOException;
}
