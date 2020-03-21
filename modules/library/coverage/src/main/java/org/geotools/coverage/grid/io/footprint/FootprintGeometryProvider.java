/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.footprint;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

public interface FootprintGeometryProvider {

    /**
     * Retrieves the footprint. If a feature is specified, return the footprint from the current
     * granule representative feature as it comes from the index.
     *
     * @param feature the granule representative feature (if any). Specifying a null feature will
     *     return a default footprint (this is used in general for single-granule stores)
     */
    Geometry getFootprint(SimpleFeature feature) throws IOException;

    /**
     * Returns the list of sidecar files defining masks for the specified feature. The default
     * implementation returns an empty list.
     *
     * @param feature the granule representative feature (if any)
     * @return A list of files defining the masks for this granule (might be more than one)
     */
    default List<File> getSidecars(SimpleFeature feature) throws IOException {
        return Collections.emptyList();
    }

    /**
     * Close up the provider (in case it holds onto persistent resources such as files or database
     * connections)
     */
    void dispose();
}
