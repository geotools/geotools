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

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


public abstract class JDBCTransactionTest extends JDBCTestSupport {
    public void testCommit() throws IOException {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource("ft1");

        Transaction tx = new DefaultTransaction();

        //tx.putState( fs, new JDBCTransactionState( fs ) );
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend("ft1", tx);
        SimpleFeature feature = writer.next();
        feature.setAttribute("intProperty", new Integer(100));
        writer.write();
        writer.close();
        tx.commit();
        tx.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource("ft1").getFeatures();
        assertEquals(4, fc.size());
    }

    public void testNoCommit() throws IOException {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource("ft1");

        Transaction tx = new DefaultTransaction();

        //tx.putState( fs, new JDBCTransactionState( fs ) );
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend("ft1", tx);
        SimpleFeature feature = writer.next();
        feature.setAttribute("intProperty", new Integer(100));
        writer.write();
        writer.close();
        tx.rollback();
        tx.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource("ft1").getFeatures();
        assertEquals(3, fc.size());
    }

    public void testConcurrentTransactions() throws IOException {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource("ft1");

        Transaction tx1 = new DefaultTransaction();

        //tx1.putState( fs, new JDBCTransactionState( fs ) );
        Transaction tx2 = new DefaultTransaction();

        //tx2.putState( fs, new JDBCTransactionState( fs ) );
        FeatureWriter<SimpleFeatureType, SimpleFeature> w1 = dataStore.getFeatureWriterAppend("ft1", tx1);
        FeatureWriter<SimpleFeatureType, SimpleFeature> w2 = dataStore.getFeatureWriterAppend("ft1", tx2);

        SimpleFeature f1 = w1.next();
        SimpleFeature f2 = w2.next();

        f1.setAttribute("intProperty", new Integer(100));
        f2.setAttribute("intProperty", new Integer(101));

        w1.write();
        w2.write();

        w1.close();
        w2.close();

        tx1.commit();
        tx2.commit();
        tx1.close();
        tx2.close();

        SimpleFeatureCollection fc = dataStore.getFeatureSource("ft1").getFeatures();
        assertEquals(5, fc.size());
    }
    
    public void testSerialTransactions() throws IOException {
        SimpleFeatureStore st = (SimpleFeatureStore) dataStore.getFeatureSource( "ft1" );
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(st.getSchema());
        b.set( "intProperty", new Integer(100));
        SimpleFeature f1 = b.buildFeature(null);
        SimpleFeatureCollection features = new DefaultFeatureCollection(null,null);
        features.add( f1 );

        Transaction tx1 = new DefaultTransaction();
        st.setTransaction(tx1);
        st.addFeatures( features );
        tx1.commit();
        tx1.close();
        assertEquals(4, dataStore.getFeatureSource("ft1").getCount(Query.ALL));
        
        Transaction tx2 = new DefaultTransaction();
        st.setTransaction(tx2);
        st.addFeatures( features );
        tx2.commit();
        tx2.close();
        assertEquals(5, dataStore.getFeatureSource("ft1").getCount(Query.ALL));
    }
}
