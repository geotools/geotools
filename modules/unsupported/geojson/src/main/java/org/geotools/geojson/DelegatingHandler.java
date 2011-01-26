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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.geotools.geojson.feature.FeatureCollectionHandler;
import org.geotools.geojson.feature.FeatureHandler;
import org.geotools.geojson.geom.GeometryCollectionHandler;
import org.geotools.geojson.geom.LineHandler;
import org.geotools.geojson.geom.MultiLineHandler;
import org.geotools.geojson.geom.MultiPointHandler;
import org.geotools.geojson.geom.MultiPolygonHandler;
import org.geotools.geojson.geom.PointHandler;
import org.geotools.geojson.geom.PolygonHandler;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

public abstract class DelegatingHandler<T> implements IContentHandler<T> {

    protected static HashMap<String,Class<? extends IContentHandler>> handlers = new HashMap();
    static {
        handlers.put("Point", PointHandler.class);
        handlers.put("LineString", LineHandler.class);
        handlers.put("Polygon", PolygonHandler.class);
        handlers.put("MultiPoint", MultiPointHandler.class);
        handlers.put("MultiLineString", MultiLineHandler.class);
        handlers.put("MultiPolygon", MultiPolygonHandler.class);
        handlers.put("GeometryCollection", GeometryCollectionHandler.class);
        
        handlers.put("Feature", FeatureHandler.class);
        handlers.put("FeatureCollection", FeatureCollectionHandler.class);
    }
    protected static NullHandler NULL = new NullHandler();
    protected static NullHandler UNINITIALIZED = new NullHandler();
    
    protected static List NULL_LIST = Collections.unmodifiableList(new ArrayList(0));
    
    protected ContentHandler delegate = NULL;
    
    public void startJSON() throws ParseException, IOException {
        delegate.startJSON();
    }
    
    public void endJSON() throws ParseException, IOException {
        delegate.endJSON();
    }

    public boolean startObject() throws ParseException, IOException {
        return delegate.startObject();
    }

    public boolean endObject() throws ParseException, IOException {
        return delegate.endObject();
    }

    public boolean startObjectEntry(String key) throws ParseException, IOException {
        return delegate.startObjectEntry(key);
    }

    public boolean endObjectEntry() throws ParseException, IOException {
        return delegate.endObjectEntry();
    }

    public boolean startArray() throws ParseException, IOException {
        return delegate.startArray();
    }

    public boolean endArray() throws ParseException, IOException {
        return delegate.endArray();
    }
    
    public boolean primitive(Object value) throws ParseException, IOException {
        return delegate.primitive(value);
    }
    
    public T getValue() {
        if (delegate instanceof IContentHandler) {
            return (T) ((IContentHandler)delegate).getValue();    
        }
        return null;
    }
    
    protected Class<? extends ContentHandler> lookupDelegate(String type) {
        return handlers.get(type);
    }
    
    protected IContentHandler createDelegate(Class clazz, Object[] args) {
        try {
            if (args != null && args.length > 0) {
                Class[] types = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    types[i] = args[i].getClass();
                }
                
                return (IContentHandler) clazz.getConstructor(types).newInstance(args);
            }
            else {
                return (IContentHandler) clazz.newInstance();
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    static class NullHandler implements ContentHandler {

        public void startJSON() throws ParseException, IOException {
        }

        public void endJSON() throws ParseException, IOException {
        }

        public boolean endArray() throws ParseException, IOException {
            return true;
        }

        public boolean endObject() throws ParseException, IOException {
            return true;
        }

        public boolean endObjectEntry() throws ParseException, IOException {
            return true;
        }

        public boolean startArray() throws ParseException, IOException {
            return true;
        }

        public boolean startObject() throws ParseException, IOException {
            return true;
        }

        public boolean startObjectEntry(String key) throws ParseException, IOException {
            return true;
        }

        public boolean primitive(Object value) throws ParseException, IOException {
            return true;
        }

    }
}
