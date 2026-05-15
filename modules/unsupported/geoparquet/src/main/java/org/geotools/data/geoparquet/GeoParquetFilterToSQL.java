/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import java.util.List;
import java.util.function.Supplier;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.data.duckdb.DuckDBFilterToSQL;
import org.geotools.jackson.datatype.geoparquet.BboxCovering;
import org.geotools.jackson.datatype.geoparquet.Geometry;

/**
 * Filter SQL encoder for GeoParquet queries.
 *
 * <p>This class extends DuckDBFilterToSQL with GeoParquet-specific optimizations for encoding OGC Filter objects into
 * SQL queries. It handles translation of spatial filters, particularly optimizing bounding box (BBOX) queries to work
 * efficiently with GeoParquet's metadata and structure.
 *
 * <p>Key features include:
 *
 * <ul>
 *   <li>Optimized BBOX filter translation that uses GeoParquet's bounding box information
 *   <li>Support for the standard set of spatial operations adapted to GeoParquet's spatial model
 * </ul>
 *
 * <p>The implementation specifically targets the column structure and query patterns that perform well with GeoParquet
 * datasets, considering both the GeoParquet specification and DuckDB's spatial capabilities.
 */
public class GeoParquetFilterToSQL extends DuckDBFilterToSQL {

    /** Supplier for accessing GeoParquet dataset metadata during filter encoding. */
    private Supplier<GeoparquetDatasetMetadata> datasetMetadataSupplier;

    /**
     * Sets the supplier for GeoParquet dataset metadata.
     *
     * <p>This supplier is used to access the dataset metadata during filter encoding. It is particularly important for
     * optimizing spatial filters like BBOX by using the bounding box components defined in the metadata.
     *
     * @param supplier A supplier that provides access to the GeoParquet dataset metadata
     */
    public void setDatasetMetadataSupplier(Supplier<GeoparquetDatasetMetadata> supplier) {
        this.datasetMetadataSupplier = supplier;
    }

    /**
     * Returns a string representation of this filter encoder.
     *
     * @return A string representation showing the current SQL output
     */
    @Override
    public String toString() {
        return "GeoParquetFilterToSQL[" + out + "]";
    }

    /**
     * Converts a bounding box filter to optimized SQL using bbox components.
     *
     * <p>This method optimizes bounding box queries by using the 'bbox' column components common in GeoParquet
     * datasets. Instead of using expensive spatial functions, it uses simple comparisons on the xmin, xmax, ymin, ymax
     * components, which can be much more efficient.
     *
     * <p>The generated SQL follows the pattern: {@code bbox.xmin <= maxX and bbox.xmax >= minX and bbox.ymin <= maxY
     * and bbox.ymax >= minY} which implements a proper spatial intersection test using only simple comparisons. The
     * actual column names for the bbox components are resolved from the dataset metadata if available.
     *
     * @param filter The BBOX filter to encode
     * @param leftExp The left expression (typically the geometry column)
     * @param rightExpt The right expression (typically the literal bounding box)
     * @param extraData Extra data that might be passed through
     * @return The extraData parameter, potentially modified
     */
    @Override
    // TODO: apply a bbox and'ed filter to other spatial ops where appropriate to speed them up
    protected Object visitBBOX(BBOX filter, Expression leftExp, Expression rightExpt, Object extraData) {
        BoundingBox bounds = filter.getBounds();
        double xmin = bounds.getMinX();
        double xmax = bounds.getMaxX();
        double ymin = bounds.getMinY();
        double ymax = bounds.getMaxY();

        String[] acc = resolveCoveringBboxAccessors();
        if (acc != null) {
            // acc = [xminAccessor, yminAccessor, xmaxAccessor, ymaxAccessor]
            write(
                    "%s <= %f and %s >= %f and %s <= %f and %s >= %f",
                    acc[0], xmax, // xmin <= queryMaxX
                    acc[2], xmin, // xmax >= queryMinX
                    acc[1], ymax, // ymin <= queryMaxY
                    acc[3], ymin); // ymax >= queryMinY
            return extraData;
        }

        // Fallback to current behavior (hard-coded bbox)
        write("bbox.xmin <= %f and bbox.xmax >= %f and bbox.ymin <= %f and bbox.ymax >= %f", xmax, xmin, ymax, ymin);
        return extraData;
    }

    /**
     * Resolves the column accessors for the bounding box components from the GeoParquet dataset metadata if available.
     */
    private String[] resolveCoveringBboxAccessors() {
        if (datasetMetadataSupplier == null) return null;

        GeoparquetDatasetMetadata md = datasetMetadataSupplier.get();
        if (md == null || md.isEmpty()) return null;

        Geometry g = md.getPrimaryColumn().orElse(null);
        if (g == null || g.getCovering() == null || g.getCovering().getBbox() == null) return null;

        BboxCovering bbox = g.getCovering().getBbox();

        String xmin = pathToAccessor(bbox.getXmin());
        String ymin = pathToAccessor(bbox.getYmin());
        String xmax = pathToAccessor(bbox.getXmax());
        String ymax = pathToAccessor(bbox.getYmax());

        if (xmin == null || ymin == null || xmax == null || ymax == null) return null;
        return new String[] {xmin, ymin, xmax, ymax};
    }

    private static String pathToAccessor(List<String> path) {
        return (path == null || path.isEmpty()) ? null : String.join(".", path);
    }
}
