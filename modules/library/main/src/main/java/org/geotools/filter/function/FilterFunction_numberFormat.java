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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Logger;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Formats a number into a string given a certain pattern (specified in the format accepted by
 * {@link DecimalFormat}}
 *
 * @author Andrea Aime - OpenGeo
 */
public class FilterFunction_numberFormat extends FunctionExpressionImpl {
    static final Logger LOGGER = Logging.getLogger(FilterFunction_numberFormat.class);

    static HashSet<String> languages = new HashSet<>();

    Locale locale = Locale.ENGLISH;

    static {
        for (Locale loc : Locale.getAvailableLocales()) {
            languages.add(loc.getLanguage());
        }
    }

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "numberFormat",
                    String.class,
                    parameter("format", String.class),
                    parameter("number", Number.class),
                    parameter("language", String.class, 0, 1));

    public FilterFunction_numberFormat() {
        super(NAME);
    }

    public Object evaluate(Object feature) {
        String format;
        Double number;
        String localeString = "";
        try {
            // attempt to get value and perform conversion
            format = getExpression(0).evaluate(feature, String.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function NumberFormat argument #0 - expected type String");
        }

        try { // attempt to get value and perform conversion
            number = getExpression(1).evaluate(feature, Double.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function NumberFormat argument #1 - expected type java.util.Double");
        }

        if (format == null || number == null) {
            return null;
        }
        try { // attempt to get value and perform conversion
            if (params.size() > 2) {
                Expression second = getExpression(2);

                localeString = second.evaluate(feature, String.class);
            }
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function NumberFormat argument #2 - expected type String");
        }
        if (languages.contains(localeString)) {
            if (localeString != null && !localeString.isEmpty()) {
                locale = Locale.forLanguageTag(localeString);
            }

        } else {
            throw new IllegalArgumentException(
                    "Unknown language code '" + localeString + "' in numberFormat function");
        }

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);

        DecimalFormat numberFormat = new DecimalFormat(format, decimalFormatSymbols);
        return numberFormat.format(number);
    }
}
