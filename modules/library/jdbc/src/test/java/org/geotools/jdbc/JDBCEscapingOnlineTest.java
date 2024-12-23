package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.junit.Test;

public abstract class JDBCEscapingOnlineTest extends JDBCTestSupport {

    protected SimpleFeatureType escapingSchema;
    protected static final String ESCAPING = "esca\"ping";
    protected static final String ID = "i\"d";
    protected static final String NAME = "na\"me";

    @Override
    protected abstract JDBCEscapingTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();
        escapingSchema =
                DataUtilities.createType(dataStore.getNamespaceURI() + "." + ESCAPING, ID + ":0," + NAME + ":String");
    }

    @Test
    public void testEscaping() throws Exception {
        dataStore.createSchema(escapingSchema);
        assertFeatureTypesEqual(escapingSchema, dataStore.getSchema(tname(ESCAPING)));

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> fw =
                dataStore.getFeatureWriterAppend(tname(ESCAPING), Transaction.AUTO_COMMIT)) {
            SimpleFeature f = fw.next();
            f.setAttribute(aname(ID), 1);
            f.setAttribute(aname(NAME), "abc");
            fw.write();
        }

        ContentFeatureCollection fc =
                dataStore.getFeatureSource(tname(ESCAPING)).getFeatures();
        assertEquals(1, fc.size());
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            fr.next();
            assertFalse(fr.hasNext());
        }
    }
}
