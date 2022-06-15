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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Allows reuse of JDBCTestSetup for a different set of tests.
 *
 * <p>For example see JDBC3DTestSetup which provides a different test dataset, while still using the
 * provided delegate to access a test fixture and establish a connection.
 *
 * @see JDBC3DTestSetup
 */
@SuppressWarnings({
    "PMD.JUnit4TestShouldUseAfterAnnotation",
    "PMD.JUnit4TestShouldUseBeforeAnnotation"
})
public class JDBCDelegatingTestSetup extends JDBCTestSetup {

    protected JDBCTestSetup delegate;

    protected JDBCDelegatingTestSetup(JDBCTestSetup delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean canResetSchema() {
        return delegate.canResetSchema();
    }

    @Override
    public void setFixture(Properties fixture) {
        super.setFixture(fixture);
        delegate.setFixture(fixture);
    }

    @Override
    public DataSource getDataSource() throws IOException {
        return delegate.useDelegateDataSource() ? delegate.getDataSource() : super.getDataSource();
    }

    @Override
    protected Properties createOfflineFixture() {
        return delegate.createOfflineFixture();
    }

    @Override
    protected Properties createExampleFixture() {
        return delegate.createExampleFixture();
    }

    @Override
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

    @Override
    protected final void initializeDatabase() throws Exception {
        delegate.initializeDatabase();
    }

    @Override
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
