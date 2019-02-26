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

/** Time column of a raster table. */
class TimeColumn {

    String name;
    String type;

    TimeColumn(ResultSet rs) throws SQLException {
        name = rs.getString("column_name");
        type = rs.getString("data_type");
    }

    boolean isTimestamp() {
        return type.startsWith("timestamp");
    }

    SQL select(String fn, SQL sql) {
        sql.append("to_char(");
        if (fn != null) {
            sql.append(fn).append("(");
        }
        sql.name(name);
        if (fn != null) {
            sql.append(")");
        }

        // sql.append(" AT TIME ZONE 'UTC', 'YYYY-MM-DD\"T\"HH24:MI:SS.MS+00')");
        sql.append(", 'YYYY-MM-DD\"T\"HH24:MI:SS.MSOF')");
        sql.append(" AS ").name(fn != null ? fn : name);

        return sql;
    }
}
