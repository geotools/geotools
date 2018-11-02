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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccess;
import org.geotools.data.complex.config.Types;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/** @author Fernando Miño, Geosolutions */
public class TestFeatureSource implements Closeable {

    private String schemaBase = "/test-data/index/";
    private String filename = "";
    private String nsUri = "http://www.stations.org/1.0";
    private Name mappedTypeName = Types.typeName(null, "stationsIndexed");

    private AppSchemaDataAccessFactory factory;
    private Map params;
    private DataAccess<FeatureType, Feature> dataStore;
    private MappingFeatureSource mappedSource;

    public TestFeatureSource(
            String schemaBase, String filename, String nsUri, String mappedTypeName) {
        this.schemaBase = schemaBase;
        this.filename = filename;
        this.nsUri = nsUri;
        this.mappedTypeName = Types.typeName(null, mappedTypeName);
        setUp();
    }

    protected void setUp() {
        factory = new AppSchemaDataAccessFactory();
        params = new HashMap();
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
