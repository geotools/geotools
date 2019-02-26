/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import java.sql.ResultSet;
import java.sql.SQLException;

/** An overview of a raster column, obtained from the <code>raster_overviews</code> view. */
class RasterOverview extends RasterColumn {

    final Integer factor;

    RasterOverview(String name, String table, String schema, Integer factor) {
        super(name, table, schema);
        this.factor = factor;
    }

    RasterOverview(ResultSet rs) throws SQLException {
        this(
                rs.getString("o_raster_column"),
                rs.getString("o_table_name"),
                rs.getString("o_table_schema"),
                rs.getInt("overview_factor"));
    }
}
