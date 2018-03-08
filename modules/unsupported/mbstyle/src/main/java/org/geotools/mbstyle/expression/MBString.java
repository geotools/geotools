/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.mbstyle.expression;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.parse.MBFormatException;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

public class MBString extends MBExpression{

    public MBString(JSONArray json){
        super(json);
    }


    /**
     * Returns a string consisting of the concatenation of the inputs.
     * Example: ["concat", string, string, ...]: string
     * @return
     */
    public Expression stringConcat(){
        String concatString = "";
        for(int i =1; i < json.size(); i++) {
            String s = (String) json.get(i);
            concatString += s;
        }
        return ff.literal(concatString);
    }

    /**
     * Returns the input string converted to lowercase. Follows the Unicode Default Case Conversion algorithm
     * and the locale-insensitive case mappings in the Unicode Character Database.
     * Example: ["downcase", string]: string
     * @return
     */
    public Expression stringDowncase(){
        if (json.get(1) instanceof String){
            String s = (String) json.get(1);
            return ff.literal(s.toLowerCase());
        } else {
            return ff.literal("Requires instance of string to convert to lowercase");
        }
    }

    /**
     * Returns the input string converted to uppercase. Follows the Unicode Default Case Conversion algorithm
     * and the locale-insensitive case mappings in the Unicode Character Database.
     * ["upcase", string]: string
     * @return
     */
    public Expression stringUpcase(){
        if (json.get(1) instanceof String){
            String s = (String) json.get(1);
            return ff.literal(s.toUpperCase());
        } else {
            return ff.literal("Requires instance of string to convert to uppercase");
        }
    }

    /**
     * Will return a function base on the mbexpression string name;
     * @return
     */
    @Override
    public Expression getExpression() {
        switch (name) {
            case "concat":
                return stringConcat();
            case "downcase":
                return stringDowncase();
            case "upcase":
                return stringUpcase();
            default:
                throw new MBFormatException(name + " is an unsupported string function");
            }
        }
}
