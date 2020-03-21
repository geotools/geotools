/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.composite;

import java.awt.image.WritableRaster;

/**
 * Allows access to a grid of pixels in terms of red, green, blue and alpha components as opposed to
 * raw values
 *
 * @author Andrea Aime - GeoSolutions
 */
interface RgbaAccessor {

    /** Sets the current row. This will cause the current row to be read in memory */
    void readRow(int y);

    /** Gets the color at the current row/column from the in memory buffer of the current row */
    void getColor(int x, int[] rgba);

    /**
     * Sets the color in the current row memory buffer
     *
     * @param x The column to be written
     */
    public void setColor(int x, int r, int g, int b, int a);

    /** Writes the current row memory buffer back into the Raster */
    public void writeRow(int y, WritableRaster destination);
}
