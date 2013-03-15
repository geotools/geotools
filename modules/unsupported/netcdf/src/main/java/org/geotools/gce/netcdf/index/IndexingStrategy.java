/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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
