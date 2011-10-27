package org.geotools.data.aggregate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * 
 *
 * @source $URL$
 */
public class AggregatingDataStoreTest extends AbstractAggregatingStoreTest {

    private static final String ROAD_SEGMENTS = "RoadSegments";

    private static final String BASIC_POLYGONS = "BasicPolygons";

    AggregatingDataStore store = new AggregatingDataStore(repository,
            Executors.newCachedThreadPool());

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @Test
    public void testStoreConfiguration() throws IOException {
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));

        // check we have the expected set of types, in the expected order
        Set<String> types = new LinkedHashSet<String>();
        types.addAll(Arrays.asList(store1.getTypeNames()));
        types.addAll(Arrays.asList(store2.getTypeNames()));
        types.addAll(Arrays.asList(store3.getTypeNames()));
        String[] expected = (String[]) types.toArray(new String[types.size()]);
        assertArrayEquals(expected, store.getTypeNames());

        // grab the BasicPolygon type map and check it looks fine
        AggregateTypeConfiguration config = store.getConfigurations().get(BASIC_POLYGONS);
        assertEquals(BASIC_POLYGONS, config.getName());
        assertEquals(2, config.getSourceTypes().size());
        assertSingleSourceType(BASIC_POLYGONS, new NameImpl("store1"), config);
        assertSingleSourceType(BASIC_POLYGONS, new NameImpl("store2"), config);

        // grab the Streams type map and check it looks fine
        config = store.getConfigurations().get("Streams");
        assertEquals("Streams", config.getName());
        assertEquals(1, config.getSourceTypes().size());
        assertSingleSourceType("Streams", new NameImpl("store2"), config);

        // grab the RoadSegments type map and check it looks fine
        config = store.getConfigurations().get(ROAD_SEGMENTS);
        assertEquals(ROAD_SEGMENTS, config.getName());
        assertEquals(2, config.getSourceTypes().size());
        assertSingleSourceType(ROAD_SEGMENTS, new NameImpl("store1"), config);
        assertSingleSourceType(ROAD_SEGMENTS, new NameImpl("gt", "store3"), config);
    }

    @Test
    public void testFeatureType() throws IOException {
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));
        assertEquals(store1.getSchema(BASIC_POLYGONS), store.getSchema(BASIC_POLYGONS));
        assertEquals(store1.getSchema(ROAD_SEGMENTS), store.getSchema(ROAD_SEGMENTS));

        // store2 has one property more in basic polygons
        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store2", "gt:store3", "store1"));
        assertEquals(store2.getSchema(BASIC_POLYGONS), store.getSchema(BASIC_POLYGONS));

        // store3 has one property less in road segments
        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("gt:store3", "store2", "store1"));
        assertEquals(store3.getSchema(ROAD_SEGMENTS), store.getSchema(ROAD_SEGMENTS));
    }

    @Test
    public void testCount() throws IOException {
        // just the first store
        store.autoConfigureStores(Arrays.asList("store1"));
        assertEquals(3, store.getFeatureSource(BASIC_POLYGONS).getCount(Query.ALL));

        // add all stores
        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));
        assertEquals(4, store.getFeatureSource(BASIC_POLYGONS).getCount(Query.ALL));
    }

    @Test
    public void testBoundAll() throws Exception {
        // just the first store
        store.autoConfigureStores(Arrays.asList("store1"));
        assertEquals(new ReferencedEnvelope(-2, 2, -1, 6, CRS.decode("EPSG:4326")), store
                .getFeatureSource(BASIC_POLYGONS).getBounds(Query.ALL));

        // add all stores
        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));
        assertEquals(new ReferencedEnvelope(-2, 4, -1, 6, CRS.decode("EPSG:4326")), store
                .getFeatureSource(BASIC_POLYGONS).getBounds(Query.ALL));

        // get just one feature, it's only in the first store
        Filter eq1 = ff.equals(ff.property("ID"), ff.literal("two"));
        assertEquals(new ReferencedEnvelope(-2, 1, 3, 6, CRS.decode("EPSG:4326")), store
                .getFeatureSource(BASIC_POLYGONS).getBounds(new Query(null, eq1)));

        // get just one feature, it's only in the second store
        Filter eq2 = ff.equals(ff.property("ID"), ff.literal("four"));
        assertEquals(new ReferencedEnvelope(2, 4, 2, 4, CRS.decode("EPSG:4326")), store
                .getFeatureSource(BASIC_POLYGONS).getBounds(new Query(null, eq2)));
    }
    
    @Test
    public void testBoundMixed() throws Exception {
        AggregateTypeConfiguration config = new AggregateTypeConfiguration(BASIC_POLYGONS);
        config.addSourceType("store1", "BasicPolygons");
        config.addSourceType("store2", "BasicPolygons4269");
        store.addType(config);
        
        assertEquals(new ReferencedEnvelope(-2, 4, -1, 6, CRS.decode("EPSG:4326")), store
                .getFeatureSource(BASIC_POLYGONS).getBounds(Query.ALL));
    }

    @Test
    public void testReadAll() throws Exception {
        store.autoConfigureStores(Arrays.asList("store1"));
        Map<String, SimpleFeature> features = collectFeatures(new Query(BASIC_POLYGONS));
        assertEquals(3, features.size());

        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));
        features = collectFeatures(new Query("BasicPolygons"));
        assertSchema(features.values(), store1.getSchema(BASIC_POLYGONS));
        assertEquals(4, features.size());
    }

    @Test
    public void testReadSorted() throws Exception {
        store.autoConfigureStores(Arrays.asList("store1", "store2"));
        Query q = new Query(BASIC_POLYGONS);
        q.setSortBy(new SortBy[] { ff.sort("ID", SortOrder.DESCENDING) });
        List<SimpleFeature> features = listFeatures(q);
        
        assertEquals(4, features.size());
        String prev = null;
        for (SimpleFeature f : features) {
            String id = (String) f.getAttribute("ID");
            if(prev != null) {
                assertTrue(prev.compareTo(id) >= 0);
            }
            prev = id;
        }
    }

    @Test
    public void testReadInvalidStore() throws Exception {
        store.resetConfiguration();
        AggregateTypeConfiguration config = new AggregateTypeConfiguration(BASIC_POLYGONS);
        config.addSourceType("store1", BASIC_POLYGONS);
        config.addSourceType("store3", "ABCDE");
        store.addType(config);

        try {
            store.getFeatureSource(BASIC_POLYGONS).getBounds();
            fail("Should have thrown an exception, the store is not tolerant");
        } catch (Exception e) {
            // fine
        }

        try {
            store.getFeatureSource(BASIC_POLYGONS).getCount(new Query(BASIC_POLYGONS));
            fail("Should have thrown an exception, the store is not tolerant");
        } catch (Exception e) {
            // fine
        }

        try {
            collectFeatures(new Query(BASIC_POLYGONS));
            fail("Should have thrown an exception, the store is not tolerant");
        } catch (Exception e) {
            // fine
        }
    }

    void assertSchema(Collection<SimpleFeature> features, SimpleFeatureType schema) {
        for (SimpleFeature sf : features) {
            assertEquals(schema, sf.getFeatureType());
        }
    }

    @Test
    public void testReadFiltered() throws Exception {
        Query q = new Query(BASIC_POLYGONS);
        q.setFilter(ff.greater(ff.function("strLength", ff.property("ID")), ff.literal(3)));

        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1"));
        Map<String, SimpleFeature> features = collectFeatures(q);
        assertSchema(features.values(), store1.getSchema(BASIC_POLYGONS));
        assertEquals(1, features.size());

        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));
        features = collectFeatures(q);
        assertSchema(features.values(), store1.getSchema(BASIC_POLYGONS));
        assertEquals(2, features.size());
    }

    @Test
    public void testReadFilteredManualConfig() throws Exception {
        Query q = new Query(BASIC_POLYGONS);
        q.setFilter(ff.greater(ff.function("strLength", ff.property("ID")), ff.literal(3)));

        store.resetConfiguration();
        AggregateTypeConfiguration config = new AggregateTypeConfiguration(BASIC_POLYGONS);
        config.addSourceType("store1", BASIC_POLYGONS);
        store.addType(config);
        Map<String, SimpleFeature> features = collectFeatures(q);
        assertSchema(features.values(), store1.getSchema(BASIC_POLYGONS));
        assertEquals(1, features.size());

        config.addSourceType("store4", "BasicPolygons2");
        features = collectFeatures(q);
        assertSchema(features.values(), store1.getSchema(BASIC_POLYGONS));
        assertEquals(2, features.size());
    }

    @Test
    public void testReadFilteredManualConfigInverted() throws Exception {
        Query q = new Query(BASIC_POLYGONS);
        q.setFilter(ff.greater(ff.function("strLength", ff.property("ID")), ff.literal(3)));

        store.resetConfiguration();
        AggregateTypeConfiguration config = new AggregateTypeConfiguration(BASIC_POLYGONS);
        config.addSourceType("store4", "BasicPolygons2");
        store.addType(config);
        Map<String, SimpleFeature> features = collectFeatures(q);
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(store4.getSchema("BasicPolygons2"));
        tb.setName(BASIC_POLYGONS);
        SimpleFeatureType expectedType = tb.buildFeatureType();
        assertSchema(features.values(), expectedType);
        assertEquals(1, features.size());

        config.addSourceType("store1", BASIC_POLYGONS);
        features = collectFeatures(q);
        assertSchema(features.values(), expectedType);
        assertEquals(2, features.size());
    }

    @Test
    public void testFilterMissingAttribute() throws Exception {
        Query q = new Query(ROAD_SEGMENTS);
        q.setFilter(ff.equals(ff.property("NAME"), ff.literal("Main Street")));

        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1"));
        Map<String, SimpleFeature> features = collectFeatures(q);
        assertSchema(features.values(), store1.getSchema(ROAD_SEGMENTS));
        assertEquals(1, features.size());

        // now we filter against a store that has the feature type, but misses the property
        store.resetConfiguration();
        store.autoConfigureStores(Arrays.asList("store1", "store2", "gt:store3"));
        features = collectFeatures(q);
        assertSchema(features.values(), store1.getSchema(ROAD_SEGMENTS));
        assertEquals(1, features.size());
    }

    private Map<String, SimpleFeature> collectFeatures(Query query) throws IOException {
        Map<String, SimpleFeature> result = new HashMap<String, SimpleFeature>();
        SimpleFeatureCollection fc = store.getFeatureSource(query.getTypeName()).getFeatures(query);
        SimpleFeatureIterator fi = null;
        try {
            fi = fc.features();
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                result.put(f.getID(), f);
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
        return result;
    }

    private List<SimpleFeature> listFeatures(Query query) throws IOException {
        List<SimpleFeature> result = new ArrayList<SimpleFeature>();
        SimpleFeatureCollection fc = store.getFeatureSource(query.getTypeName()).getFeatures(query);
        SimpleFeatureIterator fi = null;
        try {
            fi = fc.features();
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                result.add(f);
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
        return result;
    }
}
