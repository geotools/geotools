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

import org.geotools.filter.FunctionExpressionImpl;

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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/filter/function/FilterFunction_strTrim2.java $
 */
public class FilterFunction_strTrim2 extends FunctionExpressionImpl {

    public FilterFunction_strTrim2() {
        super("strTrim2");
    }
    
    @Override
    public int getArgCount() {
        return 3;
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
