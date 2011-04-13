package org.geotools.filter;

import static org.junit.Assert.*;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.AbstractFilterVisitor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLike;

@SuppressWarnings("deprecation")
public class FiltersTest {

    private static final double DELTA = 0.0000001;

    private static Filters filters;

    private static FilterFactory2 ff;

    private static Filter a;

    private static Filter b;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory2(null);
        filters = new Filters(ff);
        a = ff.greater(ff.property("zone"), ff.literal(7));
        b = ff.like(ff.property("suburb"), "N%");

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
    public void testAccept() {
        Filter filter = ff.and(a, b);

        final int count[] = new int[1];
        filters.accept(filter, new FilterVisitor() {
            public void visit(org.geotools.filter.Filter filter) {
                count[0]++;
            }

            public void visit(BetweenFilter filter) {
                count[0]++;
            }

            public void visit(CompareFilter filter) {
                count[0]++;
            }

            public void visit(GeometryFilter filter) {
                count[0]++;
            }

            public void visit(LikeFilter filter) {
                count[0]++;
            }

            public void visit(LogicFilter filter) {
                count[0]++;
            }

            public void visit(NullFilter filter) {
                count[0]++;
            }

            public void visit(FidFilter filter) {
                count[0]++;
            }

            public void visit(AttributeExpression expression) {
                count[0]++;
            }

            public void visit(Expression expression) {
                count[0]++;
            }

            public void visit(LiteralExpression expression) {
                count[0]++;
            }

            public void visit(MathExpression expression) {
                count[0]++;
            }

            public void visit(FunctionExpression expression) {
                count[0]++;
            }
        });
        assertEquals(1, count[0]);
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

}
