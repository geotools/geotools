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

/** Filter SQL encoder for GeoParquet queries. */
public class GeoParquetFilterToSQL extends DuckDBFilterToSQL {

    @Override
    public String toString() {
        return "GeoParquetFilterToSQL[" + out + "]";
    }

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
