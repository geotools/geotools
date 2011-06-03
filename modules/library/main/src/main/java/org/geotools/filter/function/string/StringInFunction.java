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
package org.geotools.filter.function.string;

import java.util.LinkedHashMap;
import java.util.List;

import org.geotools.filter.FunctionImpl;
import org.opengis.filter.capability.FunctionName;

@SuppressWarnings("unchecked")
public class StringInFunction extends FunctionImpl {

    static FunctionName NAME = functionName("strIn", "result:Boolean", "string:String", 
        "matchCase:Boolean", "values:String:1,");
    
    public StringInFunction() {
        setName("strIn");
        functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        LinkedHashMap<String, Object> args = dispatchArguments(object);
        
        String str = (String) args.get("string");
        Boolean matchCase = (Boolean) args.get("matchCase");
        List<String> values = (List<String>) args.get("values");
        
        for (String value : values) {
            if (matchCase) {
                if (str.equals(value)) {
                    return true;
                }
            }
            else {
                if (str.equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
