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
package org.geotools.se.v1_1.bindings;

/**
 * Container for se:InlineContent element.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class InlineContent {

    String encoding;
    Object content;
    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    public String getEncoding() {
        return encoding;
    }
    
    public void setContent(Object content) {
        this.content = content;
    }
    
    public Object getContent() {
        return content;
    }
}
