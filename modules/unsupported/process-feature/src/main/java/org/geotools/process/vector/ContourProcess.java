/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.util.ProgressListener;

/** @author ian */
@DescribeProcess(title = "Contours", description = "Computes contours over the point features.")
public class ContourProcess implements VectorProcess {

    @DescribeResult(name = "contours", description = "The contours of the input features")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input point feature collection")
                    FeatureCollection features,
            @DescribeParameter(name = "propertyName", description = "PropertyName to be contoured")
                    String propertyName,
            @DescribeParameter(
                            name = "levels",
                            description = "Values of levels at which to generate contours")
                    double[] levels,
            @DescribeParameter(
                            name = "interval",
                            description =
                                    "Interval between contour values (ignored if levels parameter is supplied)",
                            min = 0,
                            minValue = 0)
                    Double interval,
            // @DescribeParameter(name = "nice", description = "Should the contours start and end at
            // nice values", min = 0) Boolean nice,
            @DescribeParameter(
                            name = "simplify",
                            description = "Indicates whether contour lines are simplified",
                            min = 0)
                    Boolean simplify,
            @DescribeParameter(
                            name = "smooth",
                            description =
                                    "Indicates whether contour lines are smoothed using Bezier smoothing",
                            min = 0)
                    Boolean smooth,
            // @DescribeParameter(name = "roi", description = "Geometry delineating the region of
            // interest (in raster coordinate system)", min = 0) Geometry roi,
            ProgressListener progressListener)
            throws ProcessException {
        Contours contours = new Contours();
        if (smooth != null) {
            contours.setSmooth(smooth.booleanValue());
        }
        if (simplify != null) {
            contours.setSimplify(simplify.booleanValue());
        }
        if (progressListener != null) {
            contours.setProgressListener(progressListener);
        }
        if (levels.length > 0) {
            contours.setLevels(levels);
        } else {
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            try (SimpleFeatureIterator it = (SimpleFeatureIterator) features.features()) {
                while (it.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) it.next();
                    double value = (Double) feature.getAttribute(propertyName);
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }
            }
            int nSteps = (int) Math.ceil((max - min) / interval);
            double[] l = new double[nSteps];
            for (int i = 0; i < nSteps; i++) {
                l[i] = i * interval;
            }
            contours.setLevels(l);
        }

        SimpleFeatureCollection result = contours.contour(features, propertyName);

        return result;
    }
}
