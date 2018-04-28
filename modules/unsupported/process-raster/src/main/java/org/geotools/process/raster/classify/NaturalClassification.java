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

import java.util.ArrayList;
import java.util.List;
import org.geotools.process.classify.ClassificationMethod;

/** Helper class used for raster natural breaks classification. */
public class NaturalClassification extends Classification {

    List<Double>[] values;

    public NaturalClassification(int numBands) {
        super(ClassificationMethod.NATURAL_BREAKS, numBands);
        values = new List[numBands];
        for (int i = 0; i < values.length; i++) {
            values[i] = new ArrayList<Double>();
        }
    }

    public void count(double value, int band) {
        values[band].add(value);
    }

    public List<Double> getValues(int band) {
        return values[band];
    }
}
