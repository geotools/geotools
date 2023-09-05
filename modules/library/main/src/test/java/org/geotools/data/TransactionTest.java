/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class TransactionTest {
    MemoryDataStore ds;
    SimpleFeatureType type;
    Geometry geom;

    @Before
    public void setUp() throws Exception {
        type = DataUtilities.createType("default", "name:String,*geom:Geometry");
        GeometryFactory fac = new GeometryFactory();
        geom = fac.createPoint(new Coordinate(10, 10));
        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[] {"original", geom}, null);
        ds = new MemoryDataStore(new SimpleFeature[] {f1});
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testAddFeature() throws Exception {
        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[] {"one", geom}, null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(type, new Object[] {"two", geom}, null);

        SimpleFeatureStore store = (SimpleFeatureStore) ds.getFeatureSource("default");
        store.setTransaction(new DefaultTransaction());
        store.addFeatures(DataUtilities.collection(f1));
        store.addFeatures(DataUtilities.collection(f2));

        count(store, 3);
        //        assertEquals("Number of known feature as obtained from getCount",3,
        // store.getCount(Query.ALL));
    }

    @Test
    public void testRemoveFeature() throws Exception {
        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[] {"one", geom}, null);

        SimpleFeatureStore store = (SimpleFeatureStore) ds.getFeatureSource("default");
        store.setTransaction(new DefaultTransaction());
        List<FeatureId> fid = store.addFeatures(DataUtilities.collection(f1));

        count(store, 2);
        FeatureId identifier = fid.iterator().next();
        String next = identifier.getID();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        Filter f = filterFactory.id(Collections.singleton(filterFactory.featureId(next)));
        store.removeFeatures(f);

        count(store, 1);
        //        assertEquals("Number of known feature as obtained from getCount",3,
        // store.getCount(Query.ALL));
    }

    private void count(SimpleFeatureStore store, int expected)
            throws IOException, IllegalAttributeException {
        int i = 0;
        try (SimpleFeatureIterator reader = store.getFeatures().features()) {
            while (reader.hasNext()) {
                reader.next();
                i++;
            }
        }
        Assert.assertEquals("Number of known feature as obtained from reader", expected, i);
    }
}
