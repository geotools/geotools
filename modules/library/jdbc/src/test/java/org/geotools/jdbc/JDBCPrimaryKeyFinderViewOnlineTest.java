package org.geotools.jdbc;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public abstract class JDBCPrimaryKeyFinderViewOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCPrimaryKeyFinderViewTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();
        if (setup.canResetSchema()) {
            dataStore.setDatabaseSchema(null);
        }
    }

    public void testSequencedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("seqtable"));
        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof SequencedPrimaryKeyColumn);
    }
}
