package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *

 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class TeradataViewTest extends JDBCViewTest {


    protected JDBCViewTestSetup createTestSetup() {
        return new TeradataViewTestSetup();
    }
    public void testSchema() throws Exception {
        SimpleFeatureType ft =  dataStore.getSchema(tname(LAKESVIEW));
        assertFeatureTypesEqual(lakeViewSchema, ft);
    }


}
