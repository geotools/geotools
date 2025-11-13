/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.geotools.api.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Point;

public class GeoPkgDataStoreFactoryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    private static final String dbName = "test.gpkg";
    private static final String dbName2 = "test2.gpkg";

    @Test
    public void testBaseDirectory() throws IOException {
        createGeoPackage(dbName, null, "noo");
        assertTrue(new File(tmp.getRoot(), dbName).exists());
    }

    @Test
    public void testConnectTimeout() throws Exception {
        AtomicBoolean failed = new AtomicBoolean(false);
        AtomicLong time = new AtomicLong(0);
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread1 = new Thread(() -> {
            try {
                createGeoPackage(dbName2, null, "foo");
                Connection connection = DriverManager.getConnection("jdbc:sqlite:file:"
                        + tmp.getRoot().toPath().resolve(dbName2).toAbsolutePath());
                Statement statement = connection.createStatement();
                statement.executeUpdate("PRAGMA locking_mode = EXCLUSIVE");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO foo (name) VALUES (?)");
                for (int i = 0; i < 100; i++) {
                    preparedStatement.setString(1, String.valueOf(i));
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException | IOException e) {
                failed.set(true);
            } finally {
                // Release the latch to allow Thread 2 to proceed
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            long start = System.currentTimeMillis();
            try {
                // Wait for Thread 1 to acquire the lock and start the transaction
                latch.await();
                // set the timeout to 1 ms in order to trigger the timeout exception
                // as soon as possible
                createGeoPackage(dbName2, 1, "moo");
                // Thread 2: Test should have timed out due to busy database.
                failed.set(true);
            } catch (InterruptedException | IOException e) {
                failed.set(true);
            } catch (RuntimeException re) {
                // this is expected because the database is locked
                long end = System.currentTimeMillis();
                time.set(end - start);
            }
        });

        thread1.start();
        thread2.start();
        // Wait for both threads to finish
        thread1.join();
        thread2.join();
        assertFalse(failed.get());
        assertTrue(time.get() <= 10000);
    }

    private void createGeoPackage(String geoPackageName, Integer connectTimeout, String tableName) throws IOException {
        Map<String, Serializable> map = new HashMap<>();
        map.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
        map.put(GeoPkgDataStoreFactory.DATABASE.key, geoPackageName);
        if (connectTimeout != null) {
            GeoPkgDataStoreFactory.setSqlLiteConnectTimeout(connectTimeout);
        }
        GeoPkgDataStoreFactory factory = new GeoPkgDataStoreFactory();
        factory.setBaseDirectory(tmp.getRoot());

        // create some data to trigger file creation
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName(tableName);
        b.setNamespaceURI("http://geotools.org");
        b.setSRS("EPSG:4326");
        b.add("geom", Point.class);
        b.add("name", String.class);

        DataStore data = factory.createDataStore(map);
        data.createSchema(b.buildFeatureType());
        data.dispose();
    }
}
