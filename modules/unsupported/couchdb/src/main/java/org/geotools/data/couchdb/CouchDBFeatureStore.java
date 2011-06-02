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

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.couchdb.client.CouchDBUtils;
import org.geotools.data.couchdb.client.CouchDBSpatialView;
import org.geotools.data.couchdb.client.CouchDBException;
import org.geotools.data.couchdb.client.CouchDBView;
import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author Ian Schneider (OpenGeo)
 */
public class CouchDBFeatureStore extends ContentFeatureStore {
    private final String viewName;

    public CouchDBFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
        // local name is <dbname>.<view>
        // extract view part
        String localName =  entry.getName().getLocalPart();
        int idx = localName.indexOf('.');
        viewName = localName.substring(idx + 1);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // @todo ianschneider understand query
        
        // there appears to be no way to obtain the bbox from couch documents 
        // (aka features) without getting all the geometry as well.
        // one approach might be to write a view that only returns bbox?
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CouchDBDataStore getDataStore() {
        return (CouchDBDataStore) super.getDataStore();
    }
    
    private CouchDBSpatialView spatialView() {
        return getDataStore().getConnection().spatialView(viewName);
    }
    
    private CouchDBView dataView() {
        return getDataStore().getConnection().view(viewName);
    }
    
    private Envelope getBBox(Query query) {
        Envelope envelope = (Envelope) query.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, null);
        if (envelope == null || envelope.isNull() || Double.isInfinite(envelope.getArea())) {
            envelope = new Envelope(-180, 180, -90, 90);
        }
        return envelope;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // @todo ianschneider understand query
        Envelope e = getBBox(query);
        try {
            return (int) spatialView().count(e.getMinX(),e.getMinY(),e.getMaxX(),e.getMaxY());
        } catch (CouchDBException ex) {
            throw ex.wrap();
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        // @todo ianschneider understand query
        JSONObject features;
        Envelope e = getBBox(query);
        try {
            features = spatialView().get(e.getMinX(),e.getMinY(),e.getMaxX(),e.getMaxY());
        } catch (CouchDBException ex) {
            throw ex.wrap();
        }
        return new CouchDBFeatureReader(buildFeatureType(), (JSONArray) features.get("rows"));
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        // feature types could be
        // 1. created dynamically
        // 2. pointed to statically (schema or json)
        //    a. if design document had schema in it, that'd be conventional
        
        // for now, choice 1
        return createFeatureTypeFromData();
    }
    
    protected SimpleFeatureType createFeatureTypeFromData() throws IOException {
        JSONArray res;
        try {
             res = dataView().get(1);
        } catch (CouchDBException ex) {
            throw ex.wrap();
        }
        if (res.size() == 0) {
            throw new IOException("No features exist in this view");
        }
        JSONObject row = (JSONObject) res.get(0);
        row = (JSONObject) row.get("value");
        return CouchDBUtils.createFeatureType(row, getEntry().getName().toString());
    }
    
    @Override
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(Query query, int flags) throws IOException {
        switch (flags) {
            case 0: throw new IllegalArgumentException( "no write flags set" );
            case WRITER_ADD:
                return new CouchDBAddFeatureWriter(buildFeatureType(),getDataStore());
            case WRITER_UPDATE:
                throw new UnsupportedOperationException("Update not supported");
            default:
                throw new IllegalArgumentException(" cannot handle flags " + flags);
        }
    }

    
}
