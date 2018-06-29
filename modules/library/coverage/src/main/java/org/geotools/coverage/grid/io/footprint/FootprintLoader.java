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
     * @param pathNoExtension
     * @return The footprint, or null if the file was not found
     * @throws Exception In case the file was found, but there were issues loading the geometry
     */
    Geometry loadFootprint(String pathNoExtension) throws Exception;
}
