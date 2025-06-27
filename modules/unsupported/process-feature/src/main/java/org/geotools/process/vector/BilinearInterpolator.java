/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

/**
 * Interpolates a grid to a grid of different dimensions using bilinear interpolation.
 *
 * <p>NO_DATA cell values are supported in the source grid. There are two ways of handling the boundary between NO_DATA
 * cells and data cells:
 *
 * <ul>
 *   <li><b>Truncate</b> - If any source cell is NO_DATA, the dest value is NO_DATA. This is simple and fast, but does
 *       make the data boundaries look a bit ragged.
 *   <li><b>Smooth</b> - If only one source cell is NO_DATA, the value is interpolated using the 3 valid values, across
 *       one-half of the interpolated cells. This smoothes off the boundary. If 2 or more source cells are NO_DATA, then
 *       Truncation is used.
 * </ul>
 *
 * <p>Reference: http://en.wikipedia.org/wiki/Bilinear_interpolation.
 *
 * @author Martin Davis - OpenGeo
 */
public class BilinearInterpolator {

    private static final float NULL_NO_DATA = Float.NaN;
    private final float[][] src;
    private float noDataValue = NULL_NO_DATA;

    /**
     * Creates a new interpolator on a given source grid.
     *
     * @param src the source grid
     */
    public BilinearInterpolator(final float[][] src) {
        this(src, NULL_NO_DATA);
    }

    /**
     * Creates a new interpolator on a given source grid.
     *
     * @param src the source grid
     * @param noDataValue the NO_DATA value (if none use Float.NaN)
     */
    public BilinearInterpolator(final float[][] src, final float noDataValue) {
        this.src = src;
        this.noDataValue = noDataValue;
    }

    /**
     * Interpolates the source grid to a new grid of different dimensions.
     *
     * @param width the width of the destination grid
     * @param height the height of the destination grid
     * @param smoothBoundary true if boundary smoothing should be performed
     * @return the interpolated grid
     */
    public float[][] interpolate(final int width, final int height, boolean smoothBoundary) {
        int srcWidth = src.length;
        int srcHeight = src[0].length;

        float[][] dest = new float[width][height];

        float xRatio = ((float) srcWidth - 1) / width;
        float yRatio = ((float) srcHeight - 1) / height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float x = i * xRatio;
                float y = j * yRatio;
                int ix = (int) x;
                int iy = (int) y;
                float xfrac = x - ix;
                float yfrac = y - iy;

                float val;

                if (ix < srcWidth - 1 && iy < srcHeight - 1) {
                    // interpolate if src cell is in grid
                    float v00 = src[ix][iy];
                    float v10 = src[ix + 1][iy];
                    float v01 = src[ix][iy + 1];
                    float v11 = src[ix + 1][iy + 1];
                    if (v00 == noDataValue || v10 == noDataValue || v01 == noDataValue || v11 == noDataValue) {
                        // handle src cell with NO_DATA value(s)
                        if (smoothBoundary) {
                            val = interpolateBoundaryCell(xfrac, yfrac, v00, v10, v01, v11);
                        } else {
                            val = noDataValue;
                        }
                    } else {
                        // All src cell corners have values
                        // Compute bilinear interpolation over the src cell
                        val = v00 * (1 - xfrac) * (1 - yfrac)
                                + v10 * xfrac * (1 - yfrac)
                                + v01 * yfrac * (1 - xfrac)
                                + v11 * (xfrac * yfrac);
                    }
                } else {
                    // dest index at edge of grid
                    val = src[ix][iy];
                }
                dest[i][j] = val;
            }
        }
        return dest;
    }

    /**
     * Interpolates across an edge grid cell, which has 1 or more NO_DATA values. Grid cells with 2 or or NO_DATA values
     * still receive the value NO_DATA. Otherwise, the cell is interpolated across the triangle defined by the 3 valid
     * corner values. This produces a much smoother edge appearance, containing 45-degree lines instead of a jagged
     * stairstep boundary.
     *
     * @param xfrac the fractional x location of the interpolation point within the cell
     * @param yfrac the fractional y location of the interpolation point within the cell
     * @param v00 the lower left value
     * @param v10 the lower right value
     * @param v01 the upper left value
     * @param v11 the upper right value
     * @return the interpolated value
     */
    private float interpolateBoundaryCell(float xfrac, float yfrac, float v00, float v10, float v01, float v11) {
        // count noData values
        int count = 0;
        if (v00 == noDataValue) count++;
        if (v10 == noDataValue) count++;
        if (v01 == noDataValue) count++;
        if (v11 == noDataValue) count++;

        /** Cells with >= 2 noData ==> noData */
        if (count > 1) return noDataValue;

        /**
         * Now only one cell has noData value. Compute interpolation over cell, with vertex layout normalized to put
         * noData in NE. This is done by flipping the cell across the X or Y axis, or both (and transforming the point
         * offsets likewise)
         */
        if (v00 == noDataValue) return interpolateBoundaryCellNorm(1.0f - yfrac, 1.0f - xfrac, v11, v10, v01);
        if (v11 == noDataValue) return interpolateBoundaryCellNorm(xfrac, yfrac, v00, v10, v01);
        if (v10 == noDataValue) return interpolateBoundaryCellNorm(xfrac, 1.0f - yfrac, v01, v11, v00);
        if (v01 == noDataValue) return interpolateBoundaryCellNorm(1.0f - xfrac, yfrac, v10, v00, v11);

        // should never reach here
        return noDataValue;
    }

    /**
     * Computes an interpolated value across a grid cell which has a single NO_DATA value in the NE corner
     * (<tt>v11</tt>), and valid data values in the three other corners. The interpolated value is computed using linear
     * interpolation across the 3D triangle defined by the three valid corners.
     *
     * @param xfrac the fractional x location of the interpolation point within the cell
     * @param yfrac the fractional y location of the interpolation point within the cell
     * @param v00 the lower left value
     * @param v10 the lower right value
     * @param v01 the upper left value
     * @return the interpolated value
     */
    private float interpolateBoundaryCellNorm(float xfrac, float yfrac, float v00, float v10, float v01) {
        // if point is in NE triangle, value is NO_DATA
        if (xfrac + yfrac > 1) return noDataValue;

        // interpolate across plane defined by SW triangle and values
        float dx = v10 - v00;
        float dy = v01 - v00;
        float v = v00 + xfrac * dx + yfrac * dy;
        return v;
    }
}
