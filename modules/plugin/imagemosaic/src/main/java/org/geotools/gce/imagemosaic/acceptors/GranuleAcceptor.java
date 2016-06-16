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

import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * Class responsible for determining whether a given coverage should or should not be part of the
 * image mosaic.
 */
public interface GranuleAcceptor {
    boolean accepts(GridCoverageReader coverage, String coverageName, File fileBeingProcessed,
            MosaicConfigurationBean mosaicConfig);
}
