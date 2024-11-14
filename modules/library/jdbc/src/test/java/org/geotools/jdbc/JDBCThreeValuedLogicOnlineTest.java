package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Not;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

/**
 * Checks that the filters are converted to two-value logic, unless the {@link
 * JDBCFeatureSource#FILTER_THREE_WAY_LOGIC} hint is enabled
 */
public abstract class JDBCThreeValuedLogicOnlineTest extends JDBCTestSupport {

    protected static final String ABC = "abc";
    protected static final String NAME = "name";
    protected static final String A = "a";
    protected static final String B = "b";
    protected static final String C = "c";

    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Override
    protected abstract JDBCThreeValuedLogicTestSetup createTestSetup();

    @Test
    public void testSimpleNegation() throws Exception {
        Not filter = ff.not(ff.equal(ff.property(aname(A)), ff.literal(10), false));
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(ABC));
        int count = fs.getCount(new Query(tname(ABC), filter));
        assertEquals(2, count);
    }

    /**
     * Allow full 3 way logic, ABC has a row where A is null, won't be matched by <code>not(A = 10)
     * </code> because <code>not(null = 10) -> null</code>
     *
     * @throws Exception
     */
    @Test
    public void testSimpleNegatione3way() throws Exception {
        Not filter = ff.not(ff.equal(ff.property(aname(A)), ff.literal(10), false));
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(ABC));
        Query query = new Query(tname(ABC), filter);
        query.getHints().put(JDBCFeatureSource.FILTER_THREE_WAY_LOGIC, true);
        int count = fs.getCount(query);
        assertEquals(1, count);
    }

    @Test
    public void testBetweenNegation() throws Exception {
        Not filter =
                ff.not(
                        ff.between(
                                ff.property(aname(B)),
                                ff.property(aname(A)),
                                ff.property(aname(C))));
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(ABC));
        Query q = new Query(tname(ABC), filter);
        int count = fs.getCount(q);
        assertEquals(1, count);
        SimpleFeature f = DataUtilities.first(fs.getFeatures(q));
        assertEquals("n_n_n", f.getAttribute(aname(NAME)));
    }

    @Test
    public void testNegateOr() throws Exception {
        // not(a > 3 or b = 5 or c < 0)
        Filter fa = ff.greater(ff.property(aname(A)), ff.literal(3));
        Filter fb = ff.equal(ff.property(aname(B)), ff.literal(5), false);
        Filter fc = ff.less(ff.property(aname(C)), ff.literal(0));
        Not filter = ff.not(ff.and(Arrays.asList(fa, fb, fc)));

        ContentFeatureSource fs = dataStore.getFeatureSource(tname(ABC));
        Query q = new Query(tname(ABC), filter);
        int count = fs.getCount(q);
        assertEquals(2, count);
    }

    @Test
    public void test() throws Exception {
        Not filter =
                ff.not(
                        ff.between(
                                ff.property(aname(B)),
                                ff.property(aname(A)),
                                ff.property(aname(C))));
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(ABC));
        Query q = new Query(tname(ABC), filter);
        int count = fs.getCount(q);
        assertEquals(1, count);
        SimpleFeature f = DataUtilities.first(fs.getFeatures(q));
        assertEquals("n_n_n", f.getAttribute(aname(NAME)));
    }
}
