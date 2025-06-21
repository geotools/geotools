package org.geotools.data.hana;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.FeatureStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.jdbc.VirtualTable;
import org.junit.Test;

public class HanaVirtualTableHintOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaDataStoreAPITestSetup(new HanaTestSetupPSPooling());
    }

    @Test
    public void testHintIsAppliedToVirtualTables() throws IOException {
        String schema = getFixture().getProperty("schema", "geotools");

        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        HanaTestUtil.encodeIdentifiers(sb, schema, "river");
        VirtualTable vt = new VirtualTable("riverFull", sb.toString());
        dataStore.createVirtualTable(vt);

        SimpleFeatureSource fsView = dataStore.getFeatureSource("riverFull");
        assertFalse(fsView instanceof FeatureStore);

        try {
            fsView.getCount(Query.ALL);
            fail("Expected exception");
        } catch (IOException e) {
            boolean foundMessage = false;
            for (Throwable t = e; t != null && !foundMessage; t = t.getCause()) {
                String msg = e.getMessage();
                foundMessage = msg != null && msg.contains("statement execution blocked by THROW_ERROR hint");
            }
        }
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(HanaDataStoreFactory.SELECT_HINTS.key, "THROW_ERROR");
        return params;
    }
}
