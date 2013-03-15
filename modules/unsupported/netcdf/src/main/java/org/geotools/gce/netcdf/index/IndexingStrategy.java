/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.netcdf.index;

import ucar.ma2.Array;

/**
 * Using a strategy pattern to provide multiple implementations for finding the
 * index for a latitude or longitude coordinate value in a gridded NetCDF file.
 *
 * @author Yancy Matherne - Geocent
 */
public interface IndexingStrategy {

    /**
     * Returns the array index for the desired coordinate or -1 if not in the
     * array.
     *
     * @param coordArray
     * @param desiredCoordinate
     * @return int
     */
    public int getCoordinateIndex(Array coordArray, double desiredCoordinate);
}
