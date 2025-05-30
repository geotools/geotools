package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

public class PostGISCitextOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISCitextTestSetup();
    }

    @Override
    protected String getFixtureId() {
        return super.getFixtureId() + "citeext";
    }

    @Override
    protected boolean isOnline() throws Exception {
        if (!super.isOnline()) {
            return false;
        }

        // We need to see if we have the citext extension and if we are authorized to
        // create it, to make things easy, we just directly try to do so, if this fails,
        // the test will be skipped
        JDBCTestSetup setup = createTestSetup();
        setup.setFixture(getFixture());

        DataSource dataSource = setup.getDataSource();
        try (Connection cx = dataSource.getConnection();
                Statement st = cx.createStatement()) {
            // check if the extension exists, and creates it in the database if needed
            st.execute("create extension if not exists citext");
            return true;
        } catch (Throwable t) {
            return false;
        } finally {
            try {
                setup.tearDown();
            } catch (Exception e) {
                // System.out.println("Error occurred tearing down the test setup");
            }
        }
    }

    @Test
    public void testSchema() throws IOException {
        SimpleFeatureType schema = dataStore.getSchema(tname("users"));
        assertEquals(2, schema.getAttributeCount());
        AttributeDescriptor ad = schema.getAttributeDescriptors().get(0);
        assertEquals("nick", ad.getLocalName());
        assertEquals(String.class, ad.getType().getBinding());
    }

    @Test
    public void testEquality() throws IOException {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("users"));
        FilterFactory ff = dataStore.getFilterFactory();
        Filter filter = ff.equal(ff.property(aname("nick")), ff.literal("LARRY"), true);
        int count = fs.getCount(new Query(tname("users"), filter));
        // we had a case insensitive comparison due to the type, regardless of what we asked in the
        // filter
        assertEquals(1, count);
    }

    @Test
    public void testLike() throws IOException {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("users"));
        FilterFactory ff = dataStore.getFilterFactory();
        Filter filter = ff.like(ff.property(aname("nick")), "*A*");
        int count = fs.getCount(new Query(tname("users"), filter));
        // we had a case insensitive comparison due to the type, so we get two matches
        assertEquals(2, count);
    }
}
