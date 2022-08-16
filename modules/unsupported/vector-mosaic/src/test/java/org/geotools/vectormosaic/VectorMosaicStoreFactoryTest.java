/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.junit.Test;

public class VectorMosaicStoreFactoryTest extends VectorMosaicTest {
    @Test
    public void testFactoryCreateStore() throws Exception {
        assertThat(MOSAIC_STORE, Matchers.instanceOf(VectorMosaicStore.class));
    }

    @Test
    public void testFactoryGetParametersInfo() throws Exception {
        assertThat(
                VECTOR_MOSAIC_STORE_FACTORY.getParametersInfo(),
                IsArrayContainingInAnyOrder.arrayContainingInAnyOrder(
                        VectorMosaicStoreFactory.DELEGATE_STORE_NAME,
                        VectorMosaicStoreFactory.NAMESPACE,
                        VectorMosaicStoreFactory.REPOSITORY_PARAM,
                        VectorMosaicStoreFactory.CONNECTION_PARAMETER_KEY,
                        VectorMosaicStoreFactory.PREFERRED_DATASTORE_SPI));
    }

    @Test
    public void testGetDataStoreFromFinder() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put(VectorMosaicStoreFactory.DELEGATE_STORE_NAME.getName(), "delegate");
        params.put(VectorMosaicStoreFactory.NAMESPACE.getName(), "topp");
        params.put(VectorMosaicStoreFactory.REPOSITORY_PARAM.getName(), REPOSITORY);

        DataStore vectorMosaicStore = DataStoreFinder.getDataStore(params);
        assertThat(vectorMosaicStore, Matchers.instanceOf(VectorMosaicStore.class));
    }
}
