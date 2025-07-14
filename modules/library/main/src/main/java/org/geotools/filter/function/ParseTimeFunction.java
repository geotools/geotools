/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;
import static org.geotools.util.DateTimeParser.FLAG_GET_TIME_ON_PRESENT;
import static org.geotools.util.DateTimeParser.FLAG_IS_LENIENT;
import static org.geotools.util.DateTimeParser.FLAG_SINGLE_DATE_AS_DATERANGE;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.DateRange;
import org.geotools.util.DateTimeParser;

/** Function parses GeoTools time notation into date. */
public class ParseTimeFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("parseTime", parameter("string", String.class));

    DateTimeParser parser;

    public ParseTimeFunction() {
        super(NAME);
        parser = new DateTimeParser(1, FLAG_GET_TIME_ON_PRESENT | FLAG_IS_LENIENT | FLAG_SINGLE_DATE_AS_DATERANGE);
    }

    @Override
    public Object evaluate(Object object) {
        Date dateTime = null;
        try {
            String value = (String) ((LiteralExpressionImpl) params.get(0)).getValue();
            if (value.startsWith("P")) {
                value = value + "/PRESENT";
            }
            dateTime = getDate(value, dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dateTime;
    }

    private Date getDate(String value, Date dateTime) throws ParseException {
        Collection parsedCollection = parser.parse(value);
        Object parsedObject = parsedCollection.iterator().next();
        if (parsedObject instanceof DateRange) {
            dateTime = ((DateRange) parsedObject).getMinValue();
        } else if (parsedObject instanceof Date) {
            dateTime = (Date) parsedObject;
        }
        return dateTime;
    }
}
