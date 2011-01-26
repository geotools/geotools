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

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;

public class UnWrapperTest extends TestCase {

   public void testDBCPUnwrapper() throws SQLException, IOException {
       BasicDataSource ds = new BasicDataSource();
       ds.setDriverClassName("org.h2.Driver");
       ds.setUrl("jdbc:h2:mem:test_mem");
       ds.setAccessToUnderlyingConnectionAllowed(true);
       
       Connection conn = ds.getConnection();
       UnWrapper uw = DataSourceFinder.getUnWrapper(conn);
       assertNotNull(uw);
       assertTrue(uw.canUnwrap(conn));
       Connection unwrapped = uw.unwrap(conn);
       assertNotNull(unwrapped);
       assertTrue(unwrapped instanceof org.h2.jdbc.JdbcConnection);
       
       Statement st = conn.createStatement();
       uw = DataSourceFinder.getUnWrapper(st);
       assertNotNull(uw);
       assertTrue(uw.canUnwrap(st));
       Statement uwst = uw.unwrap(st);
       assertNotNull(uwst);
       assertTrue(uwst instanceof org.h2.jdbc.JdbcStatement);
       st.close();
       
       PreparedStatement ps = conn.prepareStatement("select curtime()");
       uw = DataSourceFinder.getUnWrapper(ps);
       assertNotNull(uw);
       assertTrue(uw.canUnwrap(ps));
       PreparedStatement uwps = (PreparedStatement) uw.unwrap(ps);
       assertNotNull(uwps);
       assertTrue(uwps instanceof org.h2.jdbc.JdbcPreparedStatement);
       ps.close();
       
           
       conn.close();
       ds.close();
   }
}
