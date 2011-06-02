/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb;

import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.geom.GeometryHandler;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Handle CouchDB results.
 * @author Ian Schneider (OpenGeo)
 */
class CouchDBFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private final SimpleFeatureType type;
    private final JSONArray features;
    private final SimpleFeatureBuilder builder;
    private final GeometryHandler handler;
    private int index;

    public CouchDBFeatureReader(SimpleFeatureType type, JSONArray features) {
        this.type = type;
        this.features = features;
        this.builder = new SimpleFeatureBuilder(type);
        handler = new GeometryHandler(new GeometryFactory());
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return type;
    }

    @Override
    public boolean hasNext() throws IOException {
        return index < features.size();
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
    
    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        JSONObject row = (JSONObject) features.get(index++);
        JSONObject feature = (JSONObject) row.get("value");
        JSONObject props = (JSONObject) feature.get("properties");
        JSONObject geom = (JSONObject) feature.get("geometry");
        for (Object k : props.keySet()) {
            builder.set(k.toString(), props.get(k));
        }
        try {
            builder.set("geometry", read(geom));
        } catch (Exception ex) {
            throw new IOException("Error handling geometry",ex);
        }
        return builder.buildFeature(row.get("id").toString());
    }

    private Object read(JSONObject geom) throws Exception {
        handler.startJSON();
        visit(geom,handler);
        handler.endJSON();
        return handler.getValue();
    }

    private void visit(Object obj, GeometryHandler handler) throws Exception {
        if (obj instanceof JSONObject) {
            visit((JSONObject) obj, handler);
        } else if (obj instanceof JSONArray) {
            visit((JSONArray) obj, handler);
        } else {
            handler.primitive(obj);
        }
    }

    private void visit(JSONArray arr, GeometryHandler handler) throws Exception {
        handler.startArray();
        for (int i = 0; i < arr.size(); i++) {
            visit(arr.get(i),handler);
        }
        handler.endArray();
    }
    
    private void visit(JSONObject obj, GeometryHandler handler) throws Exception {
        handler.startObject();
        for (Object k : obj.keySet()) {
            handler.startObjectEntry(k.toString());
            visit(obj.get(k),handler);
        }
        handler.endObject();
    }
}
