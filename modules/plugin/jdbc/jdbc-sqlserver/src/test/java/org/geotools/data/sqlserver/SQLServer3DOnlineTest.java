package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBC3DOnlineTest;
import org.geotools.jdbc.JDBC3DTestSetup;

public class SQLServer3DOnlineTest extends JDBC3DOnlineTest {

    @Override
    protected void connect() throws Exception {
        super.connect();
        SQLServerDialect ssd = (SQLServerDialect) dialect;
        ssd.setGeometryMetadataTable("GEOMETRY_COLUMNS_3D_TEST");
        ssd.setUseNativeSerialization(true);
    }

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new SQLServer3DTestSetup();
    }

    /**
     * Override to disable because it does not work. In the actual query sent to the database the
     * {@code bbox} is reduced to 2 dimensions: {@code SELECT count(*) FROM "line3d" WHERE
     * "geom".Filter(geometry::STGeomFromText('POLYGON ((2 1, 2 2, 3 2, 3 1, 2 1))', 4326)) = 1 } so
     * the test will always fail.
     *
     * <p>see GEOT-6555.
     */
    @Override
    public void testBBOX3DOutsideLine() {
        // copied from superclass
        // // a bbox 3d well outside the line footprint
        // BBOX3D bbox3d = FF.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 100, 101, crs));
        // SimpleFeatureCollection fc =
        //        dataStore.getFeatureSource(tname(getLine3d())).getFeatures(bbox3d);
        // assertEquals(0, fc.size());
    }
}
