package org.geotools.data.oracle;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.TimeZone;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCFeatureStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

public class OracleDateOnlineTest extends JDBCDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new OracleDateTestSetup(new OracleTestSetup());
    }

    /*
     * Oracle has no concept of just "Time". Sigh...
     * @see org.geotools.jdbc.JDBCDateTest#testMappings()
     */
    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("dates"));

        assertEquals(Date.class, ft.getDescriptor(aname("d")).getType().getBinding());
        assertEquals(Timestamp.class, ft.getDescriptor(aname("dt")).getType().getBinding());
        assertEquals(Timestamp.class, ft.getDescriptor(aname("t")).getType().getBinding());
    }

    @Override
    public void testFilterByTime() throws Exception {
        // Oracle makes you go through various stages of pain to work simply against Time,
        // not worth supporting it until someone has real time to deal with it
    }

    public void testInsertTemporal() throws Exception {
        TimeZone originalTimeZone = TimeZone.getDefault();
        TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
        try {
            TimeZone.setDefault(gmtTimeZone);

            final String timestampsTable = tname("timestamps");
            SimpleFeatureType ft = dataStore.getSchema(timestampsTable);
            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(ft);
            long theTimestamp = 1503926476000L; // August 28, 2017 13:21:16 GMT
            builder.add(new Timestamp(theTimestamp));
            SimpleFeature feature = builder.buildFeature(null);

            Transaction t = new DefaultTransaction("add");
            JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(timestampsTable, t);
            try {
                fs.addFeatures(DataUtilities.collection(feature));
                t.commit();
            } catch (Exception ex) {
                t.rollback();
                throw ex;
            } finally {
                t.close();
            }

            FilterFactory ff = dataStore.getFilterFactory();
            Filter f = ff.equals(ff.property(aname("t")), ff.literal("2017-08-28 13:21:16"));

            JDBCFeatureStore fs2 = (JDBCFeatureStore) dataStore.getFeatureSource(timestampsTable);

            assertEquals(1, fs2.getCount(new Query(timestampsTable, f)));
        } finally {
            TimeZone.setDefault(originalTimeZone);
        }
    }
}
