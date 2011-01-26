/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.geotools.filter.FunctionExpressionImpl;

/**
 * Parses a date from a string given a certain pattern (specified in the format accepted
 * by {@link SimpleDateFormat}} 
 * @see SimpleDateFormat
 * @author Andrea Aime - TOPP
 *
 *
 * @source $URL$
 */
public class FilterFunction_dateParse extends FunctionExpressionImpl {
    
    public FilterFunction_dateParse() {
        super("dateParse");
    }

    @Override
    public int getArgCount() {
        return 2;
    }

    public Object evaluate(Object feature) {
        String format;
        String date;

        try {
            // attempt to get value and perform conversion
            format  = getExpression(0).evaluate(feature, String.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateParse argument #0 - expected type String");
        }

        try { // attempt to get value and perform conversion
            date = getExpression(1).evaluate(feature, String.class); 
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateParse argument #1 - expected type String");
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(date);
        } catch(ParseException e) {
            throw new IllegalArgumentException("Invalid date, could not parse", e);
        }
    }

    

}
