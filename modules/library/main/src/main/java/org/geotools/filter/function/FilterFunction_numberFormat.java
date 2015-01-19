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


import static org.geotools.filter.capability.FunctionNameImpl.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Formats a number into a string given a certain pattern (specified in the format accepted
 * by {@link DecimalFormat}} 
 * @author Andrea Aime - OpenGeo
 *
 *
 *
 *
 * @source $URL$
 */
public class FilterFunction_numberFormat extends FunctionExpressionImpl {
    
    public static FunctionName NAME = new FunctionNameImpl("numberFormat", String.class,
            parameter("format", String.class),
            parameter("number", Number.class));
    
    public static DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH);

    public FilterFunction_numberFormat() {
        super(NAME);
    }

    public Object evaluate(Object feature) {
        String format;
        Double number;

        try {
            // attempt to get value and perform conversion
            format  = getExpression(0).evaluate(feature, String.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateFormat argument #0 - expected type String");
        }

        try { // attempt to get value and perform conversion
            number = getExpression(1).evaluate(feature, Double.class); 
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function dateFormat argument #1 - expected type java.util.Date");
        }
        
        if(format == null || number == null) {
            return null;
        }

        DecimalFormat numberFormat = new DecimalFormat(format, decimalFormatSymbols);
        return numberFormat.format(number);
    }

    

}
