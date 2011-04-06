package org.geotools.data.teradata.ps;

import org.geotools.data.teradata.TeradataPrimaryKeyFinderTestSetup;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTest;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;
import org.geotools.jdbc.NonIncrementingPrimaryKeyColumn;
import org.opengis.feature.simple.SimpleFeature;

public class TeradataPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new TeradataPrimaryKeyFinderTestSetup(new TeradataPSTestSetup());
    }


    @Override
    public void testSequencedPrimaryKey() throws Exception {
      // sequences not a distinct type in teradata (at least as far as I can tell)
    }
    
    @Override
    public void testAssignedMultiPKeyView() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("assignedmultipk"));
        
        assertEquals( 2, fs.getPrimaryKey().getColumns().size() );
        assertTrue( fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn );
        assertTrue( fs.getPrimaryKey().getColumns().get(1) instanceof NonIncrementingPrimaryKeyColumn );
        
        FeatureIterator<?> i = fs.getFeatures().features();

        // On Teradata it's undered !!
        SimpleFeature f = (SimpleFeature) i.next();
        assertEquals( tname("assignedmultipk") + ".3.4" , f.getID() );
        f = (SimpleFeature) i.next();
        assertEquals( tname("assignedmultipk") + ".1.2" , f.getID() );
        f = (SimpleFeature) i.next();
        assertEquals( tname("assignedmultipk") + ".2.3" , f.getID() );
        
        i.close();
    }
}
