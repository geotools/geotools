package org.geotools.filter.function;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class NumberFormatTest {

    @Test
    public void testFormatDouble() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("#.##");
        Literal number = ff.literal("10.56789");

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        char ds = DecimalFormatSymbols.getInstance(Locale.ENGLISH).getDecimalSeparator();
        Assert.assertEquals("10" + ds + "57", f.evaluate(null, String.class));
    }

    @Test
    /** Test that we honour the locale of the machine if (it is set) */
    public void testFormatLocaleFrenchDouble() {
        Locale originalFormat = Locale.getDefault(Locale.Category.FORMAT);
        Locale originalDisplay = Locale.getDefault(Locale.Category.DISPLAY);
        Locale.setDefault(Locale.FRANCE);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("#.##");
        Literal number = ff.literal("10.56789");

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        char ds = DecimalFormatSymbols.getInstance(Locale.FRANCE).getDecimalSeparator();
        Assert.assertEquals("10" + ds + "57", f.evaluate(null, String.class));
        Locale.setDefault(Locale.Category.DISPLAY, originalDisplay);
        Locale.setDefault(Locale.Category.FORMAT, originalFormat);
    }

    @Test
    public void testFormatFrenchDouble() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("#.##");
        Literal number = ff.literal("10.56789");
        Literal lang = ff.literal("fr");
        Function f = ff.function("numberFormat", new Expression[] {pattern, number, lang});
        char ds = DecimalFormatSymbols.getInstance(Locale.FRANCE).getDecimalSeparator();
        Assert.assertEquals("10" + ds + "57", f.evaluate(null, String.class));
    }

    @Test
    public void testFormatInteger() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###");
        Literal number = ff.literal("123456");

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        char gs = DecimalFormatSymbols.getInstance(Locale.ENGLISH).getGroupingSeparator();
        Assert.assertEquals("123" + gs + "456", f.evaluate(null, String.class));
    }

    @Test
    public void testNumberFormat2() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal("-123456.7891");
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");

        Function f =
                ff.function("numberFormat2", new Expression[] {pattern, number, minus, ds, gs});
        Assert.assertEquals("x123;456:79", f.evaluate(null, String.class));
    }

    @Test
    public void testNumberFormatNullValue() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal(null);

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        Assert.assertNull(f.evaluate(null, String.class));
    }

    @Test
    public void testNumberFormatNullPattern() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal(null);
        Literal number = ff.literal("-123456.7891");

        Function f = ff.function("numberFormat", new Expression[] {pattern, number});
        Assert.assertNull(f.evaluate(null, String.class));
    }

    @Test
    public void testNumber2FormatNullValue() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal(null);
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");

        Function f =
                ff.function("numberFormat2", new Expression[] {pattern, number, minus, ds, gs});
        Assert.assertNull(f.evaluate(null, String.class));
    }

    @Test
    public void testNumber2FormatNullPattern() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal(null);
        Literal number = ff.literal("-123456.7891");
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");

        Function f =
                ff.function("numberFormat2", new Expression[] {pattern, number, minus, ds, gs});
        Assert.assertNull(f.evaluate(null, String.class));
    }

    @Test
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
            Assert.assertEquals("10" + ds + "57", f.evaluate(null, String.class));
        }

        Literal lang = ff.literal("AnyLang");
        Function f = ff.function("numberFormat", new Expression[] {pattern, number, lang});
        char ds = DecimalFormatSymbols.getInstance(Locale.ENGLISH).getDecimalSeparator();
        try {
            Assert.assertEquals("10" + ds + "57", f.evaluate(null, String.class));
            Assert.fail("Accepted unknown language code");
        } catch (IllegalArgumentException e) {
            // this is a good thing
        }
    }
}
