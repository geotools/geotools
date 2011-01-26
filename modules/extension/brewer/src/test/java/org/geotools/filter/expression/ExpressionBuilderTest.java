package org.geotools.filter.expression;

import static org.junit.Assert.assertEquals;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * ExpressionBuilder is the main entry point from a fluent programming point of view. We will mostly
 * test using this as a starting point; and break out other test cases on an as needed basis.
 *
 * @source $URL$
 */
public class ExpressionBuilderTest {

    @Test
    public void testLiteral() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
        ExpressionBuilder b = new ExpressionBuilder();
        Expression e;

        b.literal("hello world");
        e = b.build();
        assertEquals(ff.literal("hello world"), e);

        assertEquals(ff.literal(1), b.literal().value(1).build());

        b.literal().value(1);
        e = b.build(); // ensure delegate works
        assertEquals(ff.literal(1), e);

        assertEquals(ff.literal(null), b.literal(null).build());

        assertEquals(null, b.unset().build());
        assertEquals(ff.literal(2), b.reset(ff.literal(2)).build());
    }

    @Test
    public void testNullHandling() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
        ExpressionBuilder b = new ExpressionBuilder();
        Expression e;

        assertEquals(Expression.NIL, b.reset().build());
        assertEquals(null, b.reset(null).build());
    }

    @Test
    public void testPropertyName() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
        ExpressionBuilder b = new ExpressionBuilder();
        Expression e;

        assertEquals(ff.property("x"), b.property("x").build());
        assertEquals(ff.property("x"), b.property().property("x").build());
        assertEquals(ff.property("x"), b.property().name("x").build());

        assertEquals(ff.property(null), b.property(null).build());
    }
    @Test
    public void testFunction() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
        ExpressionBuilder b = new ExpressionBuilder();
        Expression e;
    
        // function
        assertEquals(ff.function("pi"), b.function().name("pi").build());
        assertEquals(ff.function("abs", ff.literal(-2)), b.function().name("abs").param().literal(-2).build());        
        assertEquals(ff.function("abs", ff.literal(-2)), b.function("abs").param().literal(-2).build());

        assertEquals(ff.function("min", ff.literal(1), ff.literal(2)),
                b.function("min").param().literal(1).param().literal(2).build());
        
        assertEquals(ff.function("min", ff.literal(1), ff.literal(2)),
                b.function("min").literal(1).literal(2).build());

        assertEquals(ff.function("max", ff.literal(1), ff.property("x")),
                b.function("max").literal(1).property("x").build());
        
        assertEquals(ff.function("max", ff.literal(1), ff.property("x")),
                b.function("max").literal(1).param().property("x").build());

    }
    
    @Test
    public void testAdd() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
        ExpressionBuilder b = new ExpressionBuilder();
        Expression e;
        
        assertEquals(
                ff.add( ff.literal(1), ff.literal(2)),
                b.add().expr1().literal(1).expr2().literal(2).build());
        
        assertEquals(
                ff.add( ff.literal(1), ff.literal(2)),
                b.add().expr1(1).expr2(2).build());
        
    }
}
