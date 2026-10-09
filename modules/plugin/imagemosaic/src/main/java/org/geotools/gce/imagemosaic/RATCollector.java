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
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldType;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.FieldUsage;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.GDALRasterAttributeTable;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.Row;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

public class RATCollector {

    private static final Logger LOGGER = Logging.getLogger(RATCollector.class);

    /** Stands for a geometry that could not be merged, written back as an empty cell. */
    private static final Geometry NO_GEOMETRY = new GeometryFactory().createGeometryCollection();

    private final int band;

    private final RATGeometries geometries;
    Map<String, List<String>> allRows = new TreeMap<>();

    Function<Row, String> keyBuilder;

    Integer countField;

    /** True for the fields holding a geometry, which are merged instead of compared. */
    private final boolean[] geometryFields;

    /**
     * The geometries merged so far, by row key. A table can carry several geometry columns, so each row gets one slot
     * per field, indexed the same way as {@link #geometryFields}. Only the rows whose geometry differs between granules
     * get an array, and the geometries stay parsed until {@link #replaceRows} writes them back as WKT.
     */
    private final Map<String, Geometry[]> mergedGeometries = new HashMap<>();

    private final WKTReader wktReader = new WKTReader();
    private final WKTWriter wktWriter = new WKTWriter();

    public RATCollector(int band, GDALRasterAttributeTable rat) {
        this(band, rat, RATGeometries.OFF);
    }

    public RATCollector(int band, GDALRasterAttributeTable rat, RATGeometries geometries) {
        this.band = band;
        this.geometries = geometries;
        List<PAMDataset.PAMRasterBand.FieldDefn> fields = rat.getFieldDefn();
        this.geometryFields = new boolean[fields.size()];
        int minField, maxField;
        minField = maxField = -1;
        for (int i = 0; i < fields.size(); i++) {
            PAMDataset.PAMRasterBand.FieldDefn field = fields.get(i);
            geometryFields[i] = FieldType.WKBGeometry == field.getType();
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
                    if (countField != null && i == countField) continue;
                    String prevValue = prev.get(i);
                    String currValue = curr.get(i);
                    if (prevValue.equals(currValue)) continue;
                    if (geometryFields[i] && geometries != RATGeometries.COLLECT) {
                        if (geometries == RATGeometries.MERGE) merge(key, i, prevValue, currValue);
                        else prev.set(i, "");
                        continue;
                    }
                    throw new IllegalArgumentException("Different values for band %d in row %s: %s != %s"
                            .formatted(band, key, prevValue, currValue));
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
     * Merges a geometry into the one collected for the same row and field, which a source clipping the geometry to each
     * granule makes differ. A merge that fails, for any reason, collapses the cell to {@link #NO_GEOMETRY}: no extent
     * is better than a wrong one, and one unreadable cell must not stop the mosaic build.
     *
     * @param collectedWkt the cell of the first granule carrying this row, parsed on the first merge only
     */
    private void merge(String key, int field, String collectedWkt, String wkt) {
        Geometry[] rowGeometries = mergedGeometries.computeIfAbsent(key, k -> new Geometry[geometryFields.length]);
        try {
            if (rowGeometries[field] == null) rowGeometries[field] = wktReader.read(collectedWkt);
            if (rowGeometries[field] == NO_GEOMETRY) return;
            rowGeometries[field] = rowGeometries[field].union(wktReader.read(wkt));
        } catch (Exception | OutOfMemoryError e) {
            rowGeometries[field] = NO_GEOMETRY;
            LOGGER.log(
                    Level.FINE,
                    e,
                    () -> "Could not merge the geometries of band %d row %s, leaving the cell empty: %s, %s"
                            .formatted(band, key, collectedWkt, wkt));
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
        for (Map.Entry<String, List<String>> entry : allRows.entrySet()) {
            List<String> values = entry.getValue();
            writeGeometries(values, mergedGeometries.get(entry.getKey()));
            Row row = new Row();
            row.getF().addAll(values);
            row.setIndex(idx++);
            rat.getRow().add(row);
        }
    }

    /** Turns the merged geometries of one row back into WKT, leaving the cells that were never merged alone. */
    private void writeGeometries(List<String> values, Geometry[] rowGeometries) {
        if (rowGeometries == null) return;
        for (int field = 0; field < rowGeometries.length; field++) {
            Geometry merged = rowGeometries[field];
            if (merged == null) continue;
            values.set(field, merged == NO_GEOMETRY ? "" : wktWriter.write(merged));
        }
    }
}
