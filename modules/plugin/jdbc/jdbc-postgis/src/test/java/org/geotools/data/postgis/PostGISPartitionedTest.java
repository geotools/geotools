/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortOrder;

public class PostGISPartitionedTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISPartitionedTestSetup();
    }

    @Override
    protected void connect() throws Exception {
        super.connect();
        dataStore.setExposePrimaryKeyColumns(true);
    }

    public void testTableExists() throws Exception {
        try (Connection connection = dataStore.getConnection(Transaction.AUTO_COMMIT)) {
            if (!setup.shouldRunTests(connection)) {
                return;
            }
        }
        SimpleFeatureType schema = dataStore.getSchema(tname("customers"));
        assertEquals(String.class, schema.getDescriptor("status").getType().getBinding());
        assertEquals(BigDecimal.class, schema.getDescriptor("arr").getType().getBinding());
    }

    public void testReadAll() throws Exception {
        try (Connection connection = dataStore.getConnection(Transaction.AUTO_COMMIT)) {
            if (!setup.shouldRunTests(connection)) {
                return;
            }
        }
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("customers"));
        FilterFactory ff = dataStore.getFilterFactory();
        Query q = new Query();
        q.setSortBy(ff.sort(aname("status"), SortOrder.ASCENDING));
        List<SimpleFeature> features = DataUtilities.list(fs.getFeatures(q));
        String statusAttribute = aname("status");
        assertEquals("ACTIVE", features.get(0).getAttribute(statusAttribute));
        assertEquals("EXPIRED", features.get(1).getAttribute(statusAttribute));
        assertEquals("REACTIVATED", features.get(2).getAttribute(statusAttribute));
    }

    public void testInsertNew() throws Exception {
        try (Connection connection = dataStore.getConnection(Transaction.AUTO_COMMIT)) {
            if (!setup.shouldRunTests(connection)) {
                return;
            }
        }
        // initial states, contains RECURRING AND REACTIVATED
        SimpleFeatureSource others = dataStore.getFeatureSource(tname("cust_others"));
        assertEquals(2, others.getCount(Query.ALL));

        SimpleFeatureStore customers =
                (SimpleFeatureStore) dataStore.getFeatureSource(tname("customers"));
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        customers.getSchema(), new Object[] {5, "NEWTYPE", 12345}, null);
        List<FeatureId> ids = customers.addFeatures(DataUtilities.collection(feature));
        assertEquals(1, ids.size());

        // now one more in overall table and in the other customers partition
        assertEquals(5, customers.getFeatures().size());
        assertEquals(3, others.getCount(Query.ALL));
    }
}
