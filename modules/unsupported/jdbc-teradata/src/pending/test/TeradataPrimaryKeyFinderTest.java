package org.geotools.data.teradata;

import org.geotools.feature.FeatureCollection;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;
import org.geotools.jdbc.SequencedPrimaryKeyColumn;

public class TeradataPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    @Override
    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new TeradataPrimaryKeyFinderTestSetup();
    }

    public void testUniqueGeneratedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("uniquetablenotgenerated"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof SequencedPrimaryKeyColumn);

        FeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
        addFeature(fs.getSchema(), features);
        assertPrimaryKeyValues(features, 4);
    }

    public void testUniqueNonGeneratedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("uniquetable"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof SequencedPrimaryKeyColumn);

        FeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
        addFeature(fs.getSchema(), features);
        assertPrimaryKeyValues(features, 4);
    }
}
