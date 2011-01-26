package org.geotools.arcsde.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.SdeRow;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

public class ArcSDEClobTest {
    private static ClobTestData testData;
    
    private String[] columnNames = { "IntegerField", "ClobField" };

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new ClobTestData();
        testData.setUp();
        final boolean insertTestData = true;
        testData.createTempTable(insertTestData);
    }

    @AfterClass
    public static void finalTearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    /**
     * loads {@code test-data/testparams.properties} into a Properties object, wich is used to
     * obtain test tables names and is used as parameter to find the DataStore
     */
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRead() throws Exception {
        ISession session = null;
        try {
            ArcSDEDataStore dstore = testData.getDataStore();
            session = dstore.getSession(Transaction.AUTO_COMMIT);
            // TODO: This is crap. If data can't be loaded, add another config for the existing
            // table
            String typeName = testData.getTempTableName(session);
            SimpleFeatureType ftype = dstore.getSchema(typeName);
            // The row id column is not returned, but the geometry column is (x+1-1=x)
            assertEquals("Verify attribute count.", columnNames.length, ftype.getAttributeCount());
            ArcSDEQuery query = ArcSDEQuery.createQuery(session, ftype, Query.ALL,
                    FIDReader.NULL_READER, ArcSdeVersionHandler.NONVERSIONED_HANDLER);
            query.execute();
            SdeRow row = query.fetch();
            assertNotNull("Verify first result is returned.", row);
            Object longString = row.getObject(0);
            assertNotNull("Verify the non-nullity of first CLOB.", longString);
            assertEquals("Verify stringiness.", longString.getClass(), String.class);
            row = query.fetch();
            longString = row.getObject(0);
            assertNotNull("Verify the non-nullity of second CLOB.", longString);
            query.close();
        } finally {
            if (session != null) {
                session.dispose();
            }
        }
    }

}
