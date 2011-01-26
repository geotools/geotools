/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Collections;

import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.util.Utilities;

/**
 * A class to handle overviews resolution levels. It stores overviews resolution levels information
 * and suggests the level to be used depending on the current request and the {@link OverviewPolicy}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
final  class OverviewsController {

    final ArrayList<OverviewLevel> resolutionsLevels = new ArrayList<OverviewLevel>();

    /**
     * Constructor.
     * 
     * @param highestRes The resolution values for the finest level, <b>This is treated as level 0.</b>
     * @param numberOfOvervies number of overview levels.
     * @param overviewsResolution resolutions for the various levels. <b>Implicitly, the index of the resolution is the index of the corresponding level.</b> 
     */
    public OverviewsController(
            final double[] highestRes,
            final int numberOfOvervies, 
            final double[][] overviewsResolution) {

        // notice that we assume what follows:
        // -highest resolution image is at level 0.
        // -all the overviews share the same envelope
        // -the aspect ratio for the overviews is constant
        // -the provided resolutions are taken directly from the grid
        resolutionsLevels.add(new OverviewLevel(1, highestRes[0], highestRes[1], 0));
        if (numberOfOvervies > 0) {
            for (int i = 0; i < overviewsResolution.length; i++) {
                resolutionsLevels.add(new OverviewLevel(overviewsResolution[i][0] / highestRes[0],
                        overviewsResolution[i][0], overviewsResolution[i][1], i + 1));
            }
            Collections.sort(resolutionsLevels);
        }
    }

    /**
     * Given a specified {@link OverviewPolicy} and a {@link RasterLayerRequest}, suggest the proper
     * overview level index.
     * @param policy
     * @param request
     * @return the OverviewLevel index
     */
    int pickOverviewLevel(final OverviewPolicy policy, final double[] requestedResolution) {

        // //
        //
        // If this file has only
        // one page we use decimation, otherwise we use the best page available.
        // Future versions should use both.
        //
        // //
        if (resolutionsLevels == null || resolutionsLevels.size() <= 0)
            return 0;

        // Now search for the best matching resolution.
        // Check also for the "perfect match"... unlikely in practice unless someone
        // tunes the clients to request exactly the resolution embedded in
        // the overviews, something a perf sensitive person might do in fact

        // requested scale factor for least reduced axis
        final OverviewLevel max = (OverviewLevel) resolutionsLevels.get(0);

        // the requested resolutions
        final double requestedScaleFactorX;
        final double requestedScaleFactorY;
        if (requestedResolution != null) {
            final double reqx = requestedResolution[0];
            final double reqy = requestedResolution[1];
            requestedScaleFactorX = reqx / max.resolutionX;
            requestedScaleFactorY = reqy / max.resolutionY;
        } else {
            return 0;
        }
        final int leastReduceAxis = requestedScaleFactorX <= requestedScaleFactorY ? 0 : 1;
        final double requestedScaleFactor = leastReduceAxis == 0 ? requestedScaleFactorX : requestedScaleFactorY;

        // are we looking for a resolution even higher than the native one?
        if (requestedScaleFactor <= 1) {
            return max.imageChoice;
        }
        // are we looking for a resolution even lower than the smallest overview?
        final OverviewLevel min = (OverviewLevel) resolutionsLevels.get(resolutionsLevels.size() - 1);
        if (requestedScaleFactor >= min.scaleFactor) {
            return min.imageChoice;
        }
        // Ok, so we know the overview is between min and max, skip the first
        // and search for an overview with a resolution lower than the one requested,
        // that one and the one from the previous step will bound the searched resolution
        OverviewLevel prev = max;
        final int size = resolutionsLevels.size();
        for (int i = 1; i < size; i++) {
            final OverviewLevel curr = resolutionsLevels.get(i);
            // perfect match check
            if (curr.scaleFactor == requestedScaleFactor) {
                return curr.imageChoice;
            }

            // middle check. The first part of the condition should be sufficient, but
            // there are cases where the x resolution is satisfied by the lowest resolution,
            // the y by the one before the lowest (so the aspect ratio of the request is
            // different than the one of the overviews), and we would end up going out of the 
            // loop since not even the lowest can "top" the request for one axis
            if (curr.scaleFactor > requestedScaleFactor || i == size - 1) {
                if (policy == OverviewPolicy.QUALITY) {
                    return prev.imageChoice;
                } else if (policy == OverviewPolicy.SPEED) {
                    return curr.imageChoice;
                } else if (requestedScaleFactor - prev.scaleFactor < curr.scaleFactor - requestedScaleFactor) {
                    return prev.imageChoice;
                } else {
                    return curr.imageChoice;
                }
            }
            prev = curr;
        }
        // fallback
        return max.imageChoice;
    }

    /**
     * Simple support class for sorting overview resolutions
     * 
     * @author Andrea Aime
     * @author Simone Giannecchini, GeoSolutions.
     * @since 2.5
     */
    static class OverviewLevel implements Comparable<OverviewLevel> {

        double scaleFactor;

        double resolutionX;

        double resolutionY;

        int imageChoice;

        /**
         * 
         * @param scaleFactor
         * @param resolutionX
         * @param resolutionY
         * @param imageChoice
         */
        public OverviewLevel(final double scaleFactor, final double resolutionX, 
                final double resolutionY, final int imageChoice) {
            this.scaleFactor = scaleFactor;
            this.resolutionX = resolutionX;
            this.resolutionY = resolutionY;
            this.imageChoice = imageChoice;
        }

        public int compareTo(final OverviewLevel other) {
            if (scaleFactor > other.scaleFactor) {
                return 1;
            } else if (scaleFactor < other.scaleFactor) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "OverviewLevel[Choice=" + imageChoice + ",scaleFactor="
                    + scaleFactor + ",resX:" + resolutionX + ",resY:"
                    + resolutionY + "]";
        }

        @Override
        public int hashCode() {
            int hash = Utilities.hash(imageChoice, 31);
            hash = Utilities.hash(resolutionX, hash);
            hash = Utilities.hash(resolutionY, hash);
            hash = Utilities.hash(scaleFactor, hash);
            return hash;
        }
    }
}