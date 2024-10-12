/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex;

import static org.junit.Assert.fail;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataAccess;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.complex.feature.type.Types;

/** @author Fernando Miño, Geosolutions */
public class TestFeatureSource implements Closeable {

    private String schemaBase = "/test-data/index/";
    private String filename = "";
    private String nsUri = "http://www.stations.org/1.0";
    private Name mappedTypeName = Types.typeName(null, "stationsIndexed");

    private AppSchemaDataAccessFactory factory;
    private Map<String, Serializable> params;
    private DataAccess<FeatureType, Feature> dataStore;
    private MappingFeatureSource mappedSource;

    public TestFeatureSource(String schemaBase, String filename, String nsUri, String mappedTypeName) {
        this.schemaBase = schemaBase;
        this.filename = filename;
        this.nsUri = nsUri;
        this.mappedTypeName = Types.typeName(null, mappedTypeName);
        initialize();
    }

    protected void initialize() {
        factory = new AppSchemaDataAccessFactory();
        params = new HashMap<>();
        params.put("dbtype", "app-schema");
        URL resource = getClass().getResource(schemaBase + filename);
        if (resource == null) {
            fail("Can't find resouce " + schemaBase + filename);
        }
        params.put("url", resource);
        try {
            dataStore = factory.createDataStore(params);
            mappedSource = (MappingFeatureSource) dataStore.getFeatureSource(mappedTypeName);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        dataStore.dispose();
    }

    public String getSchemaBase() {
        return schemaBase;
    }

    public String getNsUri() {
        return nsUri;
    }

    public DataAccess<FeatureType, Feature> getDataStore() {
        return dataStore;
    }

    public MappingFeatureSource getMappedSource() {
        return mappedSource;
    }
}
