/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.junit.Test;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Dean Povey
 */
public class JDBCDataStoreTest {

    // Subtype of the requested mapping type.  This is used in the mapping initialisation test
    // as it triggers the code path that previously led to a ConcurrentModificationException.
    public static class DateSubClass extends Timestamp {

        public DateSubClass(long time) {
            super(time);
        }
    }

    @Test
    public void testMappingInitialisationIsThreadSafe() throws InterruptedException, ExecutionException {
        // This test attempts to exercise a race condition on initialisation of mappings.  It is caused when
        // two threads simultaneously try to access mapping of SQL types for a datastore.  This
        // previously led to a ConcurrentModificationException. (c.f. GEOT-4506)
        int nThreads = Math.min(Runtime.getRuntime().availableProcessors(), 2);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < 100; i++) {
            final CountDownLatch latch = new CountDownLatch(nThreads);
            final JDBCDataStore jdbcDataStore = new JDBCDataStore();
            SQLDialect sqlDialect = new BasicSQLDialect(jdbcDataStore) {
                @Override
                public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql) throws IOException {
                }

                @Override
                public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
                }

                @Override
                public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx) throws SQLException, IOException {
                    return null;
                }

                @Override
                public Geometry decodeGeometryValue(GeometryDescriptor descriptor, ResultSet rs, String column, GeometryFactory factory, Connection cx) throws IOException, SQLException {
                    return null;
                }
            };
            jdbcDataStore.setSQLDialect(sqlDialect);
            List<Future> futures = new ArrayList<>();
            for (int j = 0; j < nThreads; j++) {
                Future f = executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Get all the threads to the same point to increase the likelihood
                            // of finding the issue
                            latch.countDown();
                            latch.await();
                        } catch (InterruptedException e) {
                            Thread.interrupted();
                            return;
                        }
                        jdbcDataStore.getMapping(DateSubClass.class);
                    }
                });
                futures.add(f);
            }
            for (Future future : futures) {
                future.get();
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test(expected=IOException.class)
    public void testCheckAllInsertedPositive() throws IOException {
        JDBCDataStore.checkAllInserted(new int[0], 0);
        JDBCDataStore.checkAllInserted(new int[] {1,1,1}, 3);
        JDBCDataStore.checkAllInserted(new int[] {3}, 3);
        JDBCDataStore.checkAllInserted(new int[] {1, 1, 0}, 3);
    }
}

