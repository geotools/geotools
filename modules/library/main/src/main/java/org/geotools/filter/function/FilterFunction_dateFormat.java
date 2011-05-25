package org.geotools.filter.function;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geotools.filter.FunctionExpressionImpl;

/**
 * Formats a date into a string given a certain pattern (specified in the format accepted
 * by {@link SimpleDateFormat}} 
 * @see SimpleDateFormat
 * @author Andrea Aime - TOPP
 *
 *
 *
 * @source $URL$
 */
public class FilterFunction_dateFormat extends FunctionExpressionImpl {
    
    public FilterFunction_dateFormat() {
        super("dateFormat");
    }

    @Override
    public int getArgCount() {
        return 2;
    }

    public Object evaluate(Object feature) {
        String format;
        Date date;

        try {
            // attempt to get value and perform conversion
            format  = getExpression(0).evaluate(feature, String.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateFormat argument #0 - expected type String");
        }

        try { // attempt to get value and perform conversion
            date = getExpression(1).evaluate(feature, Date.class); 
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateFormat argument #1 - expected type java.util.Date");
        }

        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    

}
