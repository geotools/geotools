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
import org.geotools.geojson.RecordingHandler;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;

public class GeometryCollectionHandler extends DelegatingHandler<GeometryCollection> {

    GeometryFactory factory;
    List<Geometry> geoms;
    GeometryCollection value;
    RecordingHandler proxy;
    Class delegateClass;

    public GeometryCollectionHandler(GeometryFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        if (geoms != null) {
            // means start of a member geometry object
            delegate = UNINITIALIZED;
        }
        return true;
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        if (delegate instanceof GeometryHandlerBase) {
            // end of a member geometry
            ((GeometryHandlerBase) delegate).endObject();
            Geometry geomObject = ((GeometryHandlerBase) delegate).getValue();
            if (geomObject != null) geoms.add(geomObject);
            delegate = NULL;
        } else {
            Geometry[] geometries = geoms.toArray(new Geometry[geoms.size()]);
            value = factory.createGeometryCollection(geometries);
            geoms = null;
        }

        return true;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("coordinates".equals(key) && delegate == UNINITIALIZED) {
            /* case of specifying coordinates before the actual geometry type. create a proxy
             * handler that will simply track calls until the type is actually specified
             */
            proxy = new RecordingHandler();
            delegate = proxy;
            return super.startObjectEntry(key);
        } else if ("type".equals(key) && delegate == proxy) {
            delegate = UNINITIALIZED;
        } else if ("geometries".equals(key)) {
            geoms = new ArrayList();
        } else if (geoms != null) {
            super.startObjectEntry(key);
        }

        return true;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        if (delegateClass != null) {
            delegate = createDelegate(delegateClass, new Object[] {factory});
            delegateClass = null;
        }
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        /* handle special case of "type" belonging to one of the collection's geometries
         * being found after "coordinates" for that geometry.
         */
        if (geoms != null && value instanceof String && delegate == UNINITIALIZED) {
            delegateClass = lookupDelegate(value.toString());
            if (proxy != null) {
                delegate = createDelegate(delegateClass, new Object[] {factory});
                delegateClass = null;
                proxy.replay(delegate);
                proxy = null;
            }
        } else {
            return super.primitive(value);
        }

        return true;
    }

    @Override
    public GeometryCollection getValue() {
        return value;
    }
}
