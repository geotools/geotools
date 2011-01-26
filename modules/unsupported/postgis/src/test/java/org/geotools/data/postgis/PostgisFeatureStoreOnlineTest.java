/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.util.List;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;

public class PostgisFeatureStoreOnlineTest extends AbstractPostgisOnlineTestCase {

    public void testWrite() throws Exception {
        assertEquals(table1+".1",attemptWrite(table1));
        assertEquals(table1+".2",attemptWrite(table1));
        assertEquals(table2+".1001",attemptWrite(table2));
        assertEquals(table2+".1002",attemptWrite(table2));
        assertEquals(table3+".1",attemptWrite(table3));
        assertEquals(table3+".2",attemptWrite(table3));
        assertEquals(table4+".1000001",attemptWrite(table4));
        assertEquals(table4+".1000002",attemptWrite(table4));
        assertEquals(table5+".1",attemptWrite(table5));
        assertEquals(table5+".2",attemptWrite(table5));
        assertEquals(table6+".1001",attemptWrite(table6));
        assertEquals(table6+".1002",attemptWrite(table6));
    }
    
    @SuppressWarnings("unchecked")
    public String attemptWrite(String table) throws Exception {
        Transaction transaction = new DefaultTransaction("attemptWriteFS");
        SimpleFeatureStore fs;
        fs = (SimpleFeatureStore) ds.getFeatureSource(table);
        fs.setTransaction(transaction);
        SimpleFeatureType ft = fs.getSchema();
        SimpleFeatureCollection fc = FeatureCollections.newCollection();
        SimpleFeature feature = SimpleFeatureBuilder.build(ft, new Object[] {"test"}, null);
        fc.add(feature);
        List<FeatureId> set = fs.addFeatures(fc);
        String id = (String) set.toArray()[0];
        transaction.commit();
        
        return id;
    }
}
