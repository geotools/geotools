/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.processing.jai;

import static org.geotools.processing.jai.ClassBreaksDescriptor.*;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.ROI;
import org.geotools.process.classify.ClassificationMethod;

/**
 * RIF for the ClassBreaks operation.
 *
 * <p>This factory ends up creating on of the following operations based on the "method" parameter.
 *
 * <ul>
 *   <li>{@link EqualIntervalBreaksOpImage}
 *   <li>{@link QuantileBreaksOpImage}
 *   <li>{@link NaturalBreaksOpImage}
 * </ul>
 */
public class ClassBreaksRIF implements RenderedImageFactory {

    public RenderedImage create(ParameterBlock pb, RenderingHints hints) {
        RenderedImage src = pb.getRenderedSource(0);

        int xStart = src.getMinX(); // default values
        int yStart = src.getMinY();

        Integer numBins = pb.getIntParameter(NUM_CLASSES_ARG);
        ClassificationMethod method = (ClassificationMethod) pb.getObjectParameter(METHOD_ARG);
        Double[][] extrema = (Double[][]) pb.getObjectParameter(EXTREMA_ARG);
        ROI roi = (ROI) pb.getObjectParameter(ROI_ARG);
        Integer[] bands = (Integer[]) pb.getObjectParameter(BAND_ARG);
        Integer xPeriod = pb.getIntParameter(X_PERIOD_ARG);
        Integer yPeriod = pb.getIntParameter(Y_PERIOD_ARG);
        Double noData = (Double) pb.getObjectParameter(NODATA_ARG);

        switch (method) {
            case EQUAL_INTERVAL:
                return new EqualIntervalBreaksOpImage(
                        src, numBins, extrema, roi, bands, xStart, yStart, xPeriod, yPeriod,
                        noData);
            case QUANTILE:
                return new QuantileBreaksOpImage(
                        src, numBins, extrema, roi, bands, xStart, yStart, xPeriod, yPeriod,
                        noData);
            case NATURAL_BREAKS:
                return new NaturalBreaksOpImage(
                        src, numBins, extrema, roi, bands, xStart, yStart, xPeriod, yPeriod,
                        noData);
            default:
                throw new IllegalArgumentException(method.name());
        }
    }
}
