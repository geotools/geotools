/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.feature.NameImpl;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;

/**
 * @author tkunicki@boundlessgeo.com
 * @param <S> MongoSchemaStore superclass
 */
public abstract class MongoSchemaStoreTest<S extends MongoSchemaStore> {

    abstract S createUniqueStore() throws IOException;

    abstract void destroyUniqueStore(S store);

    @Test
    public void test_storeCRUD() throws FactoryException, IOException {
        S store = createUniqueStore();

        try {
            SimpleFeatureType dummy0 = FeatureTypeDBObjectTest.buildDummyFeatureType("dummy0");
            SimpleFeatureType dummy1 = FeatureTypeDBObjectTest.buildDummyFeatureType("dummy1");
            dummy1.getUserData().put("dummyKey1", "this makes be different");
            SimpleFeatureType dummy2 = FeatureTypeDBObjectTest.buildDummyFeatureType("dummy2");
            dummy1.getUserData().put("dummyKey2", "i want to be different too");

            // show we have an empty store
            assertThat(store.retrieveSchema(new NameImpl("dummy0")), is(nullValue()));
            assertThat(store.retrieveSchema(new NameImpl("dummy1")), is(nullValue()));
            assertThat(store.retrieveSchema(new NameImpl("dummy2")), is(nullValue()));

            // bad inputs
            store.storeSchema(null); // no exception expected
            assertThat(store.retrieveSchema(null), is(nullValue()));
            store.deleteSchema(null); // no exception expected

            // store, retreive then test for equality
            List<String> typeNames;
            store.storeSchema(dummy0);
            typeNames = store.typeNames();
            assertThat(typeNames, is(equalTo(Arrays.asList("dummy0"))));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy0")), dummy0, false);

            // store a second, retreive then test all for equality
            store.storeSchema(dummy1);
            Collections.sort(typeNames = store.typeNames()); // FIFO isn't in API contract
            assertThat(typeNames, is(equalTo(Arrays.asList("dummy0", "dummy1"))));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy0")), dummy0, false);
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy1")), dummy1, false);

            // replace the second with itself, retreive then test all for equality
            store.storeSchema(dummy1);
            Collections.sort(typeNames = store.typeNames()); // FIFO isn't in API contract
            assertThat(typeNames, is(equalTo(Arrays.asList("dummy0", "dummy1"))));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy0")), dummy0, false);
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy1")), dummy1, false);

            // store a third, retreive then test all for equality
            store.storeSchema(dummy2);
            Collections.sort(typeNames = store.typeNames()); // FIFO isn't in API contract
            assertThat(typeNames, is(equalTo(Arrays.asList("dummy0", "dummy1", "dummy2"))));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy0")), dummy0, false);
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy1")), dummy1, false);
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy2")), dummy2, false);

            // remove the second, retreive then test all for equality
            store.deleteSchema(new NameImpl("dummy1"));
            Collections.sort(typeNames = store.typeNames()); // FIFO isn't in API contract
            assertThat(typeNames, is(equalTo(Arrays.asList("dummy0", "dummy2"))));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy0")), dummy0, false);
            assertThat(store.retrieveSchema(new NameImpl("dummy1")), is(nullValue()));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy2")), dummy2, false);

            // show that we can replace a schema with one of same name and it overwrites existing
            SimpleFeatureType dummy0_replace =
                    FeatureTypeDBObjectTest.buildDummyFeatureType("dummy0");
            dummy0_replace
                    .getUserData()
                    .put("dummyKey", "something different than what dummy0 contains");

            // replace dummy0 with a slightly different entry, retreive then test all for equality
            store.storeSchema(dummy0_replace);
            Collections.sort(typeNames = store.typeNames()); // FIFO isn't in API contract
            assertThat(typeNames, is(equalTo(Arrays.asList("dummy0", "dummy2"))));
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy0")), dummy0_replace, false);
            FeatureTypeDBObjectTest.compareFeatureTypes(
                    store.retrieveSchema(new NameImpl("dummy2")), dummy2, false);

            store.deleteSchema(new NameImpl("dummy0"));
            store.deleteSchema(new NameImpl("dummy2"));
            typeNames = store.typeNames();
            assertThat(typeNames, is(equalTo(Collections.EMPTY_LIST)));
            assertThat(store.retrieveSchema(new NameImpl("dummy0")), is(nullValue()));
            assertThat(store.retrieveSchema(new NameImpl("dummy2")), is(nullValue()));

        } finally {
            destroyUniqueStore(store);
        }
    }
}
