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

import org.geotools.geojson.DelegatingHandler;
import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GeometryHandler extends DelegatingHandler<Geometry> {

    GeometryFactory factory;
    
    public GeometryHandler(GeometryFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("type".equals(key) && delegate == NULL) {
            delegate = UNINITIALIZED;
            return true;
        }
        else {
            return super.startObjectEntry(key);
        }
    }
    
    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (delegate == UNINITIALIZED) {
            delegate = createDelegate(lookupDelegate(value.toString()), new Object[]{factory});
            return true;
        }
        else {
            return super.primitive(value);
        }
    }
}
