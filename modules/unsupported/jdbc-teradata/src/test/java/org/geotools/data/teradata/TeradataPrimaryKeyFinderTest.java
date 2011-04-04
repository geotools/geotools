package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class TeradataPrimaryKeyFinderTest extends JDBCPrimaryKeyFinderTest {

    protected JDBCPrimaryKeyFinderTestSetup createTestSetup() {
        return new TeradataPrimaryKeyFinderTestSetup(new TeradataTestSetup());
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
