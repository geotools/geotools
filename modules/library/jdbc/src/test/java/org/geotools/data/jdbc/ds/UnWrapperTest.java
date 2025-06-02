/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.jdbc.ds;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.h2.jdbc.JdbcConnection;
import org.h2.jdbc.JdbcPreparedStatement;
import org.h2.jdbc.JdbcStatement;
import org.junit.Assert;
import org.junit.Test;

public class UnWrapperTest {

    @Test
    public void testDBCPUnwrapper() throws SQLException, IOException {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:test_mem");
        ds.setAccessToUnderlyingConnectionAllowed(true);

        // some weird stuff happening in connection management, the H2 connection
        // has been already closed once the try-resource block finishes, so not using it here,
        // not our job to debug the connection pool nor H2
        Connection conn = ds.getConnection();
        UnWrapper uw = DataSourceFinder.getUnWrapper(conn);
        Assert.assertNotNull(uw);
        Assert.assertTrue(uw.canUnwrap(conn));
        try (Connection unwrapped = uw.unwrap(conn)) {
            Assert.assertNotNull(unwrapped);
            Assert.assertTrue(unwrapped instanceof JdbcConnection);

            try (Statement st = conn.createStatement()) {
                uw = DataSourceFinder.getUnWrapper(st);
                Assert.assertNotNull(uw);
                Assert.assertTrue(uw.canUnwrap(st));

                try (Statement uwst = uw.unwrap(st)) {
                    Assert.assertNotNull(uwst);
                    Assert.assertTrue(uwst instanceof JdbcStatement);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("select curtime()")) {
                uw = DataSourceFinder.getUnWrapper(ps);
                Assert.assertNotNull(uw);
                Assert.assertTrue(uw.canUnwrap(ps));

                try (PreparedStatement uwps = (PreparedStatement) uw.unwrap(ps)) {
                    Assert.assertNotNull(uwps);
                    Assert.assertTrue(uwps instanceof JdbcPreparedStatement);
                }
            }
        }
    }
}
