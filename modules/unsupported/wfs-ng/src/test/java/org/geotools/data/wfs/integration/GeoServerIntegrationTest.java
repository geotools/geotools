/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.integration;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.junit.Ignore;
import org.junit.Test;


/**
 * works only partially, type names in test data don't match with the expected ones.
 */
public class GeoServerIntegrationTest extends AbstractDataStoreTest {

    protected WFSClient wfs;

    public GeoServerIntegrationTest() {
        super("GeoServerIntegrationTest");
    }

    @Override
    public DataStore createDataStore() throws Exception {

        wfs = mockUpWfsClient();

        WFSContentDataStore wfsds = new WFSContentDataStore(wfs);
        return wfsds;
    }

    private WFSClient mockUpWfsClient() throws Exception {
        WFSConfig config = new WFSConfig();
        String baseDirectory = "GeoServer_2.0/1.1.0/";

        return new IntegrationTestWFSClient(baseDirectory, config);
    }

    @Override
    public DataStore tearDownDataStore(DataStore data) throws Exception {
        data.dispose();
        return data;
    }

    @Override
    protected String getNameAttribute() {
        return "label";
    }

    @Override
    protected String getRoadTypeName() {
        return "sf_roads";
    }

    @Override
    protected String getRiverTypeName() {
        return "topp_states";
    }
    
    @Override
    @Ignore
    @Test
    public void testFeatureEvents() throws Exception {
        // temporarily disabled until events issue solved
    }
    
    @Override
    @Ignore
    @Test
    public void testCreateSchema() throws Exception {
        // not supported
    }
    
    @Ignore
    @Override
    @Test
    public void testGetSchema() {
        // data not matching
    }
    
    @Ignore
    @Override
    @Test
    public void testGetFeatureSourceRiver() throws Exception {
        //data not matching
    }
    
    @Ignore
    @Override
    @Test
    public void testGetFeatureStoreTransactionSupport() throws Exception {
      //data not matching
    }
    
    @Test
    @Ignore
    @Override
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
      //data not matching
    }

    @Test
    @Ignore
    @Override
    public void testGetFeatureStoreModifyFeatures2() throws IOException {
      //data not matching
    }    

}
