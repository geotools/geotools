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
package org.geotools.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class JDBCDelegatingTestSetup extends JDBCTestSetup {

    protected JDBCTestSetup delegate;
    
    protected JDBCDelegatingTestSetup( JDBCTestSetup delegate ) {
        this.delegate = delegate;
    }

    @Override
    public void setFixture(Properties fixture) {
        super.setFixture(fixture);
        delegate.setFixture(fixture);
    }
    
    @Override
    protected Properties createOfflineFixture() {
        return delegate.createOfflineFixture();
    }
    
    @Override
    protected Properties createExampleFixture() {
        return delegate.createExampleFixture();
    }
    
    public void setUp() throws Exception {
        // make sure we don't forget to run eventual extra stuff
        delegate.setUp();
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        delegate.tearDown();
    }
    
    @Override
    protected void setUpData() throws Exception {
        delegate.setUpData();
    }
    
    protected final void initializeDatabase() throws Exception {
        delegate.initializeDatabase();
    }

    protected void initializeDataSource(BasicDataSource ds, Properties db) {
        delegate.initializeDataSource(ds, db);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return delegate.createDataStoreFactory();
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        delegate.setUpDataStore(dataStore);
    }

    @Override
    protected String typeName(String raw) {
        return delegate.typeName(raw);
    }
    
    @Override
    protected String attributeName(String raw) {
        return delegate.attributeName(raw);
    }
    
    @Override
    public boolean shouldRunTests(Connection cx) throws SQLException {
        return delegate.shouldRunTests(cx);
    }
}
