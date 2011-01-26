package org.geotools.data.oracle;

import java.sql.Date;
import java.sql.Timestamp;

import org.geotools.jdbc.JDBCDateTest;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.opengis.feature.simple.SimpleFeatureType;

public class OracleDateTest extends JDBCDateTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new OracleDateTestSetup(new OracleTestSetup());
    }
    
    /*
     * Oracle has no concept of just "Time". Sigh...
     * @see org.geotools.jdbc.JDBCDateTest#testMappings()
     */
    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema( tname("dates") );
        
        assertEquals( Date.class, ft.getDescriptor( aname("d") ).getType().getBinding() );
        assertEquals( Timestamp.class, ft.getDescriptor( aname("dt") ).getType().getBinding() );
        assertEquals( Timestamp.class, ft.getDescriptor( aname("t") ).getType().getBinding() );
    }

    @Override
    public void testFilterByTime() throws Exception {
        // Oracle makes you go through various stages of pain to work simply against Time,
        // not worth supporting it until someone has real time to deal with it
    }
}
