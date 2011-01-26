package org.geotools.filter.function;

import java.text.DecimalFormatSymbols;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class NumberFormatTest extends TestCase {

    public void testFormatDouble() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("#.##");
        Literal number = ff.literal("10.56789");
        
        Function f = ff.function("numberFormat", new Expression[]{pattern, number});
        char ds = new DecimalFormatSymbols().getDecimalSeparator();
        assertEquals("10" + ds + "57", f.evaluate(null , String.class));
    }
    
    public void testFormatInteger() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###");
        Literal number = ff.literal("123456");
        
        Function f = ff.function("numberFormat", new Expression[]{pattern, number});
        char gs = new DecimalFormatSymbols().getGroupingSeparator();
        assertEquals("123" + gs + "456", f.evaluate(null , String.class));
    }
    
    public void testNumberFormat2() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal("-123456.7891");
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");
        
        Function f = ff.function("numberFormat2", new Expression[]{pattern, number, minus, ds, gs});
        assertEquals("x123;456:79", f.evaluate(null, String.class));
        
    }
}
