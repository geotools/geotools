package org.geotools.jdbc;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public abstract class JDBCEscapingOnlineTest extends JDBCTestSupport {

    protected SimpleFeatureType escapingSchema;
    protected static final String ESCAPING = "esca\"ping";
    protected static final String ID = "i\"d";
    protected static final String NAME = "na\"me";

    protected abstract JDBCEscapingTestSetup createTestSetup();

    @Override
    protected void connect() throws Exception {
        super.connect();
        escapingSchema =
                DataUtilities.createType(
                        dataStore.getNamespaceURI() + "." + ESCAPING,
                        ID + ":0," + NAME + ":String");
    }

    public void testEscaping() throws Exception {
        dataStore.createSchema(escapingSchema);
        assertFeatureTypesEqual(escapingSchema, dataStore.getSchema(tname(ESCAPING)));

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> fw =
                dataStore.getFeatureWriterAppend(tname(ESCAPING), Transaction.AUTO_COMMIT)) {
            SimpleFeature f = (SimpleFeature) fw.next();
            f.setAttribute(aname(ID), 1);
            f.setAttribute(aname(NAME), "abc");
            fw.write();
        }

        ContentFeatureCollection fc = dataStore.getFeatureSource(tname(ESCAPING)).getFeatures();
        assertEquals(1, fc.size());
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            fr.next();
            assertFalse(fr.hasNext());
        }
    }
}
