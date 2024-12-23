/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldUsage;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.GDALRasterAttributeTable;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Row;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class RATCollector {

    private final int band;
    Map<String, List<String>> allRows = new TreeMap<>();

    Function<Row, String> keyBuilder;

    Integer countField;

    public RATCollector(int band, GDALRasterAttributeTable rat) {
        this.band = band;
        List<PAMDataset.PAMRasterBand.FieldDefn> fields = rat.getFieldDefn();
        int minField, maxField;
        minField = maxField = -1;
        for (int i = 0; i < fields.size(); i++) {
            PAMDataset.PAMRasterBand.FieldDefn field = fields.get(i);
            if (FieldUsage.PixelCount.equals(field.getUsage())) {
                countField = i;
            } else if (FieldUsage.MinMax.equals(field.getUsage())) {
                final int valueField = i;
                keyBuilder = row -> row.getF().get(valueField);
            } else if (FieldUsage.Min.equals(field.getUsage())) {
                minField = i;
            } else if (FieldUsage.Max.equals(field.getUsage())) {
                maxField = i;
            }
        }
        if (keyBuilder == null)
            if (minField >= 0 && maxField >= 0) {
                final int min = minField;
                final int max = maxField;
                keyBuilder = row -> row.getF().get(min) + "-" + row.getF().get(max);
            } else {
                throw new IllegalArgumentException("Could not find value fields in the raster attribute table");
            }
        // initialize the rows map
        rat.getRow().forEach(row -> allRows.computeIfAbsent(keyBuilder.apply(row), k -> row.getF()));
    }

    public void collect(GDALRasterAttributeTable rat) {
        for (Row row : rat.getRow()) {
            String key = keyBuilder.apply(row);
            List<String> prev = allRows.get(key);
            List<String> curr = row.getF();
            if (prev == null) {
                prev = row.getF();
                allRows.put(key, prev);
            } else {
                for (int i = 0; i < prev.size(); i++) {
                    String prevValue = prev.get(i);
                    String currValue = curr.get(i);
                    if (countField != null && i == countField) continue;
                    if (!prevValue.equals(currValue)) {
                        throw new IllegalArgumentException(String.format(
                                "Different values for band %d in row %s: %s != %s", band, key, prevValue, currValue));
                    }
                }
                if (countField != null) {
                    long countPrev = Long.parseLong(prev.get(countField));
                    long countCurr = Long.parseLong(curr.get(countField));
                    prev.set(countField, String.valueOf(countPrev + countCurr));
                }
            }
        }
    }

    /**
     * Sets the collected rows into the pam dataset
     *
     * @param dataset
     * @param band
     */
    public void replaceRows(PAMDataset dataset, int band) {
        List<PAMDataset.PAMRasterBand> bands = dataset.getPAMRasterBand();
        GDALRasterAttributeTable rat = bands.get(band).getGdalRasterAttributeTable();
        rat.getRow().clear();
        int idx = 0;
        for (List<String> value : allRows.values()) {
            Row row = new Row();
            row.getF().addAll(value);
            row.setIndex(idx++);
            rat.getRow().add(row);
        }
    }
}
