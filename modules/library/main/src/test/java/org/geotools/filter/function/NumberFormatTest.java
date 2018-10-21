package org.geotools.filter.function;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
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

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        char ds = DecimalFormatSymbols.getInstance(Locale.ENGLISH).getDecimalSeparator();
        assertEquals("10" + ds + "57", f.evaluate(null, String.class));
    }

    public void testFormatFrenchDouble() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("#.##");
        Literal number = ff.literal("10.56789");
        Literal lang = ff.literal("fr");
        Function f = ff.function("numberFormat", new Expression[] {pattern, number, lang});
        char ds = DecimalFormatSymbols.getInstance(Locale.FRANCE).getDecimalSeparator();
        assertEquals("10" + ds + "57", f.evaluate(null, String.class));
    }

    public void testFormatInteger() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###");
        Literal number = ff.literal("123456");

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        char gs = DecimalFormatSymbols.getInstance(Locale.ENGLISH).getGroupingSeparator();
        assertEquals("123" + gs + "456", f.evaluate(null, String.class));
    }

    public void testNumberFormat2() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal("-123456.7891");
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");

        Function f =
                ff.function("numberFormat2", new Expression[] {pattern, number, minus, ds, gs});
        assertEquals("x123;456:79", f.evaluate(null, String.class));
    }

    public void testNumberFormatNullValue() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal(null);

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        assertEquals(null, f.evaluate(null, String.class));
    }

    public void testNumberFormatNullPattern() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal(null);
        Literal number = ff.literal("-123456.7891");

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        assertEquals(null, f.evaluate(null, String.class));
    }

    public void testNumber2FormatNullValue() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal(null);
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");

        Function f =
                ff.function("numberFormat2", new Expression[] {pattern, number, minus, ds, gs});
        assertEquals(null, f.evaluate(null, String.class));
    }

    public void testNumber2FormatNullPattern() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal(null);
        Literal number = ff.literal("-123456.7891");
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");

        Function f =
                ff.function("numberFormat2", new Expression[] {pattern, number, minus, ds, gs});
        assertEquals(null, f.evaluate(null, String.class));
    }

    public void testNumberFactoryLocaleParam() {
        Locale[] locales = {
            Locale.CANADA,
            Locale.CANADA_FRENCH,
            Locale.GERMAN,
            Locale.KOREAN,
            Locale.CHINESE,
            Locale.JAPANESE,
            Locale.ENGLISH,
            Locale.TRADITIONAL_CHINESE,
            Locale.SIMPLIFIED_CHINESE
        };

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("##.##");
        Literal number = ff.literal("10.56789");
        for (Locale locale : locales) {
            Literal lang = ff.literal(locale.getLanguage());
            Function f = ff.function("numberFormat", new Expression[] {pattern, number, lang});
            char ds = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();
            assertEquals("10" + ds + "57", f.evaluate(null, String.class));
        }

        Literal lang = ff.literal("AnyLang");
        Function f = ff.function("numberFormat", new Expression[] {pattern, number, lang});
        char ds = DecimalFormatSymbols.getInstance(Locale.ENGLISH).getDecimalSeparator();
        try {
            assertEquals("10" + ds + "57", f.evaluate(null, String.class));
            fail("Accepted unknown language code");
        } catch (IllegalArgumentException e) {
            // this is a good thing
        }
    }
}
