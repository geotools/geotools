/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.coverage.io.catalog;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.gce.imagemosaic.Utils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

public class CoverageSlicesCatalogTest {

    @Test
    public void testExplicityTypeNames() throws Exception {
        testTypeNames("one,two", "one", "two");
    }

    @Test
    public void testExplicityTypeName() throws Exception {
        testTypeNames("one", "one");
    }

    private static void testTypeNames(String typeNames, String... expectedNames)
            throws IOException {
        // make sure getTypeNames is never called if we provide a list of values
        DataStore mockStore = Mockito.mock(DataStore.class);
        DefaultRepository repository = new DefaultRepository();
        repository.register("testStore", mockStore);

        Map<String, Serializable> params = Map.of(Utils.Prop.TYPENAME, typeNames);
        DataStoreConfiguration config = new DataStoreConfiguration("testStore");
        config.setParams(params);
        CoverageSlicesCatalog catalog = new CoverageSlicesCatalog(config, repository);

        Mockito.verify(mockStore, Mockito.never()).getTypeNames();
        assertThat(
                Arrays.asList(catalog.getTypeNames()), Matchers.containsInAnyOrder(expectedNames));
    }

    @Test
    public void testNoTypeNames() throws Exception {
        // make sure getTypeNames is never called if we provide a list of values
        DataStore mockStore = Mockito.mock(DataStore.class);
        Mockito.when(mockStore.getTypeNames()).thenReturn(new String[] {"a", "b"});
        DefaultRepository repository = new DefaultRepository();
        repository.register("testStore", mockStore);

        DataStoreConfiguration config = new DataStoreConfiguration("testStore");
        CoverageSlicesCatalog catalog = new CoverageSlicesCatalog(config, repository);

        assertThat(Arrays.asList(catalog.getTypeNames()), Matchers.containsInAnyOrder("a", "b"));
    }
}
