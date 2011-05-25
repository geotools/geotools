/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.color;


/**
 * DOCUMENT ME!
 *
 * @author James Macgill
 *
 * @source $URL$
 */
public class SampleScheme {
    int[][] sampleScheme = new int[15][];
    int minCount = -1;
    int maxCount = -1;

    /**
     * Creates a new instance of SampleScheme
     */
    public SampleScheme() {
    }

    /**
     * Indexed getter for property sampleScheme.
     *
     * @param length Index of the property.
     *
     * @return Value of the property at <CODE>index</CODE>.
     */
    public int[] getSampleScheme(int length) {
        return this.sampleScheme[length - 2];
    }

    /**
     * Indexed setter for property sampleScheme.
     *
     * @param length Index of the property.
     * @param sampleScheme New value of the property at <CODE>index</CODE>.
     */
    public void setSampleScheme(int length, int[] sampleScheme) {
        this.sampleScheme[length - 2] = sampleScheme;

        if ((minCount == -1) || (minCount > length)) {
            minCount = length;
        }

        if ((maxCount == -1) || (maxCount < length)) {
            maxCount = length;
        }
    }

    /**
     * Getter for the min colour count
     *
     * @return the smallest number of colours we have a scheme for
     */
    public int getMinCount() {
        return minCount;
    }

    /**
     * Getter for the max colour count
     *
     * @return the largest number of colours we have a scheme for
     */
    public int getMaxCount() {
        return maxCount;
    }
}
