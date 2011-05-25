/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

/**
 *
 * @author  jfc173
 *
 * @source $URL$
 */
public class GlyphProperty {
    
    private String name;
    private Class<?> type;
    private Object value;
    
    /** Creates a new instance of GlyphProperty */
    public GlyphProperty(String s, Class<?> c, Object o) {
        name = s;
        type = c;
        value = o;
    }
    
    public String getName(){
        return name;
    }
    
    public Class<?> getType(){
        return type;
    }
    
    public Object getValue(){
        return value;
    }
    
    public void setValue(Object v){
        value = v;
    }
    
}
