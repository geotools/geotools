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

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * Interpolates a surface across a regular grid from an irregular set of data points using the
 * Barnes Surface Interpolation technique.
 *
 * <p>Barnes Surface Interpolation is a surface estimating method commonly used as an interpolation
 * technique for meteorological datasets. The algorithm operates on a regular grid of cells covering
 * a specified extent in the input data space. It computes an initial pass to produce an averaged
 * (smoothed) value for each cell in the grid, based on the cell's proximity to the points in the
 * input observations. Subsequent refinement passes may be performed to improve the surface estimate
 * to better approximate the observed values.
 *
 * <ul>
 *   <li>The initial pass produces an averaged (smoothed) value for each grid cell using a summation
 *       of exponential (Gaussian) decay functions around each observation point.
 *   <li>Subsequent refinement passes compute an error surface in the same way, using the deltas
 *       between the previous estimated surface and the observations. The error surface is added to
 *       the previous estimated surface to refine the estimate (by reducing the delta between the
 *       estimate and the observations).
 * </ul>
 *
 * <p>For the first pass, the estimated value at each grid cell is:
 *
 * <pre>
 * E<sub>g</sub> = sum(w<sub>i</sub> * o<sub>i</sub>) / sum(w<sub>i</sub>)
 * </pre>
 *
 * where
 *
 * <ul>
 *   <li><code>E<sub>g</sub></code> is the estimated surface value at the grid cell
 *   <li><code>w<sub>i</sub></code> is the weight value for the i'th observation point (see below
 *       for definition)
 *   <li><code>o<sub>i</sub></code> is the value of the i'th observation point
 * </ul>
 *
 * <p>The weight (decay) function used is:
 *
 * <pre>
 * w<sub>i</sub> = exp(-d<sub>i</sub><sup>2</sup> / L<sup>2</sup>c )
 * </pre>
 *
 * where:
 *
 * <ul>
 *   <li><code>w<sub>i</sub></code> is the <b>weight</b> of the i'th observation point value
 *   <li><code>d<sub>i</sub></code> is the <b>distance</b> from the grid cell being estimated to the
 *       i'th observation point
 *   <li><code>L</code> is the <b>length scale</b>, which is determined by the observation spacing
 *       and the natural scale of the phenomena being measured. The length scale is in the units of
 *       the coordinate system of the data points. It will likely need to be empirically estimated.
 *   <li><code>c</code> is the <b>convergence factor</b>, which controls how much refinement takes
 *       place during each refinement step. In the first pass the convergence is automatically set
 *       to 1. For subsequent passes a value in the range 0.2 - 0.3 is usually effective.
 * </ul>
 *
 * During refinement passes the value at each grid cell is re-estimated as:
 *
 * <pre>
 *  E<sub>g</sub>' = E<sub>g</sub> + sum( w<sub>i</sub> * (o<sub>i</sub> - E<sub>i</sub>) ) / sum( w<sub>i</sub> )
 * </pre>
 *
 * To optimize performance for large input datasets, it is only necessary to provide the data points
 * which affect the surface interpolation within the specified output extent. In order to avoid
 * "edge effects", the provided data points should be taken from an area somewhat larger than the
 * output extent. The extent of the data area depends on the length scale, convergence factor, and
 * data spacing in a complex way. A reasonable heuristic for determining the size of the query
 * extent is to expand the output extent by a value of 2L.
 *
 * <p>Since the visual quality and accuracy of the computed surface is lower further from valid
 * observations, the algorithm allows limiting the extent of the computed cells. This is done by
 * using the concept of <b>supported grid cells</b>. Grid cells are supported by the input
 * observations if they are within a specified distance of a specified number of observation points.
 * Grid cells which are not supported are not computed and are output as NO_DATA values.
 *
 * <p><b>References</b>
 *
 * <ol>
 *   <li>Barnes, S. L (1964). "A technique for maximizing details in numerical weather-map
 *       analysis". <i>Journal of Applied Meterology</i> 3 (4): 396 - 409
 * </ol>
 *
 * @author Martin Davis - OpenGeo
 */
public class BarnesSurfaceInterpolator {

    /** The default grid cell value used to indicate no data was computed for that cell */
    public static final float DEFAULT_NO_DATA_VALUE = -999;

    private static final double INTERNAL_NO_DATA = Double.NaN;

    // =========== Input parameters
    /**
     * These parameters control which grid points are considered to be supported, i.e. have enough
     * nearby observation points to be reasonably estimated.
     *
     * <p>A grid point is supported if it has:
     *
     * <p>count(obs within maxObservationDistance) >= minObservationCount
     *
     * <p>Using these parameters is optional, but recommended, since estimating grid points which
     * are far from any observations can produce unrealistic surfaces.
     */
    private int minObservationCount = 2;

    private double maxObservationDistance = 0.0;

    private double convergenceFactor = 0.3;

    private double lengthScale = 0.0;

    private int passCount = 1;

    private Coordinate[] inputObs;

