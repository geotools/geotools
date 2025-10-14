/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.clickhouse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.geotools.data.jdbc.FilterToSQL;

public class ClickHouseFilterToSQL extends FilterToSQL {

    @Override
    protected void writeLiteral(Object literal) throws IOException {
        if (literal instanceof java.sql.Date) {
            LocalDate ld = ((java.sql.Date) literal).toLocalDate();
            out.write("toDate('" + ld + "')"); // ISO yyyy-MM-dd
        } else if (literal instanceof java.sql.Timestamp) {
            Instant inst = ((java.sql.Timestamp) literal).toInstant();
            out.write("parseDateTimeBestEffort('" + DateTimeFormatter.ISO_INSTANT.format(inst) + "')");
        } else if (literal instanceof java.util.Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String isoDate = sdf.format(date);
            out.write("parseDateTimeBestEffort('" + isoDate + "')");
        } else {
            super.writeLiteral(literal);
        }
    }
}
