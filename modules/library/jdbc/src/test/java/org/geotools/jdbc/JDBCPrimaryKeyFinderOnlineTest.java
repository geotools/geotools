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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public abstract class JDBCPrimaryKeyFinderOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCPrimaryKeyFinderTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();
        if (setup.canResetSchema()) {
            dataStore.setDatabaseSchema(null);
        }
    }

    @Test
    public void testSequencedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("seqtable"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof SequencedPrimaryKeyColumn);

        ContentFeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
        addFeature(fs.getSchema(), fs);
        assertPrimaryKeyValues(features, 4);
    }

    @Test
    public void testAssignedSinglePKeyView() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("assignedsinglepk"));

        assertEquals(1, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn);

        SimpleFeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
    }

    @Test
    public void testAssignedMultiPKeyView() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("assignedmultipk"));

        assertEquals(2, fs.getPrimaryKey().getColumns().size());
        assertTrue(fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn);
        assertTrue(fs.getPrimaryKey().getColumns().get(1) instanceof NonIncrementingPrimaryKeyColumn);

        // not all databases return data in insertion order, make the test independent of that
        Set<String> actual = new HashSet<>();
        Set<String> expected = new HashSet<>();
        try (FeatureIterator i = fs.getFeatures().features()) {
            for (int j = 1; i.hasNext(); j++) {
                SimpleFeature f = (SimpleFeature) i.next();
                actual.add(f.getID());
                expected.add(tname("assignedmultipk") + "." + j + "." + (j + 1));
            }
        }
        assertEquals(expected, actual);
    }

    protected void addFeature(SimpleFeatureType featureType, JDBCFeatureStore features) throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);
        b.add("four");
        b.add(new GeometryFactory().createPoint(new Coordinate(4, 4)));

        SimpleFeature f = b.buildFeature(null);
        features.addFeatures(DataUtilities.collection(f));

        // pattern match to handle the multi primary key case
        assertTrue(((String) f.getUserData().get("fid")).matches(tname(featureType.getTypeName()) + ".4(\\..*)?"));
    }

    protected void assertPrimaryKeyValues(final SimpleFeatureCollection features, int count) throws Exception {
        assertFeatureIterator(1, count, features.features(), new SimpleFeatureAssertion() {
            @Override
            public int toIndex(SimpleFeature feature) {
                return Integer.parseInt(feature.getIdentifier().getID().split("\\.", 2)[1]);
            }

            @Override
            public void check(int index, SimpleFeature feature) {
                assertEquals(
                        tname(features.getSchema().getName().getLocalPart()) + "." + index,
                        feature.getIdentifier().getID());
            }
        });
    }
}
