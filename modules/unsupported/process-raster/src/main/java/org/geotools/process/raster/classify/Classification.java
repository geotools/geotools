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
package org.geotools.process.raster.classify;

import java.util.logging.Logger;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.util.logging.Logging;

/** Helper class used for raster classification. */
public class Classification {

    static final Logger LOGGER = Logging.getLogger(Classification.class);

    /** classification method */
    ClassificationMethod method;

    /** the breaks */
    Double[][] breaks;

    /** min/max */
    Double[] min, max;

    public Classification(ClassificationMethod method, int numBands) {
        this.method = method;
        this.breaks = new Double[numBands][];
        this.min = new Double[numBands];
        this.max = new Double[numBands];
    }

    public ClassificationMethod getMethod() {
        return method;
    }

    public Number[][] getBreaks() {
        return breaks;
    }

    public void setBreaks(int b, Double[] breaks) {
        this.breaks[b] = breaks;
    }

    public Double getMin(int b) {
        return min[b];
    }

    public void setMin(int b, Double min) {
        this.min[b] = min;
    }

    public Double getMax(int b) {
        return max[b];
    }

    public void setMax(int b, Double max) {
        this.max[b] = max;
    }

    public void print() {
        for (int i = 0; i < breaks.length; i++) {
            for (Double d : breaks[i]) {
                LOGGER.info(String.valueOf(d));
            }
        }
    }
}
