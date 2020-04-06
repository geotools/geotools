/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.RasterManager.SpatialDomainManager;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.opengis.referencing.operation.TransformException;

/**
 * Class that fills up properly read params given a {@link RasterLayerRequest}, an {@link
 * OverviewsController}
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class ReadParamsController {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ReadParamsController.class);

    /**
     * This method is responsible for evaluating possible subsampling factors once the best
     * resolution level has been found, in case we have support for overviews, or starting from the
     * original coverage in case there are no overviews available.
     *
     * <p>Anyhow this method should not be called directly but subclasses should make use of the
     * setReadParams method instead in order to transparently look for overviews.
     */
    private static void performDecimation(
            final SpatialDomainManager spatialDomainManager,
            final double[] requestedResolution,
            final int levelIndex,
            final ImageReadParam readParameters,
            final OverviewsController overviewsController,
            final double[] virtualNativeResolution) {

        // the read parameters cannot be null
        Utilities.ensureNonNull("readParameters", readParameters);
        Utilities.ensureNonNull("spatialDomainManager", spatialDomainManager);
        Utilities.ensureNonNull("overviewsController", overviewsController);

        // get the requested resolution
        if (requestedResolution == null) {
            // if there is no requested resolution we don't do any
            // subsampling
            readParameters.setSourceSubsampling(1, 1, 0, 0);
            return;
        }

        boolean useVirtual = false;
        if (virtualNativeResolution != null
                && !Double.isNaN(virtualNativeResolution[0])
                && !Double.isNaN(virtualNativeResolution[1])) {
            useVirtual = true;
        }

        double selectedRes[] = new double[2];
        double requestedRes[] = new double[2];
        final OverviewLevel level = overviewsController.resolutionsLevels.get(levelIndex);
        selectedRes[0] = level.resolutionX;
        selectedRes[1] = level.resolutionY;
        requestedRes[0] = requestedResolution[0];
        requestedRes[1] = requestedResolution[1];

        final int rasterWidth, rasterHeight;
        if (levelIndex == 0) {
            // highest resolution
            rasterWidth = spatialDomainManager.coverageRasterArea.width;
            rasterHeight = spatialDomainManager.coverageRasterArea.height;
            if (useVirtual) {
                if (virtualNativeResolution[0] > requestedResolution[0]
                        && virtualNativeResolution[1] > requestedResolution[1]) {
                    requestedRes[0] = virtualNativeResolution[0];
                    requestedRes[1] = virtualNativeResolution[1];
                } else {
                    virtualNativeResolution[0] = Double.NaN;
                    virtualNativeResolution[1] = Double.NaN;
                }
            }
        } else {
            // work on overviews
            // TODO this is bad side effect of how the Overviews are managed
            // right now. There are two problems here,
            // first we are assuming that we are working with LON/LAT,
            // second is that we are getting just an approximation of
            // raster dimensions. The solution is to have the rater
            // dimensions on each level and to confront raster dimensions,
            // which means working
            rasterWidth =
                    (int) Math.round(spatialDomainManager.coverageBBox.getSpan(0) / selectedRes[0]);
            rasterHeight =
                    (int) Math.round(spatialDomainManager.coverageBBox.getSpan(1) / selectedRes[1]);
        }
        // /////////////////////////////////////////////////////////////////////
        // DECIMATION ON READING
        // Setting subsampling factors with some checks
        // 1) the subsampling factors cannot be zero
        // 2) the subsampling factors cannot be such that the w or h are
        // zero
        // /////////////////////////////////////////////////////////////////////
        int subSamplingFactorX = (int) Math.floor(requestedRes[0] / selectedRes[0]);
        subSamplingFactorX = subSamplingFactorX == 0 ? 1 : subSamplingFactorX;

        while (subSamplingFactorX > 0 && rasterWidth / subSamplingFactorX <= 0)
            subSamplingFactorX--;
        subSamplingFactorX = subSamplingFactorX <= 0 ? 1 : subSamplingFactorX;

        int subSamplingFactorY = (int) Math.floor(requestedRes[1] / selectedRes[1]);
        subSamplingFactorY = subSamplingFactorY == 0 ? 1 : subSamplingFactorY;

        while (subSamplingFactorY > 0 && rasterHeight / subSamplingFactorY <= 0)
            subSamplingFactorY--;
        subSamplingFactorY = subSamplingFactorY <= 0 ? 1 : subSamplingFactorY;

        readParameters.setSourceSubsampling(subSamplingFactorX, subSamplingFactorY, 0, 0);
    }

    /**
     * This method is responsible for preparing the read param for doing an {@link
     * ImageReader#read(int, ImageReadParam)}. It sets the passed {@link ImageReadParam} in terms of
     * decimation on reading using the provided requestedEnvelope and requestedDim to evaluate the
     * needed resolution. It also returns and {@link Integer} representing the index of the raster
     * to be read when dealing with multipage raster.
     *
     * @param overviewPolicy it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE}, {@link
     *     Hints#VALUE_OVERVIEW_POLICY_NEAREST}, {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or
     *     {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the policy to compute the
     *     overviews level upon request.
     * @param readParams an instance of {@link ImageReadParam} for setting the subsampling factors.
     * @return the index of the raster to read in the underlying data source.
     */
    static int setReadParams(
            final double[] requestedResolution,
            OverviewPolicy overviewPolicy,
            DecimationPolicy decimationPolicy,
            final ImageReadParam readParams,
            final RasterManager rasterManager,
            final OverviewsController overviewController,
            final double[] virtualNativeResolution)
            throws IOException, TransformException {

        Utilities.ensureNonNull("readParams", readParams);
        Utilities.ensureNonNull("RasterManager", rasterManager);
        Utilities.ensureNonNull("overviewsController", overviewController);

        if (overviewPolicy == null) {
            overviewPolicy = OverviewPolicy.getDefaultPolicy();
        }

        if (decimationPolicy == null) {
            decimationPolicy = DecimationPolicy.getDefaultPolicy();
        }

        // Default image index 0
        int imageChoice = 0;
        // default values for subsampling
        readParams.setSourceSubsampling(1, 1, 0, 0);

        // requested to ignore overviews
        if (overviewPolicy.equals(OverviewPolicy.IGNORE)
                && decimationPolicy.equals(DecimationPolicy.DISALLOW)
                && virtualNativeResolution == null) return imageChoice;

        if (!overviewPolicy.equals(OverviewPolicy.IGNORE)) {
            imageChoice =
                    overviewController.pickOverviewLevel(
                            overviewPolicy, requestedResolution, virtualNativeResolution);
            if (virtualNativeResolution != null
                    && !Double.isNaN(virtualNativeResolution[0])
                    && !Double.isNaN(virtualNativeResolution[1])) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Specified Resolution is: resX="
                                    + virtualNativeResolution[0]
                                    + " ; resY="
                                    + virtualNativeResolution[1]
                                    + " . Choosing imageIndex = "
                                    + imageChoice);
                }
            }
        }

        // DECIMATION ON READING
        if (!decimationPolicy.equals(DecimationPolicy.DISALLOW)) {
            ReadParamsController.performDecimation(
                    rasterManager.spatialDomainManager,
                    requestedResolution,
                    imageChoice,
                    readParams,
                    overviewController,
                    virtualNativeResolution);
        }
        return imageChoice;
    }
}
