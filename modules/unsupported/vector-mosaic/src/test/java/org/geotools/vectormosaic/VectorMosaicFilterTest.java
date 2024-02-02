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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultRepository;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.geotools.data.store.DecoratingDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;

public class VectorMosaicFilterTest {
    protected static DataStore MOSAIC_STORE;

    protected static FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @BeforeClass
    public static void initialize() throws IOException {
        DefaultRepository repository = new DefaultRepository();
        File delegate = new File("src/test/resources/org.geotools.vectormosaic.data2");
        PropertyDataStore delegateStore = new PropertyDataStore(delegate);

        File granules = new File(delegate, "granules");
        DataStore granulesStore =
                new DecoratingDataStore(new PropertyDataStore(granules)) {
                    @Override
                    public void dispose() {
                        fail("Dispose should not be called on granules store");
                    }
                };

        repository.register("delegate", delegateStore);
        repository.register("granulesStore", granulesStore);
        VectorMosaicStoreFactory factory = new VectorMosaicStoreFactory();
        Map<String, Object> params = new HashMap<>();
        params.put(VectorMosaicStoreFactory.DELEGATE_STORE_NAME.getName(), "delegate");
        params.put(VectorMosaicStoreFactory.NAMESPACE.getName(), "topp");
        params.put(VectorMosaicStoreFactory.REPOSITORY_PARAM.getName(), repository);
        params.put(
                VectorMosaicStoreFactory.PREFERRED_DATASTORE_SPI.getName(),
                PropertyDataStoreFactory.class.getName());
        MOSAIC_STORE = factory.createDataStore(params);
    }

    @Test
    public void testTypeNames() throws Exception {
        List<String> typeNames = Arrays.asList(MOSAIC_STORE.getTypeNames());
        // Lakes is not included, not valid
        assertEquals(4, typeNames.size());
        assertThat(
                typeNames,
                hasItems(
                        "RoadSegmentsAll_mosaic",
                        "RoadSegmentsFilter_mosaic",
                        "RoadSegmentsNone_mosaic",
                        "RoadSegmentsRepository_mosaic"));
    }

    @Test
    public void testAll() throws Exception {
        String typeName = "RoadSegmentsAll_mosaic";
        checkRoadSegmentsSchema(typeName);

        Set<Object> fids = collectFids(typeName, Query.ALL);
        assertThat(fids, hasItems(101, 102, 103, 104));
    }

    @Test
    public void testFilter() throws Exception {
        String typeName = "RoadSegmentsFilter_mosaic";
        checkRoadSegmentsSchema(typeName);

        // grab all
        assertThat(collectFids(typeName, Query.ALL), hasItems(101, 103));

        // mix in a user filter
        PropertyIsEqualTo left = FF.equal(FF.property("side"), FF.literal("left"), true);
        Query queryLeft = new Query(typeName, left);
        assertThat(collectFids(typeName, queryLeft), hasItems(101));

        // mix in a user filter
        PropertyIsEqualTo right = FF.equal(FF.property("side"), FF.literal("right"), true);
        Query queryRight = new Query(typeName, right);
        assertThat(collectFids(typeName, queryRight), hasItems(103));
    }

    @Test
    public void testNone() throws Exception {
        String typeName = "RoadSegmentsNone_mosaic";
        checkRoadSegmentsSchema(typeName);

        Set<Object> fids = collectFids(typeName, Query.ALL);
        assertThat(fids, empty());
    }

    @Test
    public void testRepository() throws Exception {
        String typeName = "RoadSegmentsRepository_mosaic";
        checkRoadSegmentsSchema(typeName);

        Set<Object> fids = collectFids(typeName, Query.ALL);
        assertThat(fids, hasItems(101, 102, 103, 104));
    }

    private static Set<Object> collectFids(String typeName, Query query) throws IOException {
        List<SimpleFeature> features =
                DataUtilities.list(MOSAIC_STORE.getFeatureSource(typeName).getFeatures(query));
        Set<Object> fids =
                features.stream().map(f -> f.getAttribute("fid")).collect(Collectors.toSet());
        return fids;
    }

    private static void checkRoadSegmentsSchema(String typeName) throws IOException {
        // check the schema skips both the "type" and "filter" attributes
        SimpleFeatureType schema = MOSAIC_STORE.getSchema(typeName);
        assertArrayEquals(
                new String[] {"geom", "fid", "name", "side"},
                schema.getAttributeDescriptors().stream()
                        .map(d -> d.getLocalName())
                        .toArray(String[]::new));
        assertEquals(LineString.class, schema.getDescriptor("geom").getType().getBinding());
        assertEquals(Integer.class, schema.getDescriptor("fid").getType().getBinding());
        assertEquals(String.class, schema.getDescriptor("side").getType().getBinding());
    }
}
