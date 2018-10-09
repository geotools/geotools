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

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.util.logging.Logging;

/** Helper class used for raster quantile classification. */
public class QuantileClassification extends Classification {

    static final Logger LOGGER = Logging.getLogger(Classification.class);

    int[] counts;
    SortedMap<Double, Integer>[] tables;

    public QuantileClassification(int numBands) {
        super(ClassificationMethod.QUANTILE, numBands);
        counts = new int[numBands];
        tables = new SortedMap[numBands];
    }

    public void count(double value, int band) {
        counts[band]++;

        SortedMap<Double, Integer> table = getTable(band);

        Integer count = table.get(value);
        table.put(value, count != null ? new Integer(count + 1) : new Integer(1));
    }

    public SortedMap<Double, Integer> getTable(int band) {
        SortedMap<Double, Integer> table = tables[band];
        if (table == null) {
            table = new TreeMap<Double, Integer>();
            tables[band] = table;
        }
        return table;
    }

    public int getCount(int band) {
        return counts[band];
    }

    void printTable() {
        for (int i = 0; i < tables.length; i++) {
            SortedMap<Double, Integer> table = getTable(i);
            for (Entry<Double, Integer> e : table.entrySet()) {
                LOGGER.info(String.format("%f: %d", e.getKey(), e.getValue()));
            }
        }
    }
}