    // ============= Internal parameters (could be exposed)
    private float noDataValue = DEFAULT_NO_DATA_VALUE;

    // ============= Computed parameters
    // private double effectiveRadius;

    /** Indicates whether estimated grid points are filtered based on distance from observations */
    private boolean useObservationMask;

    // ============ Working data
    private float[] estimatedObs;

    /**
     * Creates a Barnes Interpolator over a specified dataset of observation values. The observation
     * data is provided as an array of {@link Coordinate} values, where the X,Y ordinates are the
     * observation location, and the Z ordinate contains the observation value.
     *
     * @param observationData the observed data values
     */
    public BarnesSurfaceInterpolator(Coordinate[] observationData) {
        this.inputObs = observationData;
    }

    /**
     * Sets the number of passes performed during Barnes interpolation.
     *
     * @param passCount the number of estimation passes to perform (1 or more)
     */
    public void setPassCount(int passCount) {
        if (passCount < 1) return;
        this.passCount = passCount;
    }

    /**
     * Sets the length scale for the interpolation weighting function. The length scale is
     * determined from the distance between the observation points, as well as the scale of the
     * phenomena which is being measured.
     *
     * <p>
     */
    public void setLengthScale(double lengthScale) {
        this.lengthScale = lengthScale;
    }

    /**
     * Sets the convergence factor used during refinement passes. The value should be in the range
     * [0,1]. Empirically, values between 0.2 - 0.3 are most effective. Smaller values tend to make
     * the interpolated surface too "jittery". Larger values produce less refinement effect.
     *
     * @param convergenceFactor the factor determining how much to refine the surface estimate
     */
    public void setConvergenceFactor(double convergenceFactor) {
        this.convergenceFactor = convergenceFactor;
    }

    /**
     * Sets the maximum distance from an observation for a grid point to be supported by that
     * observation. Empirically determined; a reasonable starting point is between 1.5 and 2 times
     * the Length scale. If the value is 0 (which is the default), all grid points are considered to
     * be supported, and will thus be computed.
     *
     * @param maxObsDistance the maximum distance from an observation for a supported grid point
     */
    public void setMaxObservationDistance(double maxObsDistance) {
        this.maxObservationDistance = maxObsDistance;
    }

    /**
     * Sets the minimum number of in-range observations which are required for a grid point to be
     * supported. The default is 2.
     *
     * @param minObsCount the minimum in-range observation count for supported grid points
     */
    public void setMinObservationCount(int minObsCount) {
        this.minObservationCount = minObsCount;
    }

    /**
     * Sets the NO_DATA value used to indicate that a grid cell was not computed. This value should
     * be distinct from any potential data value.
     *
     * @param noDataValue the value to use to represent NO_DATA.
     */
    public void setNoData(float noDataValue) {
        this.noDataValue = noDataValue;
    }

    /**
     * Computes the estimated values for a regular grid of cells. The area covered by the grid is
     * specified by an {@link Envelope}. The size of the grid is specified by the cell count for the
     * grid width (X) and height (Y).
     *
     * @param srcEnv the area covered by the grid
     * @param xSize the width of the grid
     * @param ySize the height of the grid
     * @return the computed grid of estimated data values (in row-major order)
     */
    public float[][] computeSurface(Envelope srcEnv, int xSize, int ySize) {
        // not currently used
        // effectiveRadius = effectiveRadius(minimumWeight, influenceRadius);

        useObservationMask = minObservationCount > 0 && maxObservationDistance > 0.0;

        float[][] grid = new float[xSize][ySize];
        GridTransform trans = new GridTransform(srcEnv, xSize, ySize);

        estimateGrid(grid, trans);

        if (passCount > 1) {
            /** First refinement pass requires observation points to be estimated as well */
            estimatedObs = computeEstimatedObservations();
            refineGrid(grid, trans);

            /** For subsequent refinement passes, refine observations then recompute */
            for (int i = 3; i <= passCount; i++) {
                refineEstimatedObservations(estimatedObs);
                refineGrid(grid, trans);
            }
        }
        return grid;
    }

    private float[] computeEstimatedObservations() {
        float[] estimate = new float[inputObs.length];
        for (int i = 0; i < inputObs.length; i++) {
            Coordinate dp = inputObs[i];
            float est = (float) estimatedValue(dp.x, dp.y);
            if (!Float.isNaN(est)) estimate[i] = est;
            else estimate[i] = (float) inputObs[i].getZ();
        }
        return estimate;
    }

    private float[] refineEstimatedObservations(float[] currEst) {
        float[] estimate = new float[inputObs.length];
        for (int i = 0; i < inputObs.length; i++) {
            Coordinate dp = inputObs[i];
            float del = (float) refinedDelta(dp.x, dp.y, convergenceFactor);
            if (!Float.isNaN(del)) estimate[i] = (float) currEst[i] + del;
            else estimate[i] = (float) inputObs[i].getZ();
        }
        return estimate;
    }

