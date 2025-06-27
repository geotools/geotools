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
package org.geotools.data.shapefile.fid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class FidQueryTest extends FIDTestCase {

    private ShapefileDataStore ds;

    private static final FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

    Map<String, SimpleFeature> fids = new HashMap<>();

    SimpleFeatureStore featureStore;

    private int numFeatures;

    @Before
    public void setUpFixQueryTest() throws Exception {
        URL url = backshp.toURI().toURL();
        ds = new ShapefileDataStore(url);
        numFeatures = 0;
        featureStore = (SimpleFeatureStore) ds.getFeatureSource();
        {
            try (SimpleFeatureIterator features = featureStore.getFeatures().features()) {
                while (features.hasNext()) {
                    numFeatures++;
                    SimpleFeature feature = features.next();
                    fids.put(feature.getID(), feature);
                }
            }
            assertEquals(numFeatures, fids.size());
        }
    }

    @After
    public void dispose() throws Exception {
        ds.dispose();
    }

    @Test
    public void testGetByFID() throws Exception {
        assertFidsMatch();
    }

    @Test
    public void testAddFeature() throws Exception {
        SimpleFeature feature = fids.values().iterator().next();
        SimpleFeatureType schema = ds.getSchema();

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(schema);
        GeometryFactory gf = new GeometryFactory();
        build.add(gf.createPoint(new Coordinate(0, 0)));
        build.add(Long.valueOf(0));
        build.add(Long.valueOf(0));
        build.add("Hey");
        SimpleFeature newFeature = build.buildFeature(null);
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.add(newFeature);

        List<FeatureId> newFids = featureStore.addFeatures(collection);
        assertEquals(1, newFids.size());
        // this.assertFidsMatch();

        Query query = new Query(schema.getTypeName());
        FeatureId id = newFids.iterator().next();

        Filter filter = fac.id(Collections.singleton(id));
        query.setFilter(filter);
        try (SimpleFeatureIterator features = featureStore.getFeatures(query).features()) {
            feature = features.next();
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                Object value = feature.getAttribute(i);
                Object newValue = newFeature.getAttribute(i);
                assertEquals(newValue, value);
            }
            assertFalse(features.hasNext());
        }
    }

    @Test
    public void testModifyFeature() throws Exception {
        SimpleFeature feature = this.fids.values().iterator().next();
        int newId = 237594123;

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        Id createFidFilter = ff.id(Collections.singleton(ff.featureId(feature.getID())));

        featureStore.modifyFeatures(new NameImpl("ID"), Integer.valueOf(newId), createFidFilter);

        try (SimpleFeatureIterator features =
                featureStore.getFeatures(createFidFilter).features()) {
            assertNotEquals(feature, features.next());
        }
        feature.setAttribute("ID", Integer.valueOf(newId));
        this.assertFidsMatch();
    }

    @Test
    public void testDeleteFeature() throws Exception {
        SimpleFeature feature = DataUtilities.first(featureStore.getFeatures());
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Id fidFilter = ff.id(Collections.singleton(ff.featureId(feature.getID())));

        featureStore.removeFeatures(fidFilter);
        fids.remove(feature.getID());

        assertEquals(fids.size(), featureStore.getCount(Query.ALL));

        try (SimpleFeatureIterator features =
                featureStore.getFeatures(fidFilter).features()) {
            assertFalse(features.hasNext());
        }

        this.assertFidsMatch();
    }

    /**
     * Attempt to test GEOT-5830 User reports that deleting a feature, re-requesting the data gives a duplicate FID and
     * the subsequent attempt to delete fails due to corrupt FIX
     */
    @Test
    public void testDeleteCloseAndRerequestFID() throws Exception {
        SimpleFeature feature;
        SimpleFeatureCollection allfeatures = featureStore.getFeatures();
        int size = allfeatures.size();
        try (SimpleFeatureIterator features = allfeatures.features()) {
            feature = features.next();
        }
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Id fidFilter = ff.id(Collections.singleton(ff.featureId(feature.getID())));

        featureStore.removeFeatures(fidFilter);
        fids.remove(feature.getID());
        assertEquals(size - 1, featureStore.getCount(Query.ALL));
        assertEquals(fids.size(), featureStore.getCount(Query.ALL));

        featureStore.getDataStore().dispose();
        URL url = backshp.toURI().toURL();
        ds = new ShapefileDataStore(url);
        numFeatures = 0;
        featureStore = (SimpleFeatureStore) ds.getFeatureSource();
        assertEquals(size - 1, featureStore.getCount(Query.ALL));

        SimpleFeatureCollection features2 = featureStore.getFeatures(fidFilter);
        assertEquals("wrong number of features", 0, features2.size());
        try (SimpleFeatureIterator features = features2.features()) {
            assertFalse("found fid", features.hasNext());
        }
        // refresh the features using the new datastore
        allfeatures = featureStore.getFeatures();
        try (SimpleFeatureIterator features = allfeatures.features()) {

            SimpleFeature f = features.next();
            assertFalse(fidFilter.evaluate(f));
        }

        features2 = featureStore.getFeatures(fidFilter);
        assertEquals("wrong number of features", 0, features2.size());
        try (SimpleFeatureIterator features = features2.features()) {
            assertFalse("found fid", features.hasNext());
        }
        this.assertFidsMatch();
    }

    private void assertFidsMatch() throws IOException {
        // long start = System.currentTimeMillis();
        Query query = new Query(featureStore.getSchema().getTypeName());

        int i = 0;

        for (Entry<String, SimpleFeature> stringSimpleFeatureEntry : fids.entrySet()) {
            i++;
            Entry<String, SimpleFeature> entry = stringSimpleFeatureEntry;
            String fid = entry.getKey();
            FeatureId id = fac.featureId(fid);
            Filter filter = fac.id(Collections.singleton(id));
            query.setFilter(filter);
            try (SimpleFeatureIterator features =
                    featureStore.getFeatures(query).features()) {
                assertTrue("Missing feature for fid " + fid, features.hasNext());
                SimpleFeature feature = features.next();
                assertFalse("More than one feature with fid " + fid, features.hasNext());
                assertEquals(i + "th feature", entry.getValue(), feature);
            }
        }
    }
}
