package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCViewOnlineTest;
import org.geotools.jdbc.JDBCViewTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class PostgisViewOnlineTest extends JDBCViewOnlineTest {

    public PostgisViewOnlineTest() {
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new PostgisViewTestSetup();
    }

    public void testViewSrid() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema("lakes_null_view");
        CoordinateReferenceSystem crs =
                schema.getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertTrue(CRS.equalsIgnoreMetadata(decodeEPSG(4326), crs));
    }
}
