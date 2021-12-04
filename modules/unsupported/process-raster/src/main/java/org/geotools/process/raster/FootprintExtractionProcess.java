/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import java.util.List;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.raster.MarchingSquaresVectorizer.ImageLoadingType;
import org.geotools.util.Range;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.ProgressListener;

/**
 * A process to extract footprint from a raster. Result is returned as a {@linkplain
 * SimpleFeatureCollection} containing the Geometry of the footprint as first element of the
 * collection.
 *
 * <p>Optionally, in case the {@code computeSimplifiedFootprint} parameter has been set to {@code
 * true}, the simplified footprint will be provided as second element of the collection.
 *
 * <p>By default, the footprint is computed by looking for not-zero pixels. Luminance is computed on
 * the input dataset and not-zero luminance pixels will be used.
 *
 * <p>An optional {@code exclusionRanges} parameter is supported to define which luminance values
 * should be excluded from the search. This allows you, as an instance, to exclude "Dark pixels /
 * Almost black pixels / White pixels" from the results.
 *
 * <p>An optional {@code thresholdArea} parameter is supported to exclude small polygons from the
 * final result. In case a polygon has an area (in pixels) smaller than the provided value it will
 * not be included in the footprint. Default Threshold Area is {@link
 * MarchingSquaresVectorizer#DEFAULT_THRESHOLD_AREA}
 *
 * <p>An optional {@code computeSimplifiedFootprint} parameter is supported to return a simplified
 * version of the biggest polygon as second element of the feature collection.
 *
 * <p>An optional {@code simplifierFactor} parameter is supported to specify the simplifier factor
 * to be applied to get the simplified version of the Footprint.
 *
 * <p>An optional {@code removeCollinear} parameter is supported to specify whether the collinear
 * vertices of the retrieved polygon should be removed (Default is true)
 *
 * <p>An optional {@code forceValid} parameter is supported to specify whether polygons should be
 * forced to be valid (also removing holes)
 *
 * <p>An optional {@code loadingType} parameter is supported to specify the type of imageLoading
 * (DEFERRED vs IMMEDIATE). Default is {@link ImageLoadingType#getDefault()}.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
@DescribeProcess(title = "Footprint Extraction", description = "Extract footprint from a raster")
public class FootprintExtractionProcess implements RasterProcess {

    /**
     * Executes the raster to vector process.
     *
     * @param coverage the data coverage
     * @param exclusionRanges An optional {@code exclusionRanges} parameter is supported to define
     *     which luminance values should be excluded from the search. This allows you, as an
     *     instance, to exclude "Dark pixels / Almost black pixels" from the results.
     * @param thresholdArea An optional {@code thresholdArea} parameter is supported to exclude
     *     small polygons from the final result. In case a polygon has an area (in pixels) smaller
     *     than the provided value it will not be included in the footprint. Default Threshold Area
     *     is {@link MarchingSquaresVectorizer#DEFAULT_THRESHOLD_AREA}
     * @param computeSimplifiedFootprint An optional {@code computeSimplifiedFootprint} parameter is
     *     supported to return a simplified version of the biggest polygon.
     * @param simplifierFactor the simplifier factor to be applied to compute the simplified version
     *     of the biggest polygon.
     * @param removeCollinear specifies whether the collinear vertices of the retrieved polygon
     *     should be removed (Default is true)
     * @param forceValid specifies whether polygons should be forced to be valid (also removing
     *     holes)
     * @param imageLoadingType specifies the type of imageLoading (DEFERRED vs IMMEDIATE). Default
     *     is {@link ImageLoadingType#getDefault()}.
     */
    @DescribeResult(name = "result", description = "The compute footprint geometry")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "data", description = "Source raster")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "exclusionRanges",
                            description =
                                    "the ranges of luminance values to be excluded by the computation.",
                            min = 0)
                    List<Range<Integer>> exclusionRanges,
            @DescribeParameter(
                            name = "thresholdArea",
                            description =
                                    "Indicates the minimum area of a polygon to be included in the final result",
                            min = 0)
                    Double thresholdArea,
            @DescribeParameter(
                            name = "computeSimplifiedFootprint",
                            description =
                                    "Indicates whether the simplified footprint should be computed",
                            min = 0)
                    Boolean computeSimplifiedFootprint,
            @DescribeParameter(
                            name = "simplifierFactor",
                            description =
                                    "Indicates the simplifier factor to be applied when computing the simplified footprint",
                            min = 0)
                    Double simplifierFactor,
            @DescribeParameter(
                            name = "removeCollinear",
                            description =
                                    "Indicates whether remove collinear point should be applied",
                            min = 0)
                    Boolean removeCollinear,
            @DescribeParameter(
                            name = "forceValid",
                            description =
                                    "Indicates whether polygon should be forced to be valid, also removing holes",
                            min = 0)
                    Boolean forceValid,
            @DescribeParameter(
                            name = "loadingType",
                            description =
                                    "Indicates which type of imageLoading should be performed (DEFERRED vs IMMEDIATE)",
                            min = 0)
                    ImageLoadingType imageLoadingType,
            ProgressListener progressListener)
            throws ProcessException {

        //
        // initial checks
        //
        if (coverage == null) {
            throw new ProcessException("Invalid input, source grid coverage should be not null");
        }

        // Checking for defaults
        if (exclusionRanges == null) {
            exclusionRanges = MarchingSquaresVectorizer.DEFAULT_RANGES;
        }

        if (computeSimplifiedFootprint == null) {
            computeSimplifiedFootprint = false;
        }

        if (simplifierFactor == null) {
            simplifierFactor = MarchingSquaresVectorizer.DEFAULT_SIMPLIFIER_FACTOR;
        }

        if (forceValid == null) {
            forceValid = true;
        }

        if (removeCollinear == null) {
            removeCollinear = true;
        }

        if (imageLoadingType == null) {
            imageLoadingType = ImageLoadingType.getDefault();
        }

        if (thresholdArea == null) {
            thresholdArea = MarchingSquaresVectorizer.DEFAULT_THRESHOLD_AREA;
        }

        MarchingSquaresVectorizer vectorizer =
                new MarchingSquaresVectorizer(
                        coverage,
                        null,
                        thresholdArea,
                        simplifierFactor,
                        imageLoadingType,
                        exclusionRanges);
        vectorizer.setComputeSimplifiedFootprint(computeSimplifiedFootprint);
        vectorizer.setForceValid(forceValid);
        vectorizer.setRemoveCollinear(removeCollinear);
        try {
            vectorizer.process();

            Geometry geometry = vectorizer.getFootprint();

            // wrap as a feature collection and return
            final SimpleFeatureType featureType =
                    CoverageUtilities.createFeatureType(coverage, Geometry.class);
            final SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
            int i = 0;
            final ListFeatureCollection featureCollection = new ListFeatureCollection(featureType);

            // create feature and add to list
            builder.set("the_geom", geometry);

            featureCollection.add(builder.buildFeature(String.valueOf(i++)));
            if (computeSimplifiedFootprint) {
                builder.set("the_geom", vectorizer.getSimplifiedFootprint());
                featureCollection.add(builder.buildFeature(String.valueOf(i++)));
            }
            return featureCollection;
        } catch (Exception e) {
            throw new ProcessException("Exception occurred while computing the footprint", e);
        } finally {
            vectorizer.dispose();
        }
    }
}
