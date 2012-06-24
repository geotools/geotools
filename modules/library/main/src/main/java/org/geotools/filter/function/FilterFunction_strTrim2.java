/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;

import org.geotools.data.Parameter;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.geotools.util.KVP;
import org.opengis.filter.capability.FunctionName;

/**
 * Function implementing the Symbology Encoding Trim function.
 * <p>
 * This function takes the following arguments:
 * <ol>
 *   <li>The string being trimmed
 *   <li>The string to trim 
 *   <li>The trim direction, one of trailing, leading, both
 * </ol>
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class FilterFunction_strTrim2 extends FunctionExpressionImpl {

    public static FunctionName NAME;
    static {
        Parameter<String> method = new Parameter<String>(
                "method",String.class,
                Text.text("method"),
                Text.text("Method used to trim the provided text"),
                true,1,1,
                "both",
                new KVP(Parameter.OPTIONS,Arrays.asList(new String[]{"leading","trailing","both"}))
        );
        NAME = new FunctionNameImpl("strTrim2",
            parameter("trim", String.class),
            parameter("string", String.class),
            method,
            parameter("character", String.class));
    }
    public FilterFunction_strTrim2() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        // the string to trim
        String str = getExpression(0).evaluate(object, String.class);
   
        // leading, trailing, or both
        String pos = getExpression(1).evaluate(object, String.class);
        
        // characters to strip off
        String ch = getExpression(2).evaluate(object, String.class);

        //optimize for regular String.trim()
        if ("".equals(ch.trim()) && "both".equalsIgnoreCase(pos)) {
            return str.trim();
        }
        
        if ("both".equalsIgnoreCase(pos) || "leading".equalsIgnoreCase(pos)) {
            str = str.replaceAll(String.format("^%s*", ch), "");
        }
        if ("both".equalsIgnoreCase(pos) || "trailing".equalsIgnoreCase(pos)) {
            str = str.replaceAll(String.format("%s*$", ch), "");
        }
        return str;
    }
}
