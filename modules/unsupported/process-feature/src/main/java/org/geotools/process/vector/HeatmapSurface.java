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

import org.locationtech.jts.geom.Envelope;

/**
 * Computes a Heat Map surface from a set of irregular data points, each containing a positive
 * height value. The nature of the surface is determined by a kernelRadius value, which indicates
 * how far out each data points "spreads".
 *
 * <p>The Heatmap surface is computed as a grid (raster) of values representing the surface. For
 * stability, the compute grid is expanded by the kernel radius on all four sides. This avoids "edge
 * effects" from distorting the surface within the requested envelope.
 *
 * <p>The values in the output surface are normalized to lie in the range [0, 1].
 *
 * @author Martin Davis, OpenGeo
 */
public class HeatmapSurface {
    /** Number of iterations of box blur to approximate a Gaussian blur */
    private static final int GAUSSIAN_APPROX_ITER = 4;

    private Envelope srcEnv;

    private int xSize;

    private int ySize;

    private GridTransform gridTrans;

    private float[][] grid;

    private int kernelRadiusGrid;

    /**
     * Creates a new heatmap surface.
     *
     * @param kernelRadius the kernel radius, in grid units
     * @param srcEnv the envelope defining the data space
     * @param xSize the width of the output grid
     * @param ySize the height of the output grid
     */
    public HeatmapSurface(int kernelRadius, Envelope srcEnv, int xSize, int ySize) {
        // radius must be non-negative
        this.kernelRadiusGrid = Math.max(kernelRadius, 0);

        this.srcEnv = srcEnv;
        this.xSize = xSize;
        this.ySize = ySize;

        init();
    }

    private void init() {
        gridTrans = new GridTransform(srcEnv, xSize, ySize);
        /**
         * Do NOT clamp transform output, since the actual target grid is larger than the source
         * Envelope, due to the required buffering. This means that transform outputs MUST be
         * checked for validity.
         */
        gridTrans.setClamp(false);

        int xSizeExp = xSize + 2 * kernelRadiusGrid;
        int ySizeExp = ySize + 2 * kernelRadiusGrid;

        grid = new float[xSizeExp][ySizeExp];
    }

    /**
     * Adds a new data point to the surface. Data points can be coincident.
     *
     * @param x the X ordinate of the point
     * @param y the Y ordinate of the point
     * @param value the data value of the point
     */
    public void addPoint(double x, double y, double value) {
        /** Input points are converted to grid space, and offset by the grid expansion offset */
        int gi = gridTrans.i(x) + kernelRadiusGrid;
        int gj = gridTrans.j(y) + kernelRadiusGrid;

        // check if point falls outside grid - skip it if so
        if (gi < 0 || gi > grid.length || gj < 0 || gj > grid[0].length) return;

        grid[gi][gj] += value;
        // System.out.println("data[" + gi + ", " + gj + "] <- " + value);
    }

    /**
     * Computes a grid representing the heatmap surface. The grid is structured as an XY matrix,
     * with (0,0) being the bottom left corner of the data space
     *
     * @return a grid representing the surface
     */
    public float[][] computeSurface() {

        computeHeatmap(grid, kernelRadiusGrid);

        float[][] gridOut = extractGrid(grid, kernelRadiusGrid, kernelRadiusGrid, xSize, ySize);

        return gridOut;
    }

    private float[][] extractGrid(float[][] grid, int xBase, int yBase, int xSize, int ySize) {
        float[][] gridExtract = new float[xSize][ySize];
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                gridExtract[i][j] = grid[xBase + i][yBase + j];
            }
        }
        return gridExtract;
    }

    private float[][] computeHeatmap(float[][] grid, int kernelRadius) {
        int xSize = grid.length;
        int ySize = grid[0].length;

        int baseBoxKernelRadius = kernelRadius / GAUSSIAN_APPROX_ITER;
        int radiusIncBreak = kernelRadius - baseBoxKernelRadius * GAUSSIAN_APPROX_ITER;

        /**
         * Since Box Blur is linearly separable, can implement it by doing 2 1-D box blurs in
         * different directions. Using a flipped buffer grid allows the same code to compute each
         * direction, as well as preserving input grid values.
         */
        // holds flipped copy of first box blur pass
        float[][] grid2 = new float[ySize][xSize];
        for (int count = 0; count < GAUSSIAN_APPROX_ITER; count++) {
            int boxKernelRadius = baseBoxKernelRadius;
            /** If required, increment radius to ensure sum of radii equals total kernel radius */
            if (count < radiusIncBreak) boxKernelRadius++;
            // System.out.println(boxKernelRadius);

            boxBlur(boxKernelRadius, grid, grid2);
            boxBlur(boxKernelRadius, grid2, grid);
        }

        // testNormalizeFactor(baseBoxKernelRadius, radiusIncBreak);
        normalize(grid);
        return grid;
    }

    /** Normalizes grid values to range [0,1] */
    private void normalize(float[][] grid) {
        float max = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] > max) max = grid[i][j];
            }
        }

        float normFactor = 1.0f / max;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] *= normFactor;
            }
        }
    }

    private float kernelVal(int kernelRadius) {
        // This kernel function has been confirmed to integrate to 1 over the full radius
        float val = (float) (1.0f / (2 * kernelRadius + 1));
        return val;
    }

    private void boxBlur(int kernelRadius, float[][] input, float[][] output) {
        int width = input.length;
        int height = input[0].length;

        // init moving average total
        float kernelVal = kernelVal(kernelRadius);
        // System.out.println("boxblur: radius = " + kernelRadius + " kernel val = " + kernelVal);

        for (int j = 0; j < height; j++) {

            double tot = 0.0;

            for (int i = -kernelRadius; i <= kernelRadius; i++) {
                if (i < 0 || i >= width) continue;
                tot += kernelVal * input[i][j];
            }

            // System.out.println(tot);

            output[j][0] = (float) tot;

            for (int i = 1; i < width; i++) {

                // update box running total
                int iprev = i - 1 - kernelRadius;
                if (iprev >= 0) tot -= kernelVal * input[iprev][j];

                int inext = i + kernelRadius;
                if (inext < width) tot += kernelVal * input[inext][j];

                output[j][i] = (float) tot;
                // if (i==49 && j==147) System.out.println("val[ " + i + ", " + j + "] = " + tot);

            }
        }
    }
}
