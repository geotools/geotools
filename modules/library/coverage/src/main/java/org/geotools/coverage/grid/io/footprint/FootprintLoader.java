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
import java.util.List;
import org.locationtech.jts.geom.Geometry;

/**
 * Helper that loads a sidecar footprint file in a certain format
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface FootprintLoader {

    /**
     * Tries to load the sidecar geometry given the granule path without extension.
     *
     * @return The footprint, or null if the file was not found
     * @throws Exception In case the file was found, but there were issues loading the geometry
     */
    Geometry loadFootprint(String pathNoExtension) throws Exception;

    /**
     * Returns the footprint file used to load the footprint, or none if there was no file involved
     *
     * @param pathNoExtension The base path plus the file name, but no extension
     * @return @return The files used to load the footprints, or an empty list otherwise
     */
    default List<File> getFootprintFiles(String pathNoExtension) throws IOException {
        return null;
    }
}
