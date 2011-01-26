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

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

public class HandlerBase implements ContentHandler {

    public void startJSON() throws ParseException, IOException {
    }
    
    public void endJSON() throws ParseException, IOException {
    }
    
    public boolean startObject() throws ParseException, IOException {
        return true;
    }
    
    public boolean endObject() throws ParseException, IOException {
        return true;
    }
    
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        return true;
    }

    public boolean endObjectEntry() throws ParseException, IOException {
        return true;
    }
    
    public boolean startArray() throws ParseException, IOException {
        return true;
    }

    public boolean endArray() throws ParseException, IOException {
        return true;
    }
    
    public boolean primitive(Object value) throws ParseException, IOException {
        return true;
    }
}