    /**
     * Computes an initial estimate of the interpolated surface.
     *
     * @param grid the grid matrix buffer to use
     * @param trans the transform mapping from data space to the grid
     */
    private void estimateGrid(float[][] grid, GridTransform trans) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                double x = trans.x(i);
                double y = trans.y(j);

                grid[i][j] = (float) noDataValue;
                if (useObservationMask && !isSupportedGridPt(x, y)) continue;

                float est = (float) estimatedValue(x, y);
                if (!Float.isNaN(est)) grid[i][j] = est;
            }
        }
    }

    /**
     * Computes a refined estimate for the interpolated surface.
     *
     * @param grid the grid matrix buffer to use
     * @param trans the transform mapping from data space to the grid
     */
    private void refineGrid(float[][] grid, GridTransform trans) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                double x = trans.x(i);
                double y = trans.y(j);

                // skip NO_DATA values
                if (grid[i][j] == noDataValue) continue;

                float del = (float) refinedDelta(x, y, convergenceFactor);
                /*
                 // DEBUGGING
                if (del < 0) {
                    float d = (float) refinedDelta(x, y, convergenceFactor);
                }
                */
                if (!Float.isNaN(del)) grid[i][j] = grid[i][j] + del;
            }
        }
    }

    private boolean isSupportedGridPt(double x, double y) {
        int count = 0;
        for (int i = 0; i < inputObs.length; i++) {
            double dist = distance(x, y, inputObs[i]);
            if (dist <= maxObservationDistance) count++;
        }
        return count >= minObservationCount;
    }

    private double distance(double x, double y, Coordinate p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Computes the initial estimate for a grid point.
     *
     * @param x the x ordinate of the grid point location
     * @param y the y ordinate of the grid point location
     * @return the estimated value, or INTERNAL_NO_DATA if the grid cell is not supported
     */
    private double estimatedValue(double x, double y) {
        Coordinate p = new Coordinate(x, y);

        double sumWgtVal = 0;
        double sumWgt = 0;
        int dataCount = 0;
        for (int i = 0; i < inputObs.length; i++) {
            double wgt = weight(p, inputObs[i], lengthScale);
            /** Skip observation if unusable due to too great a distance */
            if (Double.isNaN(wgt)) continue;

            sumWgtVal += wgt * inputObs[i].getZ();
            sumWgt += wgt;
            dataCount++;
        }
        /** If grid point is not supported, return NO_DATA */
        if (dataCount < minObservationCount) return INTERNAL_NO_DATA;
        return sumWgtVal / sumWgt;
    }

    /**
     * Computes a refinement delta, which is added to a grid point estimated value to refine the
     * estimate.
     *
     * @param x the x ordinate of the grid point location
     * @param y the y ordinate of the grid point location
     * @param convergenceFactor the convergence factor
     * @return the refinement delta value, or INTERNAL_NO_DATA if the grid cell is not supported
     */
    private double refinedDelta(double x, double y, double convergenceFactor) {
        Coordinate p = new Coordinate(x, y);

        double sumWgtVal = 0;
        double sumWgt = 0;
        int dataCount = 0;
        for (int i = 0; i < inputObs.length; i++) {
            double wgt = weight(p, inputObs[i], lengthScale, convergenceFactor);
            /** Check if observation is unusable (e.g. due to too great a distance) */
            if (Double.isNaN(wgt)) continue;

            sumWgtVal += wgt * (inputObs[i].getZ() - estimatedObs[i]);
            sumWgt += wgt;
            dataCount++;
        }
        /** If grid point is not supported, return NO_DATA */
        if (dataCount < minObservationCount) return INTERNAL_NO_DATA;
        return sumWgtVal / sumWgt;
    }

    private double weight(Coordinate dataPt, Coordinate gridPt, double lengthScale) {
        return weight(gridPt, dataPt, lengthScale, 1.0);
    }

    private double weight(
            Coordinate dataPt, Coordinate gridPt, double lengthScale, double convergenceFactor) {
        double dist = dataPt.distance(gridPt);
        return weight(dist, lengthScale, convergenceFactor);
    }

    private double weight(double dist, double lengthScale, double convergenceFactor) {
        /**
         * MD - using an effective radius is problematic.
         *
         * <p>The effective radius grows as a log function of the cutoff weight, so even for very
         * small cutoff weight values, the effective radius is only a few times the size of the
         * influence radius.
         *
         * <p>Also, dropping observation terms from the estimate results in very drastic
         * (discontinuous) changes at distances around the effective radius. (Probably because
         * beyond that distance there are very few terms (maybe only 2) contributing to the
         * estimate, so there is no smoothing effect from incorporating many estimates)
         *
         * <p>So - don't use effectiveRadius.
         *
         * <p>Or, maybe it's ok as long as a observation mask is used as well, since the effect only
         * occurs at large distances from observation points?
         */
        /*
         * if (dist > effectiveRadius) return INTERNAL_NO_DATA; //
         */
        double dr = dist / lengthScale;
        double w = Math.exp(-(dr * dr / convergenceFactor));
        // if (dist > cutoffRadius) System.out.println(w);
        return w;
    }
}
