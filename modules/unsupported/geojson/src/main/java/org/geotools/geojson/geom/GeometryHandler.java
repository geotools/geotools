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
import org.geotools.geojson.RecordingHandler;
import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 *
 * @source $URL$
 */
public class GeometryHandler extends DelegatingHandler<Geometry> {

    GeometryFactory factory;
    RecordingHandler proxy;

    public GeometryHandler(GeometryFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("type".equals(key) && (delegate == NULL || delegate == proxy)) {
            delegate = UNINITIALIZED;
            return true;
        }
        else if ("coordinates".equals(key) && delegate == NULL) {
            //case of specifying coordinates before the actual geometry type, create a proxy 
            // handler that will simply track calls until the type is actually specified
            proxy = new RecordingHandler();
            delegate = proxy;
            return super.startObjectEntry(key);
        }
        else {
            return super.startObjectEntry(key);
        }
    }
    
    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        if (delegate == UNINITIALIZED) {
            delegate = createDelegate(lookupDelegate(value.toString()), new Object[]{factory});
            if (proxy != null) {
                proxy.replay(delegate);
                proxy = null;
            }
            return true;
        }
        else {
            return super.primitive(value);
        }
    }
}
