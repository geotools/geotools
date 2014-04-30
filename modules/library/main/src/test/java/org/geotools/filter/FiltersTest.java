package org.geotools.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.util.Arrays;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Or;

@SuppressWarnings("deprecation")
/**
 * 
 *
 * @source $URL$
 */
public class FiltersTest {

    private static final double DELTA = 0.0000001;

    private static Filters filters;

    private static FilterFactory2 ff;

    private static Filter a;

    private static Filter b;

    private static Filter c;

    private static Filter d;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory2(null);
        filters = new Filters(ff);
        a = ff.greater(ff.property("zone"), ff.literal(7));
        b = ff.like(ff.property("suburb"), "N%");
        c = ff.equals(ff.property("Subject"), ff.literal("foo"));
        d = ff.equals(ff.property("Subject"), ff.literal("bar"));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ff = null;
        filters = null;
    }

    @Test
    public void testAnd() {
        Filter result = filters.and(ff, a, b);
        assertEquals(ff.and(a, b), result);
    }

    @Test
    public void testOr() {
        Filter result = filters.or(ff, a, b);
        assertEquals(ff.or(a, b), result);
    }

    @Test
    public void testDuplicate() {
        Filter copy = filters.duplicate(a);
        assertNotSame(copy, a);
        assertEquals(copy, a);
    }

    @Test
    public void testGetFilterType() {
        assertEquals(FilterType.COMPARE_GREATER_THAN, Filters.getFilterType(a));
    }

    @Test
    public void testAsInt() {
        assertEquals(1, filters.asInt(ff.literal(1)));
        assertEquals(1, filters.asInt(ff.literal("1")));

        assertEquals(Filters.NOTFOUND, filters.asInt(ff.property("fred")));
    }

    @Test
    public void testAsString() {
        assertEquals("1", Filters.asString(ff.literal("1")));
        assertEquals("1", Filters.asString(ff.literal(1)));
        assertEquals("1.0", Filters.asString(ff.literal(1.0)));
        assertEquals("3.14", Filters.asString(ff.literal(3.14)));
        assertEquals("#000000", Filters.asString(ff.literal(Color.BLACK)));
    }

    @Test
    public void testAsDouble() {
        assertEquals(1.0, Filters.asInt(ff.literal(1)), DELTA);
        assertEquals(1, Filters.asInt(ff.literal("1")), DELTA);
    }

    @Test
    public void testAsType() {
        assertEquals(1.0, Filters.asType(ff.literal("1"), double.class), DELTA);
        assertEquals(Color.BLUE, Filters.asType(ff.literal("#0000FF"), Color.class));

    }

    @Test
    public void testNumber() {
        assertEquals(1.0, Filters.number("1.0"), DELTA);
        assertEquals(1, Filters.number("1"), DELTA);
    }

    @Test
    public void testGets() throws Throwable {
        assertEquals(new Long(1l), Filters.gets("1.0", Long.class));
    }

    @Test
    public void testPutsDouble() {
        assertEquals("1", Filters.puts(1.0));
        assertEquals("3.14", Filters.puts(3.14));
    }

    @Test
    public void testPutsObject() {
        assertEquals("42", Filters.puts(42));
    }

    @Test
    public void testPutsColor() {
        assertEquals("#0000ff", Filters.puts(Color.BLUE));
    }
    
    private int count( Filter filter ){
        if( filter instanceof BinaryLogicOperator ){
            BinaryLogicOperator logic = (BinaryLogicOperator) filter;
            return logic.getChildren() != null ? logic.getChildren().size() : -1;
        }
        return -1;
    }
    
    @Test
    public void testRemoveFilter() {
        Filter results = Filters.removeFilter(null, a);
        assertNull("Start with nothing should end with nothing", results);

        results = Filters.removeFilter(a, null);
        assertSame("Existing should be returned with null target", a, results);

        And base = ff.and(Arrays.asList(new Filter[]{a,b,c}));
        results = Filters.removeFilter(base, d);
        assertEquals("Should not change when target not a child", base, results);

        results = Filters.removeFilter(base, b);
        And expected = ff.and(a, c);
        assertEquals(expected, results);

        //now remove another.  it should be collapsed and only c returned
        results = Filters.removeFilter(results, a);
        assertEquals(results, c);

        //test the last ill-formed bit
        results = Filters.removeFilter(results, c);
        assertSame("Include should be returned when same filter", Filter.INCLUDE, results);
    }

    @Test
    public void testRemoveFilterCompound() {
        Or childOr = ff.or(Arrays.asList(new Filter[]{b, c, d}));
        And base = ff.and(a, childOr);

        Filter results = Filters.removeFilter(base, d, false);
        assertEquals("Filter should not be removed because it should not recurse", base, results);

        results = Filters.removeFilter(base, d);
        assertFalse("Results should be a new object with different children", base.equals(results));
        childOr = ff.or(b,c);
        And expected = ff.and(a, childOr);
        assertEquals(expected, results);

        //again
        results = Filters.removeFilter(results, c);
        expected = ff.and(a, b);
        assertEquals(expected, results);

        //again
        results = Filters.removeFilter(results, a);
        assertEquals(b, results);

        //again
        results = Filters.removeFilter(results, b);
        assertEquals(Filter.INCLUDE, results);
    }

    @Test
    public void testFindPropertyName() {
        String results = Filters.findPropertyName(b);
        assertEquals("suburb", results);

        Filter f = ff.equals(ff.literal("bar"), ff.literal("foo"));

    }

    @Test
    public void testFindPropertyNameEmpty() {
        assertNull(Filters.findPropertyName(null));

        Filter f = ff.equals(ff.literal("bar"), ff.literal("foo"));
        String results = Filters.findPropertyName(b);
        assertNull(Filters.findPropertyName(f));
    }
}
