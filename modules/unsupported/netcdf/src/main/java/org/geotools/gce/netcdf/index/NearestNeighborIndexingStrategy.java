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

import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import ucar.ma2.Array;

/**
 * A nearest neighbor implementation for the strategy for finding the index of
 * the coordinate value in a coordinate array from a NetCDF file. Computes a
 * rough index value based on the resolution of the first two points, then
 * brute force checks neighboring values to find the true closest.
 *
 * @author Yancy Matherne - Geocent
 */
public class NearestNeighborIndexingStrategy implements IndexingStrategy {

    private static final Logger LOG = Logging.getLogger(NearestNeighborIndexingStrategy.class);

    /**
     * Find the index of the closest coordinate value in an array.
     *
     * @param coordArray
     * @param desiredCoord
     * @return int
     */
    public int getCoordinateIndex(Array coordArray, double desiredCoord) {
        if (coordArray.getRank() != 1) {
            LOG.severe("ERROR passed a non-1D array into a function that wasn't expecting it");
            return 0;
        }

        int firstArrayIndex = 0;
        int lastArrayIndex = (int)coordArray.getSize() - 1;

        // Get the file's first coordinate
        double firstArrayCoord = coordArray.getDouble(firstArrayIndex);

        // Get the file's last coordinate
        double lastArrayCoord = coordArray.getDouble(lastArrayIndex);

        // Get the distance between the array's first two coordinates to use as
        // a grid resolution.
        double resolution = getAbsoluteDifference(firstArrayCoord,
                coordArray.getDouble(1));
        // If the desired coordinate is less than the array's first
        // coordinate, and the coordinate array is always sorted so that the
        // values increase, then the desired value cant be in the array.
        if (desiredCoord < firstArrayCoord) {
            // We are assuming nearest neighbor here.
            // So check if the desired coordinate is within half the resolution
            // of the array's first coordinate (i.e., half the resolution on
            // either side belongs to the original grid point).
            if (getAbsoluteDifference(desiredCoord, firstArrayCoord) < (resolution / 2)) {
                return firstArrayIndex;
            } else {
                return -1;
            }
        }

        // If the desired coordinate is greater than the array's last
        // coordinate, and the coordinate array is always sorted so that the
        // values increase, then the desired value cant be in the array.
        if (desiredCoord > lastArrayCoord) {
            // We are assuming nearest neighbor here.
            // So check if the desired coordinate is within half the resolution
            // of the array's last coordinate (i.e., half the resolution on
            // either side belongs to the original grid point).
            if (getAbsoluteDifference(desiredCoord, lastArrayCoord) <= (resolution / 2)) {
                return lastArrayIndex;
            } else {
                return -1;
            }
        }

        // The desired coordinate is somewhere after the array's first
        // coordinate. Using math, the array's first coordinate and
        // resolution, find the index of the desired coordinate.
        int index = (int) Math.round((desiredCoord - firstArrayCoord)
                / resolution);
        // Check for index out of bounds.
        if (index >= coordArray.getSize()) {
            // We already checked the desired coordinate's value against the
            // last coordinate value's in the array. So the value must be in
            // here. We must be off because of an inconsistent grid resolution
            // in the file.
            index = lastArrayIndex;
        }

        // We have seen NetCDFs where the spacing isn't consistent throughout
        // the entire file. So, check the neighboring coordinates to make sure
        // we are at the true closest index.
        return checkNeighbors(coordArray, desiredCoord, index);
    }

    private int checkNeighbors(Array coordArray, double desiredCoord,
            int startingIndex) {
        // If we are at the beginning of the array, avoid index out of bounds.
        if (startingIndex == 0) {
            return findClosestIndexToTheRight(coordArray, desiredCoord,
                    startingIndex);
        }
        // If we are at the end of the array, avoid index out of bounds.
        if (startingIndex == (coordArray.getSize() - 1)) {
            return findClosestIndexToTheLeft(coordArray, desiredCoord,
                    startingIndex);
        }

        // If we are in the middle somewhere, check both sides.
        double prevCoord = coordArray.getDouble(startingIndex - 1);
        double currCoord = coordArray.getDouble(startingIndex);
        double nextCoord = coordArray.getDouble(startingIndex + 1);

        double prevRez = getAbsoluteDifference(desiredCoord, prevCoord);
        double currRez = getAbsoluteDifference(desiredCoord, currCoord);
        double nextRez = getAbsoluteDifference(desiredCoord, nextCoord);

        if (prevRez <= currRez && prevRez < nextRez) {
            return findClosestIndexToTheLeft(coordArray, desiredCoord,
                    startingIndex - 1);
        } else if (nextRez < currRez && nextRez < prevRez) {
            return findClosestIndexToTheRight(coordArray, desiredCoord,
                    startingIndex + 1);
        }

        return startingIndex;
    }

    private int findClosestIndexToTheRight(Array coordArray,
            double desiredCoord, int startingIndex) {
        if (startingIndex == (coordArray.getSize() - 1)) {
            return startingIndex;
        }

        double currCoord = coordArray.getDouble(startingIndex);
        double nextCoord = coordArray.getDouble(startingIndex + 1);
        double currRez = getAbsoluteDifference(desiredCoord, currCoord);
        double nextRez = getAbsoluteDifference(desiredCoord, nextCoord);

        if (nextRez < currRez) {
            return findClosestIndexToTheRight(coordArray, desiredCoord,
                    startingIndex + 1);
        } else {
            return startingIndex;
        }
    }

    private int findClosestIndexToTheLeft(Array coordArray, double desiredCoord,
            int startingIndex) {
        if (startingIndex == 0) {
            return startingIndex;
        }

        double prevCoord = coordArray.getDouble(startingIndex - 1);
        double currCoord = coordArray.getDouble(startingIndex);
        double prevRez = getAbsoluteDifference(desiredCoord, prevCoord);
        double currRez = getAbsoluteDifference(desiredCoord, currCoord);

        if (prevRez <= currRez) {
            return findClosestIndexToTheLeft(coordArray, desiredCoord,
                    startingIndex - 1);
        } else {
            return startingIndex;
        }
    }

    /*
     * Returns the absolute value of the difference of two numbers.
     */
    private double getAbsoluteDifference(double a, double b) {
        return Math.abs(a - b);
    }

}
