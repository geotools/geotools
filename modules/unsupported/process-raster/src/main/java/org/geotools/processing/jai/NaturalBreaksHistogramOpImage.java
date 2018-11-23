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
import java.util.List;
import javax.media.jai.ROI;
import org.geotools.process.raster.classify.Classification;
import org.geotools.process.raster.classify.HistogramClassification;
import org.geotools.process.raster.classify.HistogramClassification.Bucket;

/** Classification op for the natural breaks method. */
public class NaturalBreaksHistogramOpImage extends ClassBreaksOpImage {

    int numBins;

    public NaturalBreaksHistogramOpImage(
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

        final int k = numClasses;
        final int m = buckets.size();

        if (k >= m) {
            // just return all the values
            Double[] breaks = new Double[m + 1];
            for (int i = 0; i < m; i++) {
                breaks[i] = buckets.get(i).getMin();
            }
            breaks[m] = buckets.get(m - 1).getMax();
            c.setBreaks(band, breaks);
            return;
        }

        int[][] iwork = new int[m + 1][k + 1];
        double[][] work = new double[m + 1][k + 1];

        for (int j = 1; j <= k; j++) {
            // the first item is always in the first class!
            iwork[0][j] = 1;
            iwork[1][j] = 1;
            // initialize work matirix
            work[1][j] = 0;
            for (int i = 2; i <= m; i++) {
                work[i][j] = Double.MAX_VALUE;
            }
        }

        // calculate the class for each data item
        for (int i = 1; i <= m; i++) {
            // sum of data values
            double s1 = 0;
            // sum of squares of data values
            double s2 = 0;

            double var = 0.0;
            int totalCount = 0;
            // consider all the previous values
            for (int ii = 1; ii <= i; ii++) {
                // index in to sorted data array
                int i3 = i - ii + 1;
                // remember to allow for 0 index
                Bucket bucket = buckets.get(i3 - 1);
                double average = bucket.getAverage();
                int count = bucket.getCount();
                // double squaredSum = bucket.getSquaredSum();
                // update running totals
                // ... adding the sum of all squares contained in the bucket
                s2 = s2 + (average * average * count);
                // ... adding the sum of all values contained in the bucket
                s1 += average * count;
                totalCount += count;
                double s0 = totalCount;
                // calculate (square of) the variance
                // (http://secure.wikimedia.org/wikipedia/en/wiki/Standard_deviation#Rapid_calculation_methods)
                var = s2 - ((s1 * s1) / s0);
                // System.out.println(s0+" "+s1+" "+s2);
                // System.out.println(i+","+ii+" var "+var);
                int ik = i3 - 1;
                if (ik != 0) {
                    // not the last value
                    for (int j = 2; j <= k; j++) {
                        // for each class compare current value to var + previous value
                        // System.out.println("\tis "+work[i][j]+" >= "+(var + work[ik][j - 1]));
                        if (work[i][j] >= (var + work[ik][j - 1])) {
                            // if it is greater or equal update classification
                            iwork[i][j] = i3 - 1;
                            // System.out.println("\t\tiwork["+i+"]["+j+"] = "+i3);
                            work[i][j] = var + work[ik][j - 1];
                        }
                    }
                }
            }
            // store the latest variance!
            iwork[i][1] = 1;
            work[i][1] = var;
        }

        Double[] breaks = new Double[k + 1];

        // go through matrix and extract class breaks
        int ik = m - 1;
        breaks[k] = buckets.get(ik).getMax();
        for (int j = k; j >= 2; j--) {
            int id =
                    (int) iwork[ik][j] - 1; // subtract one as we want inclusive breaks on the left?
            breaks[j - 1] = buckets.get(id).getAverage();
            ik = (int) iwork[ik][j] - 1;
        }
        breaks[0] = buckets.get(0).getMin();
        hc.setBreaks(band, breaks);
    }
}
