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

import java.awt.image.RenderedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import javax.media.jai.ROI;
import org.geotools.process.raster.classify.Classification;
import org.geotools.process.raster.classify.QuantileClassification;

/** Classification op for the quantile method. */
public class QuantileBreaksOpImage extends ClassBreaksOpImage {

    public QuantileBreaksOpImage(
            RenderedImage image,
            Integer numClasses,
            Double[][] extrema,
            ROI roi,
            Integer[] bands,
            Integer xStart,
            Integer yStart,
            Integer xPeriod,
            Integer yPeriod,
            Double noData) {
        super(image, numClasses, extrema, roi, bands, xStart, yStart, xPeriod, yPeriod, noData);
    }

    @Override
    protected Classification createClassification() {
        return new QuantileClassification(bands.length);
    }

    @Override
    protected void handleValue(double d, Classification c, int band) {
        QuantileClassification qc = (QuantileClassification) c;
        if (extrema != null) {
            double min = extrema[0][band];
            double max = extrema[1][band];

            if (d < min || d > max) {
                return;
            }
        }

        qc.count(d, band);
    }

    @Override
    protected void postCalculate(Classification c, int band) {
        QuantileClassification qc = (QuantileClassification) c;

        // get the total number of values
        int nvalues = qc.getCount(band);

        // calculate the number of values per class
        int size = (int) Math.ceil(nvalues / (double) numClasses);

        // grab the key iterator
        Iterator<Map.Entry<Double, Integer>> it = qc.getTable(band).entrySet().iterator();

        TreeSet<Double> set = new TreeSet<Double>();
        Map.Entry<Double, Integer> e = it.next();

        while (nvalues > 0) {
            // add the next break
            set.add(e.getKey());

            for (int i = 0; i < size && nvalues > 0; i++) {
                // consume the next value
                int count = e.getValue();
                e.setValue(--count);
                nvalues--;

                if (count == 0) {
                    // number of occurences of this entry exhausted, move to next
                    if (!it.hasNext()) {
                        break;
                    }
                    e = it.next();
                }
            }

            if (nvalues == 0) {
                // add the last value
                set.add(e.getKey());
            }
        }
        qc.setBreaks(band, set.toArray(new Double[set.size()]));
    }
}
