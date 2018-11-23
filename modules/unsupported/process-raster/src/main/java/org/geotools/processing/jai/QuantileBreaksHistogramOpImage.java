/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.RenderedImage;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.media.jai.ROI;
import org.geotools.process.raster.classify.Classification;
import org.geotools.process.raster.classify.HistogramClassification;
import org.geotools.process.raster.classify.HistogramClassification.Bucket;

/**
 * Classification op for the quantile method, using histograms instead of a fully developed list of
 * values
 */
public class QuantileBreaksHistogramOpImage extends ClassBreaksOpImage {

    int numBins;

    public QuantileBreaksHistogramOpImage(
            RenderedImage image,
            Integer numClasses,
            Double[][] extrema,
            ROI roi,
            Integer[] bands,
            Integer xStart,
            Integer yStart,
            Integer xPeriod,
            Integer yPeriod,
            Double noData,
            int numBins) {
        super(image, numClasses, extrema, roi, bands, xStart, yStart, xPeriod, yPeriod, noData);
        this.numBins = numBins;
    }

    @Override
    protected Classification createClassification() {
        return new HistogramClassification(bands.length, extrema, numBins);
    }

    @Override
    protected void handleValue(double d, Classification c, int band) {
        ((HistogramClassification) c).count(d, band);
    }

    @Override
    protected void postCalculate(Classification c, int band) {
        HistogramClassification hc = (HistogramClassification) c;
        List<Bucket> buckets = hc.getBuckets(band);

        // calculate the number of values per class
        int nvalues = buckets.stream().mapToInt(b -> b.getCount()).sum();
        int size = (int) Math.ceil(nvalues / (double) numClasses);

        // grab the key iterator
        Iterator<Bucket> it = buckets.iterator();

        TreeSet<Double> set = new TreeSet<Double>();
        Bucket e = it.next();

        int classIdx = 1;
        int count = 0;
        set.add(e.getMin());
        while (classIdx < numClasses && it.hasNext()) {
            count += e.getCount();
            e = it.next();

            if (count >= (size * classIdx)) {
                classIdx++;
                set.add(e.getMin());
            }
        }
        set.add(buckets.get(buckets.size() - 1).getMax());
        hc.setBreaks(band, set.toArray(new Double[set.size()]));
    }
}
