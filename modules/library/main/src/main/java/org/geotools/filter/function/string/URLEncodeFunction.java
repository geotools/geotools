/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function.string;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * URL encodes a string.
 *
 * <p>This function expects:
 *
 * <ol>
 *   <li>Literal: String to be URL encoded
 *   <li>Literal: (Optional) Boolean indicating if string should be form URL encoded (defaults to
 *       false)
 *
 * @see URLEncoder#encode(String)
 * @author Billy Newman (BIT Systems)
 */
public class URLEncodeFunction extends FunctionExpressionImpl {

    /** The FunctionName */
    public static FunctionName NAME =
            new FunctionNameImpl(
                    "strURLEncode",
                    String.class,
                    parameter("encodeable", String.class),
                    parameter("formUrlEncode", Boolean.class, 0, 1));

    /** Create a new FilterFunction_strURLEncode instance */
    public URLEncodeFunction() {
        super(NAME);
    }

    /**
     * URL encode the string.
     *
     * @return The URL encoded string
     */
    @Override
    public Object evaluate(Object feature) {
        String stringToBeEncoded;

        try { // attempt to get value and perform encoding
            stringToBeEncoded = getExpression(0).evaluate(feature, String.class);
        } catch (Exception e) // probably a type error
        {
            throw new IllegalArgumentException(
                    "Filter Function problem for function strURLEncode argument #0 - expected type String");
        }

        Boolean formUrlEncode = Boolean.FALSE;
        if (params.size() == 2) {
            try {
                formUrlEncode = getExpression(1).evaluate(feature, Boolean.class);
            } catch (Exception e) // probably a type error
            {
                throw new IllegalArgumentException(
                        "Filter Function problem for function strURLEncode argument #1 - expected type Boolean");
            }
        }

        try {
            String encoded = URLEncoder.encode(stringToBeEncoded, "utf-8");
            if (!formUrlEncode.booleanValue()) {
                // Using URLEncoder, spaces are converted to plus signs, convert to %20 for non form
                // url encoding
                encoded = encoded.replaceAll("\\+", "%20");
            }

            return encoded;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function strURLEncode argument #0 - "
                            + e.getMessage());
        }
    }
}
