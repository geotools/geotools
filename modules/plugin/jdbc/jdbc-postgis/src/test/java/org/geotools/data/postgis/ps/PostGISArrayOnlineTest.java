package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostGISArrayTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class PostGISArrayOnlineTest extends org.geotools.data.postgis.PostGISArrayOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISArrayTestSetup(new PostGISPSTestSetup());
    }

    @Override
    public void testGreaterMatchOne() throws Exception {
        super.testGreaterMatchOne();
    }

    @Override
    public void testAnyMatchEquals() throws Exception {
        super.testAnyMatchEquals();
    }

    @Override
    public void testRead() throws Exception {
        super.testRead();
    }
}
