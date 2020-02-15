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
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public abstract class JDBCConnectionLifecycleOnlineTest extends JDBCTestSupport {

    protected MockListener mockListener = new MockListener();

    JDBCFeatureStore featureStore;

    protected void connect() throws Exception {
        super.connect();
        featureStore = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
    }

    /** Check null encoding is working properly */
    public void testListenerCalled() throws IOException {
        dataStore.getConnectionLifecycleListeners().add(mockListener);

        // read some features, this will force unwrapping in Oracle
        try (SimpleFeatureIterator fi = featureStore.getFeatures().features()) {
            while (fi.hasNext()) {
                fi.next();
            }
        }
        assertTrue(mockListener.onBorrowCalled);
        assertTrue(mockListener.onReleaseCalled);
        assertFalse(mockListener.onCommitCalled);
        assertFalse(mockListener.onRollbackCalled);

        // now write something within a transaction
        try (Transaction t = new DefaultTransaction()) {
            SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureStore.getSchema());
            DefaultFeatureCollection collection =
                    new DefaultFeatureCollection(null, featureStore.getSchema());
            featureStore.setTransaction(t);
            for (int i = 3; i < 6; i++) {
                b.set(aname("intProperty"), Integer.valueOf(i));
                b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(i, i)));
                collection.add(b.buildFeature(null));
            }
            featureStore.addFeatures((SimpleFeatureCollection) collection);
            t.commit();
            assertTrue(mockListener.onBorrowCalled);
            assertTrue(mockListener.onReleaseCalled);
            assertTrue(mockListener.onCommitCalled);
            assertFalse(mockListener.onRollbackCalled);

            // and now do a rollback
            t.rollback();
            assertTrue(mockListener.onRollbackCalled);
        }
    }

    public void testConnectionReleased() throws IOException {
        dataStore.getConnectionLifecycleListeners().add(new ExceptionListener());

        // get a count repeatedly, if we fail to release the connections this will eventually lock
        // up
        for (int i = 0; i < 100; i++) {
            // we don't actually expect an exception to percolate up since it's happening
            // on the closeSafe method, that swallows exceptions
            featureStore.getCount(Query.ALL);
        }
    }

    private static class MockListener implements ConnectionLifecycleListener {

        boolean onBorrowCalled = false;

        boolean onReleaseCalled = false;

        boolean onCommitCalled;

        boolean onRollbackCalled;

        public void onBorrow(JDBCDataStore store, Connection cx) throws SQLException {
            onBorrowCalled = true;
        }

        public void onRelease(JDBCDataStore store, Connection cx) throws SQLException {
            onReleaseCalled = true;
        }

        public void onCommit(JDBCDataStore store, Connection cx) throws SQLException {
            onCommitCalled = true;
        }

        public void onRollback(JDBCDataStore store, Connection cx) throws SQLException {
            onRollbackCalled = true;
        }
    }

    private static class ExceptionListener implements ConnectionLifecycleListener {

        public void onBorrow(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }

        public void onRelease(JDBCDataStore store, Connection cx) throws SQLException {
            throw new SQLException("Ha, are you relasing the connection anyways??");
        }

        public void onCommit(JDBCDataStore store, Connection cx) throws SQLException {
            throw new SQLException("Nope, no writes sir");
        }

        public void onRollback(JDBCDataStore store, Connection cx) throws SQLException {
            // nothing to do
        }
    }
}
