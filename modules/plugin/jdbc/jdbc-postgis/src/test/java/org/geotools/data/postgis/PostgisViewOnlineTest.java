package org.geotools.data.postgis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.jdbc.JDBCViewOnlineTest;
import org.geotools.jdbc.JDBCViewTestSetup;
import org.geotools.referencing.CRS;
import org.junit.Test;

public class PostgisViewOnlineTest extends JDBCViewOnlineTest {

    public PostgisViewOnlineTest() {
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new PostgisViewTestSetup();
    }

    @Test
    public void testViewSrid() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema("lakes_null_view");
        CoordinateReferenceSystem crs = schema.getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull(crs);
        assertTrue(CRS.equalsIgnoreMetadata(decodeEPSG(4326), crs));
    }
}
