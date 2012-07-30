package org.geotools.data.wfs.integration;

import org.geotools.data.DataStore;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.junit.Ignore;
import org.junit.Test;

public class GeoServerIntegrationTest extends AbstractDataStoreTest {

    protected WFSClient wfs;

    public GeoServerIntegrationTest() {
        super("GeoServerIntegrationTest");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public DataStore createDataStore() throws Exception {

        wfs = mockUpWfsClient();

        WFSContentDataStore wfsds = new WFSContentDataStore(wfs);
        return wfsds;
    }

    private WFSClient mockUpWfsClient() throws Exception {
        WFSConfig config = new WFSConfig();
        String baseDirectory = "GeoServer_2.2.x/1.1.0/";

        return new IntegrationTestWFSClient(baseDirectory, config);
    }

    @Override
    public DataStore tearDownDataStore(DataStore data) throws Exception {
        data.dispose();
        return data;
    }

    @Override
    protected String getNameAttribute() {
        return "name";
    }

    @Override
    protected String getRoadTypeName() {
        return "topp_road";
    }

    @Override
    protected String getRiverTypeName() {
        return "sf_river";
    }

    @Override
    @Test
    public void testFeatureEvents() throws Exception {
        super.testFeatureEvents();
    }

    @Override
    @Ignore
    @Test
    public void testCreateSchema() throws Exception {
        // not supported
    }
}
