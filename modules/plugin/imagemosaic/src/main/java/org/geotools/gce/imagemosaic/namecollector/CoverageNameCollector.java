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
package org.geotools.gce.imagemosaic.namecollector;

import java.util.Map;
import org.geotools.coverage.grid.io.GridCoverage2DReader;

/** The {@link CoverageNameCollector} instance */
public interface CoverageNameCollector {

    /**
     * Return the coverage Name based on properties collected from optional input {@link
     * GridCoverage2DReader} and map Specific implementations may ignore the reader, the map or
     * both.
     */
    String getName(GridCoverage2DReader reader, Map<String, String> map);
}
