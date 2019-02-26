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

import java.util.logging.Logger;

/** Utility class for encoding SQL statements. */
class SQL {

    StringBuilder sql = new StringBuilder();

    SQL() {}

    SQL(String s) {
        sql.append(s);
    }

    /** Appends some "raw" sql. */
    public SQL append(String s) {
        sql.append(s);
        return this;
    }

    /** Appends a table or column name, which gets quoted. */
    public SQL name(String s) {
        sql.append("\"").append(s).append("\"");
        return this;
    }

    /** Appends a table name, encoding the schema if present. */
    public SQL table(RasterColumn col) {
        if (col.schema != null) {
            name(col.schema).append(".");
        }
        return name(col.table);
    }

    /** Trims the buffer by the specified amount. */
    public SQL trim(int i) {
        sql.setLength(sql.length() - i);
        return this;
    }

    @Override
    public String toString() {
        return sql.toString();
    }

    /** Logs the buffer and returns it. */
    public String logAndGet(Logger log) {
        String sql = toString();
        log.fine(sql);
        return sql;
    }
}
