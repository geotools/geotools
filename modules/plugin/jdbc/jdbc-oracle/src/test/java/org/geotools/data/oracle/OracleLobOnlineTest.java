package org.geotools.data.oracle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCLobOnlineTest;
import org.geotools.jdbc.JDBCLobTestSetup;
import org.junit.Test;

public class OracleLobOnlineTest extends JDBCLobOnlineTest {

    protected static final String NCLOB_FIELD = "nclob_field";

    @Override
    protected JDBCLobTestSetup createTestSetup() {
        return new OracleLobTestSetup();
    }

    /**
     * override the connect method to add NCLOB field
     *
     * @throws Exception if an error occurs
     */
    @Override
    protected void connect() throws Exception {
        super.connect();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(TESTLOB);
        tb.add(aname(BLOB_FIELD), byte[].class);
        tb.add(aname(CLOB_FIELD), String.class);
        tb.add(aname(RAW_FIELD), byte[].class);
        // override the connect method to add NCLOB field
        tb.add(aname(NCLOB_FIELD), String.class);
        lobSchema = tb.buildFeatureType();
    }

    /**
     * {@inheritDoc} override the testRead method to test for NCLOB field
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Override
    public void testRead() throws Exception {
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc =
                dataStore.getFeatureSource(tname(TESTLOB)).getFeatures();

        try (FeatureIterator<SimpleFeature> fi = fc.features()) {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();

            assertArrayEquals(new byte[] {1, 2, 3, 4, 5}, (byte[]) f.getAttribute(aname(BLOB_FIELD)));
            assertArrayEquals(new byte[] {6, 7, 8, 9, 10}, (byte[]) f.getAttribute(aname(RAW_FIELD)));
            assertEquals("small clob", f.getAttribute(aname(CLOB_FIELD)));

            // override the testRead method to test for NCLOB field
            assertEquals("small nclob", f.getAttribute(aname(NCLOB_FIELD)));
        }
    }

    /**
     * {@inheritDoc} override the testRead method to test for NCLOB field
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Override
    public void testWrite() throws Exception {
        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(tname(TESTLOB));

        SimpleFeature sf = SimpleFeatureBuilder.build(
                lobSchema, new Object[] {new byte[] {6, 7, 8}, "newclob", new byte[] {11, 12, 13}, "newnclob"}, null);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(sf));

        Filter filter = ff.id(new HashSet<>(fids));
        try (FeatureIterator<SimpleFeature> fi = fs.getFeatures(filter).features()) {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertArrayEquals(new byte[] {6, 7, 8}, (byte[]) f.getAttribute(aname(BLOB_FIELD)));
            assertArrayEquals(new byte[] {11, 12, 13}, (byte[]) f.getAttribute(aname(RAW_FIELD)));
            assertEquals("newclob", f.getAttribute(aname(CLOB_FIELD)));

            // override the testWrite method to test for NCLOB field
            assertEquals("newnclob", f.getAttribute(aname(NCLOB_FIELD)));
        }
    }
}
