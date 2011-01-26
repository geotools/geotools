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
package org.geotools.geojson.geom;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class LineHandler extends GeometryHandlerBase<LineString> {

    ArrayList coordinates;
    
    public LineHandler(GeometryFactory factory) {
        super(factory);
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("coordinates".equals(key)) {
            coordinates = new ArrayList();
        }
        return true;
    }
    
    @Override
    public boolean endObject() throws ParseException, IOException {
        if (coordinates != null) {
            Coordinate[] cs = coordinates(coordinates);
            value = factory.createLineString(cs);
            coordinates = null;
        }
        return true;
    }
    
    @Override
    public boolean startArray() throws ParseException, IOException {
        if (ordinates == null) {
            ordinates = new ArrayList();
        }
        return true;
    }
    
    @Override
    public boolean endArray() throws ParseException, IOException {
        if (ordinates != null) {
            Coordinate c = coordinate(ordinates);
            coordinates.add(c);
            ordinates = null;
        }
        
        return true;
    }
    
}
