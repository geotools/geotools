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

import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.data.duckdb.DuckDBFilterToSQL;

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
     * and bbox.ymax >= minY} which implements a proper spatial intersection test using only simple comparisons.
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

        // bbox intersection predicate
        write("bbox.xmin <= %f and bbox.xmax >= %f and bbox.ymin <= %f and bbox.ymax >= %f", xmax, xmin, ymax, ymin);
        return extraData;
    }
}
