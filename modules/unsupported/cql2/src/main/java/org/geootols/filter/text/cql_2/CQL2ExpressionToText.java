/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geootols.filter.text.cql_2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.geotools.filter.text.commons.ExpressionToText;
import org.opengis.temporal.Period;

/** Subclassed to handle the encoding of date literals */
public class CQL2ExpressionToText extends ExpressionToText {

    public CQL2ExpressionToText() {
        super(false);
    }

    @Override
    public StringBuilder dateToText(Date date, StringBuilder output) {
        final DateFormat formatter;

        // If the Date has millisecond resolution, print the millis.
        boolean sqlDate = date instanceof java.sql.Date;
        if (sqlDate) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        } else if (date.getTime() % 1000 == 0) {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        } else {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        }
        if (!(date instanceof java.sql.Date)) formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(date);

        // GMT is not part of CQL syntax so it is removed
        text = text.replace("UTC", "Z");

        if (sqlDate) {
            output.append("DATE('").append(text).append("')");
        } else {
            output.append("TIMESTAMP('").append(text).append("')");
        }

        return output;
    }

    @Override
    protected StringBuilder periodToText(Period period, StringBuilder output) {
        output.append("INTERVAL(");
        output = dateToText(period.getBeginning().getPosition().getDate(), output);
        output.append(", ");
        output = dateToText(period.getEnding().getPosition().getDate(), output);
        output.append(")");

        return output;
    }
}
