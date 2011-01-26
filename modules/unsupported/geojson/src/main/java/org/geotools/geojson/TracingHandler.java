/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.json.simple.parser.ParseException;

public class TracingHandler implements InvocationHandler {

    int indent = 0;
    Object delegate;
    
    public TracingHandler(Object delegate) {
        this.delegate = delegate;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startObject".equals(method.getName())) {
            startObject();
        }
        else if ("endObject".equals(method.getName())) {
            endObject();
        }
        else if ("startObjectEntry".equals(method.getName())) {
            startObjectEntry((String) args[0]);
        }
        else if ("endObjectEntry".equals(method.getName())) {
            endObjectEntry();
        }
        else if ("startArray".equals(method.getName())) {
            startArray();
        }
        else if ("endArray".equals(method.getName())) {
            endArray();
        }
        else if ("primitive".equals(method.getName())) {
            primitive(args[0]);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        if (args != null && args.length > 0) {
            sb.append("[");
            for (Object arg : args) {
                sb.append(arg).append(",");
            }
            sb.setLength(sb.length()-1);
            sb.append("]");
        }
        //System.out.println(sb.toString());
        return method.invoke(delegate, args);
    }
    
    void startJSON() throws ParseException, IOException {
        
    }
    
    void endJSON() throws ParseException, IOException {
        
    }
    
    boolean startObject() throws ParseException, IOException {
        //indent();
        System.out.println("{");
        indent++;
        return true;
    }
    
    boolean endObject() throws ParseException, IOException {
        indent--;
        indent();
        System.out.print("}");
        return true;
    }
    
    boolean startObjectEntry(String key) throws ParseException, IOException {
        indent();
        System.out.print(key + ": ");
        return true;
    }
    
    boolean endObjectEntry() throws ParseException, IOException {
        System.out.println(",");
        return true;
    }
    
    boolean startArray() throws ParseException, IOException {
        //indent();
        System.out.print("[");
        return true;
    }
    
    boolean endArray() throws ParseException, IOException {
        //indent();
        System.out.print("]");
        return true;
    }
    
    boolean primitive(Object value) throws ParseException, IOException {
        System.out.print(value);
        return true;
    }
    
    void indent() {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
    }
}
