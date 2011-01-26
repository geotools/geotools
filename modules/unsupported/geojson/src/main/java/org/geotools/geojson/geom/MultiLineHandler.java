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
import java.util.List;

import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

public class MultiLineHandler extends GeometryHandlerBase<MultiLineString> {

    List<Coordinate> coordinates;
    List<Coordinate[]> lines;
    
    public MultiLineHandler(GeometryFactory factory) {
        super(factory);
        
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("coordinates".equals(key)) {
            lines = new ArrayList();
        }
        return true;
    }
    
    @Override
    public boolean startArray() throws ParseException, IOException {
        if (coordinates == null) {
            coordinates = new ArrayList();
        }
        else if (ordinates == null) {
            ordinates = new ArrayList();
        }
        return true;
    }
    
    @Override
    public boolean endArray() throws ParseException, IOException {
        if (ordinates != null) {
            coordinates.add(coordinate(ordinates));
            ordinates = null;
        }
        else if (coordinates != null) {
            lines.add(coordinates(coordinates));
            coordinates = null;
        }
        
        return true;
    }
    
    @Override
    public boolean endObject() throws ParseException, IOException {
        if (lines != null) {
            LineString[] lineStrings = new LineString[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                lineStrings[i] = factory.createLineString(lines.get(i));
            }
            value = factory.createMultiLineString(lineStrings);
            lines = null;
        }
        return true;
    }
}

