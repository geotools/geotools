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
package org.geotools.geojson.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geojson.HandlerBase;
import org.geotools.geojson.IContentHandler;
import org.json.simple.parser.ParseException;

public class ArrayHandler extends HandlerBase implements IContentHandler<List> {

    List values;
    List list;
    
    @Override
    public boolean startArray() throws ParseException, IOException {
        values = new ArrayList();
        return true;
    }
    
    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (values != null) {
            values.add(value);
            return true;
        }
        else {
            return super.primitive(value);
        }
    }
    
    @Override
    public boolean endArray() throws ParseException, IOException {
        list = values;
        values = null;
        return true;
    }
    
    public List getValue() {
        return list;
    }

}
