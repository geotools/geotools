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

import org.geotools.geojson.DelegatingHandler;
import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GeometryCollectionHandler extends DelegatingHandler<GeometryCollection> {

    GeometryFactory factory;
    List<Geometry> geoms;
    GeometryCollection value;
    Class delegateClass;
    public GeometryCollectionHandler(GeometryFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public boolean startObject() throws ParseException, IOException {
        if (geoms != null) {
            //means start of a member geometry object
            delegate = UNINITIALIZED;
        }
        return true;
    }
    
    @Override
    public boolean endObject() throws ParseException, IOException {
        if (delegate instanceof GeometryHandlerBase) {
            //end of a member geometry
            ((GeometryHandlerBase)delegate).endObject();
            geoms.add(((GeometryHandlerBase)delegate).getValue());
            delegate = NULL;
        }
        else {
            Geometry[] geometries = geoms.toArray(new Geometry[geoms.size()]);
            value = factory.createGeometryCollection(geometries);
            geoms = null;
        }
        
        return true;
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("geometries".equals(key)) {
            geoms = new ArrayList();
        }
        else if (geoms != null) {
            super.startObjectEntry(key);
        }
        
        return true;
    }
    
    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (delegateClass != null) {
            delegate = createDelegate(delegateClass, new Object[]{factory});
            delegateClass = null;
        }
        return true;
    }
    
    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (geoms != null && delegate == UNINITIALIZED) {
            //this is the "type" of a member geometry
            delegateClass = lookupDelegate(value.toString());
        }
        else {
            return super.primitive(value);
        }
        
        return true;
    }
    
    @Override
    public GeometryCollection getValue() {
        return value;
    }
}
