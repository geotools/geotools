package org.geotools.data.sqlserver;

import org.geotools.data.sqlserver.jtds.JTDSSqlServerDataStoreFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCSkipColumnTest;
import org.geotools.jdbc.JDBCSkipColumnTestSetup;

public class SQLServerSkipColumnTest extends JDBCSkipColumnTest {

    @Override
    protected JDBCSkipColumnTestSetup createTestSetup() {
        return new SQLServerSkipColumnTestSetup();
    }

    @Override
    public void testSkippedColumn() throws Exception {
        if(dataStore.getDataStoreFactory() instanceof JTDSSqlServerDataStoreFactory) {
            //I can't find a weird enough column to skip - IJT
            return;
        }
        super.testSkippedColumn();
    }



    @Override
    public void testGetBounds() throws Exception {
        // sql server does not return empty bounds for a single point, but a very smal one instead
        ReferencedEnvelope env = dataStore.getFeatureSource(tname(SKIPCOLUMN)).getBounds();
        assertEquals(0.0, env.getMinX(), 1e-6);
        assertEquals(0.0, env.getMinY(), 1e-6);
        assertEquals(0.0, env.getMaxX(), 1e-6);
        assertEquals(0.0, env.getMaxY(), 1e-6);
    }
}
