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

import org.geotools.data.couchdb.client.CouchDBClient;
import org.geotools.data.couchdb.client.CouchDBException;
import org.geotools.data.couchdb.client.CouchDBConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.URIException;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.json.simple.JSONObject;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 *
 * @author Ian Schneider (OpenGeo)
 */
public class CouchDBDataStore extends ContentDataStore {

    private String couchURL;
    private String schemaURI;
    private String dbName;
    private CouchDBConnection dbConn;

    public CouchDBConnection getConnection() {
        return dbConn;
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        List<Name> names = new ArrayList<Name>();
        JSONObject design = dbconn().getDesignDocument();

        // for now, expect that db is spatial
        // for each spatial view find a normal view with the same name
        // the spatial view should be setup to return id only
        // the normal view must return geojson
        // @todo delegate to strategy for db layout
        JSONObject spatial = (JSONObject) design.get("spatial"); // safe - internal to geocouch
        JSONObject views = (JSONObject) design.get("views"); // safe - internal to geocouch
        for (Object key : spatial.keySet()) {
            if (views.containsKey(key)) {
                // using the dot separator is ok, since dbnames cannot contain dots
                names.add(new NameImpl(couchURL, dbName + "." + key.toString()));
            }
        }
        return names;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        // grab the schema, it carries a flag telling us if the feature type is read only
        SimpleFeatureType schema = entry.getState(Transaction.AUTO_COMMIT).getFeatureType();
        // @todo ianschneider - I copied this from JDBC, is it needed?
        if (schema == null) {
            // if the schema still haven't been computed, force its computation so
            // that we can decide if the feature type is read only
            schema = new CouchDBFeatureStore(entry, null).buildFeatureType();
            entry.getState(Transaction.AUTO_COMMIT).setFeatureType(schema);
        }

        return new CouchDBFeatureStore(entry, null);
    }

    private CouchDBConnection dbconn() throws IOException {
        if (dbConn == null) {
            try {
                dbConn = client().openDBConnection(dbName);
            } catch (CouchDBException ex) {
                ex.wrap();
            }
        }
        return dbConn;
    }

    private CouchDBClient client() {
        try {
            return new CouchDBClient(couchURL);
        } catch (URIException ex) {
            throw new RuntimeException("Invalid URI for CouchDB", ex);
        }
    }

    public void setCouchURL(String couchURL) {
        this.couchURL = couchURL;
        setNamespaceURI(couchURL);
    }

    public void setSchemaURI(String schemaURI) {
        this.schemaURI = schemaURI;
    }

    public void setDatabaseName(String dbName) {
        this.dbName = dbName;
    }
}
