/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.util.factory.GeoTools;
import org.mockito.Mockito;

public class JDBCJNDITestSetup extends JDBCDelegatingTestSetup {

    private BasicDataSource dataSource;

    public JDBCJNDITestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected void setupJNDIEnvironment(JDBCDataStoreFactory jdbcDataStoreFactory)
            throws IOException {

        Map params = new HashMap(fixture);
        params.put("passwd", params.get("password"));
        dataSource = jdbcDataStoreFactory.createDataSource(params);
        MockInitialDirContextFactory.setDataSource(dataSource);

        System.setProperty(
                Context.INITIAL_CONTEXT_FACTORY, MockInitialDirContextFactory.class.getName());
        try {
            GeoTools.clearInitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataSource getDataSource() throws IOException {
        System.setProperty(
                Context.INITIAL_CONTEXT_FACTORY, MockInitialDirContextFactory.class.getName());
        return super.getDataSource();
    }

    @Override
    public void tearDown() throws Exception {
        try {
            if (dataSource != null) {
                dataSource.close();
            }
            super.tearDown();
        } finally {
            System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
            GeoTools.clearInitialContext();
        }
    }

    public static class MockInitialDirContextFactory implements InitialContextFactory {

        private Context mockContext = null;
        private static BasicDataSource dataSource;

        public static void setDataSource(BasicDataSource dataSource) {
            MockInitialDirContextFactory.dataSource = dataSource;
        }

        public Context getInitialContext(Hashtable environment) throws NamingException {
            mockContext = (Context) Mockito.mock(Context.class);
            Mockito.when(mockContext.lookup("ds")).thenReturn(dataSource);
            return mockContext;
        }
    }
}
