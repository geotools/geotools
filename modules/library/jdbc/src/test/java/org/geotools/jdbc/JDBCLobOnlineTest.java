package org.geotools.jdbc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;

public abstract class JDBCLobOnlineTest extends JDBCTestSupport {

    protected static final String TESTLOB = "testlob";
    protected static final String ID = "id";
    protected static final String BLOB_FIELD = "blob_field";
    protected static final String CLOB_FIELD = "clob_field";
    protected static final String RAW_FIELD = "raw_field";

    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    protected SimpleFeatureType lobSchema;

    @Override
    protected abstract JDBCLobTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(TESTLOB);
        tb.add(aname(BLOB_FIELD), byte[].class);
        tb.add(aname(CLOB_FIELD), String.class);
        tb.add(aname(RAW_FIELD), byte[].class);
        lobSchema = tb.buildFeatureType();
    }

    @Test
    public void testSchema() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname(TESTLOB));
        assertFeatureTypesEqual(lobSchema, ft);
    }

    @Test
    public void testRead() throws Exception {
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc =
                dataStore.getFeatureSource(tname(TESTLOB)).getFeatures();

        try (FeatureIterator<SimpleFeature> fi = fc.features()) {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();

            assertArrayEquals(
                    new byte[] {1, 2, 3, 4, 5}, (byte[]) f.getAttribute(aname(BLOB_FIELD)));
            assertArrayEquals(
                    new byte[] {6, 7, 8, 9, 10}, (byte[]) f.getAttribute(aname(RAW_FIELD)));
            assertEquals("small clob", f.getAttribute(aname(CLOB_FIELD)));
        }
    }

    @Test
    public void testWrite() throws Exception {
        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(tname(TESTLOB));

        SimpleFeature sf =
                SimpleFeatureBuilder.build(
                        lobSchema,
                        new Object[] {new byte[] {6, 7, 8}, "newclob", new byte[] {11, 12, 13}},
                        null);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(sf));

        Filter filter = ff.id(new HashSet<Identifier>(fids));
        try (FeatureIterator<SimpleFeature> fi = fs.getFeatures(filter).features()) {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertArrayEquals(new byte[] {6, 7, 8}, (byte[]) f.getAttribute(aname(BLOB_FIELD)));
            assertArrayEquals(new byte[] {11, 12, 13}, (byte[]) f.getAttribute(aname(RAW_FIELD)));
            assertEquals("newclob", f.getAttribute(aname(CLOB_FIELD)));
        }
    }

    @Test
    public void testCreateSchema() throws IOException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(lobSchema);
        tb.setName(tname("testLobCreate"));
        SimpleFeatureType ft = tb.buildFeatureType();

        if (Arrays.asList(dataStore.getTypeNames()).contains(ft.getTypeName())) {
            dataStore.removeSchema(ft.getTypeName());
        }
        dataStore.createSchema(ft);
        SimpleFeatureType ft2 = dataStore.getSchema(ft.getTypeName());
        assertFeatureTypesEqual(ft, ft2);
    }
}
